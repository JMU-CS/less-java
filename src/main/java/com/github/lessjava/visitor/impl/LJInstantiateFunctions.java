package com.github.lessjava.visitor.impl;

import java.util.ArrayList;
import java.util.List;

import com.github.lessjava.types.ast.ASTAbstractFunction;
import com.github.lessjava.types.ast.ASTAbstractFunction.Parameter;
import com.github.lessjava.types.ast.ASTBlock;
import com.github.lessjava.types.ast.ASTClass;
import com.github.lessjava.types.ast.ASTExpression;
import com.github.lessjava.types.ast.ASTFunction;
import com.github.lessjava.types.ast.ASTFunctionCall;
import com.github.lessjava.types.ast.ASTMethod;
import com.github.lessjava.types.ast.ASTMethodCall;
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.types.inference.impl.HMTypeClass;
import com.github.lessjava.visitor.LJAbstractAssignTypes;

public class LJInstantiateFunctions extends LJAbstractAssignTypes {
    private static ASTProgram program;

    @Override
    public void preVisit(ASTProgram node) {
        super.preVisit(node);
        if (LJInstantiateFunctions.program == null) {
            LJInstantiateFunctions.program = node;
        }
    }

    @Override
    public void postVisit(ASTProgram node) {
        super.postVisit(node);
    }

    @Override
    public void postVisit(ASTFunctionCall node) {
        super.postVisit(node);

        ASTAbstractFunction prototype = program.functions.stream()
            .filter(f -> f.name.equals(node.name) && !f.concrete)
            .findAny()
            .orElse(null);

        if (prototype == null || prototype.body == null) {
            return;
        }

        if (prototype != null && prototype.concrete) {
            return;
        }

        ASTFunction f = instantiateFunction(prototype, node.arguments);

        if (f != null) {
            if (program.functions.contains(f)) {
                return;
            }

            program.functions.add(f);

            idFunctionMap.put(node.getIdentifyingString(), f);
        }
    }

    private ASTFunction instantiateFunction(ASTAbstractFunction prototype, List<ASTExpression> arguments) {
        if (arguments.stream().anyMatch(arg -> !arg.type.isConcrete)) {
            // Can't instantiate; arg isn't concrete
            return null;
        }

        ASTBlock blockCopy = new ASTBlock();
        blockCopy.statements = prototype.body.statements;
        blockCopy.variables = prototype.body.variables;

        ASTFunction functionInstance = new ASTFunction(prototype.name, prototype.returnType, blockCopy);

        blockCopy.setParent(functionInstance);

        functionInstance.concrete = true;
        functionInstance.parameters = new ArrayList<>();

        for (int i = 0; i < arguments.size(); i++) {
            String pname = prototype.parameters.get(i).name;
            Parameter parameter = new Parameter(pname, arguments.get(i).type.clone());
            functionInstance.parameters.add(parameter);
        }

        for (ASTAbstractFunction f : program.functions) {
            if (functionInstance.equals(f)) {
                return null;
            }
        }

        functionInstance.setParent(program);
        functionInstance.setDepth(2);

        return functionInstance;
    }

    @Override
    public void postVisit(ASTMethodCall node) {
        super.postVisit(node);

        // Haven't performed sufficient unification
        if (!(node.invoker.type instanceof HMTypeClass)) {
            return;
        }

        HMTypeClass type = (HMTypeClass) node.invoker.type;
        ASTClass containingClass = ASTClass.nameClassMap.get(type.name);
        ASTMethod m = containingClass.getMethod(node.funcCall.name);

        if (m != null && m.concrete) {
            return;
        }

        m = instantiateMethod(m, node.funcCall.arguments);

        if (m != null) {
            containingClass.block.methods.add(m);
        }
    }

    private ASTMethod instantiateMethod(ASTMethod prototype, List<ASTExpression> arguments) {
        if (arguments.stream().anyMatch(arg -> !arg.type.isConcrete)) {
            // Can't instantiate; arg isn't concrete
            return null;
        }

        ASTClass containingClass = ASTClass.nameClassMap.get(prototype.containingClassName);

        ASTBlock blockCopy = new ASTBlock();
        blockCopy.statements = prototype.body.statements;
        blockCopy.variables = prototype.body.variables;

        ASTFunction functionInstance = new ASTFunction(prototype.name, prototype.returnType, blockCopy);

        blockCopy.setParent(functionInstance);

        functionInstance.concrete = true;
        functionInstance.parameters = new ArrayList<>();

        for (int i = 0; i < arguments.size(); i++) {
            String pname = prototype.parameters.get(i).name;
            Parameter parameter = new Parameter(pname, arguments.get(i).type.clone());
            functionInstance.parameters.add(parameter);
        }

        functionInstance.setParent(program);
        functionInstance.setDepth(2);

        ASTMethod methodInstance = new ASTMethod(prototype.scope, functionInstance, prototype.containingClassName);

        for (ASTMethod m : containingClass.block.methods) {
            if (methodInstance.equals(m)) {
                return null;
            }
        }

        return methodInstance;
    }
}
