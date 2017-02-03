
/**
 * Decaf variable declaration. Contains a name, a data type, and an array
 * flag. If the flag is set, the arrayLength field contains the size.
 */
public class ASTVariable extends ASTNode
{
    public String name;
    public ASTNode.DataType type;
    public boolean isArray;
    public int arrayLength;

    public ASTVariable(String name, ASTNode.DataType type)
    {
        this.name = name;
        this.type = type;
        this.isArray = false;
        this.arrayLength = 1;
    }

    public ASTVariable(String name, ASTNode.DataType type, int arrayLength)
    {
        this.name = name;
        this.type = type;
        this.isArray = true;
        this.arrayLength = arrayLength;
    }

    @Override
    public void traverse(ASTVisitor visitor)
    {
        visitor.preVisit(this);
        visitor.postVisit(this);
    }
}

