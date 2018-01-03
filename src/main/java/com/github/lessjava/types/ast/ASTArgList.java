package com.github.lessjava.types.ast;

import java.util.List;

import com.github.lessjava.types.inference.impl.HMTypeVar;

public class ASTArgList extends ASTExpression {
    public List<ASTExpression> arguments;
    public boolean isConcrete;

    public ASTArgList(List<ASTExpression> arguments) {
        this.arguments = arguments;
        this.type = arguments.isEmpty() ? new HMTypeVar() : arguments.get(0).type;
        this.isCollection = true;
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        for (ASTExpression e : arguments) {
            e.traverse(visitor);
        }
        visitor.postVisit(this);
    }

    @Override
    public String toString() {
        StringBuilder argString = new StringBuilder();

        argString.append("{ ");
        for (ASTExpression e : arguments) {
            argString.append(e.toString() + ",");
        }
        argString.append(" }");

        return argString.toString();
    }

}
