package com.github.lessjava.types.ast;

public class ASTSet extends ASTCollection {
    public ASTSet(ASTArgList initialElements) {
        super(initialElements);
    }

    @Override
    public String toString() {
        StringBuilder initialization = new StringBuilder();
        String argString = initialElements.toString();

        initialization.append(String.format("new HashSet(Arrays.asList(new %s[] %s))", initialElements.type,
                argString));

        return initialization.toString();
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        initialElements.traverse(visitor);
        visitor.postVisit(this);
    }
}
