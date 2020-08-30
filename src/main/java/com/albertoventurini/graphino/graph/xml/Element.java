package com.albertoventurini.graphino.graph.xml;

public class Element<T> {

    private final Tag tag;

    private final T content;

    public Element(final Tag tag, final T content) {
        this.tag = tag;
        this.content = content;
    }
}
