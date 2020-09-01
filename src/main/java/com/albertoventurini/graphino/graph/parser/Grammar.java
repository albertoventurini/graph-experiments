package com.albertoventurini.graphino.graph.parser;

import com.albertoventurini.graphino.graph.parser.rules.AnyCharacter;
import com.albertoventurini.graphino.graph.parser.rules.MatchCharacter;
import com.albertoventurini.graphino.graph.parser.rules.MatchString;
import com.albertoventurini.graphino.graph.parser.rules.OneOf;
import com.albertoventurini.graphino.graph.parser.rules.OneOrMore;
import com.albertoventurini.graphino.graph.parser.rules.Rule;
import com.albertoventurini.graphino.graph.parser.rules.Sequence;
import com.albertoventurini.graphino.graph.parser.rules.TakeWhileCharacter;
import com.albertoventurini.graphino.graph.parser.rules.UntilString;
import com.albertoventurini.graphino.graph.parser.rules.Wrapper;
import com.albertoventurini.graphino.graph.parser.rules.ZeroOrMore;
import com.albertoventurini.graphino.graph.parser.rules.ZeroOrOne;

import java.util.Optional;
import java.util.function.Predicate;

public class Grammar {

    private final Rule startRule;

    private final Rule commentRule;

    public Grammar(
            final Rule startRule,
            final Rule commentRule) {
        this.startRule = startRule;
        this.commentRule = commentRule;

        // TODO: better than this
        if (commentRule != null) {
            commentRule.setComment(true);
        }
    }

    public Optional<ParseTree> parse(final String text) {
        final var ctx = new GrammarContext(text, commentRule);
        return startRule.apply(ctx);
    }

    public static MatchCharacter character(final char c) {
        return new MatchCharacter(c);
    }

    public static AnyCharacter anyChar() {
        return new AnyCharacter();
    }

    public static MatchString string(final String s) {
        return new MatchString(s);
    }

    public static TakeWhileCharacter token() {
        return new TakeWhileCharacter();
    }

    public static TakeWhileCharacter takeWhile(final Predicate<Character> characterPredicate) {
        return new TakeWhileCharacter(characterPredicate);
    }

    public static UntilString until(final String s) {
        return new UntilString(s);
    }

    public static Sequence sequence(final Rule... rules) {
        return new Sequence(rules);
    }

    public static OneOf oneOf(final Rule... rules) {
        return new OneOf(rules);
    }

    public static ZeroOrMore zeroOrMore(final Rule childRule) {
        return new ZeroOrMore(childRule);
    }

    public static ZeroOrOne zeroOrOne(final Rule childRule) {
        return new ZeroOrOne(childRule);
    }

    public static OneOrMore oneOrMore(final Rule childRule) {
        return new OneOrMore(childRule);
    }

    public static Wrapper wrapper() {
        return new Wrapper();
    }
}
