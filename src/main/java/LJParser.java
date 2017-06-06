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
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, IF=7, ELSE=8, WHILE=9, 
		RETURN=10, BREAK=11, CONTINUE=12, TEST=13, PREC1=14, PREC2=15, PREC3=16, 
		UNOP=17, ADD=18, SUB=19, MULT=20, DIV=21, MOD=22, GT=23, GTE=24, LT=25, 
		LTE=26, ET=27, NET=28, OR=29, AND=30, NOT=31, DEC=32, BOOL=33, STR=34, 
		ID=35, WS=36, EOL=37;
	public static final int
		RULE_program = 0, RULE_function = 1, RULE_paramList = 2, RULE_argList = 3, 
		RULE_block = 4, RULE_statement = 5, RULE_expr = 6, RULE_exprBin = 7, RULE_exprUn = 8, 
		RULE_exprBase = 9, RULE_funcCall = 10, RULE_loc = 11, RULE_lit = 12;
	public static final String[] ruleNames = {
		"program", "function", "paramList", "argList", "block", "statement", "expr", 
		"exprBin", "exprUn", "exprBase", "funcCall", "loc", "lit"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'('", "')'", "','", "'{'", "'}'", "'='", "'if'", "'else'", "'while'", 
		"'return'", "'break'", "'continue'", "'test'", null, null, null, null, 
		"'+'", "'-'", "'*'", "'/'", "'%'", "'>'", "'>='", "'<'", "'<='", "'=='", 
		"'!='", "'||'", "'&&'", "'!'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, "IF", "ELSE", "WHILE", "RETURN", 
		"BREAK", "CONTINUE", "TEST", "PREC1", "PREC2", "PREC3", "UNOP", "ADD", 
		"SUB", "MULT", "DIV", "MOD", "GT", "GTE", "LT", "LTE", "ET", "NET", "OR", 
		"AND", "NOT", "DEC", "BOOL", "STR", "ID", "WS", "EOL"
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
			setState(30);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IF) | (1L << WHILE) | (1L << RETURN) | (1L << BREAK) | (1L << CONTINUE) | (1L << TEST) | (1L << ID) | (1L << EOL))) != 0)) {
				{
				setState(28);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
				case 1:
					{
					setState(26);
					statement();
					}
					break;
				case 2:
					{
					setState(27);
					function();
					}
					break;
				}
				}
				setState(32);
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
			setState(33);
			match(ID);
			setState(38);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(34);
				match(T__0);
				setState(35);
				paramList();
				setState(36);
				match(T__1);
				}
			}

			setState(40);
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
			setState(42);
			match(ID);
			setState(49);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(45); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(43);
					match(T__2);
					setState(44);
					match(ID);
					}
					}
					setState(47); 
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
			setState(51);
			expr();
			setState(58);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(54); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(52);
					match(T__2);
					setState(53);
					expr();
					}
					}
					setState(56); 
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
			setState(60);
			match(T__3);
			setState(64);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IF) | (1L << WHILE) | (1L << RETURN) | (1L << BREAK) | (1L << CONTINUE) | (1L << TEST) | (1L << ID) | (1L << EOL))) != 0)) {
				{
				{
				setState(61);
				statement();
				}
				}
				setState(66);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(67);
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
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
	 
		public StatementContext() { }
		public void copyFrom(StatementContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class AssignmentContext extends StatementContext {
		public LocContext loc() {
			return getRuleContext(LocContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode EOL() { return getToken(LJParser.EOL, 0); }
		public AssignmentContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterAssignment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitAssignment(this);
		}
	}
	public static class ReturnContext extends StatementContext {
		public TerminalNode RETURN() { return getToken(LJParser.RETURN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode EOL() { return getToken(LJParser.EOL, 0); }
		public ReturnContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterReturn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitReturn(this);
		}
	}
	public static class TestContext extends StatementContext {
		public TerminalNode TEST() { return getToken(LJParser.TEST, 0); }
		public FuncCallContext funcCall() {
			return getRuleContext(FuncCallContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode EOL() { return getToken(LJParser.EOL, 0); }
		public TestContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitTest(this);
		}
	}
	public static class BreakContext extends StatementContext {
		public TerminalNode BREAK() { return getToken(LJParser.BREAK, 0); }
		public TerminalNode EOL() { return getToken(LJParser.EOL, 0); }
		public BreakContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterBreak(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitBreak(this);
		}
	}
	public static class TerminatorContext extends StatementContext {
		public TerminalNode EOL() { return getToken(LJParser.EOL, 0); }
		public TerminatorContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterTerminator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitTerminator(this);
		}
	}
	public static class ConditionalContext extends StatementContext {
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
		public ConditionalContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterConditional(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitConditional(this);
		}
	}
	public static class WhileContext extends StatementContext {
		public TerminalNode WHILE() { return getToken(LJParser.WHILE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public WhileContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterWhile(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitWhile(this);
		}
	}
	public static class ContinueContext extends StatementContext {
		public TerminalNode CONTINUE() { return getToken(LJParser.CONTINUE, 0); }
		public TerminalNode EOL() { return getToken(LJParser.EOL, 0); }
		public ContinueContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterContinue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitContinue(this);
		}
	}
	public static class VoidFunctionCallContext extends StatementContext {
		public FuncCallContext funcCall() {
			return getRuleContext(FuncCallContext.class,0);
		}
		public TerminalNode EOL() { return getToken(LJParser.EOL, 0); }
		public VoidFunctionCallContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterVoidFunctionCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitVoidFunctionCall(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_statement);
		int _la;
		try {
			setState(107);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				_localctx = new AssignmentContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(69);
				loc();
				setState(70);
				match(T__5);
				setState(71);
				expr();
				setState(72);
				match(EOL);
				}
				break;
			case 2:
				_localctx = new ConditionalContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(74);
				match(IF);
				setState(75);
				match(T__0);
				setState(76);
				expr();
				setState(77);
				match(T__1);
				setState(78);
				block();
				setState(81);
				_la = _input.LA(1);
				if (_la==ELSE) {
					{
					setState(79);
					match(ELSE);
					setState(80);
					block();
					}
				}

				}
				break;
			case 3:
				_localctx = new WhileContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(83);
				match(WHILE);
				setState(84);
				match(T__0);
				setState(85);
				expr();
				setState(86);
				match(T__1);
				setState(87);
				block();
				}
				break;
			case 4:
				_localctx = new ReturnContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(89);
				match(RETURN);
				setState(90);
				expr();
				setState(91);
				match(EOL);
				}
				break;
			case 5:
				_localctx = new BreakContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(93);
				match(BREAK);
				setState(94);
				match(EOL);
				}
				break;
			case 6:
				_localctx = new ContinueContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(95);
				match(CONTINUE);
				setState(96);
				match(EOL);
				}
				break;
			case 7:
				_localctx = new TestContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(97);
				match(TEST);
				setState(98);
				funcCall();
				setState(99);
				match(T__5);
				setState(100);
				expr();
				setState(101);
				match(EOL);
				}
				break;
			case 8:
				_localctx = new VoidFunctionCallContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(103);
				funcCall();
				setState(104);
				match(EOL);
				}
				break;
			case 9:
				_localctx = new TerminatorContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(106);
				match(EOL);
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

	public static class ExprContext extends ParserRuleContext {
		public ExprBinContext exprBin() {
			return getRuleContext(ExprBinContext.class,0);
		}
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
		ExprContext _localctx = new ExprContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_expr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(109);
			exprBin(0);
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

	public static class ExprBinContext extends ParserRuleContext {
		public ExprBinContext left;
		public Token op;
		public ExprBinContext right;
		public ExprUnContext exprUn() {
			return getRuleContext(ExprUnContext.class,0);
		}
		public List<ExprBinContext> exprBin() {
			return getRuleContexts(ExprBinContext.class);
		}
		public ExprBinContext exprBin(int i) {
			return getRuleContext(ExprBinContext.class,i);
		}
		public TerminalNode PREC1() { return getToken(LJParser.PREC1, 0); }
		public TerminalNode PREC2() { return getToken(LJParser.PREC2, 0); }
		public TerminalNode PREC3() { return getToken(LJParser.PREC3, 0); }
		public ExprBinContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprBin; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterExprBin(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitExprBin(this);
		}
	}

	public final ExprBinContext exprBin() throws RecognitionException {
		return exprBin(0);
	}

	private ExprBinContext exprBin(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprBinContext _localctx = new ExprBinContext(_ctx, _parentState);
		ExprBinContext _prevctx = _localctx;
		int _startState = 14;
		enterRecursionRule(_localctx, 14, RULE_exprBin, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(112);
			exprUn();
			}
			_ctx.stop = _input.LT(-1);
			setState(125);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(123);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
					case 1:
						{
						_localctx = new ExprBinContext(_parentctx, _parentState);
						_localctx.left = _prevctx;
						_localctx.left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_exprBin);
						setState(114);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(115);
						((ExprBinContext)_localctx).op = match(PREC1);
						setState(116);
						((ExprBinContext)_localctx).right = exprBin(5);
						}
						break;
					case 2:
						{
						_localctx = new ExprBinContext(_parentctx, _parentState);
						_localctx.left = _prevctx;
						_localctx.left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_exprBin);
						setState(117);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(118);
						((ExprBinContext)_localctx).op = match(PREC2);
						setState(119);
						((ExprBinContext)_localctx).right = exprBin(4);
						}
						break;
					case 3:
						{
						_localctx = new ExprBinContext(_parentctx, _parentState);
						_localctx.left = _prevctx;
						_localctx.left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_exprBin);
						setState(120);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(121);
						((ExprBinContext)_localctx).op = match(PREC3);
						setState(122);
						((ExprBinContext)_localctx).right = exprBin(3);
						}
						break;
					}
					} 
				}
				setState(127);
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

	public static class ExprUnContext extends ParserRuleContext {
		public Token op;
		public ExprUnContext expression;
		public TerminalNode UNOP() { return getToken(LJParser.UNOP, 0); }
		public ExprUnContext exprUn() {
			return getRuleContext(ExprUnContext.class,0);
		}
		public ExprBaseContext exprBase() {
			return getRuleContext(ExprBaseContext.class,0);
		}
		public ExprUnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprUn; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterExprUn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitExprUn(this);
		}
	}

	public final ExprUnContext exprUn() throws RecognitionException {
		ExprUnContext _localctx = new ExprUnContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_exprUn);
		try {
			setState(131);
			switch (_input.LA(1)) {
			case UNOP:
				enterOuterAlt(_localctx, 1);
				{
				setState(128);
				((ExprUnContext)_localctx).op = match(UNOP);
				setState(129);
				((ExprUnContext)_localctx).expression = exprUn();
				}
				break;
			case T__0:
			case DEC:
			case BOOL:
			case STR:
			case ID:
				enterOuterAlt(_localctx, 2);
				{
				setState(130);
				exprBase();
				}
				break;
			default:
				throw new NoViableAltException(this);
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

	public static class ExprBaseContext extends ParserRuleContext {
		public FuncCallContext funcCall() {
			return getRuleContext(FuncCallContext.class,0);
		}
		public LocContext loc() {
			return getRuleContext(LocContext.class,0);
		}
		public LitContext lit() {
			return getRuleContext(LitContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ExprBaseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprBase; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterExprBase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitExprBase(this);
		}
	}

	public final ExprBaseContext exprBase() throws RecognitionException {
		ExprBaseContext _localctx = new ExprBaseContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_exprBase);
		try {
			setState(140);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(133);
				funcCall();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(134);
				loc();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(135);
				lit();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(136);
				match(T__0);
				setState(137);
				expr();
				setState(138);
				match(T__1);
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
		enterRule(_localctx, 20, RULE_funcCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(142);
			match(ID);
			setState(143);
			match(T__0);
			setState(144);
			argList();
			setState(145);
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
		enterRule(_localctx, 22, RULE_loc);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(147);
			match(ID);
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
		enterRule(_localctx, 24, RULE_lit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(149);
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 7:
			return exprBin_sempred((ExprBinContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean exprBin_sempred(ExprBinContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 4);
		case 1:
			return precpred(_ctx, 3);
		case 2:
			return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\'\u009a\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\3\2\3\2\7\2\37\n\2\f\2\16\2\"\13\2\3\3\3"+
		"\3\3\3\3\3\3\3\5\3)\n\3\3\3\3\3\3\4\3\4\3\4\6\4\60\n\4\r\4\16\4\61\5\4"+
		"\64\n\4\3\5\3\5\3\5\6\59\n\5\r\5\16\5:\5\5=\n\5\3\6\3\6\7\6A\n\6\f\6\16"+
		"\6D\13\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\5\7T"+
		"\n\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3"+
		"\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\5\7n\n\7\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3"+
		"\t\3\t\3\t\3\t\3\t\3\t\3\t\7\t~\n\t\f\t\16\t\u0081\13\t\3\n\3\n\3\n\5"+
		"\n\u0086\n\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\5\13\u008f\n\13\3\f\3"+
		"\f\3\f\3\f\3\f\3\r\3\r\3\16\3\16\3\16\2\3\20\17\2\4\6\b\n\f\16\20\22\24"+
		"\26\30\32\2\3\3\2\"$\u00a4\2 \3\2\2\2\4#\3\2\2\2\6,\3\2\2\2\b\65\3\2\2"+
		"\2\n>\3\2\2\2\fm\3\2\2\2\16o\3\2\2\2\20q\3\2\2\2\22\u0085\3\2\2\2\24\u008e"+
		"\3\2\2\2\26\u0090\3\2\2\2\30\u0095\3\2\2\2\32\u0097\3\2\2\2\34\37\5\f"+
		"\7\2\35\37\5\4\3\2\36\34\3\2\2\2\36\35\3\2\2\2\37\"\3\2\2\2 \36\3\2\2"+
		"\2 !\3\2\2\2!\3\3\2\2\2\" \3\2\2\2#(\7%\2\2$%\7\3\2\2%&\5\6\4\2&\'\7\4"+
		"\2\2\')\3\2\2\2($\3\2\2\2()\3\2\2\2)*\3\2\2\2*+\5\n\6\2+\5\3\2\2\2,\63"+
		"\7%\2\2-.\7\5\2\2.\60\7%\2\2/-\3\2\2\2\60\61\3\2\2\2\61/\3\2\2\2\61\62"+
		"\3\2\2\2\62\64\3\2\2\2\63/\3\2\2\2\63\64\3\2\2\2\64\7\3\2\2\2\65<\5\16"+
		"\b\2\66\67\7\5\2\2\679\5\16\b\28\66\3\2\2\29:\3\2\2\2:8\3\2\2\2:;\3\2"+
		"\2\2;=\3\2\2\2<8\3\2\2\2<=\3\2\2\2=\t\3\2\2\2>B\7\6\2\2?A\5\f\7\2@?\3"+
		"\2\2\2AD\3\2\2\2B@\3\2\2\2BC\3\2\2\2CE\3\2\2\2DB\3\2\2\2EF\7\7\2\2F\13"+
		"\3\2\2\2GH\5\30\r\2HI\7\b\2\2IJ\5\16\b\2JK\7\'\2\2Kn\3\2\2\2LM\7\t\2\2"+
		"MN\7\3\2\2NO\5\16\b\2OP\7\4\2\2PS\5\n\6\2QR\7\n\2\2RT\5\n\6\2SQ\3\2\2"+
		"\2ST\3\2\2\2Tn\3\2\2\2UV\7\13\2\2VW\7\3\2\2WX\5\16\b\2XY\7\4\2\2YZ\5\n"+
		"\6\2Zn\3\2\2\2[\\\7\f\2\2\\]\5\16\b\2]^\7\'\2\2^n\3\2\2\2_`\7\r\2\2`n"+
		"\7\'\2\2ab\7\16\2\2bn\7\'\2\2cd\7\17\2\2de\5\26\f\2ef\7\b\2\2fg\5\16\b"+
		"\2gh\7\'\2\2hn\3\2\2\2ij\5\26\f\2jk\7\'\2\2kn\3\2\2\2ln\7\'\2\2mG\3\2"+
		"\2\2mL\3\2\2\2mU\3\2\2\2m[\3\2\2\2m_\3\2\2\2ma\3\2\2\2mc\3\2\2\2mi\3\2"+
		"\2\2ml\3\2\2\2n\r\3\2\2\2op\5\20\t\2p\17\3\2\2\2qr\b\t\1\2rs\5\22\n\2"+
		"s\177\3\2\2\2tu\f\6\2\2uv\7\20\2\2v~\5\20\t\7wx\f\5\2\2xy\7\21\2\2y~\5"+
		"\20\t\6z{\f\4\2\2{|\7\22\2\2|~\5\20\t\5}t\3\2\2\2}w\3\2\2\2}z\3\2\2\2"+
		"~\u0081\3\2\2\2\177}\3\2\2\2\177\u0080\3\2\2\2\u0080\21\3\2\2\2\u0081"+
		"\177\3\2\2\2\u0082\u0083\7\23\2\2\u0083\u0086\5\22\n\2\u0084\u0086\5\24"+
		"\13\2\u0085\u0082\3\2\2\2\u0085\u0084\3\2\2\2\u0086\23\3\2\2\2\u0087\u008f"+
		"\5\26\f\2\u0088\u008f\5\30\r\2\u0089\u008f\5\32\16\2\u008a\u008b\7\3\2"+
		"\2\u008b\u008c\5\16\b\2\u008c\u008d\7\4\2\2\u008d\u008f\3\2\2\2\u008e"+
		"\u0087\3\2\2\2\u008e\u0088\3\2\2\2\u008e\u0089\3\2\2\2\u008e\u008a\3\2"+
		"\2\2\u008f\25\3\2\2\2\u0090\u0091\7%\2\2\u0091\u0092\7\3\2\2\u0092\u0093"+
		"\5\b\5\2\u0093\u0094\7\4\2\2\u0094\27\3\2\2\2\u0095\u0096\7%\2\2\u0096"+
		"\31\3\2\2\2\u0097\u0098\t\2\2\2\u0098\33\3\2\2\2\20\36 (\61\63:<BSm}\177"+
		"\u0085\u008e";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}