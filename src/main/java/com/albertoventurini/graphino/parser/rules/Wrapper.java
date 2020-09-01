package com.albertoventurini.graphino.parser.rules;

import com.albertoventurini.graphino.parser.GrammarContext;
import com.albertoventurini.graphino.parser.ParseTree;

import java.util.Optional;

public final class Wrapper extends Rule {
    private Rule childRule;

    public Rule getChildRule() {
        return childRule;
    }

    public void setChildRule(final Rule childRule) {
        this.childRule = childRule;
    }

    @Override
    public Optional<ParseTree> tryApply(final GrammarContext ctx) {
        if (childRule != null) {
            return childRule.apply(ctx);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        return "Wrapper{" + childRule.toString() + "}";
    }
}
