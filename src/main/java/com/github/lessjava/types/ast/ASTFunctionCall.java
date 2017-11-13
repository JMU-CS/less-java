package com.github.lessjava.types.ast;

import static com.github.lessjava.visitor.impl.LJGenerateJava.libraryFunctions;

import java.util.ArrayList;
import java.util.List;

import com.github.lessjava.visitor.impl.LJGenerateJava;

/**
 * Decaf function call that is intended to return a value at runtime.
 */
public class ASTFunctionCall extends ASTExpression {
    public String name;
    public List<ASTExpression> arguments;

    public ASTFunctionCall(String name) {
        this.name = name;
        this.arguments = new ArrayList<ASTExpression>();
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
        StringBuffer argString = new StringBuffer();
        argString.append("(");
        for (ASTExpression e : arguments) {
            String exprString = e.toString();
            if (e instanceof ASTFunctionCall) {
                ASTFunctionCall f = (ASTFunctionCall) e;
                if (libraryFunctions.containsKey(f.name)) {
                    exprString = libraryFunctions.get(f.name);
                }
            }
            if (argString.length() > 1) {
                argString.append(", ");
            }
            argString.append(exprString);
        }
        argString.append(")");
        
        if (LJGenerateJava.libraryFunctions.containsKey(name)) {
            return LJGenerateJava.libraryFunctions.get(name) + argString;
        }

        return name + argString;
    }
}
