package com.github.lessjava.types.ast;

public class ASTForLoop extends ASTStatement {

    public ASTVariable var;
    public ASTExpression lowerBound;
    public ASTExpression upperBound;
    public ASTBlock block;

    public ASTForLoop(ASTVariable var, ASTExpression lowerBound, ASTExpression upperBound, ASTBlock block) {
        this.var = var;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.block = block;
    }

    public ASTForLoop(ASTVariable var, ASTExpression upperBound, ASTBlock block) {
        this(var, null, upperBound, block);
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        var.traverse(visitor);
        if (lowerBound != null) {
            lowerBound.traverse(visitor);
        }
        upperBound.traverse(visitor);
        block.traverse(visitor);
        visitor.postVisit(this);
    }
}
