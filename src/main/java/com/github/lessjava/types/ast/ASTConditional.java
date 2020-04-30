package com.github.lessjava.types.ast;

/**
 * Less-Java control structure; a block of code that is only executed if the
 * {@link condition} is true when the code is encountered during execution. The
 * conditional statement can optionally contain an additional block of code (the
 * "else" block) that is executed if the condition evaluates to false.
 */
public class ASTConditional extends ASTStatement {
    public ASTExpression condition;
    public ASTBlock ifBlock;
    public ASTBlock elseBlock; // could be null (no else-block)

    public ASTConditional(ASTExpression condition, ASTBlock ifBlock) {
        this(condition, ifBlock, null);
    }

    public ASTConditional(ASTExpression condition, ASTBlock ifBlock, ASTBlock elseBlock) {
        this.condition = condition;
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;
    }

    public boolean hasElseBlock() {
        return (elseBlock != null);
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        condition.traverse(visitor);
        ifBlock.traverse(visitor);
        if (hasElseBlock()) {
            visitor.inVisit(this);
            elseBlock.traverse(visitor);
        }
        visitor.postVisit(this);
    }
}
