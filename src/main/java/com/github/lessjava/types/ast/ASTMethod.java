package com.github.lessjava.types.ast;

import com.github.lessjava.types.inference.HMType;

public class ASTMethod extends ASTAbstractFunction {
    public ASTMethod(String name, HMType returnType, ASTBlock body) {
        super(name, returnType, body);
    }

    public ASTMethod(String name, ASTBlock body) {
        super(name, body);
    }

    public String getIdentifyingString() {
        StringBuilder sb = new StringBuilder();
        ASTClass c = (ASTClass) this.getParent();

        sb.append(name);
        sb.append(c.name);

        for (Parameter e : parameters) {
            sb.append(e.type.toString() + ",");
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("Method: %s.%s, %s", ((ASTClass) this.getParent()).name, this.name, this.returnType);
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        // Library methods have null bodies
        if (body != null) {
            body.traverse(visitor);
        }
        visitor.postVisit(this);
    }
}
