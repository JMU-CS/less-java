package com.github.lessjava.types.ast;

import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.types.inference.impl.HMTypeCollection;

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

        ASTNode n = this;
        boolean inTest = false;

        // TODO: Better way to handle this??
        while (n != null && !((n = n.getParent()) instanceof ASTProgram)) {
            inTest = n instanceof ASTTest;
        }

        sb.append(inTest ? String.format("__test%s", this.name) : name);

        if (this.isCollection && this.index != null) {
            HMType eType;
            if (this.type instanceof HMTypeCollection) {
                eType = ((HMTypeCollection) type).elementType;
            } else {
                eType = this.type;
            }
            return String.format("((%s[])%s", eType,
                    sb.append(String.format(".toArray(new %s[0]))[%s]", eType, index)));
        } else {
            return sb.toString();
        }
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        visitor.postVisit(this);
    }
}
