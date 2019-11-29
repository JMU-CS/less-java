package com.github.lessjava.visitor.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private ASTProgram program;

    @Override
    public void preVisit(ASTProgram node) {
        super.preVisit(node);
        if (program == null) {
            program = node;
        }
    }

    @Override
    public void postVisit(ASTProgram node) {
        super.postVisit(node);
        program = null;
    }

    @Override
    public void postVisit(ASTFunctionCall node) {
        super.postVisit(node);

        // Already handled as a method
        if (node.getParent() instanceof ASTMethodCall) {
            return;
        }

        // Don't need to instantiate library functions
        if(ASTAbstractFunction.libraryFunctions.stream().anyMatch(f -> f.name.equals(node.name))) {
            return;
        }

        if(node.name.equals(("super"))) {
            return;
        }

        ArrayList<ASTAbstractFunction> functions = new ArrayList<>(program.functions);
        program.classes.forEach(c -> functions.addAll(c.block.constructors));

        ASTAbstractFunction prototype = functions.stream()
            .filter(f -> f.name.equals(node.name) && f.parameters.size() == node.arguments.size())
            .findAny()
            .orElse(null);

        if (prototype == null) {
            StaticAnalysis.addError(node, "Cannot find function " + node.name + " with " + node.arguments.size() + " arguments");
            return;
        }

        // Don't instantiate if there's either already a concrete implementation or if the prototype is a constructor (ASTMethod)
        if (prototype.concrete || prototype instanceof ASTMethod) {
            return;
        }

        ASTFunction f = instantiateFunction(prototype, node.arguments);

        if (f != null) {
            if (program.functions.contains(f)) {
                return;
            }

            program.functions.add(f);

            idFunctionMap.get(node.getIdentifyingString()).add(f);
        }
    }

    private ASTFunction instantiateFunction(ASTAbstractFunction prototype, List<ASTExpression> arguments) {
        if (arguments.stream().anyMatch(arg -> !arg.type.isConcrete)) {
            // Can't instantiate; arg isn't concrete
            return null;
        }

        LJDuplicateBlock blockDuplicator = new LJDuplicateBlock();
        ASTBlock blockCopy = blockDuplicator.duplicateBlock(prototype.body);

        ASTFunction functionInstance = new ASTFunction(prototype.name, prototype.returnType, blockCopy);

        blockCopy.setParent(functionInstance);

        functionInstance.concrete = true;
        functionInstance.parameters = new ArrayList<>();
        functionInstance.lineNumber = prototype.lineNumber;

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

        LJDuplicateBlock blockDuplicator = new LJDuplicateBlock();
        ASTBlock blockCopy = blockDuplicator.duplicateBlock(prototype.body);

        ASTFunction functionInstance = new ASTFunction(prototype.name, prototype.returnType, blockCopy);

        blockCopy.setParent(functionInstance);

        functionInstance.concrete = true;
        functionInstance.parameters = new ArrayList<>();
        functionInstance.lineNumber = prototype.lineNumber;

        for (int i = 0; i < arguments.size(); i++) {
            String pname = prototype.parameters.get(i).name;
            Parameter parameter = new Parameter(pname, arguments.get(i).type.clone());
            functionInstance.parameters.add(parameter);
        }

        ASTMethod methodInstance = new ASTMethod(prototype.scope, functionInstance, prototype.containingClassName);

        functionInstance.setParent(methodInstance);
        functionInstance.setDepth(2);

        for (ASTMethod m : containingClass.block.methods) {
            if (methodInstance.equals(m)) {
                return null;
            }
        }

        return methodInstance;
    }
}
