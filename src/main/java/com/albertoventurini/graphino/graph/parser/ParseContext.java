package com.albertoventurini.graphino.graph.parser;

import com.albertoventurini.graphino.graph.graphml.ParseException;

import java.util.Arrays;

public class ParseContext {
    private final char[] charArr;
    private int cursor;

    public ParseContext(final String string) {
        charArr = string.toCharArray();
    }

    public char next() {
        char c = peek();
        cursor++;
        return c;
    }

    public char peek() {
        if (cursor >= charArr.length) {
            throw new ParseException("No more characters to parse");
        }
        return charArr[cursor];
    }

    public boolean hasNext() {
        return cursor < charArr.length;
    }

    public int getCursor() {
        return cursor;
    }

    public void advance() {
        advance(1);
    }

    public void advance(final int i) {
        cursor += i;
    }

    public void setCursor(final int cursor) {
        this.cursor = cursor;
    }

    public void consume(final char c) {
        final char n = next();
        if (n != c) {
            throw new ParseException("Expected character " + c + ", instead found " + n);
        }
    }

    public String substring(final int start, final int end) {
        return String.valueOf(Arrays.copyOfRange(charArr, start, end));
    }

    public String substring(final int start) {
        return substring(start, cursor);
    }

    public String substring() {
        return String.valueOf(Arrays.copyOfRange(charArr, cursor, charArr.length));
    }

    public boolean matches(final String s) {
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
