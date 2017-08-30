package com.github.lessjava;

import java.io.IOException;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.github.lessjava.generated.LJLexer;
import com.github.lessjava.generated.LJParser;
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.visitor.impl.BuildParentLinks;
import com.github.lessjava.visitor.impl.BuildSymbolTables;
import com.github.lessjava.visitor.impl.LJASTAssignTypes;
import com.github.lessjava.visitor.impl.LJASTCheckUnknownTypes;
import com.github.lessjava.visitor.impl.LJASTConverter;
import com.github.lessjava.visitor.impl.LJStaticAnalysis;
import com.github.lessjava.visitor.impl.PrintDebugTree;

public class LJCompiler
{
    public static void main(String[] args)
    {
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

        LJLexer lexer = null;

        try {
            for (String s : args) {
                lexer = new LJLexer(new ANTLRFileStream(s));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(0);
        }

        LJParser parser = new LJParser(new CommonTokenStream(lexer));
        LJASTConverter converter = new LJASTConverter();
        ParseTreeWalker walker = new ParseTreeWalker();
        LJStaticAnalysis staticAnalysis = new LJStaticAnalysis();

        ParseTree parseTree = parser.program();
        // walker.walk(staticAnalyzer, parseTree);
        walker.walk(converter, parseTree);

        ASTProgram program = converter.getAST();
        PrintDebugTree printTree = new PrintDebugTree();
        BuildParentLinks buildParentLinks = new BuildParentLinks();
        BuildSymbolTables buildSymbolTables = new BuildSymbolTables();
        LJASTAssignTypes assignTypes = new LJASTAssignTypes();

        program.traverse(buildParentLinks);
        program.traverse(staticAnalysis);

        // For debugging. Set to false to limit iterations and prevent infinite loops
        boolean loop = false;

        if (loop) {
            // While unknown types remain
            while (true) {
                program.traverse(assignTypes);
                program.traverse(buildSymbolTables);

                LJASTCheckUnknownTypes checkUnknownTypes = new LJASTCheckUnknownTypes();
                program.traverse(checkUnknownTypes);

                if (checkUnknownTypes.allTypesKnown) {
                    break;
                }
            }
        } else {
            for (int i = 0; i < 1000; i++) {
                program.traverse(buildSymbolTables);
                program.traverse(assignTypes);
            }
        }

        program.traverse(printTree);
    }
}
