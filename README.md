# LexiScan

LexiScan is a Java interpreter project for a small custom language, implemented as a full front-end + runtime pipeline.

## Project Overview

LexiScan reads source text, tokenizes it, parses it into an Abstract Syntax Tree (AST), and executes that AST.

Architecture:

`Source Code → Lexer → Tokens → Parser → AST → Interpreter`

## Purpose of LexiScan

The project is intended as a language-implementation exercise: it demonstrates how lexical analysis, parsing, AST construction, and tree-walk interpretation fit together in Java.

## Supported Language Features

Based on the current implementation:

- Variables (`let name = expression;`)
- Literals and expressions
  - Number literals (parsed)
  - Boolean literals `true` / `false` (parsed)
  - String literals are tokenized by the lexer, but not currently accepted by `Parser.primary()`
- Arithmetic operators: `+`, `-`, `*`, `/`
- Comparison operators used by the parser: `<`, `>`
- Equality operators: `==`, `!=`
- Boolean logic: `and`, `or`, unary `!`
- `if` / `else` statements
- `while` loops
- Blocks and lexical scoping (`{ ... }` with nested `Environment`)
- Functions (`fun name(params) { ... }`)
- Parameters (comma-separated)
- Function calls (`name(arg1, arg2, ...)`)
- Return statements (`return value;` or `return;`)
- Recursion (supported through function declarations + calls + closures)

## Formal Grammar (Derived from `Parser.java`)

This grammar is intentionally limited to syntax actually parsed by the current `Parser` implementation.

```ebnf
program         ::= declaration* EOF ;

declaration     ::= functionDeclaration
                  | variableDeclaration
                  | statement ;

functionDeclaration
                ::= "fun" IDENTIFIER "(" parameters? ")" "{" blockStatements "}" ;

parameters      ::= IDENTIFIER ( "," IDENTIFIER )* ;

variableDeclaration
                ::= "let" IDENTIFIER "=" expression ";" ;

statement       ::= returnStatement
                  | whileStatement
                  | ifStatement
                  | block
                  | expressionStatement ;

returnStatement ::= "return" expression? ";" ;

whileStatement  ::= "while" "(" expression ")" statement ;

ifStatement     ::= "if" "(" expression ")" statement ( "else" statement )? ;

block           ::= "{" blockStatements "}" ;

blockStatements ::= declaration* ;

expressionStatement
                ::= expression ";" ;

expression      ::= or ;

or              ::= and ( "or" and )* ;

and             ::= equality ( "and" equality )* ;

equality        ::= comparison ( ( "==" | "!=" ) comparison )* ;

comparison      ::= addition ( ( ">" | "<" ) addition )* ;

addition        ::= multiplication ( ( "+" | "-" ) multiplication )* ;

multiplication  ::= unary ( ( "*" | "/" ) unary )* ;

unary           ::= "!" unary
                  | "-" unary
                  | call ;

call            ::= primary ( "(" arguments? ")" )* ;

arguments       ::= expression ( "," expression )* ;

primary         ::= "true"
                  | "false"
                  | NUMBER
                  | IDENTIFIER
                  | "(" expression ")" ;
```

## How the Pipeline Works

### 1) Lexical Analysis (`Lexer`)

- Scans raw source one character at a time.
- Produces `Token` objects containing token type, lexeme, literal, line, and column.
- Recognizes keywords (`let`, `fun`, `if`, `else`, `while`, `return`, `and`, `or`, `true`, `false`, etc.), operators, punctuation, identifiers, and numbers.

### 2) Parsing (`Parser`)

- Consumes the token list and builds a list of AST statements (`List<Stmt>`).
- Uses recursive descent with explicit precedence levels:
  - `or` → `and` → equality → comparison → addition → multiplication → unary → call → primary.
- Parses declarations (`fun`, `let`) and statements (`if`, `while`, `return`, block, expression statement).

### 3) AST Construction (`lexiscan.ast`)

- The parser creates typed AST nodes such as:
  - Statements: `VarStmt`, `FunctionStmt`, `IfStmt`, `WhileStmt`, `BlockStmt`, `ReturnStmt`, `ExprStmt`
  - Expressions: `BinaryExpr`, `UnaryExpr`, `CallExpr`, `VariableExpr`, `LiteralExpr`
- This tree structure represents the program in executable semantic form.

### 4) Interpretation (`Interpreter`)

- Walks AST nodes and evaluates/executes them.
- Stores variable/function bindings in chained `Environment` scopes.
- Functions are represented by `LexiFunction` with captured closure environments.
- `return` exits function execution using `ReturnSignal`.
- Supports recursion naturally through function calls.

## Example LexiScan Program

```lexiscan
fun factorial(n) {
    if (n < 2) {
        return 1;
    }

    return n * factorial(n - 1);
}

let input = 5;
factorial(input);
```

This example demonstrates:

- Variable declaration (`let input = 5;`)
- Function declaration (`fun factorial(n) { ... }`)
- Function call (`factorial(input);`)
- Condition (`if (n < 2)`) and recursion (`factorial(n - 1)`)
- Return statements

## Build and Run (Maven)

From project root:

```powershell
mvn clean compile
mvn exec:java -Dexec.mainClass="lexiscan.Main"
```

If `exec-maven-plugin` is not configured in your local setup, you can also run `lexiscan.Main` directly from your IDE after `mvn compile`.

## Testing Instructions

Run all tests:

```powershell
mvn test
```

Run a specific test class:

```powershell
mvn -Dtest=lexiscan.InterpreterTest test
mvn -Dtest=lexiscan.LexerTest test
```

## Current Test Status

- Latest available Surefire reports (`target/surefire-reports`) show:
  - `lexiscan.InterpreterTest`: 8 tests, 0 failures, 0 errors, 0 skipped
  - `lexiscan.LexerTest`: 1 test, 0 failures, 0 errors, 0 skipped
- Total from latest available reports: 9 passed, 0 failed, 0 errors, 0 skipped.
- To re-verify on your machine after any change, run `mvn test` from project root.
