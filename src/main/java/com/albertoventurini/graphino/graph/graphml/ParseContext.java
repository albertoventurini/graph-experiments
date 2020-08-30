package com.albertoventurini.graphino.graph.graphml;

import java.util.Arrays;

class ParseContext {
    private final char[] charArr;
    private int cursor;

    ParseContext(final String string) {
        charArr = string.toCharArray();
    }

    char next() {
        char c = peek();
        cursor++;
        return c;
    }

    char peek() {
        if (cursor >= charArr.length) {
            throw new ParseException("No more characters to parse");
        }
        return charArr[cursor];
    }

    boolean hasNext() {
        return cursor < charArr.length;
    }

    int getCursor() {
        return cursor;
    }

    void advance() {
        cursor++;
    }

    void consume(final char c) {
        final char n = next();
        if (n != c) {
            throw new ParseException("Expected character " + c + ", instead found " + n);
        }
    }

    String substring(final int start, final int end) {
        return String.valueOf(Arrays.copyOfRange(charArr, start, end));
    }

    String substring(final int start) {
        return substring(start, cursor);
    }

    boolean matches(final String s) {
        if (cursor + s.length() > charArr.length) {
            return false;
        }

        for (int i = 0; i < s.length(); i++) {
            if (charArr[cursor + i] != s.charAt(i)) {
                return false;
            }
        }

        return true;
    }
}
