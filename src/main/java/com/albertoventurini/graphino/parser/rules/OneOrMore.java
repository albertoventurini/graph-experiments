package com.albertoventurini.graphino.parser.rules;

import com.albertoventurini.graphino.parser.GrammarContext;
import com.albertoventurini.graphino.parser.ParseTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OneOrMore extends Rule {
    private final Rule childRule;

    public OneOrMore(final Rule childRule) {
        this.childRule = childRule;
    }

    @Override
    public Optional<ParseTree> tryApply(final GrammarContext ctx) {
        final List<ParseTree> children = new ArrayList<>();

        final Optional<ParseTree> firstChild = childRule.apply(ctx);
        if (firstChild.isEmpty()) {
            return Optional.empty();
        }

        children.add(firstChild.get());

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
        return "OneOrMore{" +
                childRule +
                '}';
    }
}
