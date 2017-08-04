package com.github.lessjava.visitor.impl;

import java.io.PrintStream;

import com.github.lessjava.ast.ASTAssignment;
import com.github.lessjava.ast.ASTBinaryExpr;
import com.github.lessjava.ast.ASTBlock;
import com.github.lessjava.ast.ASTBreak;
import com.github.lessjava.ast.ASTConditional;
import com.github.lessjava.ast.ASTContinue;
import com.github.lessjava.ast.ASTExpression;
import com.github.lessjava.ast.ASTFunction;
import com.github.lessjava.ast.ASTFunctionCall;
import com.github.lessjava.ast.ASTLiteral;
import com.github.lessjava.ast.ASTLocation;
import com.github.lessjava.ast.ASTNode;
import com.github.lessjava.ast.ASTProgram;
import com.github.lessjava.ast.ASTReturn;
import com.github.lessjava.ast.ASTTest;
import com.github.lessjava.ast.ASTUnaryExpr;
import com.github.lessjava.ast.ASTVariable;
import com.github.lessjava.ast.ASTVoidFunctionCall;
import com.github.lessjava.ast.ASTWhileLoop;
import com.github.lessjava.visitor.DefaultASTVisitor;

/**
 * AST pre-order visitor; prints each node to standard output. For best output,
 * should be run AFTER {@link BuildParentLinks} and {@link CalculateNodeDepths}.
 *
 */
public class PrintDebugTree extends DefaultASTVisitor
{
    private PrintStream output;

    public PrintDebugTree()
    {
        this(System.out);
    }

    public PrintDebugTree(PrintStream output)
    {
        this.output = output;
    }

    public void indent(ASTNode node)
    {
        if (node.attributes.containsKey("depth")) {
            int level = ((Integer) node.attributes.get("depth")).intValue();
            while (level > 1) {
                output.print("  ");
                level--;
            }
        }
    }

    public void newline(ASTNode node)
    {
        newline(node, true); // change to "false" to omit source info
    }

    public void newline(ASTNode node, boolean printSource)
    {
        if (printSource) {
            // output.println(" [" + node.getSourceInfo().toString() + "]");
            output.println();
        } else {
            output.println();
        }
    }

    @Override
    public void preVisit(ASTProgram node)
    {
        indent(node);
        output.print("Program");
        newline(node, true);
    }

    @Override
    public void preVisit(ASTFunction node)
    {
        indent(node);
        output.print("Function: " + node.name + " : " + ASTNode.typeToString(node.returnType) + " "
                + node.getParameterStr());
        newline(node, true);
    }

    @Override
    public void preVisit(ASTVariable node)
    {
        indent(node);
        output.print("Variable: " + node.name + " : " + ASTNode.typeToString(node.type)
                + (node.isArray ? "[" + node.arrayLength + "]" : ""));
        newline(node, true);
    }

    @Override
    public void preVisit(ASTBlock node)
    {
        indent(node);
        output.print("Block");
        newline(node, true);
    }

    @Override
    public void preVisit(ASTAssignment node)
    {
        indent(node);
        output.print("Assignment: " + node.variable.toString() + " = " + node.value.toString());
        newline(node, true);
    }

    @Override
    public void preVisit(ASTVoidFunctionCall node)
    {
        StringBuffer args = new StringBuffer();
        args.append("(");
        for (ASTExpression e : node.arguments) {
            if (args.length() > 1) {
                args.append(", ");
            }
            args.append(e.toString());
        }
        args.append(")");

        indent(node);
        output.print("VoidFunctionCall: " + node.name + " " + args.toString());
        newline(node);
    }

    @Override
    public void preVisit(ASTConditional node)
    {
        indent(node);
        output.print("Conditional: condition=" + node.condition.toString());
        newline(node, true);
    }

    @Override
    public void preVisit(ASTWhileLoop node)
    {
        indent(node);
        output.print("WhileLoop: guard=" + node.guard.toString());
        newline(node, true);
    }

    @Override
    public void preVisit(ASTReturn node)
    {
        indent(node);
        output.print("Return");
        newline(node, true);
    }

    @Override
    public void preVisit(ASTBreak node)
    {
        indent(node);
        output.print("Break");
        newline(node, true);
    }

    @Override
    public void preVisit(ASTContinue node)
    {
        indent(node);
        output.print("Continue");
        newline(node, true);
    }

    @Override
    public void preVisit(ASTTest node)
    {
        indent(node);
        output.print("Test");
        newline(node);
    }

    @Override
    public void preVisit(ASTBinaryExpr node)
    {
        indent(node);
        output.print("BinaryExpr: " + ASTBinaryExpr.opToString(node.operator));
        newline(node);
    }

    @Override
    public void preVisit(ASTUnaryExpr node)
    {
        indent(node);
        output.print("UnaryExpr: " + ASTUnaryExpr.opToString(node.operator));
        newline(node);
    }

    @Override
    public void preVisit(ASTFunctionCall node)
    {
        StringBuffer args = new StringBuffer();
        args.append("(");
        for (ASTExpression e : node.arguments) {
            if (args.length() > 1) {
                args.append(", ");
            }
            args.append(e.toString());
        }
        args.append(")");

        indent(node);
        output.print("FunctionCall: " + node.name + " : " + node.type + " " + args.toString());
        newline(node);
    }

    @Override
    public void preVisit(ASTLocation node)
    {
        indent(node);
        output.print("Location: " + node.name.toString());
        newline(node);
    }

    @Override
    public void preVisit(ASTLiteral node)
    {
        indent(node);
        output.print("Literal: " + node.toString() + " : " + ASTNode.typeToString(node.type));
        newline(node);
    }
}
