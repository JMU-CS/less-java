package com.github.lessjava.types.ast;

public class ASTVoidMethodCall extends ASTStatement {
    public ASTExpression invoker;
    public ASTFunctionCall funcCall;

    /**
     * @param expr
     * @param funcCall
     */
    public ASTVoidMethodCall(ASTExpression invoker, ASTFunctionCall funcCall) {
        this.invoker = invoker;
        this.funcCall = funcCall;
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        invoker.traverse(visitor);
        funcCall.traverse(visitor);
        visitor.postVisit(this);
    }
}
