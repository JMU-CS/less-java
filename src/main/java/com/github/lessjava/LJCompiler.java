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
import com.github.lessjava.visitor.impl.LJASTAssignPrimitiveTypes;
import com.github.lessjava.visitor.impl.LJASTCheckUnknownTypes;
import com.github.lessjava.visitor.impl.LJASTConverter;
import com.github.lessjava.visitor.impl.LJASTInferTypes;
import com.github.lessjava.visitor.impl.LJStaticAnalysis;
import com.github.lessjava.visitor.impl.PrintDebugTree;
import com.github.lessjava.visitor.impl.StaticAnalysis;

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
        LJASTAssignPrimitiveTypes assignPrimitiveTypes = new LJASTAssignPrimitiveTypes();
        LJASTInferTypes inferTypes = new LJASTInferTypes();
        PrintDebugTree printTree = new PrintDebugTree();

        // ANTLR Parsing
        ParseTree parseTree = parser.program();

        // Convert to AST
        walker.walk(converter, parseTree);
        ASTProgram program = converter.getAST();

        // Apply visitors to AST
        program.traverse(buildParentLinks);
        program.traverse(staticAnalysis);
        
        boolean allTypesKnown = false;

        int i = 0;
        while (!allTypesKnown) {
            LJASTCheckUnknownTypes checkUnknownTypes = new LJASTCheckUnknownTypes();
            
            program.traverse(buildSymbolTables);
            program.traverse(assignPrimitiveTypes);
            program.traverse(inferTypes);

            program.traverse(checkUnknownTypes);
            
            allTypesKnown = checkUnknownTypes.allTypesKnown;
            
            if (i++ == 10) {
                break;
            }
        }

        program.traverse(printTree);
        
        if (!StaticAnalysis.getErrors().isEmpty()) {
            System.err.println();
            System.err.println(StaticAnalysis.getErrorString());
        }
    }
}
