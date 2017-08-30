package com.github.lessjava.types.ast;


/**
 * Decaf "continue" statement (should be inside a loop structure). When
 * executed, the program should jump to the next iteration of the loop.
 */
public class ASTContinue extends ASTStatement
{
    public ASTContinue() { }

    @Override
    public void traverse(ASTVisitor visitor)
    {
        visitor.preVisit(this);
        visitor.postVisit(this);
    }
}

