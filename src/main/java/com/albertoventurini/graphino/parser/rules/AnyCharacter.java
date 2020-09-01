package com.albertoventurini.graphino.parser.rules;

import com.albertoventurini.graphino.parser.GrammarContext;
import com.albertoventurini.graphino.parser.ParseTree;

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
