package com.github.lessjava.visitor.impl;

import com.github.lessjava.types.ast.ASTAbstractFunction;
import com.github.lessjava.types.ast.ASTAssignment;
import com.github.lessjava.types.ast.ASTBinaryExpr;
import com.github.lessjava.types.ast.ASTBlock;
import com.github.lessjava.types.ast.ASTClass;
import com.github.lessjava.types.ast.ASTConditional;
import com.github.lessjava.types.ast.ASTEntry;
import com.github.lessjava.types.ast.ASTExpression;
import com.github.lessjava.types.ast.ASTForLoop;
import com.github.lessjava.types.ast.ASTFunction;
import com.github.lessjava.types.ast.ASTFunctionCall;
import com.github.lessjava.types.ast.ASTList;
import com.github.lessjava.types.ast.ASTMap;
import com.github.lessjava.types.ast.ASTMethod;
import com.github.lessjava.types.ast.ASTMethodCall;
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.types.ast.ASTReturn;
import com.github.lessjava.types.ast.ASTSet;
import com.github.lessjava.types.ast.ASTStatement;
import com.github.lessjava.types.ast.ASTTest;
import com.github.lessjava.types.ast.ASTUnaryExpr;
import com.github.lessjava.types.ast.ASTVariable;
import com.github.lessjava.types.ast.ASTVoidAssignment;
import com.github.lessjava.types.ast.ASTVoidFunctionCall;
import com.github.lessjava.types.ast.ASTVoidMethodCall;
import com.github.lessjava.types.ast.ASTWhileLoop;
import com.github.lessjava.visitor.LJDefaultASTVisitor;

/**
 * AST pre-order visitor; initializes parent links for use in later AST
 * analyses.
 *
 */
public class BuildParentLinks extends LJDefaultASTVisitor {
    @Override
    public void preVisit(ASTProgram node) {
        for (ASTClass c : node.classes) {
            c.setParent(node);
        }

        for (ASTAbstractFunction function : node.functions) {
            function.setParent(node);
        }

        for (ASTTest test : node.tests) {
            test.setParent(node);
        }
    }

    @Override
    public void preVisit(ASTClass node) {
        for (ASTVariable attribute: node.attributes) {
            attribute.setParent(node);
        }

        for (ASTMethod method: node.methods) {
            method.setParent(node);
        }
    }

    @Override
    public void preVisit(ASTFunction node) {
        // Library functions have null bodies
        if (node.body != null) {
            node.body.setParent(node);
        }
    }

    @Override
    public void preVisit(ASTMethod node) {
        // Library methods don't have null bodies
        if (node.body != null) {
            node.body.setParent(node);
        }
    }

    @Override
    public void preVisit(ASTBlock node) {
        for (ASTVariable var : node.variables) {
            var.setParent(node);
        }
        for (ASTStatement stmt : node.statements) {
            stmt.setParent(node);
        }
    }

    @Override
    public void preVisit(ASTAssignment node) {
        node.variable.setParent(node);
        node.value.setParent(node);
    }

    @Override
    public void preVisit(ASTVoidAssignment node) {
        node.variable.setParent(node);
        node.value.setParent(node);
    }

    @Override
    public void preVisit(ASTVoidFunctionCall node) {
        for (ASTExpression expr : node.arguments) {
            expr.setParent(node);
        }
    }

    @Override
    public void preVisit(ASTVoidMethodCall node) {
        node.invoker.setParent(node);
        node.funcCall.setParent(node);
    }

    @Override
    public void preVisit(ASTConditional node) {
        node.condition.setParent(node);
        node.ifBlock.setParent(node);
        if (node.hasElseBlock()) {
            node.elseBlock.setParent(node);
        }
    }

    @Override
    public void preVisit(ASTWhileLoop node) {
        node.guard.setParent(node);
        node.body.setParent(node);
    }

    @Override
    public void preVisit(ASTForLoop node) {
        node.var.setParent(node);
        if (node.lowerBound != null) {
            node.lowerBound.setParent(node);
        }
        node.upperBound.setParent(node);
        node.block.setParent(node);
    }

    @Override
    public void preVisit(ASTReturn node) {
        if (node.hasValue()) {
            node.value.setParent(node);
        }
    }

    @Override
    public void preVisit(ASTTest node) {
        node.expr.setParent(node);
    }

    // no need for ASTBreak or ASTContinue handlers (no children)

    @Override
    public void preVisit(ASTBinaryExpr node) {
        node.leftChild.setParent(node);
        node.rightChild.setParent(node);
    }

    @Override
    public void preVisit(ASTUnaryExpr node) {
        node.child.setParent(node);
    }

    @Override
    public void preVisit(ASTFunctionCall node) {
        for (ASTExpression expr : node.arguments) {
            expr.setParent(node);
        }
    }

    @Override
    public void preVisit(ASTMethodCall node) {
        node.invoker.setParent(node);
        node.funcCall.setParent(node);
    }

    @Override
    public void preVisit(ASTList node) {
        node.initialElements.setParent(node);
    }

    @Override
    public void preVisit(ASTSet node) {
        node.initialElements.setParent(node);
    }

    @Override
    public void preVisit(ASTMap node) {
        node.initialElements.setParent(node);
    }

    @Override
    public void preVisit(ASTEntry node) {
        node.key.setParent(node);
        node.value.setParent(node);
    }

    // no need for ASTLocation handler (no children)

    // no need for ASTLiteral handler (no children)
}
