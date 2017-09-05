package com.github.lessjava.visitor.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.lessjava.exceptions.InvalidProgramException;
import com.github.lessjava.types.ast.ASTAssignment;
import com.github.lessjava.types.ast.ASTBinaryExpr;
import com.github.lessjava.types.ast.ASTExpression;
import com.github.lessjava.types.ast.ASTFunction;
import com.github.lessjava.types.ast.ASTFunction.Parameter;
import com.github.lessjava.types.ast.ASTLiteral;
import com.github.lessjava.types.ast.ASTNode;
import com.github.lessjava.types.ast.ASTReturn;
import com.github.lessjava.types.ast.ASTVariable;
import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.types.inference.iml.HMTypeBase;
import com.github.lessjava.types.inference.iml.HMTypeFunction;
import com.github.lessjava.types.inference.iml.HMTypeVar;
import com.github.lessjava.visitor.LJAbstractAssignTypes;

public class LJASTInferTypes extends LJAbstractAssignTypes
{
    // Map to bind nodes to their types
    private Map<ASTNode, HMType> nodeToHMType = new HashMap<>();

    // Map variables in a functions scope to function parameters
    private Map<Parameter, ASTVariable> parameterToVar = new HashMap<>();

    // Map nodes to list of type constraints
    private Map<ASTExpression, Set<ASTNode.DataType>> nodeToConstraints = new HashMap<>();

    // Current function parameters
    private List<Parameter>  parameters;
    private ASTNode.DataType returnType;

    @Override
    public void preVisit(ASTFunction node)
    {
        nodeToHMType.put(node, new HMTypeFunction(new HMTypeVar(), new HMTypeVar()));

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
        node.type = ASTBinaryExpr.opToReturnType(node.operator);

        nodeToHMType.put(node.leftChild, new HMTypeVar());
        nodeToHMType.put(node.rightChild, new HMTypeVar());
    }

    @Override
    public void postVisit(ASTBinaryExpr node)
    {
        ASTExpression leftChild, rightChild;

        leftChild = node.leftChild;
        rightChild = node.rightChild;

        if (ASTBinaryExpr.flexibleOperators.contains(node.operator)) {
            assumeIfUnknown(leftChild, ASTNode.DataType.EQ);
            assumeIfUnknown(rightChild, ASTNode.DataType.EQ);

            if (leftChild.type != rightChild.type) {
                unify(leftChild, rightChild);
            }
        } else if (ASTBinaryExpr.arithmeticOperators.contains(node.operator)) {
            assumeIfUnknown(leftChild, ASTNode.DataType.INT);
            assumeIfUnknown(rightChild, ASTNode.DataType.INT);

            unify(leftChild, rightChild);
        } else if (ASTBinaryExpr.booleanOperators.contains(node.operator)) {
            assumeIfUnknown(leftChild, ASTNode.DataType.BOOL);
            assumeIfUnknown(rightChild, ASTNode.DataType.BOOL);

            unify(leftChild, rightChild);
        }
    }

    @Override
    public void preVisit(ASTVariable node)
    {
        nodeToHMType.put(node, new HMTypeVar());

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
    public void postVisit(ASTVariable node)
    {
        if (node.type != ASTNode.DataType.UNKNOWN) {
            nodeToHMType.put(node, new HMTypeBase(node.type));
            nodeToConstraints.computeIfAbsent(node, key -> new HashSet<>()).add(node.type);
        }
    }

    @Override
    public void preVisit(ASTLiteral node)
    {
        nodeToHMType.put(node, new HMTypeBase(node.type));
    }

    @Override
    public void postVisit(ASTAssignment node)
    {
        node.variable.type = node.value.type;
    }

    // TODO
    private void unify(ASTExpression left, ASTExpression right)
    {
        if (left.type.equals(ASTNode.DataType.EQ) && nodeToHMType.get(right) instanceof HMTypeBase) {
            left.type = right.type;
        } else if (nodeToHMType.get(left) instanceof HMTypeBase && right.type.equals(ASTNode.DataType.EQ)) {
            right.type = left.type;
        }

        boolean isTypeError = false;

        if (isTypeError) {
            addError(new InvalidProgramException(String.format("Type Error:\t%s != %s", left, right)));
        }
    }

    private void assumeIfUnknown(ASTExpression node, ASTNode.DataType typeToAssume)
    {
        if (node.type.equals(ASTNode.DataType.UNKNOWN)) {
            node.type = typeToAssume;
        }
    }
}
