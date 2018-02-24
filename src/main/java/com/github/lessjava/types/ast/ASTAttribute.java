package com.github.lessjava.types.ast;

public class ASTAttribute extends ASTNode {
    public String scope;
    public ASTAssignment assignment;

    public ASTAttribute(String scope, ASTAssignment assignment) {
        this.scope = scope;
        this.assignment = assignment;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%s %s %s;", scope, assignment.type, assignment));

        return sb.toString();
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        assignment.traverse(visitor);
        visitor.postVisit(this);
    }
}
