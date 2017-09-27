package com.github.lessjava.visitor.impl;

import java.util.LinkedHashMap;
import java.util.Stack;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.github.lessjava.generated.LJBaseListener;
import com.github.lessjava.generated.LJParser;
import com.github.lessjava.generated.LJParser.AssignmentContext;
import com.github.lessjava.generated.LJParser.BlockContext;
import com.github.lessjava.generated.LJParser.BreakContext;
import com.github.lessjava.generated.LJParser.ConditionalContext;
import com.github.lessjava.generated.LJParser.ContinueContext;
import com.github.lessjava.generated.LJParser.ExprBaseContext;
import com.github.lessjava.generated.LJParser.ExprBinContext;
import com.github.lessjava.generated.LJParser.ExprContext;
import com.github.lessjava.generated.LJParser.ExprUnContext;
import com.github.lessjava.generated.LJParser.FuncCallContext;
import com.github.lessjava.generated.LJParser.FunctionContext;
import com.github.lessjava.generated.LJParser.ProgramContext;
import com.github.lessjava.generated.LJParser.ReturnContext;
import com.github.lessjava.generated.LJParser.StatementContext;
import com.github.lessjava.generated.LJParser.TestContext;
import com.github.lessjava.generated.LJParser.VoidFunctionCallContext;
import com.github.lessjava.generated.LJParser.WhileContext;
import com.github.lessjava.types.ast.ASTAssignment;
import com.github.lessjava.types.ast.ASTBinaryExpr;
import com.github.lessjava.types.ast.ASTBlock;
import com.github.lessjava.types.ast.ASTBreak;
import com.github.lessjava.types.ast.ASTConditional;
import com.github.lessjava.types.ast.ASTContinue;
import com.github.lessjava.types.ast.ASTExpression;
import com.github.lessjava.types.ast.ASTFunction;
import com.github.lessjava.types.ast.ASTFunctionCall;
import com.github.lessjava.types.ast.ASTLiteral;
import com.github.lessjava.types.ast.ASTNode;
import com.github.lessjava.types.ast.ASTProgram;
import com.github.lessjava.types.ast.ASTReturn;
import com.github.lessjava.types.ast.ASTStatement;
import com.github.lessjava.types.ast.ASTTest;
import com.github.lessjava.types.ast.ASTUnaryExpr;
import com.github.lessjava.types.ast.ASTVariable;
import com.github.lessjava.types.ast.ASTVoidFunctionCall;
import com.github.lessjava.types.ast.ASTWhileLoop;
import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.types.inference.impl.HMTypeVar;

public class LJASTConverter extends LJBaseListener
{
    private ASTProgram                                ast;
    private LinkedHashMap<ParserRuleContext, ASTNode> map;

    private Stack<ASTBlock> blocks;

    public LJASTConverter()
    {
        map = new LinkedHashMap<>();
        blocks = new Stack<ASTBlock>();
    }

    @Override
    public void exitProgram(ProgramContext ctx)
    {
        ast = new ASTProgram();

        for (StatementContext s : ctx.statement()) {
            if (!s.getText().equals("\n")) {
                ast.statements.add((ASTStatement) map.get(s));
            }
        }

        for (FunctionContext f : ctx.function()) {
            ast.functions.add((ASTFunction) map.get(f));
        }

        ast.setDepth(ctx.depth());

        map.put(ctx, ast);
    }

    @Override
    public void exitFunction(FunctionContext ctx)
    {
        ASTFunction function;
        ASTFunction.Parameter parameter;

        function = new ASTFunction(ctx.ID().getText(), (ASTBlock) map.get(ctx.block()));
        if (ctx.paramList() != null && ctx.paramList().ID().size() > 0) {
            for (TerminalNode tn : ctx.paramList().ID()) {
                parameter = new ASTFunction.Parameter(tn.getText(), new HMTypeVar());
                function.parameters.add(parameter);
            }
        }

        function.setDepth(ctx.depth());

        map.put(ctx, function);
    }

    @Override
    public void enterBlock(BlockContext ctx)
    {
        ASTBlock block;

        block = new ASTBlock();

        blocks.push(block);

        block.setDepth(ctx.depth());

        map.put(ctx, block);
    }

    @Override
    public void exitBlock(BlockContext ctx)
    {
        blocks.pop();
    }

    @Override
    public void exitAssignment(AssignmentContext ctx)
    {
        ASTAssignment assignment;
        ASTVariable variable;
        ASTExpression expression;

        variable = (ASTVariable) map.get(ctx.var());
        expression = (ASTExpression) map.get(ctx.expr());

        assignment = new ASTAssignment(variable, expression);

        if (!blocks.empty()) {
            blocks.peek().statements.add(assignment);
        }

        assignment.setDepth(ctx.depth());

        map.put(ctx, assignment);
    }

    @Override
    public void exitConditional(ConditionalContext ctx)
    {
        ASTConditional conditional;
        ASTExpression condition;
        ASTBlock ifBlock;
        ASTBlock elseBlock;

        condition = (ASTExpression) map.get(ctx.expr());
        ifBlock = (ASTBlock) map.get(ctx.block().get(0));

        if (ctx.block().size() > 1) {
            elseBlock = (ASTBlock) map.get(ctx.block().get(1));
            conditional = new ASTConditional(condition, ifBlock, elseBlock);
        } else {
            conditional = new ASTConditional(condition, ifBlock);
        }

        if (!blocks.empty()) {
            blocks.peek().statements.add(conditional);
        }

        conditional.setDepth(ctx.depth());

        map.put(ctx, conditional);
    }

    @Override
    public void exitWhile(WhileContext ctx)
    {
        ASTWhileLoop whileLoop;
        ASTExpression guard;
        ASTBlock body;

        guard = (ASTExpression) map.get(ctx.expr());
        body = (ASTBlock) map.get(ctx.block());

        whileLoop = new ASTWhileLoop(guard, body);

        if (!blocks.empty()) {
            blocks.peek().statements.add(whileLoop);
        }

        whileLoop.setDepth(ctx.depth());

        map.put(ctx, whileLoop);
    }

    @Override
    public void exitReturn(ReturnContext ctx)
    {
        ASTReturn ret;
        ASTExpression expression;

        expression = (ASTExpression) map.get(ctx.expr());

        ret = new ASTReturn(expression);

        if (!blocks.empty()) {
            blocks.peek().statements.add(ret);
        }

        ret.setDepth(ctx.depth());

        map.put(ctx, ret);
    }

    @Override
    public void exitBreak(BreakContext ctx)
    {
        ASTBreak br;

        br = new ASTBreak();

        if (!blocks.empty()) {
            blocks.peek().statements.add(br);
        }

        br.setDepth(ctx.depth());

        map.put(ctx, br);
    }

    @Override
    public void exitContinue(ContinueContext ctx)
    {
        ASTContinue cont;

        cont = new ASTContinue();

        if (!blocks.empty()) {
            blocks.peek().statements.add(cont);
        }

        cont.setDepth(ctx.depth());

        map.put(ctx, cont);
    }

    @Override
    public void exitTest(TestContext ctx)
    {
        ASTTest test;
        ASTFunctionCall function;
        ASTExpression expectedValue;

        function = (ASTFunctionCall) map.get(ctx.funcCall());
        expectedValue = (ASTExpression) map.get(ctx.expr());

        test = new ASTTest(function, expectedValue);

        if (!blocks.empty()) {
            blocks.peek().statements.add(test);
        }

        test.setDepth(ctx.depth());

        map.put(ctx, test);
    }

    @Override
    public void exitVoidFunctionCall(VoidFunctionCallContext ctx)
    {
        ASTVoidFunctionCall voidFuncCall;

        voidFuncCall = new ASTVoidFunctionCall(ctx.funcCall().ID().getText());

        for (ExprContext expr : ctx.funcCall().argList().expr()) {
            voidFuncCall.arguments.add((ASTExpression) map.get(expr));
        }

        voidFuncCall.setDepth(ctx.depth());

        if (!blocks.empty()) {
            blocks.peek().statements.add(voidFuncCall);
        }

        map.put(ctx, voidFuncCall);
    }

    @Override
    public void exitExpr(ExprContext ctx)
    {
        ASTExpression expr;

        expr = (ASTExpression) map.get(ctx.exprBin());

        expr.setDepth(ctx.depth());

        map.put(ctx, expr);
    }

    @Override
    public void exitExprBin(ExprBinContext ctx)
    {
        ASTExpression expr;
        ASTBinaryExpr binExpr;
        ASTExpression left, right;
        ASTBinaryExpr.BinOp binOp;

        // If unary expression
        if (ctx.op == null) {
            expr = (ASTExpression) map.get(ctx.exprUn());

            expr.setDepth(ctx.depth());

            map.put(ctx, expr);
        } else {
            left = (ASTExpression) map.get(ctx.left);
            right = (ASTExpression) map.get(ctx.right);
            binOp = findBinOp(ctx.op.getText());

            binExpr = new ASTBinaryExpr(binOp, left, right);

            binExpr.setDepth(ctx.depth());

            map.put(ctx, binExpr);
        }
    }

    @Override
    public void exitExprUn(ExprUnContext ctx)
    {
        ASTUnaryExpr unExpr;
        ASTUnaryExpr.UnaryOp op;
        ASTExpression expr;

        // If base expression
        if (ctx.op == null) {
            expr = (ASTExpression) map.get(ctx.exprBase());

            expr.setDepth(ctx.depth());

            map.put(ctx, expr);
        } else {
            op = findUnaryOp(ctx.op.getText());
            expr = (ASTExpression) map.get(ctx.expression);

            unExpr = new ASTUnaryExpr(op, expr);

            unExpr.setDepth(ctx.depth());

            map.put(ctx, unExpr);
        }
    }

    @Override
    public void exitExprBase(ExprBaseContext ctx)
    {
        ASTExpression expr;

        if (ctx.funcCall() != null) {
            expr = (ASTFunctionCall) map.get(ctx.funcCall());
        } else if (ctx.lit() != null) {
            expr = (ASTLiteral) map.get(ctx.lit());
        } else if (ctx.var() != null) {
            expr = (ASTVariable) map.get(ctx.var());
        } else {
            expr = (ASTExpression) map.get(ctx.expr());
        }

        expr.setDepth(ctx.depth());

        map.put(ctx, expr);
    }

    @Override
    public void exitFuncCall(FuncCallContext ctx)
    {
        ASTFunctionCall funcCall;

        funcCall = new ASTFunctionCall(ctx.ID().getText());

        for (ExprContext expr : ctx.argList().expr()) {
            funcCall.arguments.add((ASTExpression) map.get(expr));
        }

        funcCall.setDepth(ctx.depth());

        map.put(ctx, funcCall);
    }

    @Override
    public void exitVar(LJParser.VarContext ctx)
    {
        ASTVariable var;

        var = new ASTVariable(ctx.ID().getText());

        var.setDepth(ctx.depth());

        map.put(ctx, var);
    }

    @Override
    public void exitLit(LJParser.LitContext ctx)
    {
        ASTLiteral lit;

        if (ctx.BOOL() != null) {
            lit = new ASTLiteral(HMType.BaseDataType.BOOL, Boolean.parseBoolean(ctx.BOOL().getText()));
        } else if (ctx.DEC() != null) {
            lit = new ASTLiteral(HMType.BaseDataType.INT, Integer.parseInt(ctx.DEC().getText()));
        } else {
            assert (ctx.STR() != null);
            lit = new ASTLiteral(HMType.BaseDataType.STR, ctx.STR().getText());
        }

        lit.setDepth(ctx.depth());

        map.put(ctx, lit);
    }

    public ASTProgram getAST()
    {
        return ast;
    }

    public ASTUnaryExpr.UnaryOp findUnaryOp(String op)
    {
        switch (op) {
            case "!":
                return ASTUnaryExpr.UnaryOp.NOT;
            default:
                return ASTUnaryExpr.UnaryOp.INVALID;
        }
    }

    private ASTBinaryExpr.BinOp findBinOp(String s)
    {
        ASTBinaryExpr.BinOp op;

        switch (s) {
            case "+":
                op = ASTBinaryExpr.BinOp.ADD;
                break;

            case "*":
                op = ASTBinaryExpr.BinOp.MUL;
                break;

            case "/":
                op = ASTBinaryExpr.BinOp.DIV;
                break;

            case "-":
                op = ASTBinaryExpr.BinOp.SUB;
                break;

            case ">":
                op = ASTBinaryExpr.BinOp.GT;
                break;

            case ">=":
                op = ASTBinaryExpr.BinOp.GE;
                break;

            case "<":
                op = ASTBinaryExpr.BinOp.LT;
                break;

            case "<=":
                op = ASTBinaryExpr.BinOp.LE;
                break;

            case "==":
                op = ASTBinaryExpr.BinOp.EQ;
                break;

            case "!=":
                op = ASTBinaryExpr.BinOp.NE;
                break;

            case "||":
                op = ASTBinaryExpr.BinOp.OR;
                break;

            case "&&":
                op = ASTBinaryExpr.BinOp.AND;
                break;

            default:
                op = null;
        }

        return op;
    }
}
