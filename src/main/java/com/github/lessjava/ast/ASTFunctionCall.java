package com.github.lessjava.ast;
import java.util.*;

/**
 * Decaf function call that is intended to return a value at runtime.
 */
public class ASTFunctionCall extends ASTExpression
{
    public String name;
    public List<ASTExpression> arguments;

    public ASTFunctionCall(String name)
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

    @Override
    public String toString()
    {
        StringBuffer argString = new StringBuffer();
        argString.append("(");
        for (ASTExpression e : arguments) {
            if (argString.length() > 1) {
                argString.append(", ");
            }
            argString.append(e.toString());
        }
        argString.append(")");
        return name + argString;
    }
}

