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
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.types.ast.ASTReturn;
import com.github.lessjava.types.ast.ASTTest;
import com.github.lessjava.types.ast.ASTWhileLoop;
import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.visitor.LJDefaultASTVisitor;

public class LJGenerateJava extends LJDefaultASTVisitor
{
    Path         file  = Paths.get("Main.java");
    List<String> lines = new ArrayList<>();

    int indent = 1;

    @Override
    public void preVisit(ASTProgram node)
    {
        lines.add("public class Main");
        lines.add("{");
    }

    @Override
    public void postVisit(ASTProgram node)
    {
        lines.add("}");
        try {
            Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    public void preVisit(ASTFunction node)
    {
        String spaces = (indent == 0) ? "" : String.format("%" + (indent * 4) + "s", "");
        indent++;
        String paramaterString = node.parameters.toString().substring(1, node.parameters.toString().length() - 1);
        lines.add(String.format("%spublic static %s %s(%s)", spaces, HMType.typeToString(node.returnType), node.name,
                paramaterString));
        indent--;
    }

    @Override
    public void preVisit(ASTBlock node)
    {
        String spaces = (indent == 0) ? "" : String.format("%" + (indent * 4) + "s", "");
        lines.add(String.format("%s{", spaces));
        indent++;
    }

    @Override
    public void postVisit(ASTBlock node)
    {
        indent--;
        String spaces = (indent == 0) ? "" : String.format("%" + (indent * 4) + "s", "");
        lines.add(String.format("%s}", spaces));
    }

    @Override
    public void preVisit(ASTAssignment node)
    {
        String spaces = (indent == 0) ? "" : String.format("%" + (indent * 4) + "s", "");
        lines.add(String.format("%s%s %s = %s;", spaces, HMType.typeToString(node.variable.type), node.variable.name,
                node.value));
    }

    @Override
    public void preVisit(ASTConditional node)
    {
        String spaces = (indent == 0) ? "" : String.format("%" + (indent * 4) + "s", "");
        lines.add(String.format("%sif (%s)", spaces, node.condition));
    }

    @Override
    public void preVisit(ASTWhileLoop node)
    {
        String spaces = (indent == 0) ? "" : String.format("%" + (indent * 4) + "s", "");
        lines.add(String.format("%swhile (%s)", spaces, node.guard));
    }

    @Override
    public void preVisit(ASTReturn node)
    {
        String spaces = (indent == 0) ? "" : String.format("%" + (indent * 4) + "s", "");
        lines.add(String.format("%sreturn %s;", spaces, node.value));
    }

    @Override
    public void preVisit(ASTBreak node)
    {
        String spaces = (indent == 0) ? "" : String.format("%" + (indent * 4) + "s", "");
        lines.add(String.format("%sbreak;%n", spaces));
    }

    @Override
    public void preVisit(ASTContinue node)
    {
        String spaces = (indent == 0) ? "" : String.format("%" + (indent * 4) + "s", "");
        lines.add(String.format("%scontinue;%n", spaces));
    }

    @Override
    public void preVisit(ASTTest node)
    {
        // TODO: ugh...
    }

    @Override
    public void preVisit(ASTFunctionCall node)
    {
        String spaces = (indent == 0) ? "" : String.format("%" + (indent * 4) + "s", "");
        lines.add(String.format("%s%s", spaces, node));
    }

}
