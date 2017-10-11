package com.github.lessjava.types.ast;
import java.util.ArrayList;
import java.util.List;

/**
 * Decaf function call that is intended to return a value at runtime.
 */
public class ASTFunctionCall extends ASTExpression
{
    public String name;
    public List<ASTExpression> arguments;
    private boolean cached;
    private boolean inTest;

    public ASTFunctionCall(String name)
    {
        this.name = name;
        this.arguments = new ArrayList<ASTExpression>();

        this.cached = false;
    }
    
    private boolean inTest() {
        if (cached) {
            return inTest;
        }
        
        cached = true;
        
        ASTNode parent = this;
        while (!((parent = parent.getParent()) instanceof ASTProgram)) {
            if (parent instanceof ASTTest) {
                return (inTest = true);
            }
        }

        return (inTest = false);
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
        String s = name + argString;
        return inTest() ? "Main." + s : s;
    }
}
