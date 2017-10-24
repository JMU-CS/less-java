package com.github.lessjava.types.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * Decaf program. Basically just a list of {@link ASTVariable} and
 * {@link ASTFunction} declarations.
 */
public class ASTProgram extends ASTNode
{
    public List<ASTStatement> statements;
    public List<ASTFunction>  functions;
    public List<ASTTest>  tests;

    public ASTProgram()
    {
        super();
        this.statements = new ArrayList<ASTStatement>();
        this.functions = new ArrayList<ASTFunction>();
        this.tests = new ArrayList<ASTTest>();
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
        for (ASTTest t : tests) {
            t.traverse(visitor);
        }
        visitor.postVisit(this);
    }
}
