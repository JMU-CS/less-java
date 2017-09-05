package com.github.lessjava.visitor.impl;

import com.github.lessjava.types.ast.ASTAssignment;
import com.github.lessjava.types.ast.ASTBinaryExpr;
import com.github.lessjava.types.ast.ASTFunction;
import com.github.lessjava.types.ast.ASTFunctionCall;
import com.github.lessjava.types.ast.ASTNode.DataType;
import com.github.lessjava.types.ast.ASTReturn;
import com.github.lessjava.types.ast.ASTUnaryExpr;
import com.github.lessjava.types.ast.ASTVariable;
import com.github.lessjava.visitor.LJAbstractAssignTypes;

public class LJASTCheckUnknownTypes extends LJAbstractAssignTypes
{
    public boolean allTypesKnown;

    public LJASTCheckUnknownTypes()
    {
        this.allTypesKnown = true;
    }

    @Override
    public void postVisit(ASTFunction node)
    {
        allTypesKnown = typeIsKnown(node.returnType);
    }

    @Override
    public void postVisit(ASTVariable node)
    {
        allTypesKnown = typeIsKnown(node.type);
    }

    @Override
    public void postVisit(ASTAssignment node)
    {
        allTypesKnown = typeIsKnown(node.value.type);
        allTypesKnown = typeIsKnown(node.variable.type);
    }

    @Override
    public void postVisit(ASTReturn node)
    {
        allTypesKnown = typeIsKnown(node.value.type);
    }

    @Override
    public void postVisit(ASTBinaryExpr node)
    {
        allTypesKnown = typeIsKnown(node.type);
    }

    @Override
    public void postVisit(ASTUnaryExpr node)
    {
        allTypesKnown = typeIsKnown(node.type);
    }

    @Override
    public void postVisit(ASTFunctionCall node)
    {
        allTypesKnown = typeIsKnown(node.type);
    }

    @Override
    public boolean typeIsKnown(DataType type)
    {
        return allTypesKnown && super.typeIsKnown(type);
    }

}
