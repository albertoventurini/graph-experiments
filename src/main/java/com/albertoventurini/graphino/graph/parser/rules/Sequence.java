package com.albertoventurini.graphino.graph.parser.rules;

import com.albertoventurini.graphino.graph.parser.GrammarContext;
import com.albertoventurini.graphino.graph.parser.ParseTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class Sequence extends Rule {
    private final Rule[] rules;

    public Sequence(final Rule... rules) {
        this.rules = rules;
    }

    @Override
    public Optional<ParseTree> tryApply(final GrammarContext ctx) {
        final int start = ctx.getCursor();
        final List<ParseTree> children = new ArrayList<>(rules.length);

        for (final Rule rule : rules) {
            if (!ctx.hasNext()) {
                ctx.setCursor(start);
                return Optional.empty();
            }

            final Optional<ParseTree> child = rule.apply(ctx);

            if (child.isEmpty()) {
                ctx.setCursor(start);
                return Optional.empty();
            } else if (!rule.discard) {
                children.add(child.get());
            }
        }

        return Optional.of(ParseTree.node(name, children));
    }

    @Override
    public String toString() {
        return "Sequence{" +
                "rules=" + Arrays.toString(rules) +
                '}';
    }
}
