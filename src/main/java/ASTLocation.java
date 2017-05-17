
/**
 * Decaf location. This could be a local variable or a global variable,
 * depending on the context. It could also be a local or global array location,
 * if the {@link index} member is not {@code null}.
 */
public class ASTLocation extends ASTExpression
{
    public String name;
    public ASTExpression index;     // can be null (non-array)

    public ASTLocation(String name)
    {
        this(name, null);
    }

    public ASTLocation(String name, ASTExpression index)
    {
        this.name = name;
        this.index = index;
    }

    public boolean hasIndex()
    {
        return index != null;
    }

    @Override
    public void traverse(ASTVisitor visitor)
    {
        visitor.preVisit(this);
        if (hasIndex()) {
            index.traverse(visitor);
        }
        visitor.postVisit(this);
    }

    @Override
    public String toString()
    {
        if (hasIndex()) {
            return name + "[" + index.toString() + "]";
        } else {
            return name;
        }
    }
}
