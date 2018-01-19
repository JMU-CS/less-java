package com.github.lessjava.types.ast;

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
        INVOKE, ASGN, ADDASGN, SUBASGN, INDEX, OR, AND, EQ, NE, LT, GT, LE, GE, ADD, SUB, MUL, DIV, MOD
    }

    public static String opToString(BinOp op) {
        switch (op) {
            case INVOKE:
                return ".";
            case ASGN:
                return "=";
            case ADDASGN:
                return "+=";
            case SUBASGN:
                return "-=";
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
                System.err.println(op);
                return "???";
        }
    }

    public static BinOp stringToOp(String s) {
        BinOp op;

        switch (s) {
            case "[":
                op = BinOp.INDEX;
                break;

            case "+":
                op = BinOp.ADD;
                break;

            case "*":
                op = BinOp.MUL;
                break;

            case "%":
                op = BinOp.MOD;
                break;

            case "/":
                op = BinOp.DIV;
                break;

            case "-":
                op = BinOp.SUB;
                break;

            case ">":
                op = BinOp.GT;
                break;

            case ">=":
                op = BinOp.GE;
                break;

            case "<":
                op = BinOp.LT;
                break;

            case "<=":
                op = BinOp.LE;
                break;

            case "==":
                op = BinOp.EQ;
                break;

            case "!=":
                op = BinOp.NE;
                break;

            case "||":
                op = BinOp.OR;
                break;

            case "&&":
                op = BinOp.AND;
                break;
            case "=":
                op = BinOp.ASGN;
                break;

            case "+=":
                op = BinOp.ADDASGN;
                break;

            case "-=":
                op = BinOp.SUBASGN;
                break;

            case ".":
                op = BinOp.INVOKE;
                break;

            default:
                op = null;
        }

        return op;
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
