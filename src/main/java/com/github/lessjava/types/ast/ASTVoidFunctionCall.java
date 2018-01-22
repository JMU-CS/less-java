package com.github.lessjava.types.ast;

public class ASTVoidFunctionCall extends ASTStatement {
    public ASTFunctionCall functionCall;

    public ASTVoidFunctionCall(ASTFunctionCall functionCall) {
        this.functionCall = functionCall;
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        functionCall.traverse(visitor);
        visitor.postVisit(this);
    }
}
