package com.github.lessjava.visitor.impl;

import java.util.List;

import com.github.lessjava.exceptions.InvalidProgramException;
import com.github.lessjava.types.Symbol;
import com.github.lessjava.types.ast.ASTArgList;
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
import com.github.lessjava.types.ast.ASTVoidAssignment;
import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.types.inference.HMType.BaseDataType;
import com.github.lessjava.types.inference.impl.HMTypeBase;
import com.github.lessjava.types.inference.impl.HMTypeCollection;
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

        if (node.body == null) {
            return;
        }

        node.returnType = this.returnType == null ? new HMTypeBase(BaseDataType.VOID)
                : unify(node.returnType, this.returnType);
    }

    @Override
    public void postVisit(ASTReturn node) {
        super.postVisit(node);

        if (node.value == null) {
            this.returnType = new HMTypeBase(BaseDataType.VOID);
        } else if (node.value instanceof ASTVariable) {
            ASTVariable var = (ASTVariable) node.value;

            if (var.index != null && var.type instanceof HMTypeCollection) {
                this.returnType = ((HMTypeCollection) var.type).getCollectionType();
            } else {
                this.returnType = node.value.type;
            }
        } else {
            this.returnType = node.value.type;
        }
    }

    @Override
    public void preVisit(ASTBinaryExpr node) {
        super.preVisit(node);

        if (node.operator.equals(BinOp.INDEX)) {
            node.type = ((HMTypeCollection) node.leftChild.type).getCollectionType();
        } else if (node.operator.equals(BinOp.ASGN)) {
            node.type = node.rightChild.type;
        } else {
            node.type = new HMTypeBase(ASTBinaryExpr.opToReturnType(node.operator));
        }
    }

    @Override
    public void postVisit(ASTBinaryExpr node) {
        super.postVisit(node);

        ASTExpression leftChild, rightChild;

        leftChild = node.leftChild;
        rightChild = node.rightChild;

        leftChild.type = rightChild.type = unify(leftChild.type, rightChild.type, node.operator);
    }

    @Override
    public void postVisit(ASTFunctionCall node) {
        super.postVisit(node);

        if (nameparamFunctionMap.containsKey(node.getNameArgString())) {
            node.type = unify(node.type, nameparamFunctionMap.get(node.getNameArgString()).returnType);
        }
    }

    @Override
    public void preVisit(ASTVariable node) {
        super.preVisit(node);

        if (parameters == null) {
            return;
        }

        for (Parameter p : parameters) {
            if (p.name.equals(node.name)) {
                node.type = unify(node.type, p.type);
            }
        }
    }

    @Override
    public void preVisit(ASTArgList node) {
        super.preVisit(node);

        node.isConcrete = node.isConcrete || node.collectionType instanceof HMTypeBase;
    }

    @Override
    public void postVisit(ASTArgList node) {
        super.postVisit(node);

        if (!node.arguments.isEmpty()) {
            node.type = node.arguments.get(0).type = unify(node.type, node.arguments.get(0).type);
            node.type.isCollection = true;
            // TODO: actually determine if concrete..
            node.type.isConcrete = true;
        }
    }

    @Override
    public void postVisit(ASTVariable node) {
        super.postVisit(node);

        List<Symbol> symbols = BuildSymbolTables.searchScopesForSymbol(node, node.name);

        if (symbols != null && !symbols.isEmpty()) {
            for (Symbol symbol : symbols) {
                node.type = unify(node.type, symbol.type);
            }
        }
    }

    @Override
    public void postVisit(ASTVoidAssignment node) {
        super.postVisit(node);

        System.err.println(node.value.type);

        node.variable.type = node.value.type;

        if (node.value instanceof ASTVariable) {
            ASTVariable var = (ASTVariable) node.value;

            if (var.index != null && var.type instanceof HMTypeCollection) {
                node.variable.type = ((HMTypeCollection) var.type).getCollectionType();
            }
        }
    }

    @Override
    public void postVisit(ASTAssignment node) {
        super.postVisit(node);

        node.variable.type = node.value.type;

        if (node.value instanceof ASTVariable) {
            ASTVariable var = (ASTVariable) node.value;

            if (var.index != null && var.type instanceof HMTypeCollection) {
                node.variable.type = ((HMTypeCollection) var.type).getCollectionType();
            }
        }

        node.type = node.variable.type;
    }

    @Override
    public void preVisit(ASTConditional node) {
        super.preVisit(node);

        node.condition.type = unify(node.condition.type, new HMTypeBase(BaseDataType.BOOL));
    }

    private HMType unify(HMType left, HMType right) {
        HMType unifiedType = left;

        boolean leftIsBase = left instanceof HMTypeBase;
        boolean rightIsBase = right instanceof HMTypeBase;
        boolean leftIsVar = left instanceof HMTypeVar;
        boolean rightIsVar = right instanceof HMTypeVar;
        boolean leftIsCollection = left instanceof HMTypeCollection;
        boolean rightIsCollection = right instanceof HMTypeCollection;

        if (leftIsCollection && !rightIsCollection) {
            HMTypeCollection cleft = (HMTypeCollection) left;
            unifiedType = new HMTypeCollection(unify(cleft.getCollectionType(), right));
        } else if (!leftIsCollection && rightIsCollection) {
            HMTypeCollection cright = (HMTypeCollection) right;
            unifiedType = new HMTypeCollection(unify(cright.getCollectionType(), left));
        } else if (leftIsCollection && rightIsCollection) {
            HMTypeCollection cleft = (HMTypeCollection) left;
            HMTypeCollection cright = (HMTypeCollection) left;
            unifiedType = new HMTypeCollection(unify(cleft.getCollectionType(), cright.getCollectionType()));
        } else if (!leftIsBase && rightIsVar) {
            unifiedType = left;
        } else if (leftIsVar && rightIsBase) {
            unifiedType = right;
        } else if (leftIsBase && rightIsBase) {
            HMTypeBase leftBase = (HMTypeBase) left;
            HMTypeBase rightBase = (HMTypeBase) right;

            if (unify(leftBase, rightBase)) {
                unifiedType = leftBase;
            }
        }

        return unifiedType;
    }

    private HMType unify(HMType left, HMType right, BinOp op) {
        // HMType unifiedType = unify(left, right);
        HMType unifiedType = null;

        if (op != BinOp.EQ && op != BinOp.NE) {
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
                case ASGN:
                    unifiedType = unify(left, right);
                    break;
                default:
                    return null;
            }
        } else {
            unifiedType = unify(left, right);
        }

        return unifiedType;
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
}
