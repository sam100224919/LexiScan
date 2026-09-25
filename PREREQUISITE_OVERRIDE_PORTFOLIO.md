# LexiScan — CMSC 330 Prerequisite Override Portfolio

## 1. Purpose

LexiScan is a Java-based programming-language implementation project developed to demonstrate my existing preparation in concepts relevant to CMSC 330.

The project implements a lexer, parser, abstract syntax tree (AST), interpreter, lexical scoping, functions, recursion, control flow, and automated testing.

This portfolio documents the technical work and evidence contained in the project for consideration as part of a CMSC 330 prerequisite override request.

---

## 2. Project Overview

**Project:** LexiScan  
**Language:** Java  
**Build system:** Maven  
**Testing:** JUnit  
**Version control:** Git / GitHub

LexiScan processes source code through the following pipeline:

Source Code
→ Lexer
→ Tokens
→ Parser
→ AST
→ Interpreter
→ Execution

---

## 3. Programming Language Implementation

### Lexical Analysis

Implemented in:

- `Lexer.java`
- `Token.java`
- `TokenType.java`

The lexer converts source code into tokens including keywords, identifiers, literals, operators, and punctuation.

### Parsing

Implemented in:

- `Parser.java`

The parser consumes tokens and constructs an abstract syntax tree representing the program.

### Abstract Syntax Tree

Implemented through AST classes including:

- `Expr.java`
- `BinaryExpr.java`
- `LiteralExpr.java`
- `VariableExpr.java`
- `CallExpr.java`
- `Stmt.java`
- `VarStmt.java`
- `BlockStmt.java`
- `IfStmt.java`
- `WhileStmt.java`
- `FunctionStmt.java`
- `ReturnStmt.java`

### Interpretation and Semantics

Implemented in:

- `Interpreter.java`

The interpreter evaluates expressions and executes statements represented by the AST.

---

## 4. Scope Management

LexiScan implements lexical scope through:

- `Environment.java`
- `Interpreter.java`
- `LexiFunction.java`

Nested environments allow variables to be resolved according to their lexical scope.

Functions retain their declaration environment, providing lexical closure behavior.

---

## 5. Functions and Recursion

LexiScan supports:

- Function declarations
- Parameters
- Arguments
- Function calls
- Return statements
- Return values
- Lexical closures
- Recursive function calls

Relevant classes include:

- `LexiCallable.java`
- `LexiFunction.java`
- `FunctionStmt.java`
- `CallExpr.java`
- `ReturnStmt.java`
- `ReturnSignal.java`

---

## 6. Control Flow

The language supports:

- `if`
- `else`
- `while`
- Blocks
- `return`

These features require coordination between the lexer, parser, AST, interpreter, and environment system.

---

## 7. Formal Grammar

A formal grammar describing the implemented LexiScan language is documented in `README.md`.

The grammar was derived from the actual parser implementation so that the documented syntax corresponds to the implemented language.

---

## 8. Testing

LexiScan uses JUnit automated testing.

Test files present in the repository:

- `src/test/java/lexiscan/InterpreterTest.java`
- `src/test/java/lexiscan/LexerTest.java`

Repository test artifacts currently available in `target/surefire-reports` show the latest recorded Maven test run as:

- `lexiscan.InterpreterTest`: 8 tests, 0 failures, 0 errors, 0 skipped
- `lexiscan.LexerTest`: 1 test, 0 failures, 0 errors, 0 skipped

Total from those recorded reports: **9 passed / 9 total**.

The current test source files include broader coverage of expressions, booleans, control flow, functions, scope behavior, recursion, and lexing. A fresh full Maven execution should be used to confirm current totals in a specific environment.

---

## 9. Software Engineering Evidence

The project demonstrates experience with:

- Java
- Object-oriented programming
- Maven
- JUnit
- Git
- GitHub
- Automated testing
- Debugging
- Incremental development
- Technical documentation

The project has been developed through multiple implementation stages, with functionality tested after changes.

---

## 10. CMSC 330 Relevance

The current CMSC 330 course description includes topics such as:

- Syntax and semantics
- Programming paradigms
- Lexical analysis
- Parsing
- Regular expressions
- Grammars
- Scope management
- Compiler principles

LexiScan provides practical experience directly related to several of these areas.

Specifically:

| CMSC 330 Concept | LexiScan Evidence |
|---|---|
| Syntax | Parser and formal grammar |
| Semantics | Interpreter |
| Lexical analysis | Lexer |
| Parsing | Parser |
| Grammars | Formal grammar in README |
| Scope management | `Environment` and lexical closures |
| Interpreter/compiler concepts | Lexer → Parser → AST → Interpreter architecture |

Regular expressions and other CMSC 330 topics are recognized as course concepts; this project is not presented as implementing every topic in the course.

---

## 11. Evidence Available for Review

The project provides:

- Source code
- AST implementation
- Lexer implementation
- Parser implementation
- Interpreter implementation
- Scope/environment implementation
- Function and recursion implementation
- Automated tests
- Formal grammar
- README documentation
- Git commit history
- GitHub repository

---

## 12. Conclusion

LexiScan demonstrates practical experience implementing a programming language from source-code tokenization through parsing, AST construction, semantic interpretation, scope management, functions, and execution.

The project is submitted as supporting technical evidence for consideration of a CMSC 330 prerequisite override.