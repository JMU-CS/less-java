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
    }

    @Override
    public void exitProgram(LJParser.ProgramContext ctx) {
        ast = new ASTProgram();
        map.put(ctx, ast);

        for (LJParser.StatementContext s: ctx.statement()) {
            ast.statements.add((ASTStatement) map.get(s));
        }

        for (LJParser.FunctionContext f: ctx.function()) {
            ast.functions.add((ASTFunction) map.get(f));
        }
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
    }

    @Override
    public void enterBlock(LJParser.BlockContext ctx) {
        ASTBlock block;

        block = new ASTBlock();

        blocks.push(block);
        map.put(ctx, block);
    }

    @Override
    public void exitBlock(LJParser.BlockContext ctx) {
        blocks.pop();
    }

    @Override
    public void exitAssignment_t(LJParser.Assignment_tContext ctx) {
        ASTAssignment assignment;
        ASTLocation location;
        ASTExpression expression;

        location = (ASTLocation) map.get(ctx.loc());
        expression = (ASTExpression) map.get(ctx.expr());

        assignment = new ASTAssignment(location, expression);

        if (!blocks.empty()) {
            blocks.peek().statements.add(assignment);
        }

        map.put(ctx, assignment);
    }

    @Override
    public void exitConditional_t(LJParser.Conditional_tContext ctx) {
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

        map.put(ctx, conditional);
    }

    @Override
    public void exitWhileLoop_t(LJParser.WhileLoop_tContext ctx) {
        ASTWhileLoop whileLoop;
        ASTExpression guard;
        ASTBlock body;

        guard = (ASTExpression) map.get(ctx.expr());
        body = (ASTBlock) map.get(ctx.block());

        whileLoop = new ASTWhileLoop(guard, body);

        if (!blocks.empty()) {
            blocks.peek().statements.add(whileLoop);
        }

        map.put(ctx, whileLoop);
    }

    @Override
    public void exitReturn_t(LJParser.Return_tContext ctx) {
        ASTReturn ret;
        ASTExpression expression;

        expression = (ASTExpression) map.get(ctx.expr());

        ret = new ASTReturn(expression);

        if (!blocks.empty()) {
            blocks.peek().statements.add(ret);
        }

        map.put(ctx, ret);
    }

    @Override
    public void exitBreak_t(LJParser.Break_tContext ctx) {
        ASTBreak br;

        br = new ASTBreak();

        if (!blocks.empty()) {
            blocks.peek().statements.add(br);
        }

        map.put(ctx, br);
    }

    @Override
    public void exitContinue_t(LJParser.Continue_tContext ctx) {
        ASTContinue cont;

        cont = new ASTContinue();

        if (!blocks.empty()) {
            blocks.peek().statements.add(cont);
        }

        map.put(ctx, cont);
    }

    @Override
    public void exitTest_t(LJParser.Test_tContext ctx) {
        ASTTest test;
        ASTFunctionCall function;
        ASTExpression expectedValue;

        function = (ASTFunctionCall) map.get(ctx.funcCall());
        expectedValue = (ASTExpression) map.get(ctx.expr());

        test = new ASTTest(function, expectedValue);

        if (!blocks.empty()) {
            blocks.peek().statements.add(test);
        }

        map.put(ctx, test);
    }

    public ASTProgram getAST() {
        return ast;
    }
}
