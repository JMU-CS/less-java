package com.github.lessjava.types.ast;

/**
 * Definition of a Visitor interface for AST trees.
 *
 * Because there are so many functions, it is recommended that new visitor
 * classes extend {@link DefaultASTVisitor} (which contains empty default stubs
 * for all of these functions) rather than attempt to implement
 * {@link ASTVisitor} directly.
 */
public interface ASTVisitor {
    public void preVisit(ASTProgram node);

    public void postVisit(ASTProgram node);

    public void preVisit(ASTFunction node);

    public void postVisit(ASTFunction node);

    public void preVisit(ASTVariable node);

    public void postVisit(ASTVariable node);

    public void preVisit(ASTBlock node);

    public void postVisit(ASTBlock node);

    public void preVisit(ASTAssignment node);

    public void postVisit(ASTAssignment node);

    public void preVisit(ASTVoidFunctionCall node);

    public void postVisit(ASTVoidFunctionCall node);

    public void preVisit(ASTConditional node);

    public void inVisit(ASTConditional node);

    public void postVisit(ASTConditional node);

    public void preVisit(ASTWhileLoop node);

    public void postVisit(ASTWhileLoop node);

    public void preVisit(ASTReturn node);

    public void postVisit(ASTReturn node);

    public void preVisit(ASTBreak node);

    public void postVisit(ASTBreak node);

    public void preVisit(ASTContinue node);

    public void postVisit(ASTContinue node);

    public void preVisit(ASTTest node);

    public void postVisit(ASTTest node);

    public void preVisit(ASTBinaryExpr node);

    public void inVisit(ASTBinaryExpr node);

    public void postVisit(ASTBinaryExpr node);

    public void preVisit(ASTUnaryExpr node);

    public void postVisit(ASTUnaryExpr node);

    public void preVisit(ASTFunctionCall node);

    public void postVisit(ASTFunctionCall node);

    public void preVisit(ASTLocation node);

    public void postVisit(ASTLocation node);

    public void preVisit(ASTLiteral node);

    public void postVisit(ASTLiteral node);

    public void preVisit(ASTArgList astArgList);

    public void postVisit(ASTArgList astArgList);
}
