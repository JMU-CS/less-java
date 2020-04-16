package com.github.lessjava.types.ast;

import com.github.lessjava.types.inference.impl.HMTypeMap;
import com.github.lessjava.types.inference.impl.HMTypeTuple;

public class ASTMap extends ASTCollection {
    public ASTMap(ASTArgList initialElements) {
        super(initialElements);
    }

    @Override
    public String toString() {
        if(!(type instanceof HMTypeMap)) {
            return "ASTMap";
        }
        StringBuilder initialization = new StringBuilder();
        StringBuilder entries = new StringBuilder();

        HMTypeTuple t = (HMTypeTuple)((HMTypeMap) type).elementType;

        String keyType = t.types.get(0).toString();
        String valueType = t.types.get(1).toString();

        for (ASTExpression e : initialElements.arguments) {
            ASTEntry entry = (ASTEntry) e;
            entries.append(String.format("put(%s, %s);", entry.key, entry.value));
        }

        initialization.append(String.format("new LJMap<%s, %s>(new HashMap<%s, %s>() {{%s}})", keyType, valueType, keyType,
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
