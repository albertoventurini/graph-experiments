package com.albertoventurini.graphino.graph.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        final var ctx = new GrammarContext(text);
        return startRule.apply(ctx);
    }

    private class GrammarContext extends ParseContext {
        private boolean inComment;

        private GrammarContext(final String string) {
            super(string);
        }

        private void advanceToNextToken() {
            discardWhitespaces(this);
            if (!inComment) {
                discardComments(this);
                discardWhitespaces(this);
            }
        }

        public boolean isInComment() {
            return inComment;
        }

        public void setInComment(final boolean inComment) {
            this.inComment = inComment;
        }
    }

    private void discardComments(final GrammarContext ctx) {
        if (ctx.hasNext() && commentRule != null) {
            commentRule.apply(ctx);
        }
    }

    private void discardWhitespaces(final GrammarContext ctx) {
        while (ctx.hasNext()) {
            final char c = ctx.peek();
            if (!(java.lang.Character.isWhitespace(c) || c == '\n')) {
                break;
            }
            ctx.advance();
        }
    }

    public abstract static class Rule {
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
                var result = tryApply(ctx);
                return result;
            } finally {
                ctx.setInComment(inComment);
            }
        }

        public abstract Optional<ParseTree> tryApply(final GrammarContext ctx);

        public void setComment(final boolean comment) {
            isComment = comment;
        }
    }

    public static final class MatchCharacter extends Rule {
        private final char c;

        public MatchCharacter(final char c) {
            this.c = c;
        }

        @Override
        public Optional<ParseTree> tryApply(final GrammarContext ctx) {
            ctx.advanceToNextToken();

            if (ctx.peek() == c) {
                return Optional.of(ParseTree.leaf(java.lang.Character.toString(ctx.next())));
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

    public static MatchCharacter character(final char c) {
        return new MatchCharacter(c);
    }

    public static final class AnyCharacter extends Rule {
        @Override
        public Optional<ParseTree> tryApply(final GrammarContext ctx) {
            return Optional.of(ParseTree.leaf(java.lang.Character.toString(ctx.next())));
        }

        @Override
        public String toString() {
            return "CharacterRule{}";
        }
    }

    public static AnyCharacter anyChar() {
        return new AnyCharacter();
    }

    public static final class MatchString extends Rule {
        private final String s;

        public MatchString(final String s) {
            this.s = s;
        }

        @Override
        public Optional<ParseTree> tryApply(final GrammarContext ctx) {
            ctx.advanceToNextToken();

            if (ctx.matches(s)) {
                ctx.advance(s.length());
                return Optional.of(ParseTree.leaf(s));
            } else {
                return Optional.empty();
            }
        }

        @Override
        public String toString() {
            return "MatchString{" +
                    "s='" + s + '\'' +
                    '}';
        }
    }

    public static MatchString string(final String s) {
        return new MatchString(s);
    }

    public static final class TakeWhileCharacter extends Rule {
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

    public static TakeWhileCharacter token() {
        return new TakeWhileCharacter();
    }

    public static TakeWhileCharacter takeWhile(final Predicate<Character> characterPredicate) {
        return new TakeWhileCharacter(characterPredicate);
    }

    public static final class UntilString extends Rule {
        private final char[] charArr;

        public UntilString(final String s) {
            this.charArr = s.toCharArray();
        }

        @Override
        public Optional<ParseTree> tryApply(final GrammarContext ctx) {
            ctx.advanceToNextToken();

            final int start = ctx.getCursor();

            int matched = 0;
            while (ctx.hasNext() && matched < charArr.length) {
                if (ctx.next() == charArr[matched]) {
                    matched++;
                } else {
                    matched = 0;
                }
            }

            if (!ctx.hasNext()) {
                return Optional.of(ParseTree.leaf(ctx.substring(start)));
            } else {
                return Optional.of(ParseTree.leaf(ctx.substring(start, ctx.getCursor())));
            }
        }

        @Override
        public String toString() {
            return "UntilString{" +
                    "name='" + name + '\'' +
                    ", charArr=" + Arrays.toString(charArr) +
                    '}';
        }
    }

    public static UntilString until(final String s) {
        return new UntilString(s);
    }

    public static final class Sequence extends Rule {
        private final Rule[] rules;

        public Sequence(final Rule... rules) {
            this.rules = rules;
        }

        @Override
        public Optional<ParseTree> tryApply(final GrammarContext ctx) {
            final int start = ctx.getCursor();
            final List<ParseTree> children = new ArrayList<>(rules.length);

            for (final Rule rule : rules) {
                if (!ctx.hasNext()) {
                    ctx.setCursor(start);
                    return Optional.empty();
                }

                final Optional<ParseTree> child = rule.apply(ctx);

                if (child.isEmpty()) {
                    ctx.setCursor(start);
                    return Optional.empty();
                } else {
                    children.add(child.get());
                }
            }

            return Optional.of(ParseTree.node(name, children));
        }

        @Override
        public String toString() {
            return "Sequence{" +
                    "rules=" + Arrays.toString(rules) +
                    '}';
        }
    }

    public static Sequence sequence(final Rule... rules) {
        return new Sequence(rules);
    }

    public static final class OneOf extends Rule {
        private final Rule[] rules;

        public OneOf(final Rule... rules) {
            this.rules = rules;
        }

        @Override
        public Optional<ParseTree> tryApply(final GrammarContext ctx) {
            final int start = ctx.getCursor();

            for (final Rule rule : rules) {
                final Optional<ParseTree> result = rule.apply(ctx);
                if (result.isPresent()) {
                    return result;
                }
            }

            ctx.setCursor(start);
            return Optional.empty();
        }

        @Override
        public String toString() {
            return "OneOf{" +
                    "rules=" + Arrays.toString(rules) +
                    '}';
        }
    }

    public static OneOf oneOf(final Rule... rules) {
        return new OneOf(rules);
    }

    public static final class ZeroOrMore extends Rule {
        private final Rule childRule;

        public ZeroOrMore(final Rule childRule) {
            this.childRule = childRule;
        }

        @Override
        public Optional<ParseTree> tryApply(final GrammarContext ctx) {
            final List<ParseTree> children = new ArrayList<>();

            while (ctx.hasNext()) {
                final Optional<ParseTree> child = childRule.apply(ctx);
                if (child.isEmpty()) {
                    break;
                } else {
                    children.add(child.get());
                }
            }

            return Optional.of(ParseTree.node(name, children));
        }

        @Override
        public String toString() {
            return "ZeroOrMore{" +
                    childRule +
                    '}';
        }
    }

    public static ZeroOrMore zeroOrMore(final Rule childRule) {
        return new ZeroOrMore(childRule);
    }

    public static final class ZeroOrOne extends Rule {
        private final Rule childRule;

        public ZeroOrOne(final Rule childRule) {
            this.childRule = childRule;
        }

        @Override
        public Optional<ParseTree> tryApply(final GrammarContext ctx) {
            final Optional<ParseTree> child = childRule.apply(ctx);
            if (child.isPresent()) {
                return child;
            } else {
                return Optional.of(ParseTree.leaf(""));
            }
        }

        @Override
        public String toString() {
            return "ZeroOrOne{" +
                    childRule +
                    '}';
        }
    }

    public static ZeroOrOne zeroOrOne(final Rule childRule) {
        return new ZeroOrOne(childRule);
    }

    public static final class Wrapper extends Rule {
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

    public static Wrapper wrapper() {
        return new Wrapper();
    }
}
