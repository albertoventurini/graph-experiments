package com.albertoventurini.graphino.parser.rules;

import com.albertoventurini.graphino.parser.GrammarContext;
import com.albertoventurini.graphino.parser.ParseTree;

import java.util.Arrays;
import java.util.Optional;

public final class OneOf extends Rule {
    private final Rule[] rules;

    public OneOf(final Rule... rules) {
        this.rules = rules;
    }

    @Override
    public Optional<ParseTree> tryApply(final GrammarContext ctx) {
        final int start = ctx.getCursor();

        for (final Rule rule : rules) {
            final Optional<ParseTree> result = rule.apply(ctx);
            if (result.isPresent()) {
                return result;
            }
        }

        ctx.setCursor(start);
        return Optional.empty();
    }

    @Override
    public String toString() {
        return "OneOf{" +
                "rules=" + Arrays.toString(rules) +
                '}';
    }
}
