package com.github.lessjava.types.ast;
/**
 * Decaf assignment statement. When executed, the program should fully evaluate
 * the value expression and store the result in the given memory location.
 */
public class ASTAssignment extends ASTStatement
{
    public ASTVariable variable;
    public ASTExpression value;

    public ASTAssignment(ASTVariable variable, ASTExpression value)
    {
        this.variable = variable;
        this.value = value;
    }

    @Override
    public void traverse(ASTVisitor visitor)
    {
        visitor.preVisit(this);
        variable.traverse(visitor);
        value.traverse(visitor);
        visitor.postVisit(this);
    }
}

