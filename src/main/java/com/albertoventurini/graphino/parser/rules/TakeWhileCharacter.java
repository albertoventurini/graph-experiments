package com.albertoventurini.graphino.parser.rules;

import com.albertoventurini.graphino.parser.GrammarContext;
import com.albertoventurini.graphino.parser.ParseTree;

import java.util.Optional;
import java.util.function.Predicate;

public final class TakeWhileCharacter extends Rule {
    private final Predicate<Character> characterPredicate;

    private static final Predicate<Character> DEFAULT_PREDICATE = (c) -> !Character.isWhitespace(c);

    public TakeWhileCharacter() {
        this(DEFAULT_PREDICATE);
    }

    public TakeWhileCharacter(final Predicate<Character> characterPredicate) {
        this.characterPredicate = characterPredicate;
    }

    @Override
    public Optional<ParseTree> tryApply(final GrammarContext ctx) {
        ctx.advanceToNextToken();

        final int start = ctx.getCursor();

        while (ctx.hasNext()) {
            if (!characterPredicate.test(ctx.peek())) {
                break;
            }
            ctx.advance();
        }

        if (ctx.getCursor() > start) {
            return Optional.of(ParseTree.leaf(ctx.substring(start)));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        return "Token{" +
                "characterPredicate=" + characterPredicate +
                '}';
    }
}
