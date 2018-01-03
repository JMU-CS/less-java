package com.github.lessjava.types.ast;

public abstract class ASTCollection extends ASTExpression {
    protected ASTArgList initialElements;

    public ASTCollection(ASTArgList initialElements) {
        this.initialElements = initialElements;
    }
}
