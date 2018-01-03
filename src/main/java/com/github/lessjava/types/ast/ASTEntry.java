package com.github.lessjava.types.ast;

public class ASTEntry extends ASTExpression{
    public ASTExpression key;
    public ASTExpression value;

    /**
     * @param key
     * @param value
     */
    public ASTEntry(ASTExpression key, ASTExpression value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%s : %s", this.key, this.value);
    }
}
