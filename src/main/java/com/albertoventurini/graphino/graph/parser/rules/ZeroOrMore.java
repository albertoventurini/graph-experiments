package com.albertoventurini.graphino.graph.parser.rules;

import com.albertoventurini.graphino.graph.parser.GrammarContext;
import com.albertoventurini.graphino.graph.parser.ParseTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class ZeroOrMore extends Rule {
    private final Rule childRule;

    public ZeroOrMore(final Rule childRule) {
        this.childRule = childRule;
    }

    @Override
    public Optional<ParseTree> tryApply(final GrammarContext ctx) {
        final List<ParseTree> children = new ArrayList<>();

        while (ctx.hasNext()) {
            final Optional<ParseTree> child = childRule.apply(ctx);
            if (child.isEmpty()) {
                break;
            } else {
                children.add(child.get());
            }
        }

        return Optional.of(ParseTree.node(name, children));
    }

    @Override
    public String toString() {
        return "ZeroOrMore{" +
                childRule +
                '}';
    }
}
