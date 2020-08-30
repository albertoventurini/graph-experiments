package com.albertoventurini.graphino.graph.xml;

import java.util.Map;

public class Tag {

    public enum Type {
        START_TAG,
        END_TAG,
        EMPTY_ELEMENT_TAG;
    }

    private final String name;

    private final Map<String, String> attributes;

    private final Type type;

    public Tag(
            final String name,
            final Map<String, String> attributes,
            final Type type) {
        this.name = name;
        this.attributes = attributes;
        this.type = type;
    }
}
