package com.github.lessjava.visitor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.github.lessjava.types.ast.ASTAbstractFunction;
import com.github.lessjava.types.ast.ASTBinaryExpr.BinOp;
import com.github.lessjava.types.ast.ASTClass;
import com.github.lessjava.types.ast.ASTMethod;
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.types.inference.HMType.BaseDataType;
import com.github.lessjava.types.inference.impl.HMTypeBase;
import com.github.lessjava.types.inference.impl.HMTypeCollection;
import com.github.lessjava.types.inference.impl.HMTypeVar;
import com.github.lessjava.visitor.impl.StaticAnalysis;

public abstract class LJAbstractAssignTypes extends StaticAnalysis implements LJAssignTypes {
    protected static Map<String, ASTAbstractFunction> idFunctionMap = new HashMap<>();

    @Override
    public void preVisit(ASTProgram node) {
        for (ASTAbstractFunction function : node.functions) {
            idFunctionMap.put(function.getIdentifyingString(), function);

        }

        for (ASTClass astClass: node.classes) {
            for (ASTMethod method: astClass.block.methods) {
                idFunctionMap.put(method.getIdentifyingString(), method);
            }
        }
    }

    protected HMType unify(HMType left, HMType right) {
        boolean leftIsBase = left instanceof HMTypeBase;
        boolean leftIsVar = left instanceof HMTypeVar;
        boolean leftIsCollection = left instanceof HMTypeCollection;

        boolean rightIsBase = right instanceof HMTypeBase;
        boolean rightIsVar = right instanceof HMTypeVar;
        boolean rightIsCollection = right instanceof HMTypeCollection;

        if (leftIsBase && rightIsBase) {
            return unify((HMTypeBase) left, (HMTypeBase) right);
        } else if (leftIsVar && rightIsVar) {
            return unify((HMTypeVar) left, (HMTypeVar) right);
        } else if (leftIsCollection && rightIsCollection) {
            return unify((HMTypeCollection) left, (HMTypeCollection) right);
        }

        // TODO: This can't be right...
        // Promote to collection
        if (leftIsCollection && !rightIsCollection) {
            return left;
        } else if (!leftIsCollection && rightIsCollection) {
            return right;
        }

        if (leftIsVar && !rightIsVar) {
            return right;
        } else if (!leftIsVar && rightIsVar) {
            return left;
        }

        return left;
    }

    protected HMType unify(HMTypeCollection left, HMTypeCollection right) {
        HMType unifiedType = left;

        if (left.collectionName.equals(right.collectionName)) {
            left.elementType = right.elementType = unify(left.elementType, right.elementType);
        }

        return unifiedType;
    }

    protected HMType unify(HMTypeVar left, HMTypeVar right) {
        return left;
    }

    protected HMTypeBase unify(HMTypeBase left, HMTypeBase right) {
        if (left.getBaseType().equals(right.getBaseType())) {
            return left;
        } else if (left.getBaseType().equals(BaseDataType.REAL) && right.getBaseType().equals(BaseDataType.INT)) {
            return left;
        } else if (right.getBaseType().equals(BaseDataType.REAL) && left.getBaseType().equals(BaseDataType.INT)) {
            return right;
        }

        return left;
    }

    protected static HashSet<BinOp> ignoreOps = new HashSet<BinOp>(
            Arrays.asList(new BinOp[] { BinOp.EQ, BinOp.NE, BinOp.ADDASGN, BinOp.SUBASGN, BinOp.INVOKE }));

    protected HMType unify(HMType left, HMType right, BinOp op) {
        HMType unifiedType = unify(left, right);

        if (!ignoreOps.contains(op)) {
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
                case ASGN:
                    unifiedType = unify(left, right);
                    break;
                case AND:
                case OR:
                    unifiedType = new HMTypeBase(BaseDataType.BOOL);
                    break;
                default:
                    return null;
            }
        }

        return unifiedType;
    }
}
