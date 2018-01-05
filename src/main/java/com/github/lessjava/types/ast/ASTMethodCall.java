package com.github.lessjava.types.ast;

public class ASTMethodCall extends ASTBinaryExpr {
    public ASTVariable var;
    public ASTFunctionCall funcCall;

    /**
     * @param var
     * @param funcCall
     */
    public ASTMethodCall(ASTVariable var, ASTFunctionCall funcCall) {
        super(BinOp.INVOKE, var, funcCall);
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
