package com.github.lessjava;
import java.io.IOException;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.github.lessjava.ast.ASTProgram;
import com.github.lessjava.generated.LJLexer;
import com.github.lessjava.generated.LJParser;
import com.github.lessjava.visitor.impl.BuildSymbolTables;
import com.github.lessjava.visitor.impl.LJASTConverter;
import com.github.lessjava.visitor.impl.LJStaticAnalysis;
import com.github.lessjava.visitor.impl.PrintDebugTree;

public class LJCompiler {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: LJCompiler <Files>");
            System.exit(0);
        }

        for (String s: args) {
            if (!s.endsWith("lj")) {
                System.err.println("Only accepts .lj files");
                System.exit(0);
            }
        }

        LJLexer lexer = null;
        LJParser parser = null;
        ParseTree parseTree = null;
        LJASTConverter converter = null;
        ParseTreeWalker walker = null;
        LJStaticAnalysis staticAnalyzer = null;
        ASTProgram program = null;
        PrintDebugTree printTree = null;
        BuildSymbolTables bst = null;

        try {
            for (String s: args) {
                lexer = new LJLexer(new ANTLRFileStream(s));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(0);
        }

        parser = new LJParser(new CommonTokenStream(lexer));
        converter = new LJASTConverter();
        walker = new ParseTreeWalker();
        staticAnalyzer = new LJStaticAnalysis();

        parseTree = parser.program();
        //walker.walk(staticAnalyzer, parseTree);
        walker.walk(converter, parseTree);

        program = converter.getAST();
        printTree = new PrintDebugTree();
        bst = new BuildSymbolTables();

        program.traverse(bst);
        program.traverse(printTree);
    }
}
