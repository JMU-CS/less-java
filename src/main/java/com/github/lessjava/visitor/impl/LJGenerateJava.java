package com.github.lessjava.visitor.impl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.lessjava.types.Symbol;
import com.github.lessjava.types.SymbolTable;
import com.github.lessjava.types.ast.ASTAssignment;
import com.github.lessjava.types.ast.ASTBlock;
import com.github.lessjava.types.ast.ASTBreak;
import com.github.lessjava.types.ast.ASTConditional;
import com.github.lessjava.types.ast.ASTContinue;
import com.github.lessjava.types.ast.ASTFunction;
import com.github.lessjava.types.ast.ASTNode;
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.types.ast.ASTReturn;
import com.github.lessjava.types.ast.ASTTest;
import com.github.lessjava.types.ast.ASTVoidFunctionCall;
import com.github.lessjava.types.ast.ASTWhileLoop;
import com.github.lessjava.visitor.LJDefaultASTVisitor;

public class LJGenerateJava extends LJDefaultASTVisitor
{
    private Set<Symbol>  declaredVariables    = new HashSet<>();
    private Path         file                 = Paths.get("Main.java");
    private List<String> lines                = new ArrayList<>();
    private List<String> mainLines            = new ArrayList<>();
    private List<String> mainDeclarationLines = new ArrayList<>();
    private Set<String>  mainVariables        = new HashSet<>();
    private int          indent               = 1;

    private ASTFunction currentFunction;

    @Override
    public void preVisit(ASTProgram node)
    {
        this.currentFunction = null;

        lines.add("public class Main");
        lines.add("{");

        String spaces = String.format("%" + (indent * 4) + "s", "");
        mainLines.add(String.format("%spublic static void main(String[] args)", spaces));
        mainLines.add(String.format("%s{", spaces));
    }

    @Override
    public void postVisit(ASTProgram node)
    {
        String spaces = String.format("%" + (indent * 4) + "s", "");

        mainLines.add(String.format("%s}", spaces));
        mainLines.addAll(2, mainDeclarationLines);
        lines.add("}");
        lines.addAll(2, mainLines);

        try {
            Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    public void preVisit(ASTFunction node)
    {
        this.currentFunction = node;

        String paramaterString = node.parameters.toString().substring(1, node.parameters.toString().length() - 1);
        String line = String.format("public static %s %s(%s)", node.returnType.toString(), node.name, paramaterString);
        addLine(node, line);
    }

    @Override
    public void postVisit(ASTFunction node)
    {
        this.currentFunction = null;
    }

    @Override
    public void preVisit(ASTBlock node)
    {
        String line = String.format("{");
        addLine(node, line);
        indent++;

        // Emit declarations if this block is the body of a function
        if (node.getParent() instanceof ASTFunction) {
            SymbolTable symbolTable = (SymbolTable) node.attributes.get("symbolTable");
            List<Symbol> symbols = symbolTable.getSymbols();

            System.out.println("ALL: \n" + symbolTable.getAllSymbols());
            System.out.println("Inherited: \n" + symbolTable.getInheritedSymbols());
            System.out.println("Local: \n" + symbolTable.getSymbols());

            for (Symbol s : symbols) {
                if (!declaredVariables.contains(s)) {
                    line = String.format("%s %s;", s.type, s.name);
                    addLine(node, line);

                    declaredVariables.add(s);
                }
                // symbolSet.remove(s);
            }
        }
    }

    @Override
    public void postVisit(ASTBlock node)
    {
        indent--;
        String line = String.format("}");
        addLine(node, line);
    }

    @Override
    public void preVisit(ASTAssignment node)
    {
        // Emit main declarations
        if (this.currentFunction == null) {
            if (!mainVariables.contains(node.variable.name)) {
                String spaces = (indent == 0) ? "" : String.format("%" + (indent * 4) + "s", "");
                String declaration = String.format("%s%s%s %s;", spaces, spaces, node.variable.type,
                        node.variable.name);
                mainDeclarationLines.add(declaration);

                mainVariables.add(node.variable.name);
            }
        }

        String line = String.format("%s = %s;", node.variable.name, node.value);
        addLine(node, line);
    }

    @Override
    public void preVisit(ASTConditional node)
    {
        String line = String.format("if (%s)", node.condition);
        addLine(node, line);
    }

    @Override
    public void preVisit(ASTWhileLoop node)
    {
        String line = String.format("while (%s)", node.guard);
        addLine(node, line);
    }

    @Override
    public void preVisit(ASTReturn node)
    {
        String line = String.format("return %s;", node.value);
        addLine(node, line);
    }

    @Override
    public void preVisit(ASTBreak node)
    {
        String line = String.format("break;", node);
        addLine(node, line);
    }

    @Override
    public void preVisit(ASTContinue node)
    {
        String line = String.format("continue;", node);
        addLine(node, line);
    }

    @Override
    public void preVisit(ASTTest node)
    {
        // TODO: ugh...
    }

    @Override
    public void preVisit(ASTVoidFunctionCall node)
    {
        if (node.name.equals("print")) {
            String arguments = node.arguments.toString().substring(1, node.arguments.toString().length() - 1);
            String line = String.format("System.out.println(%s);", arguments);
            addLine(node, line);
        }
    }

    private void addLine(ASTNode node, String line)
    {
        String spaces = (indent == 0) ? "" : String.format("%" + (indent * 4) + "s", "");
        if (currentFunction == null) {
            mainLines.add(String.format("%s%s%s", spaces, spaces, line));
        } else {
            lines.add(String.format("%s%s", spaces, line));
        }
    }

}
