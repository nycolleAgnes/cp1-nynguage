package nynguage.lexer;

public enum TokenType {
    // Categorias gerais
    ID, STRING_LIT,  INT_LIT, DOUBLE_LIT,

    // Palavras reservadas
    IF, ELSE, WHILE, INT, DOUBLE, RETURN, FLOAT, FOR, CASE, SWITCH,
    BREAK, DEFAULT, VOID, FUNC, TRUE, FALSE, BOOL, CHAR, STRING, DO, NULL,

    // Operadores
    ASSIGN,     // = CERTO
    EQ,         // == CERTO
    PLUS,       // + CERTO
    MINUS,       // - CERTO
    MULT,       // * CERTO
    DIV,        // / CERTO
    PERC,       // % CERTO
    ANDAND,     // && CERTO
    OROR,       // || CERTO
    LE,         // <= CERTO
    GE,         // >= CERTO
    GT,        // > CERTO
    LT,        // < CERTO
    NOT,        // ! CERTO
    NOTASSIGN,  // != CERTO

    // Pontuações

    SEMI,          // ; CERTO
    COMMA,         // , CERTO
    LPAREN,        // ( CERTO
    RPAREN,        // )
    LBRACE,        // {
    RBRACE,        // }
    LBRACKET,      // [
    RBRACKET,      // ]

    // Utilitário
    EOF,     // fim de arquivo
    ERROR,   // Erro
}