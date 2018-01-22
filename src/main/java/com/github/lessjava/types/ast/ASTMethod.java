package com.github.lessjava.types.ast;

import java.util.ArrayList;
import java.util.List;

import com.github.lessjava.types.inference.HMType;

public class ASTMethod extends ASTAbstractFunction {
    public List<ASTExpression> internalArguments;

    public ASTMethod(String name, HMType returnType, ASTBlock body) {
        super(name, returnType, body);
        this.internalArguments = new ArrayList<>();
    }

    public ASTMethod(String name, ASTBlock body) {
        super(name, body);
    }

    public String getIdentifyingString() {
        StringBuilder sb = new StringBuilder();
        ASTClass c = (ASTClass) this.getParent();

        sb.append(c.name);
        sb.append(name);
        sb.append(getParameterStr());

        return sb.toString();
    }

    public ASTMethod clone() {
        return new ASTMethod(name, returnType, body);
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
