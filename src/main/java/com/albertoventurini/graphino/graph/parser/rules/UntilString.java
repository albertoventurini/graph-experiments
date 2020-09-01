package com.albertoventurini.graphino.graph.parser.rules;

import com.albertoventurini.graphino.graph.parser.GrammarContext;
import com.albertoventurini.graphino.graph.parser.ParseTree;

import java.util.Arrays;
import java.util.Optional;

public final class UntilString extends Rule {
    private final char[] charArr;

    public UntilString(final String s) {
        this.charArr = s.toCharArray();
    }

    @Override
    public Optional<ParseTree> tryApply(final GrammarContext ctx) {
        ctx.advanceToNextToken();

        final int start = ctx.getCursor();

        int matched = 0;
        while (ctx.hasNext() && matched < charArr.length) {
            if (ctx.next() == charArr[matched]) {
                matched++;
            } else {
                matched = 0;
            }
        }

        if (!ctx.hasNext()) {
            return Optional.of(ParseTree.leaf(ctx.substring(start)));
        } else {
            return Optional.of(ParseTree.leaf(ctx.substring(start, ctx.getCursor())));
        }
    }

    @Override
    public String toString() {
        return "UntilString{" +
                "name='" + name + '\'' +
                ", charArr=" + Arrays.toString(charArr) +
                '}';
    }
}
