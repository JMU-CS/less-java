package com.github.lessjava.visitor.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.lessjava.types.Symbol;
import com.github.lessjava.types.ast.ASTAbstractFunction;
import com.github.lessjava.types.ast.ASTAbstractFunction.Parameter;
import com.github.lessjava.types.ast.ASTBlock;
import com.github.lessjava.types.ast.ASTClass;
import com.github.lessjava.types.ast.ASTExpression;
import com.github.lessjava.types.ast.ASTFunction;
import com.github.lessjava.types.ast.ASTFunctionCall;
import com.github.lessjava.types.ast.ASTMethod;
import com.github.lessjava.types.ast.ASTMethodCall;
import com.github.lessjava.types.ast.ASTNode;
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.types.ast.ASTStatement;
import com.github.lessjava.types.ast.ASTVoidFunctionCall;
import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.types.inference.impl.HMTypeBase;
import com.github.lessjava.types.inference.impl.HMTypeClass;
import com.github.lessjava.types.inference.impl.HMTypeCollection;
import com.github.lessjava.types.inference.impl.HMTypeVar;
import com.github.lessjava.visitor.LJAbstractAssignTypes;

public class LJInstantiateFunctions extends LJAbstractAssignTypes {
    private ASTProgram program;
    private ASTClass currentClass;
    private ASTMethod currentMethod;

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
    public void preVisit(ASTClass node) {
        super.postVisit(node);
        currentClass = node;
    }

    @Override
    public void postVisit(ASTClass node) {
        super.postVisit(node);
        currentClass = null;
    }

    @Override
    public void preVisit(ASTMethod node) {
        super.postVisit(node);
        currentMethod = node;
    }

    @Override
    public void postVisit(ASTMethod node) {
        super.postVisit(node);
        currentMethod = null;
    }

    @Override
    public void postVisit(ASTFunctionCall node) {
        super.postVisit(node);

        // Already handled as a method
        if (node.getParent() instanceof ASTMethodCall) {
            return;
        }

        if(node.name.equals(("super"))) {
            if(currentMethod == null || !currentMethod.isConstructor) {
                addError(node, "Cannot call super outside of a constructor");
            } else if(currentClass.parent == null) {
                addError(node, "Cannot call super constructor without a parent class");
            } else {
                if(!isFirstStatementInParent(node)) {
                    addError(node, "Call to super constructor must be the first statement in a constructor");
                }
                ASTFunction superconstructor = currentClass.parent.block.constructors.stream()
                        .map(c -> c.function)
                        .filter(c -> c.parameters.size() == node.arguments.size())
                        .findFirst().orElse(null);
                if(superconstructor == null) {
                    addError(node, "No matching super constructor found");
                } else {
                    verifyConstructorCall(node, superconstructor);
                }
            }
            return;
        }

        ArrayList<ASTAbstractFunction> functions = new ArrayList<>(program.functions);
        program.classes.forEach(c -> functions.addAll(c.block.constructors));

        ASTAbstractFunction prototype = functions.stream()
            .filter(f -> f.body != null && f.name.equals(node.name) && f.parameters.size() == node.arguments.size())
            .findAny()
            .orElse(null);

        // Don't need to instantiate library functions
        if(prototype == null && ASTAbstractFunction.libraryFunctions.stream().anyMatch(f -> f.name.equals(node.name))) {
            return;
        }

        if (prototype == null) {
            StaticAnalysis.addError(node, "Cannot find function " + node.name + " with " + node.arguments.size() + " arguments");
            return;
        }
        
        // If this is a call to a constructor, verify that the arguments all match up
        if(prototype instanceof ASTMethod) {
            verifyConstructorCall(node, ((ASTMethod) prototype).function);
        }

        // Don't instantiate if there's either already a concrete implementation or if the prototype is a constructor (ASTMethod)
        if (prototype.concrete || prototype instanceof ASTMethod) {
            return;
        }

        ASTFunction f = instantiateFunction(prototype, node.arguments);

        if (f != null) {
            // Use the new mangled function name as the function call name
            node.name = f.name;
            if (program.functions.contains(f)) {
                return;
            }

            program.functions.add(f);

            idFunctionMap.computeIfAbsent(node.getIdentifyingString(), k -> new ArrayList<>()).add(f);
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

        // Mangle function name to support multiple return types
        functionInstance.name = mangleFunctionName(functionInstance.name, arguments);

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
            node.funcCall.name = m.function.name;
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

        functionInstance.name = mangleFunctionName(functionInstance.name, arguments);
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

    private boolean isFirstStatementInParent(ASTFunctionCall node) {
        ASTNode block = node.getParent();
        while(!(block instanceof ASTBlock)) {
            block = block.getParent();
        }
        ASTStatement firstStatement = ((ASTBlock) block).statements.get(0);
        return firstStatement instanceof ASTVoidFunctionCall && ((ASTVoidFunctionCall) firstStatement).functionCall == node;
    }

    /**
     * Verify that each argument sent to a constructor is of the type that the constructor requires
     * @param node the call to the constructor
     * @param constructor the constructor implementation
     */
    public void verifyConstructorCall(ASTFunctionCall node, ASTFunction constructor) {
        for (int i = 0; i < constructor.parameters.size(); i++) {
            HMType arg = node.arguments.get(i).type;
            HMType param = constructor.parameters.get(i).type;
            boolean fits = false;
            if (arg instanceof HMTypeBase || arg instanceof HMTypeCollection) {
                fits = arg.equals(param);
            } else if (arg instanceof HMTypeClass) {
                if (!(param instanceof HMTypeClass)) {
                    fits = false;
                } else {
                    ASTClass argClass = ASTClass.nameClassMap.get(((HMTypeClass) arg).name);
                    ASTClass paramClass = ASTClass.nameClassMap.get(((HMTypeClass) param).name);
                    while (argClass != null && paramClass != argClass) {
                        argClass = argClass.parent;
                    }
                    if (argClass == null) {
                        fits = false;
                    }
                }
            }
            if (!fits) {
                addError(node, "Argument " + i + " to " + node.name + " is of type " + arg + "; expected " + param);
            }
        }
    }

    private String mangleFunctionName(String name, List<ASTExpression> arguments) {
        String mangledName = String.format(
                "%s$%s",
                name,
                arguments.stream()
                         .map(a -> a.type.toString().replaceAll("<", "LAB").replaceAll(">", "RAB").replaceAll(", ", "\\$"))
                         .collect(Collectors.joining("$"))
            );

        return mangledName;
    }
}
