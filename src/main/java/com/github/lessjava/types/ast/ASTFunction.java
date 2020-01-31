package com.github.lessjava.types.ast;

import com.github.lessjava.types.inference.HMType;

/**
 * Less-Java function declaration. Contains a name, a return type, a list of formal
 * parameters, and a function body (block).
 */
public class ASTFunction extends ASTAbstractFunction {
    public ASTFunction(String name, HMType returnType, ASTBlock body) {
        super(name, returnType, body);
    }

    public ASTFunction(String name, ASTBlock body) {
        super(name, body);
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        // Library functions have null bodies
        if (body != null) {
            body.traverse(visitor);
        }
        visitor.postVisit(this);
    }
}
