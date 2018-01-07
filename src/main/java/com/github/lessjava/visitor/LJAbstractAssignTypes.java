package com.github.lessjava.visitor;

import java.util.HashMap;
import java.util.Map;

import com.github.lessjava.types.ast.ASTAbstractFunction;
import com.github.lessjava.types.ast.ASTClass;
import com.github.lessjava.types.ast.ASTMethod;
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.visitor.impl.StaticAnalysis;

public abstract class LJAbstractAssignTypes extends StaticAnalysis implements LJAssignTypes {
    protected static Map<String, ASTAbstractFunction> nameparamFunctionMap = new HashMap<>();

    @Override
    public void preVisit(ASTProgram node) {
        for (ASTAbstractFunction function : node.functions) {
            nameparamFunctionMap.put(function.getIdentifyingString(), function);
        }

        for (ASTClass astClass: node.classes) {
            for (ASTMethod method: astClass.methods) {
                nameparamFunctionMap.put(method.getIdentifyingString(), method);
            }
        }
    }
}
