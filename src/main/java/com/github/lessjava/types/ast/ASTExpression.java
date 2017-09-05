package com.github.lessjava.types.ast;

/**
 * Any Decaf expression that can be evaluated to a value at runtime.
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
public abstract class ASTExpression extends ASTNode
{
    public ASTNode.DataType type = ASTNode.DataType.UNKNOWN;
}
