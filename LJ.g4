grammar LJ;

/* Parser */

program:    (statement | function)*;

block:      '{'statement*'}';

function:   ID ('('paramList')')? block;

statement:  var '=' expr NL
//            | functionCall NL
            | IF '(' expr ')' block (ELSE block)?
            | WHILE '(' expr ')' block
            | RETURN expr NL
            | BREAK NL
            | CONTINUE NL
            | NL
            ;

paramList:  ID ((','ID)+)?;

expr:       expr binOp expr
 //           | functionCall
            | LIT
            ;

binOp:      ADD | MULT | DIV | SUB;

var:        ID;

/* Lexer */

// Key Words
IF:         'if';
ELSE:       'else';
WHILE:      'while';
RETURN:     'return';
BREAK:      'break';
CONTINUE:   'continue';

ID:         [a-zA-Z]+;
ADD:        '+';
MULT:       '*';
DIV:        '/';
SUB:        '-';
LIT:        [0-9]+;
WS:         [ \t]+ -> skip;
NL:         '\r'? '\n';
