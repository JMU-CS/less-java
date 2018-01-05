package com.github.lessjava.types.ast;

import java.util.ArrayList;

public abstract class ASTCollection extends ASTExpression {
    public ASTArgList initialElements;

    public ASTCollection(ASTArgList initialElements) {
        this.initialElements = initialElements == null ? new ASTArgList(new ArrayList<ASTExpression>())
                : initialElements;
    }
}
