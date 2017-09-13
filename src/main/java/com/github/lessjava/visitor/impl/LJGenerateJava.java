package com.github.lessjava.visitor.impl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.github.lessjava.types.ast.ASTAssignment;
import com.github.lessjava.types.ast.ASTBlock;
import com.github.lessjava.types.ast.ASTBreak;
import com.github.lessjava.types.ast.ASTConditional;
import com.github.lessjava.types.ast.ASTContinue;
import com.github.lessjava.types.ast.ASTFunction;
import com.github.lessjava.types.ast.ASTFunctionCall;
import com.github.lessjava.types.ast.ASTNode;
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.types.ast.ASTReturn;
import com.github.lessjava.types.ast.ASTTest;
import com.github.lessjava.types.ast.ASTWhileLoop;
import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.visitor.LJDefaultASTVisitor;

public class LJGenerateJava extends LJDefaultASTVisitor
{
    private Path         file      = Paths.get("Main.java");
    private List<String> lines     = new ArrayList<>();
    private List<String> mainLines = new ArrayList<>();
    private int          indent    = 1;

    private ASTProgram programNode;

    @Override
    public void preVisit(ASTProgram node)
    {
        this.programNode = node;
        lines.add("public class Main");
        lines.add("{");

        String spaces = String.format("%" + (indent * 4) + "s", "");
        mainLines.add(String.format("%spublic static void main(String[] args)", spaces));
        mainLines.add(String.format("%s{", spaces));
    }

    @Override
    public void postVisit(ASTProgram node)
    {
        String line = String.format("}"); 
        addLine(node, line);

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
        String paramaterString = node.parameters.toString().substring(1, node.parameters.toString().length() - 1);
        String line = String.format("public static %s %s(%s)", node.returnType.toString(), node.name,
                paramaterString);
        addLine(node, line);
    }

    @Override
    public void preVisit(ASTBlock node)
    {
        String line = String.format("{");
        addLine(node, line);
        indent++;
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
        String line = String.format("%s %s = %s;", node.variable.type.toString(), node.variable.name,
                node.value);
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
    public void preVisit(ASTFunctionCall node)
    {
        String line = String.format("%s", node);
        addLine(node, line);
        
        System.out.println(node);
    }

    private void addLine(ASTNode node, String line)
    {
        String spaces = (indent == 0) ? "" : String.format("%" + (indent * 4) + "s", "");
        if (node.equals(programNode) || !(node instanceof ASTFunction) && node.getParent().equals(programNode)) {
            mainLines.add(String.format("%s%s%s", spaces, spaces, line));
        } else {
            lines.add(String.format("%s%s", spaces, line));
        }
    }

}
