package com.github.lessjava.ast;


/**
 * Decaf "break" statement (should be inside a loop structure). When executed,
 * the program should jump to the code following the loop.
 */
public class ASTBreak extends ASTStatement
{
    public ASTBreak() { }

    @Override
    public void traverse(ASTVisitor visitor)
    {
        visitor.preVisit(this);
        visitor.postVisit(this);
    }
}

