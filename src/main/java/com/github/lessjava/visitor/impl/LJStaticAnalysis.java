package com.github.lessjava.visitor.impl;

import com.github.lessjava.ast.ASTNode;
import com.github.lessjava.generated.LJBaseListener;
import com.github.lessjava.generated.LJParser;

public class LJStaticAnalysis extends LJBaseListener {

    private ASTNode.DataType functionReturnType;

    public LJStaticAnalysis() {
    }

    @Override
    public void exitProgram(LJParser.ProgramContext ctx) {
    }

    @Override
    public void exitFunction(LJParser.FunctionContext ctx) {
    }

    @Override
    public void enterBlock(LJParser.BlockContext ctx) {
    }

    @Override
    public void exitBlock(LJParser.BlockContext ctx) {
    }

    @Override
    public void exitAssignment(LJParser.AssignmentContext ctx) {
    }

    @Override
    public void exitConditional(LJParser.ConditionalContext ctx) {
    }

    @Override
    public void exitWhile(LJParser.WhileContext ctx) {
    }

    @Override
    public void exitReturn(LJParser.ReturnContext ctx) {
    }

    @Override
    public void exitBreak(LJParser.BreakContext ctx) {
    }

    @Override
    public void exitContinue(LJParser.ContinueContext ctx) {
    }

    @Override
    public void exitTest(LJParser.TestContext ctx) {
    }

    @Override
    public void exitLit(LJParser.LitContext ctx) {
    }
}
