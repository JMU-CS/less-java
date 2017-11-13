package com.github.lessjava.visitor.impl;

import java.util.ArrayList;
import java.util.List;

import com.github.lessjava.exceptions.InvalidProgramException;
import com.github.lessjava.types.ast.ASTAssignment;
import com.github.lessjava.types.ast.ASTBinaryExpr;
import com.github.lessjava.types.ast.ASTBinaryExpr.BinOp;
import com.github.lessjava.types.ast.ASTConditional;
import com.github.lessjava.types.ast.ASTExpression;
import com.github.lessjava.types.ast.ASTFunction;
import com.github.lessjava.types.ast.ASTFunction.Parameter;
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.types.ast.ASTReturn;
import com.github.lessjava.types.ast.ASTVariable;
import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.types.inference.HMType.BaseDataType;
import com.github.lessjava.types.inference.impl.HMTypeBase;
import com.github.lessjava.types.inference.impl.HMTypeVar;
import com.github.lessjava.visitor.LJAbstractAssignTypes;

public class LJASTInferTypes extends LJAbstractAssignTypes {
    private HMType returnType;

    private static ASTProgram program;

    private List<Parameter> parameters;

    @Override
    public void preVisit(ASTProgram node) {
	super.preVisit(node);
	if (LJASTInferTypes.program == null) {
	    LJASTInferTypes.program = node;
	}
    }

    @Override
    public void preVisit(ASTFunction node) {
	super.preVisit(node);
	this.returnType = null;
	this.parameters = node.parameters;
    }

    @Override
    public void postVisit(ASTFunction node) {
	super.postVisit(node);

	node.concrete = node.parameters.stream().noneMatch(p -> p.type instanceof HMTypeVar);
	this.parameters = null;

	if (!node.concrete) {
	    unify(node, this.returnType);
	}
    }

    @Override
    public void postVisit(ASTReturn node) {
	super.postVisit(node);

	this.returnType = node.value == null ? new HMTypeBase(BaseDataType.VOID) : node.value.type;
    }

    @Override
    public void preVisit(ASTBinaryExpr node) {
	super.preVisit(node);
	node.type = new HMTypeBase(ASTBinaryExpr.opToReturnType(node.operator));
    }

    @Override
    public void postVisit(ASTBinaryExpr node) {
	super.postVisit(node);
	ASTExpression leftChild, rightChild;

	leftChild = node.leftChild;
	rightChild = node.rightChild;

	unify(leftChild, rightChild, node.operator);
    }

    @Override
    public void preVisit(ASTVariable node) {
	super.preVisit(node);

	if (this.parameters == null) {
	    return;
	}

	for (Parameter p : parameters) {
	    if (p.name.equals(node.name)) {
		unify(p, node);
	    }
	}
    }

    @Override
    public void postVisit(ASTVariable node) {
	super.postVisit(node);
    }

    @Override
    public void postVisit(ASTAssignment node) {
	super.postVisit(node);
	node.variable.type = node.value.type;
    }

    @Override
    public void preVisit(ASTConditional node) {
	super.preVisit(node);
	unify(node.condition, BaseDataType.BOOL);
    }

    private boolean unify(ASTExpression left, BaseDataType right) {
	boolean successfullyUnified = true;

	HMTypeBase baseRight = new HMTypeBase(right);

	if (left.type instanceof HMTypeVar) {
	    left.type = new HMTypeBase(right);
	} else if (left.type instanceof HMTypeBase) {
	    successfullyUnified = unify((HMTypeBase) left.type, baseRight);
	}

	return successfullyUnified;
    }

    private boolean unify(ASTExpression left, ASTExpression right) {
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
	    addError(new InvalidProgramException(String.format("Type Unification Error:\t%s, %s", left, right)));
	}

	return successfullyUnified;
    }

    private boolean unify(ASTExpression left, ASTExpression right, BinOp op) {
	boolean successfullyUnified = unify(left, right);

	if (successfullyUnified) {
	    HMTypeBase unifiedType = null;

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

	    left.type = right.type = unifiedType;
	}

	return successfullyUnified;
    }

    private boolean unify(HMTypeBase left, HMTypeBase right) {
	boolean successfullyUnified = true;

	if (left.getBaseType() != right.getBaseType()) {
	    successfullyUnified = false;
	    addError(new InvalidProgramException(
		    String.format("Type Unification Error:\t%s, %s", left.toString(), right.toString())));
	}

	return successfullyUnified;
    }

    private boolean unify(Parameter left, ASTVariable right) {
	boolean successfulyUnified = true;

	boolean leftIsVar = left.type instanceof HMTypeVar;
	boolean rightIsVar = right.type instanceof HMTypeVar;

	if (!leftIsVar && !rightIsVar) {
	    successfulyUnified = unify((HMTypeBase) left.type, (HMTypeBase) right.type);
	} else if (leftIsVar && !rightIsVar) {
	    left.type = new HMTypeBase(((HMTypeBase) right.type).getBaseType());
	} else if (!leftIsVar && rightIsVar) {
	    right.type = new HMTypeBase(((HMTypeBase) left.type).getBaseType());
	} else if (leftIsVar && rightIsVar) {
	    left.type = right.type;
	}

	return successfulyUnified;
    }

    private boolean unify(ASTFunction left, HMType right) {
	boolean successfulyUnified = true;

	boolean leftIsVar = left.returnType instanceof HMTypeVar;
	boolean rightIsVar = right instanceof HMTypeVar;

	if (!leftIsVar && !rightIsVar) {
	    successfulyUnified = unify((HMTypeBase) left.returnType, (HMTypeBase) right);
	} else if (leftIsVar && !rightIsVar) {
	    left.returnType = new HMTypeBase(((HMTypeBase) right).getBaseType());
	}

	return successfulyUnified;
    }

}
