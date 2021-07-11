grammar Expr;

expr: INT              // "1", "94117"
    | ID '[' expr ']'  // "a[1]", "a[b[1]]", "a[(2*b[1])]"
    | '(' expr ')'     // "(1)", "(a[1])", "(((1)))", "(2*a[1])"
    ;

INT : '0'..'9'+ ;
ID  : 'a'..'z'+ ;
WS  : (' '|'\n')+ {skip();} ;
