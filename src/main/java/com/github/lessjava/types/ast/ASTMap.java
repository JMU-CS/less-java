package com.github.lessjava.types.ast;

public class ASTMap extends ASTCollection {
    public ASTMap(ASTArgList initialElements) {
        super(initialElements);
    }

    @Override
    public String toString() {
        StringBuilder initialization = new StringBuilder();
        StringBuilder entries = new StringBuilder();

        String keyType = ((ASTEntry) initialElements.arguments.get(0)).key.type.toString();
        String valueType = ((ASTEntry) initialElements.arguments.get(0)).key.type.toString();

        for (ASTExpression e : initialElements.arguments) {
            ASTEntry entry = (ASTEntry) e;
            entries.append(String.format("put(%s, %s);", entry.key, entry.value));
        }

        initialization.append(String.format("new HashMap<%s, %s>() {{%s}}", keyType,
                valueType, entries));

        return initialization.toString();
    }

}
