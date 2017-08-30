package com.github.lessjava.types.ast;

/**
 * Any Decaf statement that can be executed at runtime.
 * 
 * Can be any of the following:
 *
 * <ul>
 * <li> {@link ASTAssignment}
 * <li> {@link ASTVoidFunctionCall}
 * <li> {@link ASTConditional}
 * <li> {@link ASTWhileLoop}
 * <li> {@link ASTReturn}
 * <li> {@link ASTBreak}
 * <li> {@link ASTContinue}
 * </ul>
 *
 */
public abstract class ASTStatement extends ASTNode { }

