package com.github.lessjava.types.ast;

import com.github.lessjava.types.inference.impl.HMTypeList;

public class ASTList extends ASTCollection {
    public ASTList(ASTArgList initialElements) {
        super(initialElements);
    }

    @Override
    public String toString() {
        StringBuilder initialization = new StringBuilder();
        String argString = initialElements.toString();

        HMTypeList t = (HMTypeList) type;

        initialization.append(String.format("new LJList<%s>(Arrays.asList(new %s[] %s))", t.elementType, t.elementType, argString));

        return initialization.toString();
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        initialElements.traverse(visitor);
        visitor.postVisit(this);
    }
}
