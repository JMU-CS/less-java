package com.github.lessjava.ast;

/**
 * Decaf variable declaration. Contains a name, a data type, and an array
 * flag. If the flag is set, the arrayLength field contains the size.
 */
public class ASTVariable extends ASTNode
{
    public ASTLocation loc;
    public ASTNode.DataType type;
    public boolean isArray;
    public int arrayLength;

    public ASTVariable(ASTLocation loc, ASTNode.DataType type)
    {
        this.loc = loc;
        this.type = type;
        this.isArray = false;
        this.arrayLength = 1;
    }

    public ASTVariable(ASTLocation loc, ASTNode.DataType type, int arrayLength)
    {
        this (loc, type);
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
