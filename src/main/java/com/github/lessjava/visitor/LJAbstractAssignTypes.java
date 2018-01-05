package com.github.lessjava.visitor;

import java.util.HashMap;
import java.util.Map;

import com.github.lessjava.types.ast.ASTClass;
import com.github.lessjava.types.ast.ASTFunction;
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.visitor.impl.StaticAnalysis;

public abstract class LJAbstractAssignTypes extends StaticAnalysis implements LJAssignTypes {
    protected static Map<String, ASTFunction> nameparamFunctionMap = new HashMap<>();

    @Override
    public void preVisit(ASTProgram node) {
        for (ASTFunction function : node.functions) {
            nameparamFunctionMap.put(function.getNameParamString(), function);
        }

        for (ASTClass astClass: node.classes) {
            for (ASTFunction method: astClass.methods) {
                nameparamFunctionMap.put(method.getNameParamString(), method);
            }
        }
    }
}
