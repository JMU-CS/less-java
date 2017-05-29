// Generated from LJ.g4 by ANTLR 4.5.3
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LJParser}.
 */
public interface LJListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link LJParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(LJParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link LJParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(LJParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link LJParser#function}.
	 * @param ctx the parse tree
	 */
	void enterFunction(LJParser.FunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LJParser#function}.
	 * @param ctx the parse tree
	 */
	void exitFunction(LJParser.FunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link LJParser#paramList}.
	 * @param ctx the parse tree
	 */
	void enterParamList(LJParser.ParamListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LJParser#paramList}.
	 * @param ctx the parse tree
	 */
	void exitParamList(LJParser.ParamListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LJParser#argList}.
	 * @param ctx the parse tree
	 */
	void enterArgList(LJParser.ArgListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LJParser#argList}.
	 * @param ctx the parse tree
	 */
	void exitArgList(LJParser.ArgListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LJParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(LJParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link LJParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(LJParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Assignment}
	 * labeled alternative in {@link LJParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(LJParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Assignment}
	 * labeled alternative in {@link LJParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(LJParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Conditional}
	 * labeled alternative in {@link LJParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterConditional(LJParser.ConditionalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Conditional}
	 * labeled alternative in {@link LJParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitConditional(LJParser.ConditionalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code While}
	 * labeled alternative in {@link LJParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterWhile(LJParser.WhileContext ctx);
	/**
	 * Exit a parse tree produced by the {@code While}
	 * labeled alternative in {@link LJParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitWhile(LJParser.WhileContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Return}
	 * labeled alternative in {@link LJParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterReturn(LJParser.ReturnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Return}
	 * labeled alternative in {@link LJParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitReturn(LJParser.ReturnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Break}
	 * labeled alternative in {@link LJParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterBreak(LJParser.BreakContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Break}
	 * labeled alternative in {@link LJParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitBreak(LJParser.BreakContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Continue}
	 * labeled alternative in {@link LJParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterContinue(LJParser.ContinueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Continue}
	 * labeled alternative in {@link LJParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitContinue(LJParser.ContinueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Test}
	 * labeled alternative in {@link LJParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterTest(LJParser.TestContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Test}
	 * labeled alternative in {@link LJParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitTest(LJParser.TestContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VoidFunctionCall}
	 * labeled alternative in {@link LJParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterVoidFunctionCall(LJParser.VoidFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VoidFunctionCall}
	 * labeled alternative in {@link LJParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitVoidFunctionCall(LJParser.VoidFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Terminator}
	 * labeled alternative in {@link LJParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterTerminator(LJParser.TerminatorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Terminator}
	 * labeled alternative in {@link LJParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitTerminator(LJParser.TerminatorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprLocation}
	 * labeled alternative in {@link LJParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprLocation(LJParser.ExprLocationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprLocation}
	 * labeled alternative in {@link LJParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprLocation(LJParser.ExprLocationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BinExprPrec2}
	 * labeled alternative in {@link LJParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBinExprPrec2(LJParser.BinExprPrec2Context ctx);
	/**
	 * Exit a parse tree produced by the {@code BinExprPrec2}
	 * labeled alternative in {@link LJParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBinExprPrec2(LJParser.BinExprPrec2Context ctx);
	/**
	 * Enter a parse tree produced by the {@code BinExprPrec3}
	 * labeled alternative in {@link LJParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBinExprPrec3(LJParser.BinExprPrec3Context ctx);
	/**
	 * Exit a parse tree produced by the {@code BinExprPrec3}
	 * labeled alternative in {@link LJParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBinExprPrec3(LJParser.BinExprPrec3Context ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprFunctionCall}
	 * labeled alternative in {@link LJParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprFunctionCall(LJParser.ExprFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprFunctionCall}
	 * labeled alternative in {@link LJParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprFunctionCall(LJParser.ExprFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprParen}
	 * labeled alternative in {@link LJParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprParen(LJParser.ExprParenContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprParen}
	 * labeled alternative in {@link LJParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprParen(LJParser.ExprParenContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BinExprPrec1}
	 * labeled alternative in {@link LJParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBinExprPrec1(LJParser.BinExprPrec1Context ctx);
	/**
	 * Exit a parse tree produced by the {@code BinExprPrec1}
	 * labeled alternative in {@link LJParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBinExprPrec1(LJParser.BinExprPrec1Context ctx);
	/**
	 * Enter a parse tree produced by the {@code UnExpr}
	 * labeled alternative in {@link LJParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterUnExpr(LJParser.UnExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnExpr}
	 * labeled alternative in {@link LJParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitUnExpr(LJParser.UnExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprLiteral}
	 * labeled alternative in {@link LJParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprLiteral(LJParser.ExprLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprLiteral}
	 * labeled alternative in {@link LJParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprLiteral(LJParser.ExprLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link LJParser#funcCall}.
	 * @param ctx the parse tree
	 */
	void enterFuncCall(LJParser.FuncCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link LJParser#funcCall}.
	 * @param ctx the parse tree
	 */
	void exitFuncCall(LJParser.FuncCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link LJParser#loc}.
	 * @param ctx the parse tree
	 */
	void enterLoc(LJParser.LocContext ctx);
	/**
	 * Exit a parse tree produced by {@link LJParser#loc}.
	 * @param ctx the parse tree
	 */
	void exitLoc(LJParser.LocContext ctx);
	/**
	 * Enter a parse tree produced by {@link LJParser#lit}.
	 * @param ctx the parse tree
	 */
	void enterLit(LJParser.LitContext ctx);
	/**
	 * Exit a parse tree produced by {@link LJParser#lit}.
	 * @param ctx the parse tree
	 */
	void exitLit(LJParser.LitContext ctx);
}