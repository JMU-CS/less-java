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

public class LJASTCheckTypesHaveChanged extends StaticAnalysis {
    private static Map<ASTNode, HMType> exprTypeMap = new HashMap<>();

    public static boolean typesChanged;

    @Override
    public void preVisit(ASTProgram node) {
        typesChanged = false;
    }

    @Override
    public void postVisit(ASTFunction node) {
        typesChanged = typeChanged(node, node.returnType);
    }

    @Override
    public void postVisit(ASTFunctionCall node) {
        typesChanged = typeChanged(node, node.type);
    }

    @Override
    public void postVisit(ASTVariable node) {
        typesChanged = typeChanged(node, node.type);
    }

    @Override
    public void postVisit(ASTBinaryExpr node) {
        typesChanged = typeChanged(node, node.type);
    }

    @Override
    public void postVisit(ASTUnaryExpr node) {
        typesChanged = typeChanged(node, node.type);
    }

    private boolean typeChanged(ASTNode node, HMType type) {
        boolean typeChanged = false;

        boolean inMap = type != null && exprTypeMap.containsKey(node);
        boolean equalToMapValue = inMap && exprTypeMap.get(node).equals(type);

        if (!equalToMapValue) {
            System.err.println(node);
            System.err.println(type);
            exprTypeMap.put(node, type);

            typeChanged = true;
        }

        return typesChanged || typeChanged;
    }

}
