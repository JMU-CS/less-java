package com.github.lessjava;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

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
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: LJCompiler <Files>");
            System.exit(0);
        }

        for (String s : args) {
            if (!s.endsWith("lj")) {
                System.err.println("Only accepts .lj files");
                System.exit(0);
            }
        }

        // Lexing

        LJLexer lexer = null;
        try {
            for (String s : args) {
                lexer = new LJLexer(new ANTLRFileStream(s));
            }
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
        //PrintDebugTree printTree = new PrintDebugTree();
        LJGenerateJava generateJava = new LJGenerateJava();
        LJASTCheckTypesHaveChanged checkTypesHaveChanged = new LJASTCheckTypesHaveChanged();
        LJInstantiateFunctions instantiateFunctions = new LJInstantiateFunctions();
        LJASTInferConstructors inferConstructors = new LJASTInferConstructors();

        // ANTLR Parsing
        ParseTree parseTree = parser.program();

        // Convert to AST
        walker.walk(converter, parseTree);
        ASTProgram program = converter.getAST();

        // Apply visitors to AST
        program.traverse(buildParentLinks);
        program.traverse(buildClassLinks);
        program.traverse(inferConstructors);
        program.traverse(staticAnalysis);

        boolean typesHaveChanged = true;
        while (typesHaveChanged) {
            program.traverse(buildSymbolTables);
            program.traverse(instantiateFunctions);
            program.traverse(inferTypes);
            program.traverse(checkTypesHaveChanged);

            typesHaveChanged = LJASTCheckTypesHaveChanged.typesChanged;
        }

        // TODO: Determine if necessary
        //program.traverse(new LJUnifyVariables());

        LJAssignTestVariables assignTestVariables = new LJAssignTestVariables();

        program.traverse(assignTestVariables);

        program.traverse(new LJStaticAnalysis());

        // program.traverse(printTree);
        program.traverse(generateJava);

        if (!StaticAnalysis.getErrors().isEmpty()) {
            System.out.printf("%n%s%n", StaticAnalysis.getErrorString());
            System.exit(1);
        }

        // Compile java source file

        List<String> optionList = new ArrayList<>();
        // set compiler's classpath to be same as the runtime's
        optionList.addAll(Arrays.asList("-classpath",System.getProperty("java.class.path")));

        List<JavaFileObject> jfos = new ArrayList<>();
        jfos.add(new FileSimpleJavaFileObject(new File(LJGenerateJava.mainFile.toString())));

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.getTask(null, null, null, optionList, null, jfos);

        // TODO: exec junit
    }

    private static void runTests(Class test) {
        Result result = JUnitCore.runClasses(test);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
    }

    private static class FileSimpleJavaFileObject extends SimpleJavaFileObject {
        public FileSimpleJavaFileObject(File file) {
            super(file.toURI(), JavaFileObject.Kind.SOURCE);
        }
    }
}
