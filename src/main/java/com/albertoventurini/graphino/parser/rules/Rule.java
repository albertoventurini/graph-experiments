package com.albertoventurini.graphino.parser.rules;

import com.albertoventurini.graphino.parser.GrammarContext;
import com.albertoventurini.graphino.parser.ParseTree;

import java.util.Optional;

public abstract class Rule {
    protected String name;

    public boolean isComment = false;

    protected boolean discard = false;

    public Rule as(final String name) {
        this.name = name;
        return this;
    }

    public Rule discard() {
        discard = true;
        return this;
    }

    public Rule discard(final boolean discard) {
        this.discard = discard;
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
