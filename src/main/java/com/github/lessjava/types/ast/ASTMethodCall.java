package com.github.lessjava.types.ast;

public class ASTMethodCall extends ASTBinaryExpr {
    public ASTExpression invoker;
    public ASTFunctionCall funcCall;

    public ASTMethodCall(ASTExpression invoker, ASTFunctionCall funcCall) {
        super(BinOp.INVOKE, invoker, funcCall);
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
