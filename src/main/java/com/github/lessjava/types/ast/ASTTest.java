package com.github.lessjava.types.ast;

/**
 * Decaf assignment statement. When executed, the program should fully evaluate
 * the value expression and store the result in the given memory location.
 */
public class ASTTest extends ASTStatement {

    public ASTExpression expr;

    public ASTTest(ASTExpression expr) {
        this.expr = expr;
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        expr.traverse(visitor);
        visitor.postVisit(this);
    }
}
