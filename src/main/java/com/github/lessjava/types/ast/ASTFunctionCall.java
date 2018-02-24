package com.github.lessjava.types.ast;

import java.util.ArrayList;
import java.util.List;

public class ASTFunctionCall extends ASTExpression {
    public String name;
    public List<ASTExpression> arguments;

    public ASTFunctionCall(String name) {
        this.name = name;
        this.arguments = new ArrayList<ASTExpression>();
    }

    public String getArgString() {
        StringBuilder sb = new StringBuilder();

        for (ASTExpression e : arguments) {
            sb.append(e.type.toString() + ",");
        }

        return sb.toString();
    }

    public String getIdentifyingString() {
        StringBuilder sb = new StringBuilder();

        sb.append(name);
        sb.append(getArgString());

        return sb.toString();
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
        argString.append("(");
        for (ASTExpression e : arguments) {
            String exprString = e.toString();
            if (argString.length() > 1) {
                argString.append(", ");
            }
            argString.append(exprString);
        }
        argString.append(")");

        if (ASTFunction.libraryFunctionStrings.containsKey(name)) {
            return String.format("%s%s", ASTFunction.libraryFunctionStrings.get(name), argString);
        }

        StringBuilder sb = new StringBuilder();

        // Must be a constructor
        if (ASTClass.nameClassMap.containsKey(name)) {
            sb.append("new ");
        }

        sb.append(String.format("%s%s", name, argString));

        return sb.toString();
    }
}
