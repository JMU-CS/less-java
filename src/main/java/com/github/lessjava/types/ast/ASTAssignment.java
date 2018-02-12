package com.github.lessjava.types.ast;

/**
 * Decaf assignment statement. When executed, the program should fully evaluate
 * the value expression and store the result in the given memory location.
 */
public class ASTAssignment extends ASTBinaryExpr {
    public BinOp op;
    public ASTVariable variable;
    public ASTExpression value;
    public String className;

    public ASTAssignment(BinOp op, ASTVariable variable, ASTExpression value) {
        super(BinOp.ASGN, variable, value);
        this.variable = (ASTVariable) super.leftChild;
        this.value = value;
    }

    public ASTAssignment(BinOp op, ASTMemberAccess variable, ASTExpression value) {
        this(BinOp.ASGN, variable.var, value);
        this.className = variable.className;
        this.value = value;
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        variable.traverse(visitor);
        value.traverse(visitor);
        visitor.postVisit(this);
    }

    @Override
    public String toString() {
        return String.format("%s = %s", variable, value);
    }
}
