/**
 * Decaf assignment statement. When executed, the program should fully evaluate
 * the value expression and store the result in the given memory location.
 */
public class ASTAssignment extends ASTStatement
{
    public ASTVariable variable;
    public ASTLocation location;
    public ASTExpression value;

    public ASTAssignment(ASTLocation location, ASTExpression value)
    {
        this.variable = null;
        this.location = location;
        this.value = value;
    }

    public ASTAssignment(ASTVariable variable, ASTExpression value)
    {
        this.variable = variable;
        this.location = variable.loc;
        this.value = value;
    }

    @Override
    public void traverse(ASTVisitor visitor)
    {
        visitor.preVisit(this);
        if (variable == null) {
            location.traverse(visitor);
        } else {
            variable.traverse(visitor);
        }
        value.traverse(visitor);
        visitor.postVisit(this);
    }
}

