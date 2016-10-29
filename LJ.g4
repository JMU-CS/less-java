grammar LJ;

/* Parser Grammar */

program:    (statement | function)*;

function:   ID ('('paramList')')? block;
paramList:  (loc | lit)(((','(loc|lit))+))?;
block:      '{'statement*'}';

statement:  loc '=' expr NL
            | funcCall NL
            | IF '(' expr ')' block (ELSE block)?
            | WHILE '(' expr ')' block
            // FOR LOOP
            | RETURN expr NL
            | BREAK NL
            | CONTINUE NL
            | NL
            ;

expr:       expr BINOP expr
            | funcCall
            | loc
            | lit
            ;

loc:        ID ('['DEC']')?;

funcCall:   ID '('paramList')';

lit:        DEC | BOOL | STR;




/* Lexer Rules */

// Key Words
IF:         'if';
ELSE:       'else';
WHILE:      'while';
RETURN:     'return';
BREAK:      'break';
CONTINUE:   'continue';

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
