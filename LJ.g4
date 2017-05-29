grammar LJ;

/* Parser Grammar */

program:        (statement | function)*;

function:       ID ('('paramList')')? block;
paramList:      ID((','ID)+)?;
argList:        expr((','expr)+)?;
block:          '{'statement*'}';

statement:      loc '=' expr EOL                        #Assignment
                | IF '(' expr ')' block (ELSE block)?   #Conditional
                | WHILE '(' expr ')' block              #While
                | RETURN expr EOL                       #Return
                | BREAK EOL                             #Break
                | CONTINUE EOL                          #Continue
                | TEST funcCall '=' expr EOL            #Test
                | funcCall EOL                          #VoidFunctionCall
                | EOL                                   #Terminator
                ;

expr:           left=expr op=PREC1 right=expr           #BinExprPrec1
                | left=expr op=PREC2 right=expr         #BinExprPrec2
                | left=expr op=PREC3 right=expr         #BinExprPrec3
                | op=UNOP expr                          #UnExpr
                | funcCall                              #ExprFunctionCall
                | loc                                   #ExprLocation
                | lit                                   #ExprLiteral
                | '(' expr ')'                          #ExprParen
                ;

funcCall:       ID '('argList')';

loc:            ID;
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

PREC1:      MULT
            | DIV
            | MOD
            ;

PREC2:      ADD
            | SUB
            ;

PREC3:      | GT
            | GTE
            | LT
            | LTE
            | ET
            | NET
            | OR
            | AND
            ;

UNOP:       NOT;

ADD:        '+';
SUB:        '-';
MULT:       '*';
DIV:        '/';
MOD:        '%';
GT:         '>';
GTE:       '>=';
LT:         '<';
LTE:       '<=';
ET:        '==';
NET:       '!=';
OR:        '||';
AND:       '&&';

NOT:        '!';

// Literals
DEC:        [0-9]+;
BOOL:       'true'|'false';
STR:        '\"'.*?'\"';

ID:         [a-zA-Z]+;

WS:         [ \t]+ -> skip;
EOL:        '\r'? '\n';
