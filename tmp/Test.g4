grammar Test;

program: (expr ';')*
       ;

expr: ebin;

ebin: ebin ('*' | '/') ebin
    | ebin ('+' | '-') ebin
    | ebin ('==' | '<') ebin
    | eun
    ;

eun: '!' eun
   | ebase
   ;

ebase: '(' expr ')'
     | ID
     ;

ID: [a-zA-Z][a-zA-Z0-9_]*;

WS: [ \t\r\n]+ -> skip;
