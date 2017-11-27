package com.github.lessjava.types.ast;

/**
 * Decaf location. This could be a local variable or a global variable,
 * depending on the context. It could also be a local or global array location,
 * if the {@link index} member is not {@code null}.
 */
public class ASTLocation extends ASTExpression {
    public String name;

    public ASTLocation(String name) {
        this.name = name;
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        visitor.postVisit(this);
    }

    @Override
    public String toString() {
        return name;
    }
}
