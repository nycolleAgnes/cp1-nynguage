# Especificação Lexica - Nynguage

## Lista de Tokens

## Identificadores:
### Notação (regex/EBNF) :
    letra (letra|digito|"_")*
### Exemplos Válidos:
    produto, Produto, maior, Menor, p1, P1, 
    estoque_produtos, total_final
### Decisões:
    1-Não é Case-sensitive;
    2-UbderLine é permitido;
    3-Não tem tamanho máximo;

## Palavras Reservadas:
### Notação (regex/EBNF) :
    letra (letra|digito|"_")* busca em tabela
### Exemplos Válidos:
    if, else, while, for, case, switch, break, default,
    void, return, func, true, false, int, double, 
    float, bool, char, string, do, null.
### Decisões:
    1-Lista fechada acima.

## Strings:
### Notação (regex/EBNF):
    " (qualquer caractere ≠ ") * "
### Exemplos Válidos:
    "tabela", "linha 1", "ok".
### Decisões:
    1-Sem escapes (\");
    2-String não fechada até fim de linha ou EOF é erro léxico;
    3-Não permite string multilinha;

## Operadores:
### Notação (regex/EBNF):
    1 ou 2 caracteres.
### Exemplos Válidos:
    =, ==, <=, >=, <, >, !, !=, +, -, *, /,  %, &&, ||,
### Decisões:
    1-Desambiguação por maximal munch com 1 caractere de lookahead (`peek`); 
    2-Formas compostas: ==, <=, >=, !=, &&, ||.

## Literais Numéricos:
### Notação (regex/EBNF):
    dígito+ ( "." dígito+ )?
### Exemplos Válidos:
    20, 300, 3.14, 7.13333
### Decisões:
    1-Não terá notação científica; 
    2- Não terá hexadecimal;

## Respondendo As 5 Perguntas

### 1 - Alfabeto de entrada (Σ)
    O alfabeto de entrada é composto por:
    - Letras minúsculas e maiúsculas: `a-z`, `A-Z`
    - Dígitos: `0-9`
    - Underline: `_`
    - Espaços em branco: espaço, tab (`\t`), quebra de linha (`\n`), retorno de carro (`\r`)
    - Símbolos de pontuação e operadores: `= < > ! + - * / % ( ) { } [ ] ; , " &, |`
    - Qualquer caractere imprimível dentro de literais de string (exceto `"`)
    - Qualquer caractere fora desse conjunto (ex.: `@`, `#`, `$`, emojis, caracteres de controle) 
      é considerado inválido e gera erro léxico.

### 2. Não é Case-sensitivity
    - A linguagem não é case-sensitive;
    - total e Total são os mesmos identificadores. 
    - Palavras reservadas seguem a mesma regra: if, IF, If são palavras-chave.

### 3. Espaços em branco e comentários
    - Espaço, tab e quebra de linha são delimitadores e não geram token — o scanner os ignora e continua.
    - Comentário de linha: inicia em // e vai até o fim da linha (\n ou EOF);
    - Comentário de bloco: inicia em /* e termina no primeiro */ encontrado;
    - Comentários de bloco NÃO podem ser aninhados;
    - Um comentário de bloco não fechado até o EOF é erro léxico;

### 4. Maximal munch
    - A regra de desambiguação é "maximal munch": o scanner sempre tenta consumir o prefixo 
      válido mais longo antes de decidir o token. 

### 5. Palavras reservadas
    if, else, while, for, case, switch, break, default,
    void, return, func, true, false, int, double, 
    float, bool, char, string, do, null.