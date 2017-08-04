package com.github.lessjava.visitor.impl;

import java.util.Iterator;

import com.github.lessjava.ast.ASTAssignment;
import com.github.lessjava.ast.ASTBinaryExpr;
import com.github.lessjava.ast.ASTExpression;
import com.github.lessjava.ast.ASTFunction;
import com.github.lessjava.ast.ASTFunction.Parameter;
import com.github.lessjava.ast.ASTFunctionCall;
import com.github.lessjava.ast.ASTNode.DataType;
import com.github.lessjava.ast.ASTReturn;
import com.github.lessjava.ast.ASTUnaryExpr;
import com.github.lessjava.ast.ASTVariable;
import com.github.lessjava.visitor.LJAbstractAssignTypes;

public class LJASTAssignTypes extends LJAbstractAssignTypes
{
    private ASTFunction currentFunction;

    @Override
    public void preVisit(ASTFunction node)
    {
        currentFunction = node;
    }

    @Override
    public void postVisit(ASTFunction node)
    {
        currentFunction = null;
    }

    @Override
    public void preVisit(ASTReturn node)
    {
        currentFunction.returnType = evalExprType(node.value);
    }

    @Override
    public void preVisit(ASTFunctionCall node)
    {
        ASTFunction function = nameFunctionMap.get(node.name);
        Iterator<ASTExpression> functionCallArgIterator = node.arguments.iterator();
        
        // Assign types of parameters of associated function
        for (Parameter p : function.parameters) {
            ASTExpression arg = functionCallArgIterator.next();
            DataType argType = evalExprType(arg);

            if (!typeIsKnown(p.type) && typeIsKnown(argType)) {
                p.type = argType;
            }
        }
        
        if (!typeIsKnown(node.type) && typeIsKnown(function.returnType)) {
            node.type = function.returnType;
        }
    }

    @Override
    public void postVisit(ASTAssignment node)
    {
        node.variable.type = evalExprType(node.value);
    }

    @Override
    public void postVisit(ASTBinaryExpr node)
    {
        node.type = node.leftChild.type;
    }

    @Override
    public void postVisit(ASTUnaryExpr node)
    {
        node.type = node.child.type;
    }

    @Override
    public void postVisit(ASTVariable node)
    {
        node.type = evalExprType(node);
    }

}
