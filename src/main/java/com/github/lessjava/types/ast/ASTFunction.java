package com.github.lessjava.types.ast;

import com.github.lessjava.types.inference.HMType;

/**
 * Decaf function declaration. Contains a name, a return type, a list of formal
 * parameters, and a function body (block).
 */
public class ASTFunction extends ASTAbstractFunction {
    public ASTFunction(String name, HMType returnType, ASTBlock body) {
        super(name, returnType, body);
    }

    public ASTFunction(String name, ASTBlock body) {
        super(name, body);
    }
}
