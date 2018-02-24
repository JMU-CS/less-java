package com.github.lessjava.visitor.impl;

import java.util.HashMap;
import java.util.Map;

import com.github.lessjava.types.ast.ASTClass;
import com.github.lessjava.visitor.LJDefaultASTVisitor;

public class LJASTBuildClassLinks extends LJDefaultASTVisitor {

    public final static Map<String, ASTClass> nameClassMap = new HashMap<>();

    @Override
    public void postVisit(ASTClass node) {
        super.postVisit(node);

        nameClassMap.putIfAbsent(node.signature.className, node);
        node.parent = nameClassMap.get(node.signature.superName);
    }
}
