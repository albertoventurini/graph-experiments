package com.albertoventurini.graphino.parser;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ParseTree {

    private final String text;

    private final List<ParseTree> children;

    public static final class Leaf extends ParseTree {
        public Leaf(final String text) {
            super(text, Collections.emptyList());
        }
    }

    public static final class Node extends ParseTree {
        public Node(final String text, final List<ParseTree> children) {
            super(text, children);
        }
    }

    public ParseTree(final String text, final List<ParseTree> children) {
        this.text = text;
        this.children = children;
    }

    public static Leaf leaf(final String text) {
        return new Leaf(text);
    }

    public static Node node(final String text, final List<ParseTree> children) {
        return new Node(text, children);
    }

    public String getText() {
        return text;
    }

    public List<ParseTree> getChildren() {
        return children;
    }

    public ParseTree child(final int i) {
        return children.get(i);
    }

    public Optional<ParseTree> getFirstDescendantByName(final String name) {
        if (name.equals(text)) {
            return Optional.of(this);
        }

        for (final ParseTree child : children) {
            final Optional<ParseTree> descendant = child.getFirstDescendantByName(name);
            if (descendant.isPresent()) {
                return descendant;
            }
        }

        return Optional.empty();
    }

    public ParseTree child(final String name) {
        return children.stream().filter(c -> name.equals(c.text))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Child not found: " + name));
    }

    @Override
    public String toString() {
        return text;
    }
}
