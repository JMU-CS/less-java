package com.github.lessjava.visitor;

import com.github.lessjava.types.ast.ASTArgList;
import com.github.lessjava.types.ast.ASTAssignment;
import com.github.lessjava.types.ast.ASTAttribute;
import com.github.lessjava.types.ast.ASTBinaryExpr;
import com.github.lessjava.types.ast.ASTBlock;
import com.github.lessjava.types.ast.ASTBreak;
import com.github.lessjava.types.ast.ASTClass;
import com.github.lessjava.types.ast.ASTClassBlock;
import com.github.lessjava.types.ast.ASTClassSignature;
import com.github.lessjava.types.ast.ASTCollection;
import com.github.lessjava.types.ast.ASTConditional;
import com.github.lessjava.types.ast.ASTContinue;
import com.github.lessjava.types.ast.ASTCoerceIntToDouble;
import com.github.lessjava.types.ast.ASTEntry;
import com.github.lessjava.types.ast.ASTForLoop;
import com.github.lessjava.types.ast.ASTFunction;
import com.github.lessjava.types.ast.ASTFunctionCall;
import com.github.lessjava.types.ast.ASTGlobalAssignment;
import com.github.lessjava.types.ast.ASTList;
import com.github.lessjava.types.ast.ASTLiteral;
import com.github.lessjava.types.ast.ASTLocation;
import com.github.lessjava.types.ast.ASTMap;
import com.github.lessjava.types.ast.ASTMemberAccess;
import com.github.lessjava.types.ast.ASTMethod;
import com.github.lessjava.types.ast.ASTMethodCall;
import com.github.lessjava.types.ast.ASTNode;
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.types.ast.ASTReturn;
import com.github.lessjava.types.ast.ASTSet;
import com.github.lessjava.types.ast.ASTTest;
import com.github.lessjava.types.ast.ASTUnaryExpr;
import com.github.lessjava.types.ast.ASTVariable;
import com.github.lessjava.types.ast.ASTVisitor;
import com.github.lessjava.types.ast.ASTVoidAssignment;
import com.github.lessjava.types.ast.ASTVoidFunctionCall;
import com.github.lessjava.types.ast.ASTVoidMethodCall;
import com.github.lessjava.types.ast.ASTWhileLoop;

/**
 * Basic "stub" implementation of the {@link ASTVisitor} interface. All of the
 * {@code preVisit} functions call {@link #defaultPreVisit defaultPreVisit()}
 * and all of the {@code postVisit} functions call {@link #defaultPostVisit
 * defaultPostVisit()}. This allows subclasses to selectively implement any
 * parts of the visitor interface, and optionally to define default behavior for
 * any unimplemented parts of the interface.
 */
public class LJDefaultASTVisitor implements ASTVisitor {
    public void defaultPreVisit(ASTNode node) {
    }

    public void defaultInVisit(ASTNode node) {
    }

    public void defaultPostVisit(ASTNode node) {
    }

    public void preVisit(ASTProgram node) {
        defaultPreVisit(node);
    }

    public void postVisit(ASTProgram node) {
        defaultPostVisit(node);
    }

    public void preVisit(ASTFunction node) {
        defaultPreVisit(node);
    }

    public void postVisit(ASTFunction node) {
        defaultPostVisit(node);
    }

    public void preVisit(ASTGlobalAssignment node) {
        defaultPreVisit(node);
    }

    public void postVisit(ASTGlobalAssignment node) {
        defaultPostVisit(node);
    }

    public void preVisit(ASTMethod node) {
        defaultPreVisit(node);
    }

    public void postVisit(ASTMethod node) {
        defaultPostVisit(node);
    }

    public void preVisit(ASTClass node) {
        defaultPreVisit(node);
    }

    public void postVisit(ASTClass node) {
        defaultPostVisit(node);
    }

    public void preVisit(ASTVariable node) {
        defaultPreVisit(node);
    }

    public void postVisit(ASTVariable node) {
        defaultPostVisit(node);
    }

    public void preVisit(ASTMemberAccess node) {
        defaultPreVisit(node);
    }

    public void postVisit(ASTMemberAccess node) {
        defaultPostVisit(node);
    }

    public void preVisit(ASTBlock node) {
        defaultPreVisit(node);
    }

    public void postVisit(ASTBlock node) {
        defaultPostVisit(node);
    }

    public void preVisit(ASTArgList node) {
        defaultPreVisit(node);
    }

    public void postVisit(ASTArgList node) {
        defaultPostVisit(node);
    }

    public void preVisit(ASTVoidAssignment node) {
        defaultPreVisit(node);
    }

    public void postVisit(ASTVoidAssignment node) {
        defaultPostVisit(node);
    }

    public void preVisit(ASTVoidFunctionCall node) {
        defaultPreVisit(node);
    }

    public void postVisit(ASTVoidFunctionCall node) {
        defaultPostVisit(node);
    }

    public void preVisit(ASTVoidMethodCall node) {
        defaultPreVisit(node);
    }

    public void postVisit(ASTVoidMethodCall node) {
        defaultPostVisit(node);
    }

    public void preVisit(ASTConditional node) {
        defaultPreVisit(node);
    }

    public void postVisit(ASTConditional node) {
        defaultPostVisit(node);
    }

    public void preVisit(ASTWhileLoop node) {
        defaultPreVisit(node);
    }

    public void postVisit(ASTWhileLoop node) {
        defaultPostVisit(node);
    }

    public void preVisit(ASTForLoop node) {
        defaultPreVisit(node);
    }

    public void postVisit(ASTForLoop node) {
        defaultPostVisit(node);
    }

    public void preVisit(ASTReturn node) {
        defaultPreVisit(node);
    }

    public void postVisit(ASTReturn node) {
        defaultPostVisit(node);
    }

    public void preVisit(ASTBreak node) {
        defaultPreVisit(node);
    }

    public void postVisit(ASTBreak node) {
        defaultPostVisit(node);
    }

    public void preVisit(ASTContinue node) {
        defaultPreVisit(node);
    }

    public void postVisit(ASTContinue node) {
        defaultPostVisit(node);
    }

    public void preVisit(ASTTest node) {
        defaultPreVisit(node);
    }

    public void postVisit(ASTTest node) {
        defaultPostVisit(node);
    }

    public void preVisit(ASTBinaryExpr node) {
        defaultPreVisit(node);
    }

    public void inVisit(ASTBinaryExpr node) {
        defaultInVisit(node);
    }

    public void postVisit(ASTBinaryExpr node) {
        defaultPostVisit(node);
    }

    public void preVisit(ASTUnaryExpr node) {
        defaultPreVisit(node);
    }

    public void postVisit(ASTUnaryExpr node) {
        defaultPostVisit(node);
    }

    public void preVisit(ASTAssignment node) {
        defaultPreVisit(node);
    }

    public void postVisit(ASTAssignment node) {
        defaultPostVisit(node);
    }

    public void preVisit(ASTFunctionCall node) {
        defaultPreVisit(node);
    }

    public void postVisit(ASTFunctionCall node) {
        defaultPostVisit(node);
    }

    public void preVisit(ASTMethodCall node) {
        defaultPreVisit(node);
    }

    public void postVisit(ASTMethodCall node) {
        defaultPostVisit(node);
    }

    public void preVisit(ASTLocation node) {
        defaultPreVisit(node);
    }

    public void postVisit(ASTLocation node) {
        defaultPostVisit(node);
    }

    public void preVisit(ASTLiteral node) {
        defaultPreVisit(node);
    }

    public void postVisit(ASTLiteral node) {
        defaultPostVisit(node);
    }

    public void preVisit(ASTList node) {
        defaultPreVisit(node);
    }

    public void postVisit(ASTList node) {
        defaultPostVisit(node);
    }

    public void preVisit(ASTSet node) {
        defaultPreVisit(node);
    }

    public void postVisit(ASTSet node) {
        defaultPostVisit(node);
    }

    public void preVisit(ASTMap node) {
        defaultPreVisit(node);
    }

    public void postVisit(ASTMap node) {
        defaultPostVisit(node);
    }

    public void postVisit(ASTCollection node) {
        defaultPostVisit(node);
    }

    public void preVisit(ASTEntry node) {
        defaultPreVisit(node);
    }

    public void postVisit(ASTEntry node) {
        defaultPostVisit(node);
    }

    public void inVisit(ASTConditional node) {
        defaultInVisit(node);
    }

    @Override
    public void preVisit(ASTClassSignature node) {
        defaultPreVisit(node);
    }

    @Override
    public void postVisit(ASTClassSignature node) {
        defaultPostVisit(node);
    }

    @Override
    public void preVisit(ASTClassBlock node) {
        defaultPreVisit(node);
    }

    @Override
    public void postVisit(ASTClassBlock node) {
        defaultPostVisit(node);
    }

    @Override
    public void preVisit(ASTAttribute node) {
        defaultPreVisit(node);
    }

    @Override
    public void postVisit(ASTAttribute node) {
        defaultPostVisit(node);
    }

    @Override
    public void preVisit(ASTCoerceIntToDouble node) {
        defaultPreVisit(node);
    }

    @Override
    public void postVisit(ASTCoerceIntToDouble node) {
        defaultPostVisit(node);
    }
}
