package com.github.lessjava.visitor.impl;

import java.util.HashMap;
import java.util.Map;

import com.github.lessjava.types.ast.ASTBinaryExpr;
import com.github.lessjava.types.ast.ASTFunction;
import com.github.lessjava.types.ast.ASTFunctionCall;
import com.github.lessjava.types.ast.ASTNode;
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.types.ast.ASTUnaryExpr;
import com.github.lessjava.types.ast.ASTVariable;
import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.types.inference.impl.HMTypeBase;

public class LJASTCheckTypesHaveChanged extends StaticAnalysis
{
    private Map<ASTNode, HMType> exprTypeMap;

    public boolean typesChanged;

    public LJASTCheckTypesHaveChanged()
    {
        this.exprTypeMap = new HashMap<>();
        this.typesChanged = false;
    }

    @Override
    public void preVisit(ASTProgram node)
    {
        typesChanged = false;
    }

    @Override
    public void postVisit(ASTFunction node)
    {
        typesChanged = typeChanged(node, node.returnType);
    }

    @Override
    public void postVisit(ASTFunctionCall node)
    {
        typesChanged = typeChanged(node, node.type);
    }

    @Override
    public void postVisit(ASTVariable node)
    {
        typesChanged = typeChanged(node, node.type);
    }

    @Override
    public void postVisit(ASTBinaryExpr node)
    {
        typesChanged = typeChanged(node, node.type);
    }

    @Override
    public void postVisit(ASTUnaryExpr node)
    {
        typesChanged = typeChanged(node, node.type);
    }

    private boolean baseTypeChanged(ASTNode node, HMType type)
    {
        boolean typeChanged = false;

        HMType oldType = exprTypeMap.get(node);
        HMType newType = type;

        if (newType instanceof HMTypeBase && oldType instanceof HMTypeBase) {
            HMTypeBase newBaseType = (HMTypeBase) newType;
            HMTypeBase oldBaseType = (HMTypeBase) oldType;
            typeChanged = !newBaseType.getBaseType().equals(oldBaseType.getBaseType());
        } else {
            typeChanged = true;
        }

        return typeChanged;
    }

    private boolean typeChanged(ASTNode node, HMType type)
    {
        boolean typeChanged = false;
        
        if (type == null) {
            exprTypeMap.put(node, null);
            return false;
        }

        if (!exprTypeMap.containsKey(node) || !exprTypeMap.get(node).equals(type)) {
            typeChanged = true;
        } else {
            // Check for changes in basetype
            typeChanged = baseTypeChanged(node, type);
        }

        exprTypeMap.put(node, type);
        return this.typesChanged || typeChanged;
    }

}
