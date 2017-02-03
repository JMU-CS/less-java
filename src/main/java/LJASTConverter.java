import java.util.LinkedHashMap;
import java.util.Stack;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

public class LJASTConverter extends LJBaseListener {
    private ASTProgram ast;
    private LinkedHashMap<ParserRuleContext, ASTNode> map;

    //private ASTFunction currentFunction;
    private Stack<ASTBlock> blocks;

    public LJASTConverter() {
        map = new LinkedHashMap<>();
        blocks = new Stack<ASTBlock>();
    }

    @Override
    public void exitProgram(LJParser.ProgramContext ctx) {
        ast = new ASTProgram();

        for (LJParser.StatementContext s: ctx.statement()) {
            if (!s.getText().equals("\n")) {
                ast.statements.add((ASTStatement) map.get(s));
            }
        }

        for (LJParser.FunctionContext f: ctx.function()) {
            ast.functions.add((ASTFunction) map.get(f));
        }

        ast.setDepth(ctx.depth());

        map.put(ctx, ast);
    }

    @Override
    public void exitFunction(LJParser.FunctionContext ctx) {
        ASTFunction             function;
        ASTFunction.Parameter   parameter;

        function = new ASTFunction(ctx.ID().getText(), (ASTBlock) map.get(ctx.block()));
        if (ctx.paramList() != null) {
            if (ctx.paramList().ID().size() > 0) {
                for (TerminalNode tn: ctx.paramList().ID()) {
                    parameter = new ASTFunction.Parameter(tn.getText(),
                            ASTNode.DataType.UNKNOWN);
                    function.parameters.add(parameter);
                }
            }
        }

        function.setDepth(ctx.depth());

        map.put(ctx, function);
    }

    @Override
    public void enterBlock(LJParser.BlockContext ctx) {
        ASTBlock block;

        block = new ASTBlock();

        blocks.push(block);

        block.setDepth(ctx.depth());

        map.put(ctx, block);
    }

    @Override
    public void exitBlock(LJParser.BlockContext ctx) {
        blocks.pop();
    }

    @Override
    public void exitAssignment(LJParser.AssignmentContext ctx) {
        ASTAssignment assignment;
        ASTLocation location;
        ASTExpression expression;

        location = (ASTLocation) map.get(ctx.loc());
        expression = (ASTExpression) map.get(ctx.expr());

        assignment = new ASTAssignment(location, expression);

        if (!blocks.empty()) {
            blocks.peek().statements.add(assignment);
        }

        assignment.setDepth(ctx.depth());

        map.put(ctx, assignment);
    }

    @Override
    public void exitConditional(LJParser.ConditionalContext ctx) {
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
    public void exitWhile(LJParser.WhileContext ctx) {
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
    public void exitReturn(LJParser.ReturnContext ctx) {
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
    public void exitBreak(LJParser.BreakContext ctx) {
        ASTBreak br;

        br = new ASTBreak();

        if (!blocks.empty()) {
            blocks.peek().statements.add(br);
        }

        br.setDepth(ctx.depth());

        map.put(ctx, br);
    }

    @Override
    public void exitContinue(LJParser.ContinueContext ctx) {
        ASTContinue cont;

        cont = new ASTContinue();

        if (!blocks.empty()) {
            blocks.peek().statements.add(cont);
        }

        cont.setDepth(ctx.depth());

        map.put(ctx, cont);
    }

    @Override
    public void exitTest(LJParser.TestContext ctx) {
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
    public void exitBinExpr(LJParser.BinExprContext ctx) {
        ASTBinaryExpr binExpr;
        ASTExpression left;
        ASTExpression right;

        left = (ASTExpression) map.get(ctx.expr(0));
        right = (ASTExpression) map.get(ctx.expr(1));

        binExpr = new ASTBinaryExpr(findBinOp(ctx.BINOP().getText()), left, right);

        binExpr.setDepth(ctx.depth());

        map.put(ctx, binExpr);
    }

    @Override
    public void exitExprFunctionCall(LJParser.ExprFunctionCallContext ctx) {
        ASTFunctionCall funcCall;

        funcCall = new ASTFunctionCall(ctx.funcCall().ID().getText());

        funcCall.setDepth(ctx.depth());

        map.put(ctx, funcCall);
    }

    @Override
    public void exitExprLocation(LJParser.ExprLocationContext ctx) {
        ASTLocation loc;

        loc = new ASTLocation(ctx.loc().ID().getText());

        if (ctx.loc().expr() != null) {
            loc.index = (ASTExpression) map.get(ctx.loc().expr());
        }

        loc.setDepth(ctx.depth());

        map.put(ctx, loc);
    }

    @Override
    public void exitExprLiteral(LJParser.ExprLiteralContext ctx) {
        ASTLiteral lit;

        if (ctx.lit().BOOL() != null) {
            lit = new ASTLiteral(ASTNode.DataType.BOOL,
                    Boolean.parseBoolean(ctx.lit().BOOL().getText()));
        } else if (ctx.lit().DEC() != null) {
            lit = new ASTLiteral(ASTNode.DataType.INT,
                    Integer.parseInt(ctx.lit().DEC().getText()));
        } else {
            assert(map.get(ctx.lit().STR()) != null);
            lit = new ASTLiteral(ASTNode.DataType.STR, ctx.lit().STR().getText());
        }

        lit.setDepth(ctx.depth());

        map.put(ctx, lit);
    }

    @Override
    public void exitFuncCall(LJParser.FuncCallContext ctx) {
        ASTFunctionCall funcCall;

        funcCall = new ASTFunctionCall(ctx.ID().getText());

        funcCall.setDepth(ctx.depth());

        map.put(ctx, funcCall);
    }

    @Override
    public void exitLoc(LJParser.LocContext ctx) {
        ASTLocation loc;

        loc = new ASTLocation(ctx.ID().getText());

        if (ctx.expr() != null) {
            loc.index = (ASTExpression) map.get(ctx.expr());
        }

        loc.setDepth(ctx.depth());

        map.put(ctx, loc);
    }

    @Override
    public void exitLit(LJParser.LitContext ctx) {
        ASTLiteral lit;

        if (ctx.BOOL() != null) {
            lit = new ASTLiteral(ASTNode.DataType.BOOL,
                    Boolean.parseBoolean(ctx.BOOL().getText()));
        } else if (ctx.DEC() != null) {
            lit = new ASTLiteral(ASTNode.DataType.INT,
                    Integer.parseInt(ctx.DEC().getText()));
        } else {
            assert(map.get(ctx.STR()) != null);
            lit = new ASTLiteral(ASTNode.DataType.STR, ctx.STR().getText());
        }

        lit.setDepth(ctx.depth());

        map.put(ctx, lit);
    }

    public ASTProgram getAST() {
        return ast;
    }

    private ASTBinaryExpr.BinOp findBinOp(String s) {
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

            case  "<":
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
