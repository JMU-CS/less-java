
/**
 * Decaf assignment statement. When executed, the program should fully evaluate
 * the value expression and store the result in the given memory location.
 */
public class ASTAssignment extends ASTStatement
{
    public ASTLocation location;
    public ASTExpression value;

    public ASTAssignment(ASTLocation location, ASTExpression value)
    {
        this.location = location;
        this.value = value;
    }

    @Override
    public void traverse(ASTVisitor visitor)
    {
        visitor.preVisit(this);
        location.traverse(visitor);
        value.traverse(visitor);
        visitor.postVisit(this);
    }
}

