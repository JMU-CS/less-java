package com.github.lessjava.types.ast;

public class ASTGlobalAssignment extends ASTStatement {
    public ASTAssignment assignment;

    public ASTGlobalAssignment(ASTAssignment assignment) {
        this.assignment = assignment;
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        assignment.traverse(visitor);
        visitor.postVisit(this);
    }
}
