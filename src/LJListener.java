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
	 * Enter a parse tree produced by {@link LJParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(LJParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link LJParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(LJParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link LJParser#assignment_t}.
	 * @param ctx the parse tree
	 */
	void enterAssignment_t(LJParser.Assignment_tContext ctx);
	/**
	 * Exit a parse tree produced by {@link LJParser#assignment_t}.
	 * @param ctx the parse tree
	 */
	void exitAssignment_t(LJParser.Assignment_tContext ctx);
	/**
	 * Enter a parse tree produced by {@link LJParser#conditional_t}.
	 * @param ctx the parse tree
	 */
	void enterConditional_t(LJParser.Conditional_tContext ctx);
	/**
	 * Exit a parse tree produced by {@link LJParser#conditional_t}.
	 * @param ctx the parse tree
	 */
	void exitConditional_t(LJParser.Conditional_tContext ctx);
	/**
	 * Enter a parse tree produced by {@link LJParser#whileLoop_t}.
	 * @param ctx the parse tree
	 */
	void enterWhileLoop_t(LJParser.WhileLoop_tContext ctx);
	/**
	 * Exit a parse tree produced by {@link LJParser#whileLoop_t}.
	 * @param ctx the parse tree
	 */
	void exitWhileLoop_t(LJParser.WhileLoop_tContext ctx);
	/**
	 * Enter a parse tree produced by {@link LJParser#return_t}.
	 * @param ctx the parse tree
	 */
	void enterReturn_t(LJParser.Return_tContext ctx);
	/**
	 * Exit a parse tree produced by {@link LJParser#return_t}.
	 * @param ctx the parse tree
	 */
	void exitReturn_t(LJParser.Return_tContext ctx);
	/**
	 * Enter a parse tree produced by {@link LJParser#break_t}.
	 * @param ctx the parse tree
	 */
	void enterBreak_t(LJParser.Break_tContext ctx);
	/**
	 * Exit a parse tree produced by {@link LJParser#break_t}.
	 * @param ctx the parse tree
	 */
	void exitBreak_t(LJParser.Break_tContext ctx);
	/**
	 * Enter a parse tree produced by {@link LJParser#continue_t}.
	 * @param ctx the parse tree
	 */
	void enterContinue_t(LJParser.Continue_tContext ctx);
	/**
	 * Exit a parse tree produced by {@link LJParser#continue_t}.
	 * @param ctx the parse tree
	 */
	void exitContinue_t(LJParser.Continue_tContext ctx);
	/**
	 * Enter a parse tree produced by {@link LJParser#test_t}.
	 * @param ctx the parse tree
	 */
	void enterTest_t(LJParser.Test_tContext ctx);
	/**
	 * Exit a parse tree produced by {@link LJParser#test_t}.
	 * @param ctx the parse tree
	 */
	void exitTest_t(LJParser.Test_tContext ctx);
	/**
	 * Enter a parse tree produced by {@link LJParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(LJParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LJParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(LJParser.ExprContext ctx);
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
	/**
	 * Enter a parse tree produced by {@link LJParser#terminator}.
	 * @param ctx the parse tree
	 */
	void enterTerminator(LJParser.TerminatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link LJParser#terminator}.
	 * @param ctx the parse tree
	 */
	void exitTerminator(LJParser.TerminatorContext ctx);
}