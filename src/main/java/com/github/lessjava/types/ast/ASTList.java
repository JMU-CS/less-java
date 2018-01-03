package com.github.lessjava.types.ast;

public class ASTList extends ASTCollection {
    public ASTList(ASTArgList initialElements) {
        super(initialElements);
    }

    @Override
    public String toString() {
        StringBuilder initialization = new StringBuilder();
        String argString = initialElements.toString();

        initialization.append(String.format("new ArrayDeque(Arrays.asList(new %s[] %s))", initialElements.collectionType,
                argString));

        return initialization.toString();
    }
}
