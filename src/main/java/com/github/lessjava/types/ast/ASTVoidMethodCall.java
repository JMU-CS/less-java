package com.github.lessjava.types.ast;

public class ASTVoidMethodCall extends ASTStatement {
    public ASTVariable var;
    public ASTFunctionCall funcCall;

    /**
     * @param var
     * @param funcCall
     */
    public ASTVoidMethodCall(ASTVariable var, ASTFunctionCall funcCall) {
        this.var = var;
        this.funcCall = funcCall;
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        var.traverse(visitor);
        funcCall.traverse(visitor);
        visitor.postVisit(this);
    }
}
