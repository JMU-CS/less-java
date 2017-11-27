package com.github.lessjava.types.ast;

/**
 * Decaf "return" statement (should be inside of a function). When executed,
 * this statement should immediately exit the function and return control flow
 * to the call site. If there is a return value expression (i.e., {@code value}
 * is not {@code null}), the expression should be fully evaluated and the result
 * should be returned to the caller.
 */
public class ASTReturn extends ASTStatement {
    public ASTExpression value; // could be null (void return)

    public ASTReturn() {
        this(null);
    }

    public ASTReturn(ASTExpression value) {
        this.value = value;
    }

    public boolean hasValue() {
        return (value != null);
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        if (hasValue()) {
            value.traverse(visitor);
        }
        visitor.postVisit(this);
    }
}
