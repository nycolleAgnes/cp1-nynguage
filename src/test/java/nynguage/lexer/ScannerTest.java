package nynguage.lexer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScannerTest {

    @Test
    void reconheceIdentificador() {
        Scanner sc = new Scanner("total_1");
        Token t = sc.proximoToken();

        assertEquals(TokenType.ID, t.type);
        assertEquals("total_1", t.lexeme);
    }

    @Test
    void reconhecePalavraReservada() {
        Scanner sc = new Scanner("if");
        Token t = sc.proximoToken();

        assertEquals(TokenType.IF, t.type);
    }

    @Test
    void stringNaoFechadaAteEOF() {
        Scanner sc = new Scanner("\"faltou fechar aspas em algum lugar");
        sc.proximoToken(); // dispara o erro internamente (capturado e recuperado)

        assertEquals(1, sc.getErros().size());
    }

    @Test
    void stringNaoFechadaAteFimDeLinha() {
        Scanner sc = new Scanner("\"abc\ntotal");
        sc.proximoToken(); // string não fecha antes do \n -> erro

        assertEquals(1, sc.getErros().size());
    }
    @Test
    void reconhecerOperador() {
        Scanner sc = new Scanner("&&");
        Token t = sc.proximoToken();

        assertEquals(TokenType.ANDAND, t.type);
        assertEquals("&&", t.lexeme);
    }

    @Test
    void reconhecerLiteraisNumericos() {
        Scanner sc = new Scanner("3.1");
        Token t = sc.proximoToken();

        assertEquals(TokenType.DOUBLE_LIT, t.type);
        assertEquals("3.1", t.lexeme);
    }
    @Test
    void trechoRealista(){

        String codigo =
                "int total = 10; // soma\n" +
                        "/* calcula o dobro */\n" +
                        "total = total * 2;";

        Scanner sc = new Scanner(codigo);

        Token t;

        t = sc.proximoToken();
        assertEquals(TokenType.INT, t.type);

        t = sc.proximoToken();
        assertEquals(TokenType.ID, t.type);
        assertEquals("total", t.lexeme);

        t = sc.proximoToken();
        assertEquals(TokenType.ASSIGN, t.type);

        t = sc.proximoToken();
        assertEquals(TokenType.INT_LIT, t.type);
        assertEquals("10", t.lexeme);

        t = sc.proximoToken();
        assertEquals(TokenType.SEMI, t.type);

        t = sc.proximoToken();
        assertEquals(TokenType.ID, t.type);
        assertEquals("total", t.lexeme);

        t = sc.proximoToken();
        assertEquals(TokenType.ASSIGN, t.type);

        t = sc.proximoToken();
        assertEquals(TokenType.ID, t.type);
        assertEquals("total", t.lexeme);

        t = sc.proximoToken();
        assertEquals(TokenType.MULT, t.type);

        t = sc.proximoToken();
        assertEquals(TokenType.INT_LIT, t.type);
        assertEquals("2", t.lexeme);

        t = sc.proximoToken();
        assertEquals(TokenType.SEMI, t.type);

    }
}