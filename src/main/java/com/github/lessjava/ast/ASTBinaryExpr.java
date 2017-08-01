package com.github.lessjava.ast;

/**
 * Decaf binary operation with an operation tag and two child sub-expressions.
 * When executed, the program should evaluate the left-hand side and the
 * right-hand side separately and then perform the given operation on the two
 * values. Some operations (e.g., boolean AND and OR) can be short-circuited.
 */
public class ASTBinaryExpr extends ASTExpression
{
    /**
     * List of permitted binary operations.
     *
     * <ul>
     * <li> OR - boolean or (inclusive) </li>
     * <li> AND - boolean and </li>
     * <li> EQ - equal to </li>
     * <li> NEQ - not equal to </li>
     * <li> LT - less than </li>
     * <li> GT - greater than </li>
     * <li> LEQ - less than or equal to </li>
     * <li> GEQ - greater than or equal to </li>
     * <li> ADD - addition </li>
     * <li> SUB - subtraction </li>
     * <li> MUL - multiplication </li>
     * <li> DIV - division </li>
     * <li> MOD - modular (remainder) division </li>
     * </ul>
     */
    public enum BinOp
    {
        INVALID, OR, AND, EQ, NE, LT, GT, LE, GE,
        ADD, SUB, MUL, DIV, MOD
    }

    public static String opToString(BinOp op)
    {
        switch (op) {
            case OR:    return "||";
            case AND:   return "&&";
            case EQ:    return "==";
            case NE:    return "!=";
            case LT:    return "<";
            case GT:    return ">";
            case LE:    return "<=";
            case GE:    return ">=";
            case ADD:   return "+";
            case SUB:   return "-";
            case MUL:   return "*";
            case DIV:   return "/";
            case MOD:   return "%";
            default:    return "???";
        }
    }

    public BinOp operator;
    public ASTExpression leftChild;
    public ASTExpression rightChild;

    public ASTBinaryExpr(BinOp operator, ASTExpression leftChild,
            ASTExpression rightChild)
    {
        this.operator = operator;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    @Override
    public void traverse(ASTVisitor visitor)
    {
        visitor.preVisit(this);
        leftChild.traverse(visitor);
        visitor.inVisit(this);
        rightChild.traverse(visitor);
        visitor.postVisit(this);
    }

    @Override
    public String toString()
    {
        return "(" + leftChild.toString() +
            opToString(operator) +
            rightChild.toString() + ")";
    }
}

