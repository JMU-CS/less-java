grammar LJ;

/* Parser Grammar */

program:        (statement | function)*;

function:       ID ('('paramList')')? block;
paramList:      ID((','ID)+)?;
argList:        expr((','expr)+)?;
block:          '{'statement*'}';

statement:      loc '=' expr NL                         #Assignment
                | IF '(' expr ')' block (ELSE block)?   #Conditional
                | WHILE '(' expr ')' block              #While
                | RETURN expr NL                        #Return
                | BREAK NL                              #Break
                | CONTINUE NL                           #Continue
                | TEST funcCall '=' expr                #Test
                | NL                                    #Terminator
                ;

expr:           expr BINOP expr                         #BinExpr
                | loc                                   #Location
                | lit                                   #Literal
                | funcCall                              #FuncCall
                ;

funcCall:       ID '('argList')';
voidFuncCall:   ^ID '('argList')'NL$;

loc:            ID ('['DEC']')?;
lit:            DEC | BOOL | STR;


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
