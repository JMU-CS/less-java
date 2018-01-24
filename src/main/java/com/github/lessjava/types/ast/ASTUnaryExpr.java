package com.github.lessjava.types.ast;

import com.github.lessjava.types.inference.impl.HMTypeBase;

/**
 * Unary operation with an operation tag and a child sub-expression. When
 * executed, the program should fully evaluate the child and then apply the
 * given operation to the resulting value.
 */
public class ASTUnaryExpr extends ASTExpression {
    /**
     * List of permitted unary operations.
     *
     * <ul>
     * <li>NOT - boolean not</li>
     * <li>NEG - negation</li>
     * </ul>
     */
    public enum UnaryOp {
        INVALID, NOT, NEG
    }

    public static String opToString(UnaryOp op) {
        switch (op) {
            case NOT:
                return "!";
            case NEG:
                return "-";
            default:
                System.err.println(op);
                return "???";
        }
    }

    public UnaryOp operator;
    public ASTExpression child;

    public ASTUnaryExpr(UnaryOp operator, ASTExpression child) {
        this.operator = operator;
        this.child = child;
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        child.traverse(visitor);
        visitor.postVisit(this);
    }

    @Override
    public String toString() {
        String s = String.format("(%s%s)", opToString(operator), child.toString());

        String t;
        if (this.type.equals(HMTypeBase.BOOL)) {
            t = "Boolean.valueOf(%s)";
        } else if (this.type.equals(HMTypeBase.INT)) {
            t = "Integer.valueOf(%s)";
        } else if (this.type.equals(HMTypeBase.REAL)) {
            t = "Double.valueOf(%s)";
        } else {
            t = "%s";
        }

        return String.format(t,s);
    }
}
