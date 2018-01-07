package com.github.lessjava.visitor.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.lessjava.types.ast.ASTAbstractFunction;
import com.github.lessjava.types.ast.ASTAbstractFunction.Parameter;
import com.github.lessjava.types.ast.ASTBlock;
import com.github.lessjava.types.ast.ASTExpression;
import com.github.lessjava.types.ast.ASTFunction;
import com.github.lessjava.types.ast.ASTFunctionCall;
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.types.ast.ASTVoidFunctionCall;
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
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTFunctionCall node) {
        super.postVisit(node);


        ASTAbstractFunction f = nameparamFunctionMap.get(node.getNameArgString());

        if (f != null && f.concrete) {
            return;
        }

        f = instantiateFunction(node.name, node.arguments);

        if (f != null) {
            if (program.functions.contains(f)) {
                return;
            }

            program.functions.add(f);

            nameparamFunctionMap.put(node.getNameArgString(), f);
        }
    }

    @Override
    public void postVisit(ASTVoidFunctionCall node) {
        super.postVisit(node);

        ASTAbstractFunction f = nameparamFunctionMap.get(node.getNameArgString());

        if (f != null && f.concrete) {
            return;
        }

        f = instantiateFunction(node.name, node.arguments);

        if (f != null) {
            if (program.functions.contains(f)) {
                return;
            }

            program.functions.add(f);

            nameparamFunctionMap.put(node.getNameArgString(), f);
        }
    }

    private ASTFunction instantiateFunction(String name, List<ASTExpression> arguments) {
        if (arguments.stream().anyMatch(arg -> !arg.type.isConcrete)) {
            // Can't instantiate; arg isn't concrete
            return null;
        }

        List<ASTAbstractFunction> functions = program.functions.stream().filter(func -> func.name.equals(name))
                .collect(Collectors.toList());

        Optional<ASTAbstractFunction> prototype = functions.stream().filter(func -> !func.concrete).findFirst();

        if (!prototype.isPresent() || prototype.get().body == null) {
            // Can't instantiate
            return null;
        }

        ASTBlock blockCopy = new ASTBlock();
        blockCopy.statements = prototype.get().body.statements;
        blockCopy.variables = prototype.get().body.variables;

        ASTFunction functionInstance = new ASTFunction(prototype.get().name, prototype.get().returnType, blockCopy);

        blockCopy.setParent(functionInstance);

        functionInstance.concrete = true;
        functionInstance.parameters = new ArrayList<>();

        for (int i = 0; i < arguments.size(); i++) {
            String pname = prototype.get().parameters.get(i).name;
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

}
