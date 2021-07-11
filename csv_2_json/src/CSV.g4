grammar CSV;

file    : head row+;
head    : row;
row     : field (',' field)* '\r'?'\n';

field
         :TEXT       #text
         |STRING     #string
         |           #empty
        ;

TEXT    :~[,\n\r"]+;
STRING  :'"' ('""'|~'"')*  '"';
