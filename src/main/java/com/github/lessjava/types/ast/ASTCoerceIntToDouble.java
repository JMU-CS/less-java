package com.github.lessjava.types.ast;

import com.github.lessjava.types.inference.impl.HMTypeBase;

/**
 * Converts Integer types to Double types by wrapping with Double.valueOf(...).
 *
 * Necessary because Java can't natively do this, so things like 4 == 4.0 would break unless the 4
 * is converted to a Double.
 */
public class ASTCoerceIntToDouble extends ASTExpression {

    public ASTExpression expression;

    public ASTCoerceIntToDouble(ASTExpression expression) {
        this.expression = expression;
        this.type = HMTypeBase.REAL;
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        expression.traverse(visitor);
        visitor.postVisit(this);
    }

    @Override
    public String toString() {
        return String.format("Double.valueOf(%s)", expression);
    }

}
