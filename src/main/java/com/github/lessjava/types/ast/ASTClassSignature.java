package com.github.lessjava.types.ast;

public class ASTClassSignature extends ASTNode {
    String className;
    String parentClass;

    public ASTClassSignature(String className) {
        this(className, null);
    }

    public ASTClassSignature(String className, String parentClass) {
        this.className = className;
        this.parentClass = parentClass;
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        visitor.postVisit(this);
    }
}
