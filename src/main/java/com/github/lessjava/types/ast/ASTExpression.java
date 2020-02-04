package com.github.lessjava.types.ast;

import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.types.inference.impl.HMTypeVar;

/**
 * Any Less-Java expression that can be evaluated to a value at runtime.
 *
 * Can be any of the following:
 *
 * <ul>
 * <li>{@link ASTBinaryExpr}
 * <li>{@link ASTUnaryExpr}
 * <li>{@link ASTFunctionCall}
 * <li>{@link ASTLocation}
 * <li>{@link ASTLiteral}
 * </ul>
 *
 */
public abstract class ASTExpression extends ASTNode {
    public HMType type = new HMTypeVar();
    public boolean isCollection;
}
