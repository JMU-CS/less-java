package com.github.lessjava.types.ast;

/**
 * Decaf variable declaration. Contains a name, a data type, and an array flag.
 * If the flag is set, the arrayLength field contains the size.
 */
public class ASTVariable extends ASTExpression {
    public String name;
    public boolean isCollection;

    public ASTVariable(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        ASTNode n = this;
        boolean inTest = false;

        while (n != null && !((n = n.getParent()) instanceof ASTProgram)) {
            inTest = n instanceof ASTTest;
        }

        if (inTest) {
            return String.format("__test%s", this.name);
        }

        return name;
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        visitor.postVisit(this);
    }
}
