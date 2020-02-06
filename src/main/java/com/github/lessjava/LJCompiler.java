package com.github.lessjava;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import static org.junit.platform.engine.discovery.DiscoverySelectors.*;
import static org.junit.platform.engine.discovery.ClassNameFilter.*;

import com.github.lessjava.generated.LJLexer;
import com.github.lessjava.generated.LJParser;
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.visitor.impl.BuildParentLinks;
import com.github.lessjava.visitor.impl.BuildSymbolTables;
import com.github.lessjava.visitor.impl.LJASTBuildClassLinks;
import com.github.lessjava.visitor.impl.LJASTCheckTypesHaveChanged;
import com.github.lessjava.visitor.impl.LJASTConverter;
import com.github.lessjava.visitor.impl.LJASTInferConstructors;
import com.github.lessjava.visitor.impl.LJASTInferTypes;
import com.github.lessjava.visitor.impl.LJAssignTestVariables;
import com.github.lessjava.visitor.impl.LJGenerateJava;
import com.github.lessjava.visitor.impl.LJInstantiateFunctions;
import com.github.lessjava.visitor.impl.LJStaticAnalysis;
import com.github.lessjava.visitor.impl.StaticAnalysis;

public class LJCompiler {

    private static String fileName = null;
    private static boolean printTypeChanges = false;

    public static void main(String[] args) throws IOException {
        parseArgs(args);

        // Lexing

        LJLexer lexer = null;
        try {
            lexer = new LJLexer(new ANTLRFileStream(fileName));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(0);
        }

        // Parsing

        // Initialize visitors
        ParseTreeWalker walker = new ParseTreeWalker();

        LJParser parser = new LJParser(new CommonTokenStream(lexer));
        LJASTConverter converter = new LJASTConverter();

        BuildParentLinks buildParentLinks = new BuildParentLinks();
        LJASTBuildClassLinks buildClassLinks = new LJASTBuildClassLinks();
        LJStaticAnalysis staticAnalysis = new LJStaticAnalysis();
        BuildSymbolTables buildSymbolTables = new BuildSymbolTables();
        LJASTInferTypes inferTypes = new LJASTInferTypes();
        // PrintDebugTree printTree = new PrintDebugTree();
        LJGenerateJava generateJava = new LJGenerateJava(fileName);
        LJASTCheckTypesHaveChanged checkTypesHaveChanged = new LJASTCheckTypesHaveChanged();
        LJInstantiateFunctions instantiateFunctions = new LJInstantiateFunctions();
        LJASTInferConstructors inferConstructors = new LJASTInferConstructors();

        // ANTLR Parsing
        ParseTree parseTree = parser.program();

        if (parser.getNumberOfSyntaxErrors() != 0) {
            System.exit(1);
        }

        // Convert to AST
        walker.walk(converter, parseTree);
        ASTProgram program = converter.getAST();

        // Apply visitors to AST
        program.traverse(buildParentLinks);
        program.traverse(buildClassLinks);
        if(!StaticAnalysis.getErrors().isEmpty()) {
            System.out.printf("%n%s%n", StaticAnalysis.getErrorString());
            System.exit(1);
        }
        program.traverse(inferConstructors);

        do {
            program.traverse(buildParentLinks); // Rebuild parent links in case we instantiated new functions
            StaticAnalysis.resetErrors();   // Remove errors found in old iterations
            program.traverse(buildSymbolTables);
            program.traverse(instantiateFunctions);
            program.traverse(inferTypes);
            program.traverse(checkTypesHaveChanged);
        } while (LJASTCheckTypesHaveChanged.typesChanged);

        if(printTypeChanges) {
            for (String message : checkTypesHaveChanged.getTypeMessages()) {
                System.out.println(message);
            }
        }

        // TODO: Determine if necessary
        // program.traverse(new LJUnifyVariables());

        LJAssignTestVariables assignTestVariables = new LJAssignTestVariables();

        program.traverse(assignTestVariables);

        program.traverse(staticAnalysis);

        if (!StaticAnalysis.getErrors().isEmpty()) {
            System.out.printf("%n%s%n", StaticAnalysis.getErrorString());
            System.exit(1);
        }

        // program.traverse(printTree);
        program.traverse(generateJava);

        // Compile the generated Java source
        JCompiler.compile();

        // TODO: exec junit
    }

    private static void parseArgs(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: LJCompiler [OPTIONS] <File>");
            System.out.println("    OPTIONS:");
            System.out.println("        -t, --printTypeChanges: print type inference changes of expressions, variables, and functions.");
            System.out.println("    File:");
            System.out.println("        A Less-Java source file.");
            System.exit(0);
        }

        for (String s : args) {
            if(s.equals("-t") || s.equals("--printTypeChanges")) {
                printTypeChanges = true;
            }
        }

        fileName = args[args.length - 1];
        if(!fileName.endsWith(".lj")) {
            System.err.println("Only accepts .lj files");
            System.exit(0);
        }
    }

    private static void runTests(Class test) {
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
            .selectors(selectClass(test))
            .build();
        Launcher launcher = LauncherFactory.create();
        TestPlan testPlan = launcher.discover(request);
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);

        TestExecutionSummary summary = listener.getSummary();
        summary.printTo(new PrintWriter(System.out));
    }

    private static class JCompiler {
        private static final JavaCompiler COMPILER = ToolProvider.getSystemJavaCompiler();
        private static final StandardJavaFileManager FM = COMPILER.getStandardFileManager(null, null, null);
        private static final List<String> OPTIONS = new ArrayList<>(
                Arrays.asList("-classpath", System.getProperty("java.class.path") + ":generated"));
        private static final Iterable<? extends JavaFileObject> SOURCE = FM
                .getJavaFileObjectsFromFiles(JCompiler.getJavaFileObjects());

        private static final String PATH_TO_SOURCE = "generated";

        public static void compile() {
            COMPILER.getTask(null, FM, null, OPTIONS, null, SOURCE).call();
        }

        private static List<File> getJavaFileObjects() {
            List<File> source = new ArrayList<>();

            try {
                Files.walk(Paths.get(PATH_TO_SOURCE)).filter(p -> Files.isRegularFile(p) && isJavaSource(p))
                        .forEach(p -> source.add(p.toFile()));

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            return source;
        }

        private static boolean isJavaSource(Path file) {
            String name = file.toFile().getName();
            try {
                return name.substring(name.lastIndexOf(".")).equals(".java");
            } catch (Exception e) {
                return false;
            }
        }
    }
}
