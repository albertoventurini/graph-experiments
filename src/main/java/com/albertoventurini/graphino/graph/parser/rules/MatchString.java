package com.albertoventurini.graphino.graph.parser.rules;

import com.albertoventurini.graphino.graph.parser.GrammarContext;
import com.albertoventurini.graphino.graph.parser.ParseTree;

import java.util.Optional;

public final class MatchString extends Rule {
    private final String s;

    public MatchString(final String s) {
        this.s = s;

        discard = true;
    }

    @Override
    public Optional<ParseTree> tryApply(final GrammarContext ctx) {
        ctx.advanceToNextToken();

        if (ctx.matches(s)) {
            ctx.advance(s.length());
            return Optional.of(ParseTree.leaf(s));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        return "MatchString{" +
                "s='" + s + '\'' +
                '}';
    }
}
