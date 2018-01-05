package com.github.lessjava.types.ast;

import com.github.lessjava.types.inference.HMType;

public class ASTMethod extends ASTFunction {
    public ASTMethod(String name, HMType returnType, ASTBlock body) {
        super(name, returnType, body);
    }
}
