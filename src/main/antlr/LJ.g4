grammar LJ;

/* Parser Grammar */

program:        (class_ | function | global | test | EOL)*;

class_:          classSignature classBlock;

classSignature: name=ID (EXTENDS superName=ID)?;
classBlock:     (EOL)? LCB (EOL)? (attribute | EOL)* (method | EOL)* RCB (EOL)?;
attribute:      scope=(PUBLIC|PRIVATE) assignment EOL;
method:         (scope=(PUBLIC|PRIVATE))? function;

function:       ID LP (paramList)? RP block;
paramList:      ID (','ID)*;
block:          (EOL)? LCB (EOL)? statement* RCB (EOL)?;

global:         GLOBAL assignment EOL;

test:           TEST expr EOL;

statement:      IF LP expr RP block (ELSE block)?           #Conditional
                | WHILE LP expr RP block                    #While
                | FOR LP var ':' (expr '->')? expr RP block #For
                | RETURN expr EOL                           #Return
                | BREAK EOL                                 #Break
                | CONTINUE EOL                              #Continue
                | funcCall EOL                              #VoidFunctionCall
                | methodCall EOL                            #VoidMethodCall
                | assignment EOL                            #VoidAssignment
                | EOL                                       #Terminator
                ;

expr:           exprBin;

exprBin:        left=exprBin op=PREC1 right=exprBin
                | left=exprBin op=('+'|'-') right=exprBin
                | left=exprBin op=PREC3 right=exprBin
                | left=exprBin op=PREC4 right=exprBin
                | left=exprBin op=PREC5 right=exprBin
                | left=exprBin op=PREC6 right=exprBin
                | methodCall
                | assignment
                | exprUn
                ;

exprUn:         op=('!'|'-') expression=exprUn
                | exprBase
                ;

exprBase:       funcCall
                | methodCall
                | collection
                | var
                | memberAccess
                | lit
                | LP expr RP
                ;

funcCall:       ID LP (argList)? RP;

methodCall:     (var | funcCall) op=INVOKE funcCall;

collection:     LSB (argList)? RSB       #List
                | LCB (argList)? RCB     #Set
                | PREC3 (argList)? PREC3 #Map
                ;

assignment:     (var | memberAccess) op=PREC7 expr;

argList:        entry (',' entry)*
                | expr (',' expr)*
                ;

entry:          key=expr ':' value=expr;

var:            name=ID (LSB (index=expr)? RSB )?;
memberAccess:   instance=ID INVOKE var;

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
GLOBAL:     'global';
PUBLIC:     'public';
PRIVATE:    'private';
EXTENDS:    'extends';

PREC1:      MULT
            | DIV
            | MOD
            ;

// Remove to prevent collisions with unop
//PREC2:      ADD
//            | SUB
//            ;

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

PREC7:      ASGN
            | ADDASGN
            | SUBASGN
            ;

NOT:     '!';
ADD:     '+';
SUB:     '-';
MULT:    '*';
DIV:     '/';
MOD:     '%';
GT:      '>';
GTE:     '>=';
LT:      '<';
LTE:     '<=';
ET:      '==';
NET:     '!=';
OR:      '||';
AND:     '&&';
ASGN:    '=';
ADDASGN: '+=';
SUBASGN: '-=';
INCR:    '++';
DECR:    '--';

LSB: '[';
RSB: ']';
LCB: '{';
RCB: '}';
LP:  '(';
RP:  ')';

INVOKE: '.';


// Literals
INT:        [0-9]+;
REAL:       [0-9]*'.'[0-9]+;
BOOL:       'true'|'false';
STR:        '\"'.*?'\"';

ID:         [a-zA-Z][a-zA-Z0-9_]*;
EOL:        '\r'? '\n';

// Ignore
WHITESPACE:    	[ \t]+ -> skip;
BLOCK_COMMENT: 	'/*' .*? '*/' -> skip;
LINE_COMMENT:  	'//' ~[\r\n]* -> skip;
