package com.github.lessjava.visitor.impl;

import java.util.HashMap;
import java.util.Map;

import com.github.lessjava.types.ast.ASTClass;
import com.github.lessjava.visitor.LJDefaultASTVisitor;

public class LJASTBuildClassLinks extends LJDefaultASTVisitor {

    public final Map<String, ASTClass> nameClassMap;

    public LJASTBuildClassLinks() {
        nameClassMap = new HashMap<>();
        nameClassMap.put("Object",null);
        nameClassMap.put("String", null);
        nameClassMap.put("Integer", null);
        nameClassMap.put("Double", null);
        nameClassMap.put("Boolean", null);
        nameClassMap.put("Char", null);
        nameClassMap.put("LJList", null);
        nameClassMap.put("LJSet", null);
        nameClassMap.put("LJMap", null);
    }

    @Override
    public void postVisit(ASTClass node) {
        super.postVisit(node);

        String className = node.signature.className;
        if(nameClassMap.containsKey(className)) {
            StaticAnalysis.addError(node, className + " already declared.");
        } else {
            nameClassMap.put(className, node);
        }

        String superName = node.signature.superName;
        if(superName != null) {
            if(nameClassMap.containsKey(superName)) {
                node.parent = nameClassMap.get(superName);
            } else {
                StaticAnalysis.addError(node, "Superclass " + superName + " not found.");
            }
        }
    }
}
