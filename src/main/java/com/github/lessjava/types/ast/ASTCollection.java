package com.github.lessjava.types.ast;

public abstract class ASTCollection extends ASTExpression {
    public ASTArgList initialElements;

    public ASTCollection(ASTArgList initialElements) {
        this.initialElements = initialElements;
    }
}
