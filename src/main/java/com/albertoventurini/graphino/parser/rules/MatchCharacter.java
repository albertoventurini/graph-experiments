package com.albertoventurini.graphino.parser.rules;

import com.albertoventurini.graphino.parser.GrammarContext;
import com.albertoventurini.graphino.parser.ParseTree;

import java.util.Optional;

public final class MatchCharacter extends Rule {
    private final char c;

    public MatchCharacter(final char c) {
        this.c = c;

        discard = true;
    }

    @Override
    public Optional<ParseTree> tryApply(final GrammarContext ctx) {
        ctx.advanceToNextToken();

        if (ctx.peek() == c) {
            return Optional.of(ParseTree.leaf(Character.toString(ctx.next())));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        return "Character{" +
                "c=" + c +
                '}';
    }
}
