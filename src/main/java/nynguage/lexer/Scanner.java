package nynguage.lexer;
import java.util.ArrayList;
import java.util.List;
public class Scanner {
    private final String source;
    private int pos = 0;
    private int line = 1;
    private int col = 1;

    public Scanner(String source) {
        this.source = source;
    }

    public boolean hasNext() {
        return pos < source.length();
    }

    public char peek() {
        if (!hasNext()) return '\0';
        return source.charAt(pos);
    }

    public char advance() {
        char c = source.charAt(pos);
        pos++;
        if (c == '\n') {
            line++;
            col = 1;
        } else {
            col++;
        }
        return c;
    }

    public int getLine() {
        return line;
    }

    public int getCol() {
        return col;
    }

    public void pularEspacosEComentarios() {
        while (hasNext()) {
            char c = peek();

            // Espaços, tabs, quebras de linha, retorno
            if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
                advance();
                continue;
            }

            // Comentário de linha ou fim de arquivo
            if (c == '/' && pos + 1 < source.length() && source.charAt(pos + 1) == '/') {
                advance(); // consome o primeiro '/'
                advance(); // consome o segundo '/'
                while (hasNext() && peek() != '\n') {
                    advance();
                }
                continue;
            }

            // Comentário de bloco: /* ... */ (sem aninhamento)
            if (c == '/' && pos + 1 < source.length() && source.charAt(pos + 1) == '*') {
                advance(); // consome '/'
                advance(); // consome '*'
                boolean fechou = false;
                while (hasNext()) {
                    if (peek() == '*' && pos + 1 < source.length() && source.charAt(pos + 1) == '/') {
                        advance(); // consome '*'
                        advance(); // consome '/'
                        fechou = true;
                        break;
                    }
                    advance();
                }
                if (!fechou) {
                    // Fim do arquivo no meio do comentário de bloco — erro léxico
                    throw new LexicalError("comentário de bloco não fechado", line, col);
                }
                continue;
            }

            // Se não é espaço nem início de comentário, então para
            break;
        }
    }

    public Token proximoToken() {
        while (true) {
            //int posAntes = pos; // marca a posição ANTES de tentar
            pularEspacosEComentarios(); // pula ANTES de marcar a posição

            if (!hasNext()) {
                return new Token(TokenType.EOF, "", line, col);
            }

            int posAntes = pos;

            try {
                return escanearToken();
            } catch (LexicalError erro) {
                erros.add(erro); // registra o erro

                // Garante que o cursor avançou pelo menos 1 caractere,
                // para não entrar em loop infinito tentando o mesmo ponto de novo
                if (pos == posAntes && hasNext()) {
                    advance();
                }
                // volta ao topo do while e tenta escanear o PRÓXIMO token
            }
        }
    }

    // Com base no exemplo dado pelo professor em sala de aula
    public Token escanearToken() {
        pularEspacosEComentarios();

       /* if (!hasNext()) {
            return new Token(TokenType.EOF, "", line, col);
        }*/

        char c = peek();

        if (Character.isLetter(c)) {
            return scanIdentifierOrKeyword();
        }
        if (Character.isDigit(c)) {
            return scanNumber();
        }
        if (c == '"') {
            return scanString();
        }
        if (isPunctuationOrOperatorStart(c)) {
            return scanOperatorOrPunctuation();
        }

        throw new LexicalError("caractere inesperado: " + c, line, col);
    }

    private Token scanIdentifierOrKeyword() {
        int startLine = line, startCol = col;
        int start = pos;

        while (hasNext() && (Character.isLetterOrDigit(peek()) || peek() == '_')) {
            advance();
        }

        String lexeme = source.substring(start, pos);
        TokenType tipo = buscarPalavraReservada(lexeme);
        return new Token(tipo, lexeme, startLine, startCol);
    }

    private Token scanNumber() {
        int startLine = line, startCol = col;
        int start = pos;

        while (hasNext() && Character.isDigit(peek())) {
            advance();
        }

        boolean isDouble = false;

        if (hasNext() && peek() == '.' && pos + 1 < source.length()
                && Character.isDigit(source.charAt(pos + 1))) {
            isDouble = true;
            advance(); // consome o '.'
            while (hasNext() && Character.isDigit(peek())) {
                advance();
            }
        }

        String lexeme = source.substring(start, pos);
        TokenType tipo;
        if (isDouble) {
           tipo= TokenType.DOUBLE_LIT;
        }
        else {
            tipo = TokenType.INT_LIT;
        }
        return new Token(tipo, lexeme, startLine, startCol);
    }

    private Token scanString() {
        int startLine = line, startCol = col;

        advance(); // consome a aspa de abertura
        int start = pos; // início do CONTEÚDO, depois da aspa

        while (hasNext() && peek() != '"' && peek() != '\n') {
            advance(); // auto-laço: consome qualquer char
        }

        if (!hasNext() || peek() == '\n') {
            // string não fechada ou fim de linha — erro léxico
            throw new LexicalError("string não fechada", startLine, startCol);
        }

        String lexeme = source.substring(start, pos); // conteúdo sem as aspas
        advance(); // consome a aspa de fechamento

        return new Token(TokenType.STRING_LIT, lexeme, startLine, startCol);
    }

    private Token scanOperatorOrPunctuation() {
        int startLine = line, startCol = col;
        char c = advance(); // já consome o primeiro caractere

        switch (c) {
            case '=':
                if (hasNext() && peek() == '=') {
                    advance();
                    return new Token(TokenType.EQ, "==", startLine, startCol);
                }
                return new Token(TokenType.ASSIGN, "=", startLine, startCol);

            case '<':
                if (hasNext() && peek() == '=') {
                    advance();
                    return new Token(TokenType.LE, "<=", startLine, startCol);
                }
                return new Token(TokenType.LT, "<", startLine, startCol);

            case '>':
                if (hasNext() && peek() == '=') {
                    advance();
                    return new Token(TokenType.GE, ">=", startLine, startCol);
                }
                return new Token(TokenType.GT, ">", startLine, startCol);

            case '!':
                if (hasNext() && peek() == '=') {
                    advance();
                    return new Token(TokenType.NOTASSIGN, "!=", startLine, startCol);
                }
                return new Token(TokenType.NOT, "!", startLine, startCol);

            case '&':
                if (hasNext() && peek() == '&') {
                    advance();
                    return new Token(TokenType.ANDAND, "&&", startLine, startCol);
                }
                throw new LexicalError("caractere inesperado: &", startLine, startCol);

            case '|':
                if (hasNext() && peek() == '|') {
                    advance();
                    return new Token(TokenType.OROR, "||", startLine, startCol);
                }
                throw new LexicalError("caractere inesperado: |", startLine, startCol);

            case '+':
                return new Token(TokenType.PLUS, "+", startLine, startCol);
            case '-':
                return new Token(TokenType.MINUS, "-", startLine, startCol);
            case '*':
                return new Token(TokenType.MULT, "*", startLine, startCol);
            case '/':
                return new Token(TokenType.DIV, "/", startLine, startCol);
            case '%':
                return new Token(TokenType.PERC, "%", startLine, startCol);

            case ';':
                return new Token(TokenType.SEMI, ";", startLine, startCol);
            case ',':
                return new Token(TokenType.COMMA, ",", startLine, startCol);
            case '(':
                return new Token(TokenType.LPAREN, "(", startLine, startCol);
            case ')':
                return new Token(TokenType.RPAREN, ")", startLine, startCol);
            case '{':
                return new Token(TokenType.LBRACE, "{", startLine, startCol);
            case '}':
                return new Token(TokenType.RBRACE, "}", startLine, startCol);
            case '[':
                return new Token(TokenType.LBRACKET, "[", startLine, startCol);
            case ']':
                return new Token(TokenType.RBRACKET, "]", startLine, startCol);

            default:
                throw new LexicalError("caractere inesperado: " + c, startLine, startCol);
        }
    }

    private boolean isPunctuationOrOperatorStart(char c) {
        return "=<>!&|+-*/%;,(){}[]".indexOf(c) != -1;
    }

    private TokenType buscarPalavraReservada(String lexeme) {
        String normalizado = lexeme.toLowerCase();

        switch (normalizado) {
            case "if": return TokenType.IF;
            case "else": return TokenType.ELSE;
            case "while": return TokenType.WHILE;
            case "for": return TokenType.FOR;
            case "case": return TokenType.CASE;
            case "switch": return TokenType.SWITCH;
            case "break": return TokenType.BREAK;
            case "default": return TokenType.DEFAULT;
            case "void": return TokenType.VOID;
            case "return": return TokenType.RETURN;
            case "func": return TokenType.FUNC;
            case "true": return TokenType.TRUE;
            case "false": return TokenType.FALSE;
            case "int": return TokenType.INT;
            case "double": return TokenType.DOUBLE;
            case "float": return TokenType.FLOAT;
            case "bool": return TokenType.BOOL;
            case "char": return TokenType.CHAR;
            case "string": return TokenType.STRING;
            case "do": return TokenType.DO;
            case "null": return TokenType.NULL;
            default: return TokenType.ID; // não é palavra reservada - é identificador comum
        }
    }
    private final List<LexicalError> erros = new ArrayList<>();

    public List<LexicalError> getErros() {
        return erros;
    }
}
