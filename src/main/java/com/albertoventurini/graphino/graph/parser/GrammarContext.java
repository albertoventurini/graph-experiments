package com.albertoventurini.graphino.graph.parser;

class GrammarContext extends ParseContext {
    private final Rule commentRule;
    private boolean inComment;

    GrammarContext(
            final String string,
            final Rule commentRule) {
        super(string);
        this.commentRule = commentRule;
    }

    void advanceToNextToken() {
        discardWhitespaces();
        if (!inComment) {
            discardComments();
            discardWhitespaces();
        }
    }

    boolean isInComment() {
        return inComment;
    }

    void setInComment(final boolean inComment) {
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