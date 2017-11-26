package com.github.lessjava.types.ast;


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

    public String getNameArgString() {
	StringBuilder sb = new StringBuilder(name);
	for (ASTExpression e: arguments) {
	    sb.append(e.type.toString() + ",");
	}
	
	return sb.toString();
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
