

/**
 * Unary operation with an operation tag and a child sub-expression. When
 * executed, the program should fully evaluate the child and then apply the
 * given operation to the resulting value.
 */
public class ASTUnaryExpr extends ASTExpression
{
    /**
     * List of permitted unary operations.
     *
     * <ul>
     * <li> NOT - boolean not </li>
     * <li> NEG - negation </li>
     * </ul>
     */
    public enum UnaryOp { INVALID, NOT, NEG }

    public static String opToString(UnaryOp op)
    {
        switch (op) {
            case NOT:    return "!";
            case NEG:   return "-";
            default:    return "???";
        }
    }

    public UnaryOp operator;
    public ASTExpression child;

    public ASTUnaryExpr(UnaryOp operator, ASTExpression child)
    {
        this.operator = operator;
        this.child = child;
    }

    @Override
    public void traverse(ASTVisitor visitor)
    {
        visitor.preVisit(this);
        child.traverse(visitor);
        visitor.postVisit(this);
    }

    @Override
    public String toString()
    {
        return "(" + opToString(operator) +
            child.toString() + ")";
    }
}

