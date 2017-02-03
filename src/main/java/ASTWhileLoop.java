

/**
 * Decaf loop structure; contains a "guard" expression and a loop body block.
 * The block is executed an arbitrary number of times as long as the guard
 * expression evaluates to true.
 */
public class ASTWhileLoop extends ASTStatement
{
    public ASTExpression guard;
    public ASTBlock body;

    public ASTWhileLoop(ASTExpression guard, ASTBlock body)
    {
        this.guard = guard;
        this.body = body;
    }

    @Override
    public void traverse(ASTVisitor visitor)
    {
        visitor.preVisit(this);
        guard.traverse(visitor);
        body.traverse(visitor);
        visitor.postVisit(this);
    }
}

