package com.github.lessjava.ast;

import java.util.*;

/**
 * Lexical scope in a Decaf program; e.g., a function body or a while-loop
 * body. Blocks are denoted in the source code using curly braces ("{"
 * and "}"). Contains lists of local variable definitions and enclosed
 * statements.
 */

public class ASTBlock extends ASTNode
{
    public List<ASTVariable> variables;
    public List<ASTStatement> statements;

    public ASTBlock()
    {
        this.variables = new ArrayList<ASTVariable>();
        this.statements = new ArrayList<ASTStatement>();
    }

    @Override
    public void traverse(ASTVisitor visitor)
    {
        visitor.preVisit(this);
        for (ASTVariable v : variables) {
            v.traverse(visitor);
        }
        for (ASTStatement s : statements) {
            s.traverse(visitor);
        }
        visitor.postVisit(this);
    }
}

