package com.github.lessjava.ast;

/**
 * Decaf variable declaration. Contains a name, a data type, and an array
 * flag. If the flag is set, the arrayLength field contains the size.
 */
public class ASTVariable extends ASTExpression
{
    public String name;
    public boolean isArray;
    public int arrayLength;
    
    public ASTVariable(String name) {
    		this.name = name;
    }

    public ASTVariable(String name, int arrayLength)
    {
        this (name);
        this.isArray = true;
        this.arrayLength = arrayLength;
    }
    
    @Override
    public String toString() {
    		return name;
    }

    @Override
    public void traverse(ASTVisitor visitor)
    {
        visitor.preVisit(this);
        visitor.postVisit(this);
    }
}
