package com.github.lessjava.types.ast;

import com.github.lessjava.types.ast.ASTBinaryExpr.BinOp;

public class ASTVoidAssignment extends ASTStatement {
    public BinOp op;
    public ASTVariable variable;
    public ASTExpression value;

    public ASTVoidAssignment(BinOp op, ASTVariable variable, ASTExpression value) {
        this.op = op;
        this.variable = variable;
        this.value = value;
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        variable.traverse(visitor);
        value.traverse(visitor);
        visitor.postVisit(this);
    }
}

