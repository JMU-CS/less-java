// Generated from LJ.g4 by ANTLR 4.5.3
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class LJParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, IF=9, 
		ELSE=10, WHILE=11, RETURN=12, BREAK=13, CONTINUE=14, TEST=15, BINOP=16, 
		ADD=17, MULT=18, DIV=19, SUB=20, GT=21, GTE=22, LT=23, LTE=24, ET=25, 
		NET=26, OR=27, AND=28, DEC=29, BOOL=30, STR=31, ID=32, WS=33, NL=34;
	public static final int
		RULE_program = 0, RULE_function = 1, RULE_paramList = 2, RULE_argList = 3, 
		RULE_block = 4, RULE_statement = 5, RULE_assignment_t = 6, RULE_conditional_t = 7, 
		RULE_whileLoop_t = 8, RULE_return_t = 9, RULE_break_t = 10, RULE_continue_t = 11, 
		RULE_test_t = 12, RULE_expr = 13, RULE_funcCall = 14, RULE_loc = 15, RULE_lit = 16, 
		RULE_terminator = 17;
	public static final String[] ruleNames = {
		"program", "function", "paramList", "argList", "block", "statement", "assignment_t", 
		"conditional_t", "whileLoop_t", "return_t", "break_t", "continue_t", "test_t", 
		"expr", "funcCall", "loc", "lit", "terminator"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'('", "')'", "','", "'{'", "'}'", "'='", "'['", "']'", "'if'", 
		"'else'", "'while'", "'return'", "'break'", "'continue'", "'test'", null, 
		"'+'", "'*'", "'/'", "'-'", "'>'", "'>='", "'<'", "'<='", "'=='", "'!='", 
		"'||'", "'&&'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, "IF", "ELSE", "WHILE", 
		"RETURN", "BREAK", "CONTINUE", "TEST", "BINOP", "ADD", "MULT", "DIV", 
		"SUB", "GT", "GTE", "LT", "LTE", "ET", "NET", "OR", "AND", "DEC", "BOOL", 
		"STR", "ID", "WS", "NL"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "LJ.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public LJParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ProgramContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public List<FunctionContext> function() {
			return getRuleContexts(FunctionContext.class);
		}
		public FunctionContext function(int i) {
			return getRuleContext(FunctionContext.class,i);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitProgram(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(40);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IF) | (1L << WHILE) | (1L << RETURN) | (1L << BREAK) | (1L << CONTINUE) | (1L << TEST) | (1L << ID) | (1L << NL))) != 0)) {
				{
				setState(38);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
				case 1:
					{
					setState(36);
					statement();
					}
					break;
				case 2:
					{
					setState(37);
					function();
					}
					break;
				}
				}
				setState(42);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(LJParser.ID, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ParamListContext paramList() {
			return getRuleContext(ParamListContext.class,0);
		}
		public FunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitFunction(this);
		}
	}

	public final FunctionContext function() throws RecognitionException {
		FunctionContext _localctx = new FunctionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_function);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(43);
			match(ID);
			setState(48);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(44);
				match(T__0);
				setState(45);
				paramList();
				setState(46);
				match(T__1);
				}
			}

			setState(50);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParamListContext extends ParserRuleContext {
		public List<TerminalNode> ID() { return getTokens(LJParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(LJParser.ID, i);
		}
		public ParamListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_paramList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterParamList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitParamList(this);
		}
	}

	public final ParamListContext paramList() throws RecognitionException {
		ParamListContext _localctx = new ParamListContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_paramList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(52);
			match(ID);
			setState(59);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(55); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(53);
					match(T__2);
					setState(54);
					match(ID);
					}
					}
					setState(57); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==T__2 );
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgListContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ArgListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterArgList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitArgList(this);
		}
	}

	public final ArgListContext argList() throws RecognitionException {
		ArgListContext _localctx = new ArgListContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_argList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(61);
			expr(0);
			setState(68);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(64); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(62);
					match(T__2);
					setState(63);
					expr(0);
					}
					}
					setState(66); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==T__2 );
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BlockContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitBlock(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(70);
			match(T__3);
			setState(74);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IF) | (1L << WHILE) | (1L << RETURN) | (1L << BREAK) | (1L << CONTINUE) | (1L << TEST) | (1L << ID) | (1L << NL))) != 0)) {
				{
				{
				setState(71);
				statement();
				}
				}
				setState(76);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(77);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementContext extends ParserRuleContext {
		public Assignment_tContext assignment_t() {
			return getRuleContext(Assignment_tContext.class,0);
		}
		public Conditional_tContext conditional_t() {
			return getRuleContext(Conditional_tContext.class,0);
		}
		public WhileLoop_tContext whileLoop_t() {
			return getRuleContext(WhileLoop_tContext.class,0);
		}
		public Return_tContext return_t() {
			return getRuleContext(Return_tContext.class,0);
		}
		public Break_tContext break_t() {
			return getRuleContext(Break_tContext.class,0);
		}
		public Continue_tContext continue_t() {
			return getRuleContext(Continue_tContext.class,0);
		}
		public Test_tContext test_t() {
			return getRuleContext(Test_tContext.class,0);
		}
		public TerminatorContext terminator() {
			return getRuleContext(TerminatorContext.class,0);
		}
		public FuncCallContext funcCall() {
			return getRuleContext(FuncCallContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitStatement(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_statement);
		try {
			setState(88);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(79);
				assignment_t();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(80);
				conditional_t();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(81);
				whileLoop_t();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(82);
				return_t();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(83);
				break_t();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(84);
				continue_t();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(85);
				test_t();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(86);
				terminator();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(87);
				funcCall();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Assignment_tContext extends ParserRuleContext {
		public LocContext loc() {
			return getRuleContext(LocContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode NL() { return getToken(LJParser.NL, 0); }
		public Assignment_tContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignment_t; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterAssignment_t(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitAssignment_t(this);
		}
	}

	public final Assignment_tContext assignment_t() throws RecognitionException {
		Assignment_tContext _localctx = new Assignment_tContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_assignment_t);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(90);
			loc();
			setState(91);
			match(T__5);
			setState(92);
			expr(0);
			setState(93);
			match(NL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Conditional_tContext extends ParserRuleContext {
		public TerminalNode IF() { return getToken(LJParser.IF, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public List<BlockContext> block() {
			return getRuleContexts(BlockContext.class);
		}
		public BlockContext block(int i) {
			return getRuleContext(BlockContext.class,i);
		}
		public TerminalNode ELSE() { return getToken(LJParser.ELSE, 0); }
		public Conditional_tContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditional_t; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterConditional_t(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitConditional_t(this);
		}
	}

	public final Conditional_tContext conditional_t() throws RecognitionException {
		Conditional_tContext _localctx = new Conditional_tContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_conditional_t);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(95);
			match(IF);
			setState(96);
			match(T__0);
			setState(97);
			expr(0);
			setState(98);
			match(T__1);
			setState(99);
			block();
			setState(102);
			_la = _input.LA(1);
			if (_la==ELSE) {
				{
				setState(100);
				match(ELSE);
				setState(101);
				block();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WhileLoop_tContext extends ParserRuleContext {
		public TerminalNode WHILE() { return getToken(LJParser.WHILE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public WhileLoop_tContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whileLoop_t; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterWhileLoop_t(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitWhileLoop_t(this);
		}
	}

	public final WhileLoop_tContext whileLoop_t() throws RecognitionException {
		WhileLoop_tContext _localctx = new WhileLoop_tContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_whileLoop_t);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(104);
			match(WHILE);
			setState(105);
			match(T__0);
			setState(106);
			expr(0);
			setState(107);
			match(T__1);
			setState(108);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Return_tContext extends ParserRuleContext {
		public TerminalNode RETURN() { return getToken(LJParser.RETURN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode NL() { return getToken(LJParser.NL, 0); }
		public Return_tContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_return_t; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterReturn_t(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitReturn_t(this);
		}
	}

	public final Return_tContext return_t() throws RecognitionException {
		Return_tContext _localctx = new Return_tContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_return_t);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(110);
			match(RETURN);
			setState(111);
			expr(0);
			setState(112);
			match(NL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Break_tContext extends ParserRuleContext {
		public TerminalNode BREAK() { return getToken(LJParser.BREAK, 0); }
		public TerminalNode NL() { return getToken(LJParser.NL, 0); }
		public Break_tContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_break_t; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterBreak_t(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitBreak_t(this);
		}
	}

	public final Break_tContext break_t() throws RecognitionException {
		Break_tContext _localctx = new Break_tContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_break_t);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(114);
			match(BREAK);
			setState(115);
			match(NL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Continue_tContext extends ParserRuleContext {
		public TerminalNode CONTINUE() { return getToken(LJParser.CONTINUE, 0); }
		public TerminalNode NL() { return getToken(LJParser.NL, 0); }
		public Continue_tContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_continue_t; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterContinue_t(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitContinue_t(this);
		}
	}

	public final Continue_tContext continue_t() throws RecognitionException {
		Continue_tContext _localctx = new Continue_tContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_continue_t);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(117);
			match(CONTINUE);
			setState(118);
			match(NL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Test_tContext extends ParserRuleContext {
		public TerminalNode TEST() { return getToken(LJParser.TEST, 0); }
		public FuncCallContext funcCall() {
			return getRuleContext(FuncCallContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public Test_tContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_test_t; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterTest_t(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitTest_t(this);
		}
	}

	public final Test_tContext test_t() throws RecognitionException {
		Test_tContext _localctx = new Test_tContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_test_t);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(120);
			match(TEST);
			setState(121);
			funcCall();
			setState(122);
			match(T__5);
			setState(123);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public FuncCallContext funcCall() {
			return getRuleContext(FuncCallContext.class,0);
		}
		public LocContext loc() {
			return getRuleContext(LocContext.class,0);
		}
		public LitContext lit() {
			return getRuleContext(LitContext.class,0);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode BINOP() { return getToken(LJParser.BINOP, 0); }
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitExpr(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 26;
		enterRecursionRule(_localctx, 26, RULE_expr, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(129);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(126);
				funcCall();
				}
				break;
			case 2:
				{
				setState(127);
				loc();
				}
				break;
			case 3:
				{
				setState(128);
				lit();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(136);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ExprContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_expr);
					setState(131);
					if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
					setState(132);
					match(BINOP);
					setState(133);
					expr(5);
					}
					} 
				}
				setState(138);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class FuncCallContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(LJParser.ID, 0); }
		public ArgListContext argList() {
			return getRuleContext(ArgListContext.class,0);
		}
		public FuncCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterFuncCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitFuncCall(this);
		}
	}

	public final FuncCallContext funcCall() throws RecognitionException {
		FuncCallContext _localctx = new FuncCallContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_funcCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(139);
			match(ID);
			setState(140);
			match(T__0);
			setState(141);
			argList();
			setState(142);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LocContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(LJParser.ID, 0); }
		public TerminalNode DEC() { return getToken(LJParser.DEC, 0); }
		public LocContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_loc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterLoc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitLoc(this);
		}
	}

	public final LocContext loc() throws RecognitionException {
		LocContext _localctx = new LocContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_loc);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(144);
			match(ID);
			setState(148);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				{
				setState(145);
				match(T__6);
				setState(146);
				match(DEC);
				setState(147);
				match(T__7);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LitContext extends ParserRuleContext {
		public TerminalNode DEC() { return getToken(LJParser.DEC, 0); }
		public TerminalNode BOOL() { return getToken(LJParser.BOOL, 0); }
		public TerminalNode STR() { return getToken(LJParser.STR, 0); }
		public LitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterLit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitLit(this);
		}
	}

	public final LitContext lit() throws RecognitionException {
		LitContext _localctx = new LitContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_lit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(150);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DEC) | (1L << BOOL) | (1L << STR))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TerminatorContext extends ParserRuleContext {
		public TerminalNode NL() { return getToken(LJParser.NL, 0); }
		public TerminatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_terminator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterTerminator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitTerminator(this);
		}
	}

	public final TerminatorContext terminator() throws RecognitionException {
		TerminatorContext _localctx = new TerminatorContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_terminator);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(152);
			match(NL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 13:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 4);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3$\u009d\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\3\2\3\2\7\2)\n\2\f\2\16\2,\13\2\3\3\3\3\3\3\3\3\3\3\5\3\63"+
		"\n\3\3\3\3\3\3\4\3\4\3\4\6\4:\n\4\r\4\16\4;\5\4>\n\4\3\5\3\5\3\5\6\5C"+
		"\n\5\r\5\16\5D\5\5G\n\5\3\6\3\6\7\6K\n\6\f\6\16\6N\13\6\3\6\3\6\3\7\3"+
		"\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\5\7[\n\7\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3"+
		"\t\3\t\3\t\3\t\3\t\5\ti\n\t\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13"+
		"\3\f\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17"+
		"\5\17\u0084\n\17\3\17\3\17\3\17\7\17\u0089\n\17\f\17\16\17\u008c\13\17"+
		"\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\5\21\u0097\n\21\3\22\3\22"+
		"\3\23\3\23\3\23\2\3\34\24\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$\2"+
		"\3\3\2\37!\u009f\2*\3\2\2\2\4-\3\2\2\2\6\66\3\2\2\2\b?\3\2\2\2\nH\3\2"+
		"\2\2\fZ\3\2\2\2\16\\\3\2\2\2\20a\3\2\2\2\22j\3\2\2\2\24p\3\2\2\2\26t\3"+
		"\2\2\2\30w\3\2\2\2\32z\3\2\2\2\34\u0083\3\2\2\2\36\u008d\3\2\2\2 \u0092"+
		"\3\2\2\2\"\u0098\3\2\2\2$\u009a\3\2\2\2&)\5\f\7\2\')\5\4\3\2(&\3\2\2\2"+
		"(\'\3\2\2\2),\3\2\2\2*(\3\2\2\2*+\3\2\2\2+\3\3\2\2\2,*\3\2\2\2-\62\7\""+
		"\2\2./\7\3\2\2/\60\5\6\4\2\60\61\7\4\2\2\61\63\3\2\2\2\62.\3\2\2\2\62"+
		"\63\3\2\2\2\63\64\3\2\2\2\64\65\5\n\6\2\65\5\3\2\2\2\66=\7\"\2\2\678\7"+
		"\5\2\28:\7\"\2\29\67\3\2\2\2:;\3\2\2\2;9\3\2\2\2;<\3\2\2\2<>\3\2\2\2="+
		"9\3\2\2\2=>\3\2\2\2>\7\3\2\2\2?F\5\34\17\2@A\7\5\2\2AC\5\34\17\2B@\3\2"+
		"\2\2CD\3\2\2\2DB\3\2\2\2DE\3\2\2\2EG\3\2\2\2FB\3\2\2\2FG\3\2\2\2G\t\3"+
		"\2\2\2HL\7\6\2\2IK\5\f\7\2JI\3\2\2\2KN\3\2\2\2LJ\3\2\2\2LM\3\2\2\2MO\3"+
		"\2\2\2NL\3\2\2\2OP\7\7\2\2P\13\3\2\2\2Q[\5\16\b\2R[\5\20\t\2S[\5\22\n"+
		"\2T[\5\24\13\2U[\5\26\f\2V[\5\30\r\2W[\5\32\16\2X[\5$\23\2Y[\5\36\20\2"+
		"ZQ\3\2\2\2ZR\3\2\2\2ZS\3\2\2\2ZT\3\2\2\2ZU\3\2\2\2ZV\3\2\2\2ZW\3\2\2\2"+
		"ZX\3\2\2\2ZY\3\2\2\2[\r\3\2\2\2\\]\5 \21\2]^\7\b\2\2^_\5\34\17\2_`\7$"+
		"\2\2`\17\3\2\2\2ab\7\13\2\2bc\7\3\2\2cd\5\34\17\2de\7\4\2\2eh\5\n\6\2"+
		"fg\7\f\2\2gi\5\n\6\2hf\3\2\2\2hi\3\2\2\2i\21\3\2\2\2jk\7\r\2\2kl\7\3\2"+
		"\2lm\5\34\17\2mn\7\4\2\2no\5\n\6\2o\23\3\2\2\2pq\7\16\2\2qr\5\34\17\2"+
		"rs\7$\2\2s\25\3\2\2\2tu\7\17\2\2uv\7$\2\2v\27\3\2\2\2wx\7\20\2\2xy\7$"+
		"\2\2y\31\3\2\2\2z{\7\21\2\2{|\5\36\20\2|}\7\b\2\2}~\5\34\17\2~\33\3\2"+
		"\2\2\177\u0080\b\17\1\2\u0080\u0084\5\36\20\2\u0081\u0084\5 \21\2\u0082"+
		"\u0084\5\"\22\2\u0083\177\3\2\2\2\u0083\u0081\3\2\2\2\u0083\u0082\3\2"+
		"\2\2\u0084\u008a\3\2\2\2\u0085\u0086\f\6\2\2\u0086\u0087\7\22\2\2\u0087"+
		"\u0089\5\34\17\7\u0088\u0085\3\2\2\2\u0089\u008c\3\2\2\2\u008a\u0088\3"+
		"\2\2\2\u008a\u008b\3\2\2\2\u008b\35\3\2\2\2\u008c\u008a\3\2\2\2\u008d"+
		"\u008e\7\"\2\2\u008e\u008f\7\3\2\2\u008f\u0090\5\b\5\2\u0090\u0091\7\4"+
		"\2\2\u0091\37\3\2\2\2\u0092\u0096\7\"\2\2\u0093\u0094\7\t\2\2\u0094\u0095"+
		"\7\37\2\2\u0095\u0097\7\n\2\2\u0096\u0093\3\2\2\2\u0096\u0097\3\2\2\2"+
		"\u0097!\3\2\2\2\u0098\u0099\t\2\2\2\u0099#\3\2\2\2\u009a\u009b\7$\2\2"+
		"\u009b%\3\2\2\2\17(*\62;=DFLZh\u0083\u008a\u0096";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}