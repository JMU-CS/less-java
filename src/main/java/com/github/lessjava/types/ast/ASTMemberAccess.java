package com.github.lessjava.types.ast;

public class ASTMemberAccess extends ASTExpression {
    public ASTVariable instance;
    public ASTVariable var;

    public ASTMemberAccess(String className, ASTVariable var) {
        this.instance = new ASTVariable(className);
        this.var = var;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%s.%s", instance.name, var));

        return sb.toString();
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        this.instance.traverse(visitor);
        this.var.traverse(visitor);
        visitor.postVisit(this);
    }
}
