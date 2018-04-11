package com.github.lessjava.types.ast;

import com.github.lessjava.types.inference.impl.HMTypeCollection;
import com.github.lessjava.types.inference.impl.HMTypeList;

/**
 * Decaf variable declaration. Contains a name, a data type, and an array flag.
 * If the flag is set, the arrayLength field contains the size.
 */
public class ASTVariable extends ASTExpression {
    public String name;
    public ASTExpression index;

    public ASTVariable(String name) {
        this(name, null);
    }

    public ASTVariable(String name, ASTExpression index) {
        this.name = name;
        this.index = index;

        if (this.index != null || this.type instanceof HMTypeCollection) {
            this.isCollection = true;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(name);
        if (type instanceof HMTypeList && index != null) {
            sb.append(String.format(".get(%s)", index.toString()));
        }

        return sb.toString();
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        visitor.postVisit(this);
    }
}
