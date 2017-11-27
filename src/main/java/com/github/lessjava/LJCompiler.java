package com.github.lessjava;

import java.io.IOException;

import javax.tools.JavaCompiler;
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
import com.github.lessjava.visitor.impl.LJASTCheckTypesHaveChanged;
import com.github.lessjava.visitor.impl.LJASTConverter;
import com.github.lessjava.visitor.impl.LJASTInferTypes;
import com.github.lessjava.visitor.impl.LJAssignTestVariables;
import com.github.lessjava.visitor.impl.LJGenerateJava;
import com.github.lessjava.visitor.impl.LJInstantiateFunctions;
import com.github.lessjava.visitor.impl.LJStaticAnalysis;
import com.github.lessjava.visitor.impl.PrintDebugTree;
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
        LJStaticAnalysis staticAnalysis = new LJStaticAnalysis();
        BuildSymbolTables buildSymbolTables = new BuildSymbolTables();
        LJASTInferTypes inferTypes = new LJASTInferTypes();
        PrintDebugTree printTree = new PrintDebugTree();
        LJGenerateJava generateJava = new LJGenerateJava();

        // ANTLR Parsing
        ParseTree parseTree = parser.program();

        // Convert to AST
        walker.walk(converter, parseTree);
        ASTProgram program = converter.getAST();

        // Apply visitors to AST
        program.traverse(buildParentLinks);
        program.traverse(staticAnalysis);

        boolean typesHaveChanged = true;

        LJASTCheckTypesHaveChanged checkTypesHaveChanged = new LJASTCheckTypesHaveChanged();
        LJInstantiateFunctions instantiateFunctions = new LJInstantiateFunctions();

        while (typesHaveChanged) {
            program.traverse(buildSymbolTables);
            program.traverse(inferTypes);
            program.traverse(checkTypesHaveChanged);
            program.traverse(instantiateFunctions);

            typesHaveChanged = LJASTCheckTypesHaveChanged.typesChanged;
        }

        LJAssignTestVariables assignTestVariables = new LJAssignTestVariables();

        program.traverse(assignTestVariables);

        // program.traverse(printTree);
        program.traverse(generateJava);

        if (!StaticAnalysis.getErrors().isEmpty()) {
            System.out.printf("%n%s%n", StaticAnalysis.getErrorString());
        }

        // Compile java source file
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, generateJava.mainFile.toString());

        // TODO: exec junit
    }

    private static void runTests(Class test) {
        Result result = JUnitCore.runClasses(test);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
    }
}
