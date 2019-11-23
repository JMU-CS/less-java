package com.github.lessjava.visitor.impl;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import com.github.lessjava.types.ast.ASTArgList;
import com.github.lessjava.types.ast.ASTAssignment;
import com.github.lessjava.types.ast.ASTBinaryExpr;
import com.github.lessjava.types.ast.ASTBlock;
import com.github.lessjava.types.ast.ASTBreak;
import com.github.lessjava.types.ast.ASTConditional;
import com.github.lessjava.types.ast.ASTContinue;
import com.github.lessjava.types.ast.ASTEntry;
import com.github.lessjava.types.ast.ASTExpression;
import com.github.lessjava.types.ast.ASTForLoop;
import com.github.lessjava.types.ast.ASTFunctionCall;
import com.github.lessjava.types.ast.ASTList;
import com.github.lessjava.types.ast.ASTLiteral;
import com.github.lessjava.types.ast.ASTLocation;
import com.github.lessjava.types.ast.ASTMap;
import com.github.lessjava.types.ast.ASTMemberAccess;
import com.github.lessjava.types.ast.ASTMethodCall;
import com.github.lessjava.types.ast.ASTReturn;
import com.github.lessjava.types.ast.ASTSet;
import com.github.lessjava.types.ast.ASTStatement;
import com.github.lessjava.types.ast.ASTUnaryExpr;
import com.github.lessjava.types.ast.ASTVariable;
import com.github.lessjava.types.ast.ASTVoidAssignment;
import com.github.lessjava.types.ast.ASTVoidFunctionCall;
import com.github.lessjava.types.ast.ASTVoidMethodCall;
import com.github.lessjava.types.ast.ASTWhileLoop;
import com.github.lessjava.types.inference.impl.HMTypeBase;
import com.github.lessjava.visitor.LJDefaultASTVisitor;

public class LJDuplicateBlock extends LJDefaultASTVisitor {

    private Deque<ASTBlock> blocks;
    private Deque<ASTStatement> statements;
    private Deque<ASTExpression> expressions;
    private ASTBlock lastCompletedBlock;

    /**
     * Returns a duplicated version of the given block. The duplicated block will have all type
     * information erased, so that it can be retyped independent from the types calculated for the
     * given block. Useful for instantiating functions, so that different parameter bindings don't
     * cause issues.
     * @param blockToDuplicate the block to be duplicated
     * @return a copy of the given block that has type information erased
     */
    public ASTBlock duplicateBlock(ASTBlock blockToDuplicate) {
        blocks = new ArrayDeque<>();
        statements = new ArrayDeque<>();
        expressions = new ArrayDeque<>();
        blockToDuplicate.traverse(this);
        return blocks.pop();
    }

    @Override
    public void preVisit(ASTBreak node) {
        ASTBreak breakCopy = new ASTBreak();
        breakCopy.lineNumber = node.lineNumber;
        statements.push(breakCopy);
    }

    @Override
    public void postVisit(ASTBreak node) {
        blocks.peek().statements.add(statements.pop());
    }

    @Override
    public void preVisit(ASTConditional node) {
        ASTConditional conditionalCopy = new ASTConditional(null, null);
        conditionalCopy.lineNumber = node.lineNumber;
        statements.push(conditionalCopy);
    }

    @Override
    public void postVisit(ASTConditional node) {
        ASTConditional conditionalCopy = (ASTConditional)statements.pop();
        conditionalCopy.condition = expressions.pop();
        if(node.hasElseBlock()) {
            conditionalCopy.elseBlock = blocks.pop();
            conditionalCopy.ifBlock = blocks.pop();
        } else {
            conditionalCopy.ifBlock = blocks.pop();
        }
        blocks.peek().statements.add(conditionalCopy);
    }

    @Override
    public void preVisit(ASTContinue node) {
        ASTContinue continueCopy = new ASTContinue();
        continueCopy.lineNumber = node.lineNumber;
        statements.push(continueCopy);
    }

    @Override
    public void postVisit(ASTContinue node) {
        blocks.peek().statements.add(statements.pop());
    }

    @Override
    public void preVisit(ASTForLoop node) {
        ASTForLoop forCopy = new ASTForLoop(null, null, null, null);
        forCopy.lineNumber = node.lineNumber;
        statements.push(forCopy);
    }

    public void postVisit(ASTForLoop node) {
        ASTForLoop forCopy = (ASTForLoop)statements.pop();
        forCopy.block = blocks.pop();
        forCopy.upperBound = expressions.pop();
        if(node.lowerBound != null) {
            forCopy.lowerBound = expressions.pop();
        }
        forCopy.var = (ASTVariable)expressions.pop();
        blocks.peek().statements.add(forCopy);
    }

    @Override
    public void preVisit(ASTReturn node) {
        ASTReturn returnCopy = new ASTReturn();
        returnCopy.lineNumber = node.lineNumber;
        statements.push(returnCopy);
    }

    @Override
    public void postVisit(ASTReturn node) {
        ASTReturn returnCopy = (ASTReturn)statements.pop();
        if(node.hasValue()) {
            returnCopy.value = expressions.pop();
        }
        blocks.peek().statements.add(returnCopy);
    }

    @Override
    public void preVisit(ASTVoidAssignment node) {
        ASTVoidAssignment assignmentCopy = new ASTVoidAssignment(null);
        assignmentCopy.lineNumber = node.lineNumber;
        statements.push(assignmentCopy);
    }

    @Override
    public void postVisit(ASTVoidAssignment node) {
        ASTVoidAssignment assignmentCopy = (ASTVoidAssignment)statements.pop();
        assignmentCopy.assignment = (ASTAssignment)expressions.pop();
        blocks.peek().statements.add(assignmentCopy);
    }

    @Override
    public void preVisit(ASTVoidFunctionCall node) {
        ASTVoidFunctionCall functionCallCopy = new ASTVoidFunctionCall(null);
        functionCallCopy.lineNumber = node.lineNumber;
        statements.push(functionCallCopy);
    }

    @Override
    public void postVisit(ASTVoidFunctionCall node) {
        ASTVoidFunctionCall functionCallCopy = (ASTVoidFunctionCall)statements.pop();
        functionCallCopy.functionCall = (ASTFunctionCall)expressions.pop();
        blocks.peek().statements.add(functionCallCopy);
    }

    @Override
    public void preVisit(ASTVoidMethodCall node) {
        ASTVoidMethodCall methodCallCopy = new ASTVoidMethodCall(null);
        methodCallCopy.lineNumber = node.lineNumber;
        statements.push(methodCallCopy);
    }

    @Override
    public void postVisit(ASTVoidMethodCall node) {
        ASTVoidMethodCall methodCallCopy = (ASTVoidMethodCall)statements.pop();
        methodCallCopy.methodCall = (ASTMethodCall)expressions.pop();
        blocks.peek().statements.add(methodCallCopy);
    }

    @Override
    public void preVisit(ASTWhileLoop node) {
        ASTWhileLoop whileCopy = new ASTWhileLoop(null, null);
        whileCopy.lineNumber = node.lineNumber;
        statements.push(whileCopy);
    }

    @Override
    public void postVisit(ASTWhileLoop node) {
        ASTWhileLoop whileCopy = (ASTWhileLoop)statements.pop();
        whileCopy.guard = expressions.pop();
        whileCopy.body = blocks.pop();
        blocks.peek().statements.add(whileCopy);
    }

    @Override
    public void postVisit(ASTSet node) {
        ASTArgList argListCopy = (ASTArgList)expressions.pop();
        ASTSet setCopy = new ASTSet(argListCopy);
        setCopy.lineNumber = node.lineNumber;
        expressions.push(setCopy);
    }

    @Override
    public void postVisit(ASTMap node) {
        ASTArgList argListCopy = (ASTArgList)expressions.pop();
        ASTMap mapCopy = new ASTMap(argListCopy);
        mapCopy.lineNumber = node.lineNumber;
        expressions.push(mapCopy);
    }

    @Override
    public void postVisit(ASTList node) {
        ASTArgList argListCopy = (ASTArgList)expressions.pop();
        ASTList listCopy = new ASTList(argListCopy);
        listCopy.lineNumber = node.lineNumber;
        expressions.push(listCopy);
    }

    @Override
    public void postVisit(ASTLocation node) {
        ASTLocation locationCopy = new ASTLocation(node.name);
        locationCopy.lineNumber = node.lineNumber;
        expressions.push(locationCopy);
    }

    @Override
    public void postVisit(ASTBinaryExpr node) {
        ASTExpression rightChild = expressions.pop();
        ASTExpression leftChild = expressions.pop();
        ASTBinaryExpr binExCopy = new ASTBinaryExpr(node.operator, leftChild, rightChild);
        binExCopy.lineNumber = node.lineNumber;
        expressions.push(binExCopy);
    }

    @Override
    public void postVisit(ASTAssignment node) {
        ASTExpression valueCopy = expressions.pop();
        ASTAssignment assignmentCopy;
        if(expressions.peek() instanceof ASTMemberAccess) {
            ASTMemberAccess memberCopy = (ASTMemberAccess)expressions.pop();
            assignmentCopy = new ASTAssignment(node.op, memberCopy, valueCopy);
        } else {
            ASTVariable varCopy = (ASTVariable)expressions.pop();
            assignmentCopy = new ASTAssignment(node.op, varCopy, valueCopy);
        }
        assignmentCopy.lineNumber = node.lineNumber;
        expressions.push(assignmentCopy);
    }

    @Override
    public void postVisit(ASTMethodCall node) {
        ASTFunctionCall functionCallCopy = (ASTFunctionCall)expressions.pop();
        ASTExpression invokerCopy = expressions.pop();
        ASTMethodCall methodCallCopy = new ASTMethodCall(invokerCopy, functionCallCopy);
        methodCallCopy.lineNumber = node.lineNumber;
        expressions.push(methodCallCopy);
    }

    @Override
    public void postVisit(ASTMemberAccess node) {
        ASTVariable varCopy = (ASTVariable)expressions.pop();
        ASTVariable instanceCopy = (ASTVariable) expressions.pop();
        ASTMemberAccess memberAccessCopy = new ASTMemberAccess(instanceCopy.name, varCopy);
        memberAccessCopy.lineNumber = node.lineNumber;
        expressions.push(memberAccessCopy);
    }

    @Override
    public void postVisit(ASTEntry node) {
        ASTExpression valueCopy = expressions.pop();
        ASTExpression keyCopy = expressions.pop();
        ASTEntry entryCopy = new ASTEntry(keyCopy, valueCopy);
        entryCopy.lineNumber = node.lineNumber;
        expressions.push(entryCopy);
    }

    @Override
    public void postVisit(ASTArgList node) {
        Deque<ASTExpression> argsStack = new ArrayDeque<>();
        for(int i = 0; i < node.arguments.size(); i++) {
            argsStack.push(expressions.pop());
        }
        List<ASTExpression> argsCopy = new ArrayList<>();
        while(!argsStack.isEmpty()) {
            argsCopy.add(argsStack.pop());
        }
        ASTArgList argListCopy = new ASTArgList(argsCopy);
        argListCopy.lineNumber = node.lineNumber;
        expressions.push(argListCopy);
    }

    @Override
    public void postVisit(ASTFunctionCall node) {
        ASTFunctionCall functionCallCopy = new ASTFunctionCall(node.name);
        functionCallCopy.lineNumber = node.lineNumber;
        Deque<ASTExpression> argsStack = new ArrayDeque<>();
        for(int i = 0; i < node.arguments.size(); i++) {
            argsStack.push(expressions.pop());
        }
        while(!argsStack.isEmpty()) {
            functionCallCopy.arguments.add(argsStack.pop());
        }
        expressions.push(functionCallCopy);
    }

    @Override
    public void postVisit(ASTLiteral node) {
        ASTLiteral literalCopy = new ASTLiteral(((HMTypeBase)node.type).getBaseType(), node.value);
        literalCopy.lineNumber = node.lineNumber;
        expressions.push(literalCopy);
    }

    @Override
    public void postVisit(ASTUnaryExpr node) {
        ASTUnaryExpr unaryExprCopy = new ASTUnaryExpr(node.operator, expressions.pop());
        unaryExprCopy.lineNumber = node.lineNumber;
        expressions.push(unaryExprCopy);
    }

    @Override
    public void postVisit(ASTVariable node) {
        ASTVariable varCopy;
        if(node.isCollection) {
            varCopy = new ASTVariable(node.name, expressions.pop());
        } else {
            varCopy = new ASTVariable(node.name);
        }
        varCopy.lineNumber = node.lineNumber;
        expressions.push(varCopy);
    }

    @Override
    public void preVisit(ASTBlock node) {
        ASTBlock blockCopy = new ASTBlock();
        blockCopy.lineNumber = node.lineNumber;
        blocks.push(blockCopy);
    }
}
