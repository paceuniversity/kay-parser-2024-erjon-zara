package com.scanner.project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TokenStream {

    private BufferedReader input;
    private int currentChar;

    public TokenStream(String filename) {
        try {
            input = new BufferedReader(new FileReader(filename));
            advance();
        } catch (IOException e) {
            throw new RuntimeException("Cannot open file: " + filename);
        }
    }

    private void advance() {
        try {
            currentChar = input.read();
        } catch (IOException e) {
            currentChar = -1;
        }
    }

    private void skipWhitespace() {
        while (currentChar != -1 && Character.isWhitespace(currentChar)) {
            advance();
        }
    }

    public Token nextToken() {

        skipWhitespace();

        if (currentChar == '/') {
            advance();
            if (currentChar == '/') {
                while (currentChar != -1 && currentChar != '\n' && currentChar != '\r') {
                    advance();
                }
                return nextToken();
            } else if (currentChar == '*') {
                advance();
                while (currentChar != -1) {
                    if (currentChar == '*') {
                        advance();
                        if (currentChar == '/') {
                            advance();
                            break;
                        }
                    } else {
                        advance();
                    }
                }
                return nextToken();
            } else {
                return new Token("Operator", "/");
            }
        }

        if (currentChar == -1) {
            return new Token("EOF", "EOF");
        }

        char ch = (char) currentChar;

        if (Character.isLetter(ch)) {
            StringBuilder sb = new StringBuilder();

            while (currentChar != -1 &&
                    (Character.isLetterOrDigit(currentChar) || currentChar == '_')) {
                sb.append((char) currentChar);
                advance();
            }

            String lexeme = sb.toString();

            if (lexeme.equals("True") || lexeme.equals("False")) {
                return new Token("Literal", lexeme);
            }

            if (lexeme.equals("main") ||
                lexeme.equals("integer") ||
                lexeme.equals("bool") ||
                lexeme.equals("if") ||
                lexeme.equals("else") ||
                lexeme.equals("while")) {
                return new Token("Keyword", lexeme);
            }

            return new Token("Identifier", lexeme);
        }

        if (Character.isDigit(ch)) {
            StringBuilder sb = new StringBuilder();

            while (currentChar != -1 && Character.isDigit(currentChar)) {
                sb.append((char) currentChar);
                advance();
            }

            if (currentChar != -1 &&
                (Character.isLetter(currentChar) || currentChar == '_')) {
                while (currentChar != -1 &&
                        (Character.isLetterOrDigit(currentChar) || currentChar == '_')) {
                    sb.append((char) currentChar);
                    advance();
                }
                return new Token("Other", sb.toString());
            }

            return new Token("Literal", sb.toString());
        }

        if (ch == ':') {
            advance();
            if (currentChar == '=') {
                advance();
                return new Token("Operator", ":=");
            } else {
                return new Token("Other", ":");
            }
        }

        if (ch == '&') {
            advance();
            if (currentChar == '&') {
                advance();
                return new Token("Operator", "&&");
            }
            return new Token("Other", "&");
        }

        if (ch == '|') {
            advance();
            if (currentChar == '|') {
                advance();
                return new Token("Operator", "||");
            }
            return new Token("Other", "|");
        }

        if (ch == '!') {
            advance();
            if (currentChar == '=') {
                advance();
                return new Token("Operator", "!=");
            }
            return new Token("Operator", "!");
        }

        if (ch == '<') {
            advance();
            if (currentChar == '=') {
                advance();
                return new Token("Operator", "<=");
            } else {
                return new Token("Operator", "<");
            }
        }

        if (ch == '>') {
            advance();
            if (currentChar == '=') {
                advance();
                return new Token("Operator", ">=");
            } else {
                return new Token("Operator", ">");
            }
        }

        if (ch == '=') {
            advance();
            if (currentChar == '=') {
                advance();
                return new Token("Operator", "==");
            } else {
                return new Token("Other", "=");
            }
        }

        if (ch == '+' || ch == '-' || ch == '*') {
            advance();
            return new Token("Operator", Character.toString(ch));
        }

        if (ch == '%') {
            advance();
            return new Token("Operator", "%");
        }

        if ("(){};,".indexOf(ch) >= 0) {
            advance();
            return new Token("Separator", Character.toString(ch));
        }

        advance();
        return new Token("Other", Character.toString(ch));
    }
}
