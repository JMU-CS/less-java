package com.github.lessjava.types.ast;

import com.github.lessjava.types.inference.impl.HMTypeVar;

public class ASTList extends ASTCollection {
    public ASTList(ASTArgList initialElements) {
        super(initialElements);
    }

    @Override
    public String toString() {
        StringBuilder initialization = new StringBuilder();
        String argString = initialElements.toString();

        String collectionType = initialElements.type instanceof HMTypeVar ? "Object" : initialElements.type.toString();
        initialization.append(String.format("new ArrayList<%s>(Arrays.asList(new %s[] %s))", collectionType,
                collectionType, argString));

        return initialization.toString();
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        initialElements.traverse(visitor);
        visitor.postVisit(this);
    }
}
