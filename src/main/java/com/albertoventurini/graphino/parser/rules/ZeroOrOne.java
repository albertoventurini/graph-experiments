package com.albertoventurini.graphino.parser.rules;

import com.albertoventurini.graphino.parser.GrammarContext;
import com.albertoventurini.graphino.parser.ParseTree;

import java.util.Optional;

public final class ZeroOrOne extends Rule {
    private final Rule childRule;

    public ZeroOrOne(final Rule childRule) {
        this.childRule = childRule;
    }

    @Override
    public Optional<ParseTree> tryApply(final GrammarContext ctx) {
        final Optional<ParseTree> child = childRule.apply(ctx);
        if (child.isPresent()) {
            return child;
        } else {
            return Optional.of(ParseTree.leaf(""));
        }
    }

    @Override
    public String toString() {
        return "ZeroOrOne{" +
                childRule +
                '}';
    }
}
