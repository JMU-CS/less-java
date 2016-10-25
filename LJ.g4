grammar LJ;

// Parser
program:    (statement | function)*;

block:      '{'statement*'}';

function:   ID ('('paramList')')? block;

statement:  var '=' expr NL
            | functionCall NL
            | if '(' expr ')' block (else block)?
            | while '(' expr ')' block
            | return expr NL
            | break NL
            | continue NL
            | NL
            ;

paramList:  ID ((','ID)*)?

expr:       var binOP expr
            | LIT
            ;

binOp:      ADD | MULT | DIV | SUB;

var:        ID;


// Lexer
NL:         '\r'? '\n';
ID:         [a-zA-Z]+;
WS:         [ \t]+ -> skip;
ADD:        '+';
MULT:       '*';
DIV:        '/';
SUB:        '-';
LIT:        [0-9]+;
