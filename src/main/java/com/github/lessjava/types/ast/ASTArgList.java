package com.github.lessjava.types.ast;

import java.util.List;

import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.types.inference.impl.HMTypeCollection;

public class ASTArgList extends ASTExpression {
    public List<ASTExpression> arguments;
    public boolean isConcrete;
    public HMType collectionType;

    public ASTArgList(List<ASTExpression> arguments) {
        this.arguments = arguments;
        this.collectionType = new HMTypeCollection(this.type);
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
        StringBuilder sb = new StringBuilder();
        StringBuilder argString = new StringBuilder();

        argString.append("{ ");
        for (ASTExpression e : arguments) {
            argString.append(e.toString() + ",");
        }
        argString.append(" }");

        sb.append(String.format("new ArrayDeque<%s>(Arrays.asList(new %s[] %s))", type, type, argString.toString())
                .replaceAll("\\\\\"", ""));

        return sb.toString();
    }

}
