package com.github.lessjava.types.ast;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ASTClassBlock extends ASTNode {
    public static Map<String, ASTAttribute> nameAttributeMap = new HashMap<>();

    public Set<ASTAttribute> classAttributes;
    public Set<ASTMethod> methods;
    public ASTMethod constructor;

    public ASTClassBlock() {
        this(null, null, null);
    }

    public ASTClassBlock(Set<ASTAttribute> attributes, Set<ASTMethod> methods) {
        this(attributes, methods, null);
    }

    public ASTClassBlock(Set<ASTAttribute> attributes, Set<ASTMethod> methods, ASTMethod constructor) {
        this.classAttributes = attributes != null ? attributes : new HashSet<>();
        this.methods = methods != null ? methods : new HashSet<>();
        this.constructor = constructor;

        for (ASTAttribute a: classAttributes) {
            nameAttributeMap.put(a.assignment.variable.name, a);
        }
    }

    public void addAttribute(ASTAttribute attribute) {
        this.classAttributes.add(attribute);
        nameAttributeMap.put(attribute.assignment.variable.name, attribute);
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
