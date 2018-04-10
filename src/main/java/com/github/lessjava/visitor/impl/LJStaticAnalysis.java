package com.github.lessjava.visitor.impl;

import com.github.lessjava.types.ast.ASTAssignment;
import com.github.lessjava.types.ast.ASTBinaryExpr;
import com.github.lessjava.types.ast.ASTBlock;
import com.github.lessjava.types.ast.ASTBreak;
import com.github.lessjava.types.ast.ASTConditional;
import com.github.lessjava.types.ast.ASTContinue;
import com.github.lessjava.types.ast.ASTFunction;
import com.github.lessjava.types.ast.ASTFunctionCall;
import com.github.lessjava.types.ast.ASTLiteral;
import com.github.lessjava.types.ast.ASTLocation;
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.types.ast.ASTReturn;
import com.github.lessjava.types.ast.ASTTest;
import com.github.lessjava.types.ast.ASTUnaryExpr;
import com.github.lessjava.types.ast.ASTVariable;
import com.github.lessjava.types.ast.ASTVoidFunctionCall;
import com.github.lessjava.types.ast.ASTWhileLoop;

public class LJStaticAnalysis extends StaticAnalysis {
    @Override
    public void preVisit(ASTProgram node) {
    }

    @Override
    public void postVisit(ASTProgram node) {
    }

    @Override
    public void preVisit(ASTFunction node) {
    }

    @Override
    public void postVisit(ASTFunction node) {
    }

    @Override
    public void preVisit(ASTVariable node) {
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTVariable node) {
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTBlock node) {
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTBlock node) {
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTAssignment node) {
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTAssignment node) {
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTVoidFunctionCall node) {
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTVoidFunctionCall node) {
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTConditional node) {
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTConditional node) {
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTWhileLoop node) {
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTWhileLoop node) {
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTReturn node) {
    }

    @Override
    public void postVisit(ASTReturn node) {
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTBreak node) {
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTBreak node) {
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTContinue node) {
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTContinue node) {
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTTest node) {
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTTest node) {
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTBinaryExpr node) {
        super.preVisit(node);
    }

    @Override
    public void inVisit(ASTBinaryExpr node) {
        super.inVisit(node);
    }

    @Override
    public void postVisit(ASTBinaryExpr node) {
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTUnaryExpr node) {
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTUnaryExpr node) {
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTFunctionCall node) {
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTFunctionCall node) {
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTLocation node) {
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTLocation node) {
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTLiteral node) {
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTLiteral node) {
        super.postVisit(node);
    }

}
