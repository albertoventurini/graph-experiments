package com.albertoventurini.graphino.graph.parser.rules;

import com.albertoventurini.graphino.graph.parser.GrammarContext;
import com.albertoventurini.graphino.graph.parser.ParseTree;

import java.util.Optional;

public final class AnyCharacter extends Rule {
    @Override
    public Optional<ParseTree> tryApply(final GrammarContext ctx) {
        return Optional.of(ParseTree.leaf(Character.toString(ctx.next())));
    }

    @Override
    public String toString() {
        return "AnyCharacter{}";
    }
}
