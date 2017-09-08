package com.github.lessjava.visitor.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.lessjava.exceptions.InvalidProgramException;
import com.github.lessjava.types.ast.ASTAssignment;
import com.github.lessjava.types.ast.ASTBinaryExpr;
import com.github.lessjava.types.ast.ASTBinaryExpr.BinOp;
import com.github.lessjava.types.ast.ASTExpression;
import com.github.lessjava.types.ast.ASTFunction;
import com.github.lessjava.types.ast.ASTFunction.Parameter;
import com.github.lessjava.types.ast.ASTNode;
import com.github.lessjava.types.ast.ASTReturn;
import com.github.lessjava.types.ast.ASTVariable;
import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.types.inference.HMType.BaseDataType;
import com.github.lessjava.types.inference.impl.HMTypeBase;
import com.github.lessjava.types.inference.impl.HMTypeVar;
import com.github.lessjava.visitor.LJAbstractAssignTypes;

public class LJASTInferTypes extends LJAbstractAssignTypes
{
    // Map to bind nodes to their types
    private Map<ASTNode, HMType> nodeToHMType = new HashMap<>();

    // Map variables in a functions scope to function parameters
    private Map<Parameter, ASTVariable> parameterToVar = new HashMap<>();

    // Current function parameters
    private List<Parameter> parameters;
    private HMType          returnType;

    @Override
    public void preVisit(ASTFunction node)
    {
        this.parameters = node.parameters;
    }

    @Override
    public void postVisit(ASTFunction node)
    {
        parameters.forEach(p -> p.type = parameterToVar.get(p).type);
        parameters = null;
        node.returnType = returnType;
    }

    @Override
    public void postVisit(ASTReturn node)
    {
        this.returnType = node.value.type;
    }

    @Override
    public void preVisit(ASTBinaryExpr node)
    {
        node.type = new HMTypeBase(ASTBinaryExpr.opToReturnType(node.operator));

        nodeToHMType.put(node.leftChild, new HMTypeVar());
        nodeToHMType.put(node.rightChild, new HMTypeVar());
    }

    @Override
    // TODO: change unify to use HMTypes as inputs and spit out the unified type
    public void postVisit(ASTBinaryExpr node)
    {
        ASTExpression leftChild, rightChild;

        leftChild = node.leftChild;
        rightChild = node.rightChild;

        unify(leftChild, rightChild, node.operator);
    }

    @Override
    public void preVisit(ASTVariable node)
    {
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
        node.variable.type = node.value.type;
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
        } else if (leftIsVar && rightIsVar) { // TODO: Handle cases where they don't need to be the same
            left.type = right.type;
        } else if (!leftIsVar && !rightIsVar) {
            HMTypeBase leftBase = (HMTypeBase) left.type;
            HMTypeBase rightBase = (HMTypeBase) right.type;

            successfullyUnified = unify(leftBase, rightBase);
        } else {
            successfullyUnified = false;
            addError(new InvalidProgramException(String.format("Type Unification Error:\t%s, %s", left, right)));
        }

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

        return successfullyUnified;
    }

    private boolean unify(HMTypeBase left, HMTypeBase right)
    {
        boolean successfullyUnified = true;

        if (left.getBaseType() != right.getBaseType()) {
            successfullyUnified = false;
            addError(new InvalidProgramException(String.format("Type Unification Error:\t%s, %s",
                    HMType.typeToString(left), HMType.typeToString(right))));
        }

        return successfullyUnified;
    }

}
