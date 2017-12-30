package com.github.lessjava.types.ast;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.github.lessjava.types.inference.HMType.BaseDataType;
import com.github.lessjava.types.inference.impl.HMTypeBase;

/**
 * Decaf binary operation with an operation tag and two child sub-expressions.
 * When executed, the program should evaluate the left-hand side and the
 * right-hand side separately and then perform the given operation on the two
 * values. Some operations (e.g., boolean AND and OR) can be short-circuited.
 */
public class ASTBinaryExpr extends ASTExpression {
    /**
     * List of permitted binary operations.
     *
     * <ul>
     * <li>OR - boolean or (inclusive)</li>
     * <li>AND - boolean and</li>
     * <li>EQ - equal to</li>
     * <li>NEQ - not equal to</li>
     * <li>LT - less than</li>
     * <li>GT - greater than</li>
     * <li>LEQ - less than or equal to</li>
     * <li>GEQ - greater than or equal to</li>
     * <li>ADD - addition</li>
     * <li>SUB - subtraction</li>
     * <li>MUL - multiplication</li>
     * <li>DIV - division</li>
     * <li>MOD - modular (remainder) division</li>
     * </ul>
     */
    public enum BinOp {
        ASGN, INDEX, OR, AND, EQ, NE, LT, GT, LE, GE, ADD, SUB, MUL, DIV, MOD
    }

    public static Set<BinOp> flexibleOperators = new HashSet<>(Arrays.asList(new BinOp[] { BinOp.EQ, BinOp.NE }));

    public static Set<BinOp> arithmeticOperators = new HashSet<>(
            Arrays.asList(new BinOp[] { BinOp.ADD, BinOp.SUB, BinOp.MUL, BinOp.DIV, BinOp.MOD, BinOp.LT, BinOp.GT }));

    public static Set<BinOp> booleanOperators = new HashSet<>(Arrays.asList(new BinOp[] { BinOp.OR, BinOp.AND }));

    public static String opToString(BinOp op) {
        switch (op) {
            case ASGN:
                return "=";
            case OR:
                return "||";
            case AND:
                return "&&";
            case EQ:
                return "==";
            case NE:
                return "!=";
            case LT:
                return "<";
            case GT:
                return ">";
            case LE:
                return "<=";
            case GE:
                return ">=";
            case ADD:
                return "+";
            case SUB:
                return "-";
            case MUL:
                return "*";
            case DIV:
                return "/";
            case MOD:
                return "%";
            default:
                return "???";
        }
    }

    public BinOp operator;
    public ASTExpression leftChild;
    public ASTExpression rightChild;

    public ASTBinaryExpr(BinOp operator, ASTExpression leftChild, ASTExpression rightChild) {
        this.operator = operator;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        leftChild.traverse(visitor);
        visitor.inVisit(this);
        rightChild.traverse(visitor);
        visitor.postVisit(this);
    }

    @Override
    public String toString() {
        String right = opToString(operator) + rightChild.toString();
        if (operator.equals(BinOp.EQ)) {
            right = ".equals(" + rightChild.toString() + ")";
        }

        String s = ("(" + leftChild.toString() + right + ")");

        HMTypeBase t = this.type instanceof HMTypeBase ? (HMTypeBase) this.type : null;

        if (t == null) {
            return s;
        }

        if (t.getBaseType() == BaseDataType.BOOL) {
            return String.format("Boolean.valueOf(%s)", s);
        } else if (t.getBaseType() == BaseDataType.INT) {
            return String.format("Integer.valueOf(%s)", s);
        } else if (t.getBaseType() == BaseDataType.REAL) {
            return String.format("Double.valueOf(%s)", s);
        } else if (t.getBaseType() == BaseDataType.STR) {
            return String.format("%s", s);
        } else {
            return null;
        }
    }
}
