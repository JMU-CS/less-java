package com.github.lessjava.types.ast;

/**
 * Decaf assignment statement. When executed, the program should fully evaluate
 * the value expression and store the result in the given memory location.
 */
public class ASTTest extends ASTStatement
{
    public ASTFunctionCall function;
    public ASTExpression expectedValue;

    public ASTTest(ASTFunctionCall function, ASTExpression expectedValue)
    {
        this.function = function;
        this.expectedValue = expectedValue;
    }

    @Override
    public void traverse(ASTVisitor visitor)
    {
        visitor.preVisit(this);
        function.traverse(visitor);
        expectedValue.traverse(visitor);
        visitor.postVisit(this);
    }
}

