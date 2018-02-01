package com.github.lessjava.types.ast;

import java.util.Set;

public class ASTClassBlock extends ASTNode {
    public Set<ASTAttribute> attributes;
    public Set<ASTMethod> methods;

    public ASTClassBlock(Set<ASTAttribute> attributes, Set<ASTMethod> methods) {
        this.attributes = attributes;
        this.methods = methods;
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        for (ASTAttribute attribute : attributes) {
            attribute.traverse(visitor);
        }

        for (ASTMethod method : methods) {
            method.traverse(visitor);
        }
        visitor.postVisit(this);
    }
}
