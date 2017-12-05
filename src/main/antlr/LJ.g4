grammar LJ;

/* Parser Grammar */

program:        (statement | function | test)*;

function:       ID ('('paramList')')? block;
paramList:      ID((','ID)+)?;
argList:        (expr((','expr)+)?)?;
block:          (EOL)? '{' (EOL)? statement* '}' (EOL)?;

statement:      IF '(' expr ')' block (ELSE block)?             #Conditional
                | WHILE '(' expr ')' block                      #While
                | FOR '(' var ':' (expr '->')? expr ')' block   #For
                | RETURN expr EOL                               #Return
                | BREAK EOL                                     #Break
                | CONTINUE EOL                                  #Continue
                | funcCall EOL                                  #VoidFunctionCall
                | assignment EOL                                #VoidAssignment
                | EOL                                           #Terminator
                ;

test:           TEST expr EOL;

expr:           exprBin;

exprBin:        left = exprBin '['right=exprBin']'
                | left=exprBin op=PREC1 right=exprBin
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

exprBase:       assignment
                | funcCall
                | var
                | lit
                | '[' argList ']'
                | '(' expr ')'
                ;

assignment:     var PREC7 expr;

funcCall:       ID '('argList')';

var:            ID ('['expr']')?;
lit:            DEC | BOOL | STR;


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


// Literals
DEC:        (SUB)?[0-9]+;
BOOL:       'true'|'false';
STR:        '\"'.*?'\"';

ID:         [a-zA-Z][a-zA-Z0-9_]*;
EOL:        '\r'? '\n';

// Ignore
WHITESPACE:    	[ \t]+ -> skip;
BLOCK_COMMENT: 	'/*' .*? '*/' -> skip;
LINE_COMMENT:  	'//' ~[\r\n]* -> skip;
