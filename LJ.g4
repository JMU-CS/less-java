grammar LJ;

/* Parser Grammar */

program:        (statement | function)*;

function:       ID ('('paramList')')? block;
paramList:      ID((','ID)+)?;
argList:        expr((','expr)+)?;
block:          '{'statement*'}';

statement:      assignment_t
                | conditional_t
                | whileLoop_t
                | return_t
                | break_t
                | continue_t
                | test_t
                | terminator
                | funcCall
                ;

assignment_t:     loc '=' expr NL;
conditional_t:    IF '(' expr ')' block (ELSE block)?;
whileLoop_t:      WHILE '(' expr ')' block;
return_t:         RETURN expr NL;
break_t:          BREAK NL;
continue_t:       CONTINUE NL;
test_t:           TEST funcCall '=' expr;

expr:           expr BINOP expr
                | funcCall
                | loc
                | lit
                ;

funcCall:       ID '('argList')';
loc:            ID ('['DEC']')?;
lit:            DEC | BOOL | STR;

terminator:     NL;




/* Lexer Rules */

// Key Words
IF:         'if';
ELSE:       'else';
WHILE:      'while';
RETURN:     'return';
BREAK:      'break';
CONTINUE:   'continue';
TEST:       'test';

BINOP:      ADD
            | MULT
            | DIV
            | SUB
            | GT
            | GTE
            | LT
            | LTE
            | ET
            | NET
            | OR
            | AND
            ;

ADD:        '+';
MULT:       '*';
DIV:        '/';
SUB:        '-';
GT:         '>';
GTE:       '>=';
LT:         '<';
LTE:       '<=';
ET:        '==';
NET:       '!=';
OR:        '||';
AND:       '&&';

// Literals
DEC:        [0-9]+;
BOOL:       'true'|'false';
STR:        '\"'.*?'\"';

ID:         [a-zA-Z]+;

WS:         [ \t]+ -> skip;
NL:         '\r'? '\n';
