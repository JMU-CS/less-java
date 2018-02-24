package com.github.lessjava.types.ast;

public class ASTMemberAccess extends ASTExpression {
    public String className;
    public ASTVariable var;

    public ASTMemberAccess(String className, ASTVariable var) {
        this.className = className;
        this.var = var;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%s.%s", className, var));

        return sb.toString();
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        this.var.traverse(visitor);
        visitor.postVisit(this);
    }
}
