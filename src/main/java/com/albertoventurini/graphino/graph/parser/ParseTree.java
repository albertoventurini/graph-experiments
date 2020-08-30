package com.albertoventurini.graphino.graph.parser;

import java.util.Collections;
import java.util.List;

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
}
