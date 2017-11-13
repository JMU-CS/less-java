package com.github.lessjava.visitor.impl;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.lessjava.types.SymbolTable;
import com.github.lessjava.types.ast.ASTExpression;
import com.github.lessjava.types.ast.ASTFunction;
import com.github.lessjava.types.ast.ASTFunction.Parameter;
import com.github.lessjava.types.ast.ASTFunctionCall;
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.types.ast.ASTVoidFunctionCall;
import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.types.inference.impl.HMTypeBase;
import com.github.lessjava.types.inference.impl.HMTypeVar;
import com.github.lessjava.visitor.LJAbstractAssignTypes;

public class LJInstantiateFunctions extends LJAbstractAssignTypes {
    private static ASTProgram program;

    private static Queue<ASTFunctionCall> functionInstancesToMake = new ArrayDeque<>();
    private static Queue<ASTVoidFunctionCall> voidFunctionInstancesToMake = new ArrayDeque<>();
    private static Set<String> instantiatedFunctions = new HashSet<>();

    @Override
    public void preVisit(ASTProgram node) {
	super.preVisit(node);
	if (LJInstantiateFunctions.program == null) {
	    LJInstantiateFunctions.program = node;
	}
    }

    @Override
    public void postVisit(ASTProgram node) {
	while (!functionInstancesToMake.isEmpty()) {
	    ASTFunctionCall f = functionInstancesToMake.poll();

	    instantiateFunction(f.name, f.arguments);
	}

	while (!voidFunctionInstancesToMake.isEmpty()) {
	    ASTVoidFunctionCall f = voidFunctionInstancesToMake.poll();

	    instantiateFunction(f.name, f.arguments);
	}
    }

    @Override
    public void postVisit(ASTFunctionCall node) {
	super.postVisit(node);

	boolean alreadyInstantiated = instantiatedFunctions.contains(node.name);

	if (alreadyInstantiated) {
	    return;
	}

	if (instantiateFunction(node.name, node.arguments)) {
	    instantiatedFunctions.add(node.name);
	} else {
	    functionInstancesToMake.offer(node);
	}

    }

    @Override
    public void postVisit(ASTVoidFunctionCall node) {
	boolean alreadyInstantiated = instantiatedFunctions.contains(node.name);

	if (alreadyInstantiated) {
	    return;
	}

	if (instantiateFunction(node.name, node.arguments)) {
	    instantiatedFunctions.add(node.name);
	} else {
	    voidFunctionInstancesToMake.offer(node);
	}

    }

    private boolean instantiateFunction(String name, List<ASTExpression> arguments) {
	// If the arguments aren't concrete, then we can't make an instance
	if (arguments.stream().anyMatch(arg -> arg.type instanceof HMTypeVar)) {
	    return false;
	}

	List<ASTFunction> functions = program.functions.stream().filter(func -> func.name.equals(name))
		.collect(Collectors.toList());

	if (functions == null) {
	    return true;
	}

	Optional<ASTFunction> prototype = functions.stream().filter(func -> !func.concrete).findFirst();

	if (!prototype.isPresent()) {
	    return true;
	}

	ASTFunction functionInstance = new ASTFunction(prototype.get().name, prototype.get().returnType,
		prototype.get().body);

	functionInstance.concrete = true;
	functionInstance.parameters = new ArrayList<>();

	for (int i = 0; i < arguments.size(); i++) {
	    String pname = prototype.get().parameters.get(i).name;
	    HMType type = new HMTypeBase(((HMTypeBase) arguments.get(i).type).getBaseType());
	    Parameter parameter = new Parameter(pname, type);
	    functionInstance.parameters.add(parameter);
	}

	if (program.functions.stream().noneMatch(func -> func.equals(functionInstance))) {
	    functionInstance.setParent(program);
	    functionInstance.setDepth(2);

	    program.functions.add(functionInstance);
	}

	return true;
    }

}
