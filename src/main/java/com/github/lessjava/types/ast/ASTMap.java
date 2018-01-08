package com.github.lessjava.types.ast;

import com.github.lessjava.types.inference.impl.HMTypeMap;

public class ASTMap extends ASTCollection {
    public ASTMap(ASTArgList initialElements) {
        super(initialElements);
    }

    @Override
    public String toString() {
        StringBuilder initialization = new StringBuilder();
        StringBuilder entries = new StringBuilder();

        HMTypeMap t = (HMTypeMap) type;

        String keyType = t.key.toString();
        String valueType = t.value.toString();

        for (ASTExpression e : initialElements.arguments) {
            ASTEntry entry = (ASTEntry) e;
            entries.append(String.format("put(%s, %s);", entry.key, entry.value));
        }

        initialization.append(String.format("new HashMap<%s, %s>() {{%s}}", keyType,
                valueType, entries));

        return initialization.toString();
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        initialElements.traverse(visitor);
        visitor.postVisit(this);
    }
}
