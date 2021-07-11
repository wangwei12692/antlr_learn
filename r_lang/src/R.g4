grammar R;

prog    : (expr_or_assign (';'|NL)
        |NL
        )*
        EOF
        ;

expr_or_assign
    : expr ('<-' | '=' | '<<-') expr_or_assign
    | expr
    ;


expr: expr '[[' sublist ']'']'
    | expr '[' sublist ']'
    | expr ('::'|':::') expr
    | expr ('$'|'@') expr
    | expr '^'<assoc=right> expr
    |('-'|'+') expr
    |expr ':' expr
    |expr USER_OP expr
    |expr ('*'|'/') expr
    |expr ('+'|'-') expr
    |expr ('>'|'>='|'<'|'<='|'=='|'!=') expr
    |'!' expr
    | expr ('&'|'&&') expr
    |expr ('|'|'||') expr
    |'~' expr
    |expr '~' expr
    |expr ('->'|'-->'|':=') expr
    | 'function' '(' formlist? ')' expr
    | expr '(' sublist ')'
    | '{' exprlist '}'
    | 'if' '('expr ')' expr
    | 'if' '(' expr ')' expr 'else' expr
    | 'for' '(' ID 'in'  expr ')' expr
    | 'while' '(' expr ')' expr
    | 'repeat' expr
    | '?' expr
    |'next'
    |'break'
    |'(' expr ')'
    |ID
    |STRING
    |HEX
    |FLOAT
    |COMPLEX
    |'NULL'
    |'NA'
    |'Inf'
    |'NaN'
    |'TRUE'
    |'FALSE'
    ;

exprlist
    : expr_or_assign ((';'|NL) expr_or_assign?)*
    |
    ;


formlist :form (',' form)*;

form: ID
    | ID '=' expr
    |'...'
    ;

sublist : sub (',' sub)*;
sub : expr
    | ID '='
    | ID '=' expr
    | STRING '='
    | STRING '=' expr
    | 'NULL' '='
    | 'NULL' '=' expr
    | '...'
    |
    ;

HEX : '0' ('x'|'X') HEXDIGIT+ [Ll]?;

INT : DIGIT+ [Ll]?;



fragment
HEXDIGIT :('0'..'9'|'a'..'f'|'A'..'F');

FLOAT : DIGIT+ '.' DIGIT* EXP? [Ll]?
      | DIGIT+ EXP? [Ll]?
      | '.' DIGIT+ EXP? [Ll]?
      ;

ID  :   '.' (LETTER|'_'|'.') (LETTER|DIGIT|'_'|'.')*
    |   LETTER (LETTER|DIGIT|'_'|'.')*
    ;

fragment
EXP :
    ('E'|'e') ('+' | '-')? INT;

fragment
DIGIT : '0' .. '9';

COMPLEX
    : INT 'i'
    | FLOAT 'i'
    ;

STRING
    : '"' (ESC|~[\\"])*? '"'
    | '\'' (ESC| ~[\\'])*? '\''
    ;

fragment
ESC : '||' ([abtnfrv]|'"'|'\'')
    |UNICODE_ESCAPE
    |HEX_ESCAPE
    |OCATAL_ESCAPE
    ;

fragment
UNICODE_ESCAPE
    : '\\' 'u' HEXDIGIT HEXDIGIT HEXDIGIT HEXDIGIT
    | '\\' 'u' '{' HEXDIGIT HEXDIGIT HEXDIGIT HEXDIGIT '}'
    ;

fragment
OCATAL_ESCAPE
    :'\\'[0-3][0-7][0-7]
    |'\\'[0-7][0-7]
    |'\\'[0-7]
    ;

fragment
HEX_ESCAPE
    :'\\' HEXDIGIT HEXDIGIT?
    ;

USER_OP : '%' .*? '%';

fragment LETTER  : [a-zA-Z] ;

NL:'\r'? '\n';

WS      :   [ \t]+ -> skip ;
