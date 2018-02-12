package com.github.lessjava.types.ast;

public class ASTClassSignature extends ASTNode {
    public String className;
    public String superName;

    public ASTClassSignature(String className) {
        this(className, null);
    }

    public ASTClassSignature(String className, String superName) {
        this.className = className;
        this.superName = superName;
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        visitor.postVisit(this);
    }
}
