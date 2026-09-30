package nynguage.lexer;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner("\"abc\ntotal");
        Token t;
        do {
            t = sc.proximoToken();
            System.out.println(t);
        } while (t.type != TokenType.EOF);

        System.out.println("Erros encontrados: " + sc.getErros().size());
        for (LexicalError e : sc.getErros()) {
            System.out.println("  -> " + e.getMessage() + " na linha: " + e.line + " coluna: " + e.col);
        }
    }

}
