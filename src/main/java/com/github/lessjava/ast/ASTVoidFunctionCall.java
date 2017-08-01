package com.github.lessjava.ast;


import java.util.*;

/**
 * Decaf function call that is NOT intended to return a value.
 */
public class ASTVoidFunctionCall extends ASTStatement
{
    public String name;
    public List<ASTExpression> arguments;

    public ASTVoidFunctionCall(String name)
    {
        this.name = name;
        this.arguments = new ArrayList<ASTExpression>();
    }

    @Override
    public void traverse(ASTVisitor visitor)
    {
        visitor.preVisit(this);
        for (ASTExpression e : arguments) {
            e.traverse(visitor);
        }
        visitor.postVisit(this);
    }
}
