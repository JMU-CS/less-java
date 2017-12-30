package com.github.lessjava.visitor.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.github.lessjava.generated.LJBaseListener;
import com.github.lessjava.generated.LJParser.ArgListContext;
import com.github.lessjava.generated.LJParser.AssignmentContext;
import com.github.lessjava.generated.LJParser.BlockContext;
import com.github.lessjava.generated.LJParser.BreakContext;
import com.github.lessjava.generated.LJParser.ConditionalContext;
import com.github.lessjava.generated.LJParser.ContinueContext;
import com.github.lessjava.generated.LJParser.ExprBaseContext;
import com.github.lessjava.generated.LJParser.ExprBinContext;
import com.github.lessjava.generated.LJParser.ExprContext;
import com.github.lessjava.generated.LJParser.ExprUnContext;
import com.github.lessjava.generated.LJParser.ForContext;
import com.github.lessjava.generated.LJParser.FuncCallContext;
import com.github.lessjava.generated.LJParser.FunctionContext;
import com.github.lessjava.generated.LJParser.LitContext;
import com.github.lessjava.generated.LJParser.ProgramContext;
import com.github.lessjava.generated.LJParser.ReturnContext;
import com.github.lessjava.generated.LJParser.StatementContext;
import com.github.lessjava.generated.LJParser.TestContext;
import com.github.lessjava.generated.LJParser.VarContext;
import com.github.lessjava.generated.LJParser.VoidAssignmentContext;
import com.github.lessjava.generated.LJParser.VoidFunctionCallContext;
import com.github.lessjava.generated.LJParser.WhileContext;
import com.github.lessjava.types.ast.ASTArgList;
import com.github.lessjava.types.ast.ASTAssignment;
import com.github.lessjava.types.ast.ASTBinaryExpr;
import com.github.lessjava.types.ast.ASTBinaryExpr.BinOp;
import com.github.lessjava.types.ast.ASTBlock;
import com.github.lessjava.types.ast.ASTBreak;
import com.github.lessjava.types.ast.ASTConditional;
import com.github.lessjava.types.ast.ASTContinue;
import com.github.lessjava.types.ast.ASTExpression;
import com.github.lessjava.types.ast.ASTForLoop;
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
import com.github.lessjava.types.ast.ASTVoidAssignment;
import com.github.lessjava.types.ast.ASTVoidFunctionCall;
import com.github.lessjava.types.ast.ASTWhileLoop;
import com.github.lessjava.types.inference.HMType;
import com.github.lessjava.types.inference.HMType.BaseDataType;
import com.github.lessjava.types.inference.impl.HMTypeBase;
import com.github.lessjava.types.inference.impl.HMTypeVar;

public class LJASTConverter extends LJBaseListener {
    private ASTProgram ast;
    private LinkedHashMap<ParserRuleContext, ASTNode> map;

    private Stack<ASTBlock> blocks;

    public LJASTConverter() {
        map = new LinkedHashMap<>();
        blocks = new Stack<ASTBlock>();
    }

    @Override
    public void exitProgram(ProgramContext ctx) {
        ast = new ASTProgram();

        for (StatementContext s : ctx.statement()) {
            if (!s.getText().equals("\n")) {
                ast.statements.add((ASTStatement) map.get(s));
            }
        }

        for (FunctionContext f : ctx.function()) {
            ast.functions.add((ASTFunction) map.get(f));
        }

        for (TestContext t : ctx.test()) {
            ast.tests.add((ASTTest) map.get(t));
        }

        addLibraryFunctions();

        ast.setDepth(ctx.depth());

        map.put(ctx, ast);
    }

    @Override
    public void exitFunction(FunctionContext ctx) {
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
    public void exitTest(TestContext ctx) {
        ASTTest test;
        ASTExpression expr;

        expr = (ASTExpression) map.get(ctx.expr());
        test = new ASTTest(expr);

        test.setDepth(ctx.depth());

        map.put(ctx, test);
    }

    @Override
    public void enterBlock(BlockContext ctx) {
        ASTBlock block;

        block = new ASTBlock();

        blocks.push(block);

        block.setDepth(ctx.depth());

        map.put(ctx, block);
    }

    @Override
    public void exitBlock(BlockContext ctx) {
        blocks.pop();
    }

    @Override
    public void exitArgList(ArgListContext ctx) {
        ASTArgList argList;

        List<ASTExpression> args = new ArrayList<>();
        for (ExprContext e : ctx.expr()) {
            args.add((ASTExpression) map.get(e));
        }

        argList = new ASTArgList(args);

        map.put(ctx, argList);
    }

    @Override
    public void exitVoidAssignment(VoidAssignmentContext ctx) {
        ASTVoidAssignment voidAssignment;
        ASTVariable variable;
        ASTExpression expression;

        variable = (ASTVariable) map.get(ctx.assignment().var());
        expression = (ASTExpression) map.get(ctx.assignment().expr());

        if (expression instanceof ASTArgList) {
            variable.isCollection = true;
        }

        voidAssignment = new ASTVoidAssignment(variable, expression);

        if (!blocks.empty()) {
            blocks.peek().statements.add(voidAssignment);
        }

        voidAssignment.setDepth(ctx.depth());

        map.put(ctx, voidAssignment);
    }

    @Override
    public void exitConditional(ConditionalContext ctx) {
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
    public void exitWhile(WhileContext ctx) {
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
    public void exitFor(ForContext ctx) {
        ASTForLoop forLoop;
        ASTVariable var;
        ASTExpression lowerBound;
        ASTExpression upperBound;
        ASTBlock block;

        var = (ASTVariable) map.get(ctx.var());
        block = (ASTBlock) map.get(ctx.block());

        if (ctx.expr().size() > 1) {
            lowerBound = (ASTExpression) map.get(ctx.expr(0));
            upperBound = (ASTExpression) map.get(ctx.expr(1));
            forLoop = new ASTForLoop(var, lowerBound, upperBound, block);
        } else {
            upperBound = (ASTExpression) map.get(ctx.expr(0));
            forLoop = new ASTForLoop(var, upperBound, block);
        }

        if (!blocks.empty()) {
            blocks.peek().statements.add(forLoop);
        }

        forLoop.setDepth(ctx.depth());

        map.put(ctx, forLoop);
    }

    @Override
    public void exitReturn(ReturnContext ctx) {
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
    public void exitBreak(BreakContext ctx) {
        ASTBreak br;

        br = new ASTBreak();

        if (!blocks.empty()) {
            blocks.peek().statements.add(br);
        }

        br.setDepth(ctx.depth());

        map.put(ctx, br);
    }

    @Override
    public void exitContinue(ContinueContext ctx) {
        ASTContinue cont;

        cont = new ASTContinue();

        if (!blocks.empty()) {
            blocks.peek().statements.add(cont);
        }

        cont.setDepth(ctx.depth());

        map.put(ctx, cont);
    }

    @Override
    public void exitVoidFunctionCall(VoidFunctionCallContext ctx) {
        ASTVoidFunctionCall voidFuncCall;

        voidFuncCall = new ASTVoidFunctionCall(ctx.funcCall().ID().getText());

        voidFuncCall.setDepth(ctx.depth());

        map.put(ctx, voidFuncCall);

        if (!blocks.empty()) {
            blocks.peek().statements.add(voidFuncCall);
        }

        if (ctx.funcCall().argList() == null) {
            return;
        }

        for (ExprContext expr : ctx.funcCall().argList().expr()) {
            voidFuncCall.arguments.add((ASTExpression) map.get(expr));
        }
    }

    @Override
    public void exitExpr(ExprContext ctx) {
        ASTExpression expr;

        expr = (ASTExpression) map.get(ctx.exprBin());

        expr.setDepth(ctx.depth());

        map.put(ctx, expr);
    }

    @Override
    public void exitExprBin(ExprBinContext ctx) {
        ASTExpression expr;
        ASTExpression left, right;
        BinOp binOp;

        if (ctx.op == null && ctx.assignment() == null) {
            expr = (ASTExpression) map.get(ctx.exprUn());

            expr.setDepth(ctx.depth());
        } else if (ctx.assignment() != null) {
            left = (ASTExpression) map.get(ctx.assignment().var());
            right = (ASTExpression) map.get(ctx.assignment().expr());
            binOp = findBinOp(ctx.assignment().PREC7().getText());

            expr = new ASTBinaryExpr(binOp, left, right);
        } else {
            left = (ASTExpression) map.get(ctx.left);
            right = (ASTExpression) map.get(ctx.right);
            binOp = findBinOp(ctx.op.getText());

            expr = new ASTBinaryExpr(binOp, left, right);
        }

        expr.setDepth(ctx.depth());
        map.put(ctx, expr);
    }

    @Override
    public void exitExprUn(ExprUnContext ctx) {
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
    public void exitExprBase(ExprBaseContext ctx) {
        ASTExpression expr;

        if (ctx.funcCall() != null) {
            expr = (ASTFunctionCall) map.get(ctx.funcCall());
        } else if (ctx.lit() != null) {
            expr = (ASTLiteral) map.get(ctx.lit());
        } else if (ctx.var() != null) {
            expr = (ASTVariable) map.get(ctx.var());
        } else if (ctx.argList() != null) {
            expr = (ASTArgList) map.get(ctx.argList());
        } else {
            expr = (ASTExpression) map.get(ctx.expr());
        }

        expr.setDepth(ctx.depth());

        map.put(ctx, expr);
    }

    @Override
    public void exitAssignment(AssignmentContext ctx) {
        ASTAssignment assignment;
        ASTVariable variable;
        ASTExpression expression;

        variable = (ASTVariable) map.get(ctx.var());
        expression = (ASTExpression) map.get(ctx.expr());

        if (expression instanceof ASTArgList) {
            variable.isCollection = true;
        }

        assignment = new ASTAssignment(variable, expression);

        assignment.setDepth(ctx.depth());

        map.put(ctx, assignment);
    }

    @Override
    public void exitFuncCall(FuncCallContext ctx) {
        ASTFunctionCall funcCall;

        funcCall = new ASTFunctionCall(ctx.ID().getText());

        funcCall.setDepth(ctx.depth());

        map.put(ctx, funcCall);

        if (ctx.argList() == null) {
            return;
        }

        for (ExprContext expr : ctx.argList().expr()) {
            funcCall.arguments.add((ASTExpression) map.get(expr));
        }
    }

    @Override
    public void exitVar(VarContext ctx) {
        ASTVariable var;

        if (ctx.expr() == null) {
            var = new ASTVariable(ctx.ID().getText());
        } else {
            var = new ASTVariable(ctx.ID().getText(), (ASTExpression) map.get(ctx.expr()));
        }

        var.setDepth(ctx.depth());

        map.put(ctx, var);
    }

    @Override
    public void exitLit(LitContext ctx) {
        ASTLiteral lit;

        if (ctx.BOOL() != null) {
            lit = new ASTLiteral(HMType.BaseDataType.BOOL, Boolean.parseBoolean(ctx.BOOL().getText()));
        } else if (ctx.INT() != null) {
            lit = new ASTLiteral(HMType.BaseDataType.INT, Integer.parseInt(ctx.INT().getText()));
        } else if (ctx.REAL() != null) {
            lit = new ASTLiteral(HMType.BaseDataType.REAL, Double.parseDouble(ctx.REAL().getText()));
        } else {
            assert (ctx.STR() != null);
            lit = new ASTLiteral(HMType.BaseDataType.STR,
                    ctx.STR().getText().substring(1, ctx.STR().getText().length() - 1));
        }

        lit.setDepth(ctx.depth());

        map.put(ctx, lit);
    }

    public ASTProgram getAST() {
        return ast;
    }

    public ASTUnaryExpr.UnaryOp findUnaryOp(String op) {
        switch (op) {
            case "!":
                return ASTUnaryExpr.UnaryOp.NOT;
            default:
                return ASTUnaryExpr.UnaryOp.INVALID;
        }
    }

    private ASTBinaryExpr.BinOp findBinOp(String s) {
        ASTBinaryExpr.BinOp op;

        switch (s) {
            case "[":
                op = ASTBinaryExpr.BinOp.INDEX;
                break;

            case "+":
                op = ASTBinaryExpr.BinOp.ADD;
                break;

            case "*":
                op = ASTBinaryExpr.BinOp.MUL;
                break;

            case "%":
                op = ASTBinaryExpr.BinOp.MOD;
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
            case "=":
                op = ASTBinaryExpr.BinOp.ASGN;
                break;

            default:
                op = null;
        }

        return op;
    }

    /**
     * Add Library functions
     */
    private void addLibraryFunctions() {
        // TODO: MAKE STATIC; duplicate code in build symbol tables
        List<ASTFunction> libraryFunctions = new ArrayList<>();

        // Output
        ASTFunction print = new ASTFunction("print", new HMTypeBase(BaseDataType.VOID), null);
        ASTFunction println = new ASTFunction("println", new HMTypeBase(BaseDataType.VOID), null);

        print.parameters.add(new ASTFunction.Parameter("args", new HMTypeBase(BaseDataType.STR)));
        println.parameters.add(new ASTFunction.Parameter("args", new HMTypeBase(BaseDataType.STR)));

        libraryFunctions.add(print);
        libraryFunctions.add(println);

        // Input
        libraryFunctions.add(new ASTFunction("readInt", new HMTypeBase(BaseDataType.INT), null));
        libraryFunctions.add(new ASTFunction("readReal", new HMTypeBase(BaseDataType.REAL), null));
        libraryFunctions.add(new ASTFunction("readChar", new HMTypeBase(BaseDataType.STR), null));
        libraryFunctions.add(new ASTFunction("readWord", new HMTypeBase(BaseDataType.STR), null));
        libraryFunctions.add(new ASTFunction("readLine", new HMTypeBase(BaseDataType.STR), null));

        for (ASTFunction f : libraryFunctions) {
            f.setDepth(2);
            f.setParent(ast);
        }

        ast.functions.addAll(libraryFunctions);
    }
}
