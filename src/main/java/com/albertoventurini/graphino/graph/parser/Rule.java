package com.albertoventurini.graphino.graph.parser;

import java.util.Optional;

public abstract class Rule {
    protected String name;

    public boolean isComment = false;

    public Rule named(final String name) {
        this.name = name;
        return this;
    }

    public Optional<ParseTree> apply(final GrammarContext ctx) {
        final boolean inComment = ctx.isInComment();

        try {
            if (!inComment && isComment) {
                ctx.setInComment(true);
            }
            return tryApply(ctx);
        } finally {
            ctx.setInComment(inComment);
        }
    }

    public abstract Optional<ParseTree> tryApply(final GrammarContext ctx);

    public void setComment(final boolean comment) {
        isComment = comment;
    }
}
