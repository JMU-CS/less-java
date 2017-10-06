package com.github.lessjava.types.ast;
import java.util.*;

/**
 * Decaf program. Basically just a list of {@link ASTVariable} and {@link
 * ASTFunction} declarations.
 */
public class ASTProgram extends ASTNode
{
    public List<ASTStatement> statements;
    public List<ASTFunction> functions;

    public ASTProgram()
    {
        super();
        this.statements = new ArrayList<ASTStatement>();
        this.functions = new ArrayList<ASTFunction>();
    }

    @Override
    public void traverse(ASTVisitor visitor)
    {
        visitor.preVisit(this);
        for (ASTFunction f : functions) {
            f.traverse(visitor);
        }
        for (ASTStatement s : statements) {
            s.traverse(visitor);
        }
        visitor.postVisit(this);
    }
}

