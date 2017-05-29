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
		RULE_block = 4, RULE_statement = 5, RULE_expr = 6, RULE_funcCall = 7, 
		RULE_loc = 8, RULE_lit = 9;
	public static final String[] ruleNames = {
		"program", "function", "paramList", "argList", "block", "statement", "expr", 
		"funcCall", "loc", "lit"
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
			setState(24);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IF) | (1L << WHILE) | (1L << RETURN) | (1L << BREAK) | (1L << CONTINUE) | (1L << TEST) | (1L << ID) | (1L << EOL))) != 0)) {
				{
				setState(22);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
				case 1:
					{
					setState(20);
					statement();
					}
					break;
				case 2:
					{
					setState(21);
					function();
					}
					break;
				}
				}
				setState(26);
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
			setState(27);
			match(ID);
			setState(32);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(28);
				match(T__0);
				setState(29);
				paramList();
				setState(30);
				match(T__1);
				}
			}

			setState(34);
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
			setState(36);
			match(ID);
			setState(43);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(39); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(37);
					match(T__2);
					setState(38);
					match(ID);
					}
					}
					setState(41); 
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
			setState(45);
			expr(0);
			setState(52);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(48); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(46);
					match(T__2);
					setState(47);
					expr(0);
					}
					}
					setState(50); 
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
			setState(54);
			match(T__3);
			setState(58);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IF) | (1L << WHILE) | (1L << RETURN) | (1L << BREAK) | (1L << CONTINUE) | (1L << TEST) | (1L << ID) | (1L << EOL))) != 0)) {
				{
				{
				setState(55);
				statement();
				}
				}
				setState(60);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(61);
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
			setState(101);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				_localctx = new AssignmentContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(63);
				loc();
				setState(64);
				match(T__5);
				setState(65);
				expr(0);
				setState(66);
				match(EOL);
				}
				break;
			case 2:
				_localctx = new ConditionalContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(68);
				match(IF);
				setState(69);
				match(T__0);
				setState(70);
				expr(0);
				setState(71);
				match(T__1);
				setState(72);
				block();
				setState(75);
				_la = _input.LA(1);
				if (_la==ELSE) {
					{
					setState(73);
					match(ELSE);
					setState(74);
					block();
					}
				}

				}
				break;
			case 3:
				_localctx = new WhileContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(77);
				match(WHILE);
				setState(78);
				match(T__0);
				setState(79);
				expr(0);
				setState(80);
				match(T__1);
				setState(81);
				block();
				}
				break;
			case 4:
				_localctx = new ReturnContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(83);
				match(RETURN);
				setState(84);
				expr(0);
				setState(85);
				match(EOL);
				}
				break;
			case 5:
				_localctx = new BreakContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(87);
				match(BREAK);
				setState(88);
				match(EOL);
				}
				break;
			case 6:
				_localctx = new ContinueContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(89);
				match(CONTINUE);
				setState(90);
				match(EOL);
				}
				break;
			case 7:
				_localctx = new TestContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(91);
				match(TEST);
				setState(92);
				funcCall();
				setState(93);
				match(T__5);
				setState(94);
				expr(0);
				setState(95);
				match(EOL);
				}
				break;
			case 8:
				_localctx = new VoidFunctionCallContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(97);
				funcCall();
				setState(98);
				match(EOL);
				}
				break;
			case 9:
				_localctx = new TerminatorContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(100);
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
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ExprLocationContext extends ExprContext {
		public LocContext loc() {
			return getRuleContext(LocContext.class,0);
		}
		public ExprLocationContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterExprLocation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitExprLocation(this);
		}
	}
	public static class BinExprPrec2Context extends ExprContext {
		public ExprContext left;
		public Token op;
		public ExprContext right;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode PREC2() { return getToken(LJParser.PREC2, 0); }
		public BinExprPrec2Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterBinExprPrec2(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitBinExprPrec2(this);
		}
	}
	public static class BinExprPrec3Context extends ExprContext {
		public ExprContext left;
		public Token op;
		public ExprContext right;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode PREC3() { return getToken(LJParser.PREC3, 0); }
		public BinExprPrec3Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterBinExprPrec3(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitBinExprPrec3(this);
		}
	}
	public static class ExprFunctionCallContext extends ExprContext {
		public FuncCallContext funcCall() {
			return getRuleContext(FuncCallContext.class,0);
		}
		public ExprFunctionCallContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterExprFunctionCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitExprFunctionCall(this);
		}
	}
	public static class ExprParenContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ExprParenContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterExprParen(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitExprParen(this);
		}
	}
	public static class BinExprPrec1Context extends ExprContext {
		public ExprContext left;
		public Token op;
		public ExprContext right;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode PREC1() { return getToken(LJParser.PREC1, 0); }
		public BinExprPrec1Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterBinExprPrec1(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitBinExprPrec1(this);
		}
	}
	public static class UnExprContext extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode UNOP() { return getToken(LJParser.UNOP, 0); }
		public UnExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterUnExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitUnExpr(this);
		}
	}
	public static class ExprLiteralContext extends ExprContext {
		public LitContext lit() {
			return getRuleContext(LitContext.class,0);
		}
		public ExprLiteralContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).enterExprLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LJListener ) ((LJListener)listener).exitExprLiteral(this);
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
		int _startState = 12;
		enterRecursionRule(_localctx, 12, RULE_expr, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(113);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				_localctx = new UnExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(104);
				((UnExprContext)_localctx).op = match(UNOP);
				setState(105);
				expr(5);
				}
				break;
			case 2:
				{
				_localctx = new ExprFunctionCallContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(106);
				funcCall();
				}
				break;
			case 3:
				{
				_localctx = new ExprLocationContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(107);
				loc();
				}
				break;
			case 4:
				{
				_localctx = new ExprLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(108);
				lit();
				}
				break;
			case 5:
				{
				_localctx = new ExprParenContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(109);
				match(T__0);
				setState(110);
				expr(0);
				setState(111);
				match(T__1);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(126);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(124);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
					case 1:
						{
						_localctx = new BinExprPrec1Context(new ExprContext(_parentctx, _parentState));
						((BinExprPrec1Context)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(115);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(116);
						((BinExprPrec1Context)_localctx).op = match(PREC1);
						setState(117);
						((BinExprPrec1Context)_localctx).right = expr(9);
						}
						break;
					case 2:
						{
						_localctx = new BinExprPrec2Context(new ExprContext(_parentctx, _parentState));
						((BinExprPrec2Context)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(118);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(119);
						((BinExprPrec2Context)_localctx).op = match(PREC2);
						setState(120);
						((BinExprPrec2Context)_localctx).right = expr(8);
						}
						break;
					case 3:
						{
						_localctx = new BinExprPrec3Context(new ExprContext(_parentctx, _parentState));
						((BinExprPrec3Context)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(121);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(122);
						((BinExprPrec3Context)_localctx).op = match(PREC3);
						setState(123);
						((BinExprPrec3Context)_localctx).right = expr(7);
						}
						break;
					}
					} 
				}
				setState(128);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
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
		enterRule(_localctx, 14, RULE_funcCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(129);
			match(ID);
			setState(130);
			match(T__0);
			setState(131);
			argList();
			setState(132);
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
		enterRule(_localctx, 16, RULE_loc);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(134);
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
		enterRule(_localctx, 18, RULE_lit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(136);
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
		case 6:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 8);
		case 1:
			return precpred(_ctx, 7);
		case 2:
			return precpred(_ctx, 6);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\'\u008d\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\3\2\3\2\7\2\31\n\2\f\2\16\2\34\13\2\3\3\3\3\3\3\3\3\3\3\5\3#\n\3\3"+
		"\3\3\3\3\4\3\4\3\4\6\4*\n\4\r\4\16\4+\5\4.\n\4\3\5\3\5\3\5\6\5\63\n\5"+
		"\r\5\16\5\64\5\5\67\n\5\3\6\3\6\7\6;\n\6\f\6\16\6>\13\6\3\6\3\6\3\7\3"+
		"\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\5\7N\n\7\3\7\3\7\3\7\3\7\3"+
		"\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7"+
		"\3\7\3\7\5\7h\n\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\5\bt\n\b\3\b"+
		"\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\7\b\177\n\b\f\b\16\b\u0082\13\b\3\t\3"+
		"\t\3\t\3\t\3\t\3\n\3\n\3\13\3\13\3\13\2\3\16\f\2\4\6\b\n\f\16\20\22\24"+
		"\2\3\3\2\"$\u009a\2\32\3\2\2\2\4\35\3\2\2\2\6&\3\2\2\2\b/\3\2\2\2\n8\3"+
		"\2\2\2\fg\3\2\2\2\16s\3\2\2\2\20\u0083\3\2\2\2\22\u0088\3\2\2\2\24\u008a"+
		"\3\2\2\2\26\31\5\f\7\2\27\31\5\4\3\2\30\26\3\2\2\2\30\27\3\2\2\2\31\34"+
		"\3\2\2\2\32\30\3\2\2\2\32\33\3\2\2\2\33\3\3\2\2\2\34\32\3\2\2\2\35\"\7"+
		"%\2\2\36\37\7\3\2\2\37 \5\6\4\2 !\7\4\2\2!#\3\2\2\2\"\36\3\2\2\2\"#\3"+
		"\2\2\2#$\3\2\2\2$%\5\n\6\2%\5\3\2\2\2&-\7%\2\2\'(\7\5\2\2(*\7%\2\2)\'"+
		"\3\2\2\2*+\3\2\2\2+)\3\2\2\2+,\3\2\2\2,.\3\2\2\2-)\3\2\2\2-.\3\2\2\2."+
		"\7\3\2\2\2/\66\5\16\b\2\60\61\7\5\2\2\61\63\5\16\b\2\62\60\3\2\2\2\63"+
		"\64\3\2\2\2\64\62\3\2\2\2\64\65\3\2\2\2\65\67\3\2\2\2\66\62\3\2\2\2\66"+
		"\67\3\2\2\2\67\t\3\2\2\28<\7\6\2\29;\5\f\7\2:9\3\2\2\2;>\3\2\2\2<:\3\2"+
		"\2\2<=\3\2\2\2=?\3\2\2\2><\3\2\2\2?@\7\7\2\2@\13\3\2\2\2AB\5\22\n\2BC"+
		"\7\b\2\2CD\5\16\b\2DE\7\'\2\2Eh\3\2\2\2FG\7\t\2\2GH\7\3\2\2HI\5\16\b\2"+
		"IJ\7\4\2\2JM\5\n\6\2KL\7\n\2\2LN\5\n\6\2MK\3\2\2\2MN\3\2\2\2Nh\3\2\2\2"+
		"OP\7\13\2\2PQ\7\3\2\2QR\5\16\b\2RS\7\4\2\2ST\5\n\6\2Th\3\2\2\2UV\7\f\2"+
		"\2VW\5\16\b\2WX\7\'\2\2Xh\3\2\2\2YZ\7\r\2\2Zh\7\'\2\2[\\\7\16\2\2\\h\7"+
		"\'\2\2]^\7\17\2\2^_\5\20\t\2_`\7\b\2\2`a\5\16\b\2ab\7\'\2\2bh\3\2\2\2"+
		"cd\5\20\t\2de\7\'\2\2eh\3\2\2\2fh\7\'\2\2gA\3\2\2\2gF\3\2\2\2gO\3\2\2"+
		"\2gU\3\2\2\2gY\3\2\2\2g[\3\2\2\2g]\3\2\2\2gc\3\2\2\2gf\3\2\2\2h\r\3\2"+
		"\2\2ij\b\b\1\2jk\7\23\2\2kt\5\16\b\7lt\5\20\t\2mt\5\22\n\2nt\5\24\13\2"+
		"op\7\3\2\2pq\5\16\b\2qr\7\4\2\2rt\3\2\2\2si\3\2\2\2sl\3\2\2\2sm\3\2\2"+
		"\2sn\3\2\2\2so\3\2\2\2t\u0080\3\2\2\2uv\f\n\2\2vw\7\20\2\2w\177\5\16\b"+
		"\13xy\f\t\2\2yz\7\21\2\2z\177\5\16\b\n{|\f\b\2\2|}\7\22\2\2}\177\5\16"+
		"\b\t~u\3\2\2\2~x\3\2\2\2~{\3\2\2\2\177\u0082\3\2\2\2\u0080~\3\2\2\2\u0080"+
		"\u0081\3\2\2\2\u0081\17\3\2\2\2\u0082\u0080\3\2\2\2\u0083\u0084\7%\2\2"+
		"\u0084\u0085\7\3\2\2\u0085\u0086\5\b\5\2\u0086\u0087\7\4\2\2\u0087\21"+
		"\3\2\2\2\u0088\u0089\7%\2\2\u0089\23\3\2\2\2\u008a\u008b\t\2\2\2\u008b"+
		"\25\3\2\2\2\17\30\32\"+-\64\66<Mgs~\u0080";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}