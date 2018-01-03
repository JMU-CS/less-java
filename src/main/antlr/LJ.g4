grammar LJ;

/* Parser Grammar */

program:        (statement | function | test)*;

function:       ID (LP paramList RP )? block;
paramList:      ID (','ID)*;
block:          (EOL)? '{' (EOL)? statement* '}' (EOL)?;

statement:      IF LP expr RP block (ELSE block)?           #Conditional
                | WHILE LP expr RP block                    #While
                | FOR LP var ':' (expr '->')? expr RP block #For
                | RETURN expr EOL                           #Return
                | BREAK EOL                                 #Break
                | CONTINUE EOL                              #Continue
                | funcCall EOL                              #VoidFunctionCall
                | assignment EOL                            #VoidAssignment
                | EOL                                       #Terminator
                ;

test:           TEST expr EOL;

expr:           exprBin;

exprBin:        left=exprBin op=PREC1 right=exprBin
                | left=exprBin op=PREC2 right=exprBin
                | left=exprBin op=PREC3 right=exprBin
                | left=exprBin op=PREC4 right=exprBin
                | left=exprBin op=PREC5 right=exprBin
                | left=exprBin op=PREC6 right=exprBin
                | assignment
                | exprUn
                ;

exprUn:         op=UNOP expression=exprUn
                | exprBase
                ;

exprBase:       funcCall
                | collection
                | var
                | lit
                | LP expr RP
                ;

funcCall:       ID LP (argList)? RP;

collection:     LSB (argList)? RSB         #List
                | LCB (argList)? RCB       #Set
                | PREC3 (argList)? PREC3   #Map;

assignment:     var PREC7 expr;

argList:        entry (',' entry)*
                | expr (',' expr)*;

entry:          key=expr ':' value=expr;

var:            ID (LSB (expr)? RSB )?;
lit:            INT | REAL | BOOL | STR;


/* Lexer Rules */

// Key Words
IF:         'if';
ELSE:       'else';
WHILE:      'while';
FOR:        'for';
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

PREC3:      GT
            | GTE
            | LT
            | LTE
            ;

PREC4:      ET
            | NET
            ;


PREC5:      AND;

PREC6:      OR;

PREC7:      ASGN;

UNOP:       NOT
            | SUB
            ;

NOT:        '!';
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
ASGN:       '=';

LSB:         '[';
RSB:         ']';
LCB:         '{';
RCB:         '}';
LP:          '(';
RP:          ')';


// Literals
INT:        (SUB)?[0-9]+;
REAL:       (SUB)?[0-9]*'.'[0-9]+;
BOOL:       'true'|'false';
STR:        '\"'.*?'\"';

ID:         [a-zA-Z][a-zA-Z0-9_]*;
EOL:        '\r'? '\n';

// Ignore
WHITESPACE:    	[ \t]+ -> skip;
BLOCK_COMMENT: 	'/*' .*? '*/' -> skip;
LINE_COMMENT:  	'//' ~[\r\n]* -> skip;
