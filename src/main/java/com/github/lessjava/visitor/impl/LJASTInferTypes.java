package com.github.lessjava.visitor.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.lessjava.exceptions.InvalidProgramException;
import com.github.lessjava.types.Symbol;
import com.github.lessjava.types.ast.ASTAssignment;
import com.github.lessjava.types.ast.ASTBinaryExpr;
import com.github.lessjava.types.ast.ASTBinaryExpr.BinOp;
import com.github.lessjava.types.ast.ASTConditional;
import com.github.lessjava.types.ast.ASTExpression;
import com.github.lessjava.types.ast.ASTFunction;
import com.github.lessjava.types.ast.ASTFunction.Parameter;
import com.github.lessjava.types.ast.ASTFunctionCall;
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.types.ast.ASTReturn;
import com.github.lessjava.types.ast.ASTVariable;
import com.github.lessjava.types.ast.ASTVoidFunctionCall;
import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.types.inference.HMType.BaseDataType;
import com.github.lessjava.types.inference.impl.HMTypeBase;
import com.github.lessjava.types.inference.impl.HMTypeVar;
import com.github.lessjava.visitor.LJAbstractAssignTypes;

public class LJASTInferTypes extends LJAbstractAssignTypes
{

    // Map variables in a functions scope to function parameters
    private Map<Parameter, ASTVariable> parameterToVar = new HashMap<>();

    // Current function parameters
    private List<Parameter> parameters;
    private HMType          returnType;

    private static ASTProgram program;

    @Override
    public void preVisit(ASTProgram node)
    {
        super.preVisit(node);
        if (LJASTInferTypes.program == null) {
            LJASTInferTypes.program = node;
        }
    }

    @Override
    public void preVisit(ASTFunction node)
    {
        super.preVisit(node);
        this.parameters = node.parameters;
        this.returnType = null;
    }

    @Override
    public void postVisit(ASTFunction node)
    {
        super.postVisit(node);
        this.parameters.forEach(p -> p.type = parameterToVar.containsKey(p) ? parameterToVar.get(p).type
                                                                            : null);
        this.parameters = null;
        node.returnType = returnType != null ? returnType : new HMTypeBase(BaseDataType.VOID);

        node.concrete = node.parameters.stream().noneMatch(p -> p.type instanceof HMTypeVar);
    }

    @Override
    public void postVisit(ASTFunctionCall node)
    {
        super.postVisit(node);
        instantiateFunction(node.name, node.arguments);

    }

    @Override
    public void postVisit(ASTVoidFunctionCall node)
    {
        super.postVisit(node);
        instantiateFunction(node.name, node.arguments);
    }

    private void instantiateFunction(String name, List<ASTExpression> arguments)
    {
//        program.functions.stream().map(ASTFunction::getParameterStr).forEach(System.out::println);
//        program.functions.stream().map(func -> func.concrete).forEach(System.out::println);
        
        List<ASTFunction> functions = program.functions.stream()
                                                       .filter(func -> func.name.equals(name))
                                                       .collect(Collectors.toList());

        if (functions == null) {
            return;
        }

        Optional<ASTFunction> prototype = functions.stream()
                                                   .filter(func -> !func.concrete)
                                                   .findFirst();

        if (!prototype.isPresent()) {
            return;
        }
        

        ASTFunction functionInstance = new ASTFunction(prototype.get().name,
                                                       prototype.get().returnType,
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
    }

    @Override
    public void postVisit(ASTReturn node)
    {
        super.postVisit(node);
        this.returnType = node.value.type;
    }

    @Override
    public void preVisit(ASTBinaryExpr node)
    {
        super.preVisit(node);
        node.type = new HMTypeBase(ASTBinaryExpr.opToReturnType(node.operator));
    }

    @Override
    public void postVisit(ASTBinaryExpr node)
    {
        super.postVisit(node);
        ASTExpression leftChild, rightChild;

        leftChild = node.leftChild;
        rightChild = node.rightChild;

        unify(leftChild, rightChild, node.operator);
    }

    @Override
    public void preVisit(ASTVariable node)
    {
        super.preVisit(node);

        Optional<ASTVariable> visited = visitedVariables.stream()
                                                        .filter(var -> node.name.equals(var.name))
                                                        .findFirst();

        if (visited.isPresent()) {
            node.type = visited.get().type;
        } else {
            visitedVariables.add(node);
        }

        if (parameters == null) {
            return;
        }

        for (Parameter p : parameters) {
            if (p.name.equals(node.name)) {
                parameterToVar.put(p, node);
            }
        }
    }

    @Override
    public void postVisit(ASTAssignment node)
    {
        super.postVisit(node);
        node.variable.type = node.value.type;
    }

    @Override
    public void preVisit(ASTConditional node)
    {
        super.preVisit(node);
        unify(node.condition, BaseDataType.BOOL);
    }

    private boolean unify(ASTExpression left, BaseDataType right)
    {
        boolean successfullyUnified = true;

        HMTypeBase baseRight = new HMTypeBase(right);

        if (left.type instanceof HMTypeVar) {
            left.type = new HMTypeBase(right);
        } else if (left.type instanceof HMTypeBase) {
            successfullyUnified = unify((HMTypeBase) left.type, baseRight);
        }

        return successfullyUnified;
    }

    private boolean unify(ASTExpression left, ASTExpression right)
    {
        boolean successfullyUnified = true;

        boolean leftIsVar = left.type instanceof HMTypeVar;
        boolean rightIsVar = right.type instanceof HMTypeVar;

        if (!leftIsVar && rightIsVar) {
            right.type = left.type;
        } else if (leftIsVar && !rightIsVar) {
            left.type = right.type;
        } else if (leftIsVar && rightIsVar) { // TODO: Handle cases where they
                                              // don't need to be the same
            left.type = right.type;
        } else if (!leftIsVar && !rightIsVar) {
            HMTypeBase leftBase = (HMTypeBase) left.type;
            HMTypeBase rightBase = (HMTypeBase) right.type;

            successfullyUnified = unify(leftBase, rightBase);
        } else {
            successfullyUnified = false;
            addError(new InvalidProgramException(String.format("Type Unification Error:\t%s, %s",
                                                               left, right)));
        }

        updateSymbolTable(left);
        updateSymbolTable(right);

        return successfullyUnified;
    }

    private boolean unify(ASTExpression left, ASTExpression right, BinOp op)
    {
        boolean successfullyUnified = unify(left, right);

        HMTypeBase unifiedType = new HMTypeBase(BaseDataType.UNKNOWN);

        if (successfullyUnified) {
            switch (op) {
                case ADD:
                case SUB:
                case MUL:
                case DIV:
                case MOD:
                case GT:
                case LT:
                case GE:
                case LE:
                    unifiedType = new HMTypeBase(BaseDataType.INT);
                    break;
                case AND:
                case OR:
                    unifiedType = new HMTypeBase(BaseDataType.BOOL);
                    break;
                default:
                    return successfullyUnified;
            }
        }

        left.type = right.type = unifiedType;

        updateSymbolTable(left);
        updateSymbolTable(right);

        return successfullyUnified;
    }

    private boolean unify(HMTypeBase left, HMTypeBase right)
    {
        boolean successfullyUnified = true;

        if (left.getBaseType() != right.getBaseType()) {
            successfullyUnified = false;
            addError(new InvalidProgramException(String.format("Type Unification Error:\t%s, %s",
                                                               left.toString(),
                                                               right.toString())));
        }

        return successfullyUnified;
    }

    private void updateSymbolTable(ASTExpression node)
    {
        if (node instanceof ASTVariable) {
            ASTVariable var = (ASTVariable) node;

            Optional<Symbol> oldSymbol = scopes.peek().getSymbols().stream()
                                               .filter(s -> s.name.equals(var.name))
                                               .findFirst();

            if (oldSymbol.isPresent()) {
                oldSymbol.get().type = node.type;
            }
        }

    }

}
