package com.github.lessjava.types.ast;

import java.util.HashSet;
import java.util.Set;

public class ASTClassBlock extends ASTNode {
    public Set<ASTAttribute> classAttributes;
    public Set<ASTMethod> methods;

    public ASTClassBlock() {
        this.classAttributes = new HashSet<>();
        this.methods = new HashSet<>();
    }

    public ASTClassBlock(Set<ASTAttribute> attributes, Set<ASTMethod> methods) {
        this.classAttributes = attributes != null ? attributes : new HashSet<>() ;
        this.methods = methods;
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        for (ASTAttribute attribute : classAttributes) {
            attribute.traverse(visitor);
        }

        for (ASTMethod method : methods) {
            method.traverse(visitor);
        }
        visitor.postVisit(this);
    }
}
