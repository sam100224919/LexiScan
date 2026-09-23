# LexiScan

LexiScan is a Java-based programming language interpreter built from the ground up to explore the fundamental components of language implementation.

The project currently implements a complete pipeline from source code to execution:

**Source Code → Lexer → Tokens → Parser → AST → Interpreter → Result**

LexiScan is designed as an educational and experimental language project, with the architecture intended to support additional language features as development continues.

---

## Features

### Lexical Analysis

LexiScan includes a lexer that converts source code into a sequence of tokens.

The lexer currently recognizes language elements such as:

- Identifiers
- Numbers
- Operators
- Parentheses
- Braces
- Semicolons
- Assignment operators
- Comparison operators
- Keywords
- End-of-file markers

Example:

```text
let x = 10;
