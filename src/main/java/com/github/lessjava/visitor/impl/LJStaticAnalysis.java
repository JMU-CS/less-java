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
        if (node.functions.stream().anyMatch(f -> f.name.equals("main")) && !node.statements.isEmpty()) {
            addError("Remove main function, or move statements into your main function");
        }
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
        // TODO Auto-generated method stub
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTVariable node) {
        // TODO Auto-generated method stub
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTBlock node) {
        // TODO Auto-generated method stub
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTBlock node) {
        // TODO Auto-generated method stub
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTAssignment node) {
        // TODO Auto-generated method stub
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTAssignment node) {
        // TODO Auto-generated method stub
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTVoidFunctionCall node) {
        // TODO Auto-generated method stub
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTVoidFunctionCall node) {
        // TODO Auto-generated method stub
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTConditional node) {
        // TODO Auto-generated method stub
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTConditional node) {
        // TODO Auto-generated method stub
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTWhileLoop node) {
        // TODO Auto-generated method stub
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTWhileLoop node) {
        // TODO Auto-generated method stub
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTReturn node) {
    }

    @Override
    public void postVisit(ASTReturn node) {
        // TODO Auto-generated method stub
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTBreak node) {
        // TODO Auto-generated method stub
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTBreak node) {
        // TODO Auto-generated method stub
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTContinue node) {
        // TODO Auto-generated method stub
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTContinue node) {
        // TODO Auto-generated method stub
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTTest node) {
        // TODO Auto-generated method stub
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTTest node) {
        // TODO Auto-generated method stub
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTBinaryExpr node) {
        // TODO Auto-generated method stub
        super.preVisit(node);
    }

    @Override
    public void inVisit(ASTBinaryExpr node) {
        // TODO Auto-generated method stub
        super.inVisit(node);
    }

    @Override
    public void postVisit(ASTBinaryExpr node) {
        // TODO Auto-generated method stub
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTUnaryExpr node) {
        // TODO Auto-generated method stub
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTUnaryExpr node) {
        // TODO Auto-generated method stub
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTFunctionCall node) {
        // TODO Auto-generated method stub
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTFunctionCall node) {
        // TODO Auto-generated method stub
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTLocation node) {
        // TODO Auto-generated method stub
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTLocation node) {
        // TODO Auto-generated method stub
        super.postVisit(node);
    }

    @Override
    public void preVisit(ASTLiteral node) {
        // TODO Auto-generated method stub
        super.preVisit(node);
    }

    @Override
    public void postVisit(ASTLiteral node) {
        // TODO Auto-generated method stub
        super.postVisit(node);
    }

}
