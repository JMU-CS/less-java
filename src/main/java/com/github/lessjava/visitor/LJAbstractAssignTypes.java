package com.github.lessjava.visitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.lessjava.types.ast.ASTAbstractFunction;
import com.github.lessjava.types.ast.ASTBinaryExpr.BinOp;
import com.github.lessjava.types.ast.ASTClass;
import com.github.lessjava.types.ast.ASTMethod;
import com.github.lessjava.types.ast.ASTNode;
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.types.inference.HMType.BaseDataType;
import com.github.lessjava.types.inference.impl.HMTypeBase;
import com.github.lessjava.types.inference.impl.HMTypeClass;
import com.github.lessjava.types.inference.impl.HMTypeCollection;
import com.github.lessjava.types.inference.impl.HMTypeVar;
import com.github.lessjava.visitor.impl.StaticAnalysis;

public abstract class LJAbstractAssignTypes extends StaticAnalysis implements LJAssignTypes {
    protected static Map<String, List<ASTAbstractFunction>> idFunctionMap = new HashMap<>();

    @Override
    public void preVisit(ASTProgram node) {
        for (ASTAbstractFunction function : node.functions) {
            idFunctionMap.computeIfAbsent(function.getIdentifyingString(), k -> new ArrayList<>()).add(function);
        }

        for (ASTClass astClass: node.classes) {
            for (ASTMethod method: astClass.block.methods) {
                idFunctionMap.computeIfAbsent(method.getIdentifyingString(), k -> new ArrayList<>()).add(method);
            }
        }

        for (ASTAbstractFunction function: ASTAbstractFunction.libraryFunctions) {
            idFunctionMap.computeIfAbsent(function.getIdentifyingString(), k -> new ArrayList<>()).add(function);
        }
    }

    protected HMType unify(ASTNode node, HMType left, HMType right) {
        if(left == null) {
            return right;
        }
        if(right == null) {
            return left;
        }
        boolean leftIsBase = left instanceof HMTypeBase;
        boolean leftIsVar = left instanceof HMTypeVar;
        boolean leftIsCollection = left instanceof HMTypeCollection;
        boolean leftIsClass = left instanceof HMTypeClass;

        boolean rightIsBase = right instanceof HMTypeBase;
        boolean rightIsVar = right instanceof HMTypeVar;
        boolean rightIsCollection = right instanceof HMTypeCollection;
        boolean rightIsClass = right instanceof HMTypeClass;

        if (leftIsBase && rightIsBase) {
            return unify(node, (HMTypeBase) left, (HMTypeBase) right);
        } else if (leftIsVar && rightIsVar) {
            return unify((HMTypeVar) left, (HMTypeVar) right);
        } else if (leftIsCollection && rightIsCollection) {
            return unify(node, (HMTypeCollection) left, (HMTypeCollection) right);
        } else if (leftIsClass && rightIsClass) {
            return unify(node, (HMTypeClass)left, (HMTypeClass)right);
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

    protected HMType unify(ASTNode node, HMTypeClass left, HMTypeClass right) {
        Set<String> leftClasses = new HashSet<>();
        String leftClass = left.name;
        while(leftClass != null) {
            leftClasses.add(leftClass);
            leftClass = ASTClass.nameClassMap.get(leftClass).signature.superName;
        }
        String rightClass = right.name;
        while(rightClass != null && !leftClasses.contains(rightClass)) {
            rightClass = ASTClass.nameClassMap.get(rightClass).signature.superName;
        }
        if(rightClass != null) {
            return new HMTypeClass(rightClass);
        } else {
            addError(node, "Cannot unify classes " + left.name + " and " + right.name + "; classes have no common superclass.");
            return left;
        }
    }

    protected HMType unify(ASTNode node, HMTypeCollection left, HMTypeCollection right) {
        HMType unifiedType = left;

        if (left.collectionName.equals(right.collectionName)) {
            left.elementType = right.elementType = unify(node, left.elementType, right.elementType);
        }

        return unifiedType;
    }

    protected HMType unify(HMTypeVar left, HMTypeVar right) {
        return left;
    }

    protected HMTypeBase unify(ASTNode node, HMTypeBase left, HMTypeBase right) {
        if (left.getBaseType().equals(right.getBaseType())) {
            return left;
        } else if (left.getBaseType().equals(BaseDataType.DOUBLE) && right.getBaseType().equals(BaseDataType.INT)) {
            return left;
        } else if (right.getBaseType().equals(BaseDataType.DOUBLE) && left.getBaseType().equals(BaseDataType.INT)) {
            return right;
        }

        StaticAnalysis.addError(node, "Cannot unify types " + left + " and " + right);

        return left;
    }

    protected static HashSet<BinOp> ignoreOps = new HashSet<BinOp>(
            Arrays.asList(new BinOp[] { BinOp.EQ, BinOp.NE, BinOp.ADDASGN, BinOp.SUBASGN, BinOp.INVOKE }));

    protected HMType unify(ASTNode node, HMType left, HMType right, BinOp op) {
        HMType unifiedType = unify(node, left, right);

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
                    unifiedType = unify(node, left, right);
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
