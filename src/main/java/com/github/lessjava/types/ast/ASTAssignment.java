package com.github.lessjava.types.ast;

/**
 * Less-Java assignment statement. When executed, the program should fully evaluate
 * the value expression and store the result in the given memory location.
 */
public class ASTAssignment extends ASTBinaryExpr {
    public BinOp op;
    public ASTVariable variable;
    public ASTExpression value;
    public ASTMemberAccess memberAccess;

    public ASTAssignment(BinOp op, ASTVariable variable, ASTExpression value) {
        super(op, variable, value);
        this.op = op;
        this.variable = (ASTVariable) super.leftChild;
        this.value = value;
    }

    public ASTAssignment(BinOp op, ASTMemberAccess memberAccess, ASTExpression value) {
        this(op, memberAccess.var, value);
        this.op = op;
        this.memberAccess = memberAccess;
        this.value = value;
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        if(this.memberAccess == null) {
            variable.traverse(visitor);
        } else {
            memberAccess.traverse(visitor);
        }
        value.traverse(visitor);
        visitor.postVisit(this);
    }

    @Override
    public String toString() {
        if (memberAccess != null) {
            return String.format("%s %s %s", memberAccess, ASTBinaryExpr.opToString(op), value);
        } else {
            return String.format("%s %s %s", variable, ASTBinaryExpr.opToString(op), value);
        }
    }
}
