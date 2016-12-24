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

expr:           expr BINOP expr                         #BinExpr
                | funcCall                              #ExprFunctionCall
                | loc                                   #ExprLocation
                | lit                                   #ExprLiteral
                | '(' expr ')'                          #ExprParen
                ;

funcCall:       ID '('argList')';

loc:            ID ('['expr']')?;
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
EOL:        '\r'? '\n';
