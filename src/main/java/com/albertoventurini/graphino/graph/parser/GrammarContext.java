package com.albertoventurini.graphino.graph.parser;

import com.albertoventurini.graphino.graph.parser.rules.Rule;

public class GrammarContext extends ParseContext {
    private final Rule commentRule;
    private boolean inComment;

    GrammarContext(
            final String string,
            final Rule commentRule) {
        super(string);
        this.commentRule = commentRule;
    }

    public void advanceToNextToken() {
        discardWhitespaces();
        if (!inComment) {
            discardComments();
            discardWhitespaces();
        }
    }

    public boolean isInComment() {
        return inComment;
    }

    public void setInComment(final boolean inComment) {
        this.inComment = inComment;
    }

    private void discardComments() {
        if (hasNext() && commentRule != null) {
            commentRule.apply(this);
        }
    }

    private void discardWhitespaces() {
        while (hasNext()) {
            final char c = peek();
            if (!(Character.isWhitespace(c) || c == '\n')) {
                break;
            }
            advance();
        }
    }
}
