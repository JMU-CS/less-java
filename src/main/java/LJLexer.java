// Generated from LJ.g4 by ANTLR 4.5.3
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class LJLexer extends Lexer {
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
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "IF", "ELSE", "WHILE", 
		"RETURN", "BREAK", "CONTINUE", "TEST", "PREC1", "PREC2", "PREC3", "UNOP", 
		"ADD", "SUB", "MULT", "DIV", "MOD", "GT", "GTE", "LT", "LTE", "ET", "NET", 
		"OR", "AND", "NOT", "DEC", "BOOL", "STR", "ID", "WS", "EOL"
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


	public LJLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "LJ.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\'\u00e4\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3"+
		"\6\3\6\3\7\3\7\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n"+
		"\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3"+
		"\r\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\5\17"+
		"\u0086\n\17\3\20\3\20\5\20\u008a\n\20\3\21\3\21\3\21\3\21\3\21\3\21\3"+
		"\21\3\21\3\21\5\21\u0095\n\21\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25"+
		"\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\31\3\32\3\32\3\33\3\33\3\33"+
		"\3\34\3\34\3\34\3\35\3\35\3\35\3\36\3\36\3\36\3\37\3\37\3\37\3 \3 \3!"+
		"\6!\u00bc\n!\r!\16!\u00bd\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\5\"\u00c9"+
		"\n\"\3#\3#\7#\u00cd\n#\f#\16#\u00d0\13#\3#\3#\3$\6$\u00d5\n$\r$\16$\u00d6"+
		"\3%\6%\u00da\n%\r%\16%\u00db\3%\3%\3&\5&\u00e1\n&\3&\3&\3\u00ce\2\'\3"+
		"\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37"+
		"\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37="+
		" ?!A\"C#E$G%I&K\'\3\2\5\3\2\62;\4\2C\\c|\4\2\13\13\"\"\u00f4\2\3\3\2\2"+
		"\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3"+
		"\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2"+
		"\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2"+
		"\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2"+
		"\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3"+
		"\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2"+
		"\2\2K\3\2\2\2\3M\3\2\2\2\5O\3\2\2\2\7Q\3\2\2\2\tS\3\2\2\2\13U\3\2\2\2"+
		"\rW\3\2\2\2\17Y\3\2\2\2\21\\\3\2\2\2\23a\3\2\2\2\25g\3\2\2\2\27n\3\2\2"+
		"\2\31t\3\2\2\2\33}\3\2\2\2\35\u0085\3\2\2\2\37\u0089\3\2\2\2!\u0094\3"+
		"\2\2\2#\u0096\3\2\2\2%\u0098\3\2\2\2\'\u009a\3\2\2\2)\u009c\3\2\2\2+\u009e"+
		"\3\2\2\2-\u00a0\3\2\2\2/\u00a2\3\2\2\2\61\u00a4\3\2\2\2\63\u00a7\3\2\2"+
		"\2\65\u00a9\3\2\2\2\67\u00ac\3\2\2\29\u00af\3\2\2\2;\u00b2\3\2\2\2=\u00b5"+
		"\3\2\2\2?\u00b8\3\2\2\2A\u00bb\3\2\2\2C\u00c8\3\2\2\2E\u00ca\3\2\2\2G"+
		"\u00d4\3\2\2\2I\u00d9\3\2\2\2K\u00e0\3\2\2\2MN\7*\2\2N\4\3\2\2\2OP\7+"+
		"\2\2P\6\3\2\2\2QR\7.\2\2R\b\3\2\2\2ST\7}\2\2T\n\3\2\2\2UV\7\177\2\2V\f"+
		"\3\2\2\2WX\7?\2\2X\16\3\2\2\2YZ\7k\2\2Z[\7h\2\2[\20\3\2\2\2\\]\7g\2\2"+
		"]^\7n\2\2^_\7u\2\2_`\7g\2\2`\22\3\2\2\2ab\7y\2\2bc\7j\2\2cd\7k\2\2de\7"+
		"n\2\2ef\7g\2\2f\24\3\2\2\2gh\7t\2\2hi\7g\2\2ij\7v\2\2jk\7w\2\2kl\7t\2"+
		"\2lm\7p\2\2m\26\3\2\2\2no\7d\2\2op\7t\2\2pq\7g\2\2qr\7c\2\2rs\7m\2\2s"+
		"\30\3\2\2\2tu\7e\2\2uv\7q\2\2vw\7p\2\2wx\7v\2\2xy\7k\2\2yz\7p\2\2z{\7"+
		"w\2\2{|\7g\2\2|\32\3\2\2\2}~\7v\2\2~\177\7g\2\2\177\u0080\7u\2\2\u0080"+
		"\u0081\7v\2\2\u0081\34\3\2\2\2\u0082\u0086\5)\25\2\u0083\u0086\5+\26\2"+
		"\u0084\u0086\5-\27\2\u0085\u0082\3\2\2\2\u0085\u0083\3\2\2\2\u0085\u0084"+
		"\3\2\2\2\u0086\36\3\2\2\2\u0087\u008a\5%\23\2\u0088\u008a\5\'\24\2\u0089"+
		"\u0087\3\2\2\2\u0089\u0088\3\2\2\2\u008a \3\2\2\2\u008b\u0095\3\2\2\2"+
		"\u008c\u0095\5/\30\2\u008d\u0095\5\61\31\2\u008e\u0095\5\63\32\2\u008f"+
		"\u0095\5\65\33\2\u0090\u0095\5\67\34\2\u0091\u0095\59\35\2\u0092\u0095"+
		"\5;\36\2\u0093\u0095\5=\37\2\u0094\u008b\3\2\2\2\u0094\u008c\3\2\2\2\u0094"+
		"\u008d\3\2\2\2\u0094\u008e\3\2\2\2\u0094\u008f\3\2\2\2\u0094\u0090\3\2"+
		"\2\2\u0094\u0091\3\2\2\2\u0094\u0092\3\2\2\2\u0094\u0093\3\2\2\2\u0095"+
		"\"\3\2\2\2\u0096\u0097\5? \2\u0097$\3\2\2\2\u0098\u0099\7-\2\2\u0099&"+
		"\3\2\2\2\u009a\u009b\7/\2\2\u009b(\3\2\2\2\u009c\u009d\7,\2\2\u009d*\3"+
		"\2\2\2\u009e\u009f\7\61\2\2\u009f,\3\2\2\2\u00a0\u00a1\7\'\2\2\u00a1."+
		"\3\2\2\2\u00a2\u00a3\7@\2\2\u00a3\60\3\2\2\2\u00a4\u00a5\7@\2\2\u00a5"+
		"\u00a6\7?\2\2\u00a6\62\3\2\2\2\u00a7\u00a8\7>\2\2\u00a8\64\3\2\2\2\u00a9"+
		"\u00aa\7>\2\2\u00aa\u00ab\7?\2\2\u00ab\66\3\2\2\2\u00ac\u00ad\7?\2\2\u00ad"+
		"\u00ae\7?\2\2\u00ae8\3\2\2\2\u00af\u00b0\7#\2\2\u00b0\u00b1\7?\2\2\u00b1"+
		":\3\2\2\2\u00b2\u00b3\7~\2\2\u00b3\u00b4\7~\2\2\u00b4<\3\2\2\2\u00b5\u00b6"+
		"\7(\2\2\u00b6\u00b7\7(\2\2\u00b7>\3\2\2\2\u00b8\u00b9\7#\2\2\u00b9@\3"+
		"\2\2\2\u00ba\u00bc\t\2\2\2\u00bb\u00ba\3\2\2\2\u00bc\u00bd\3\2\2\2\u00bd"+
		"\u00bb\3\2\2\2\u00bd\u00be\3\2\2\2\u00beB\3\2\2\2\u00bf\u00c0\7v\2\2\u00c0"+
		"\u00c1\7t\2\2\u00c1\u00c2\7w\2\2\u00c2\u00c9\7g\2\2\u00c3\u00c4\7h\2\2"+
		"\u00c4\u00c5\7c\2\2\u00c5\u00c6\7n\2\2\u00c6\u00c7\7u\2\2\u00c7\u00c9"+
		"\7g\2\2\u00c8\u00bf\3\2\2\2\u00c8\u00c3\3\2\2\2\u00c9D\3\2\2\2\u00ca\u00ce"+
		"\7$\2\2\u00cb\u00cd\13\2\2\2\u00cc\u00cb\3\2\2\2\u00cd\u00d0\3\2\2\2\u00ce"+
		"\u00cf\3\2\2\2\u00ce\u00cc\3\2\2\2\u00cf\u00d1\3\2\2\2\u00d0\u00ce\3\2"+
		"\2\2\u00d1\u00d2\7$\2\2\u00d2F\3\2\2\2\u00d3\u00d5\t\3\2\2\u00d4\u00d3"+
		"\3\2\2\2\u00d5\u00d6\3\2\2\2\u00d6\u00d4\3\2\2\2\u00d6\u00d7\3\2\2\2\u00d7"+
		"H\3\2\2\2\u00d8\u00da\t\4\2\2\u00d9\u00d8\3\2\2\2\u00da\u00db\3\2\2\2"+
		"\u00db\u00d9\3\2\2\2\u00db\u00dc\3\2\2\2\u00dc\u00dd\3\2\2\2\u00dd\u00de"+
		"\b%\2\2\u00deJ\3\2\2\2\u00df\u00e1\7\17\2\2\u00e0\u00df\3\2\2\2\u00e0"+
		"\u00e1\3\2\2\2\u00e1\u00e2\3\2\2\2\u00e2\u00e3\7\f\2\2\u00e3L\3\2\2\2"+
		"\f\2\u0085\u0089\u0094\u00bd\u00c8\u00ce\u00d6\u00db\u00e0\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}