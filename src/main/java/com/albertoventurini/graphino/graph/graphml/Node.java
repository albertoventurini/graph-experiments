package com.albertoventurini.graphino.graph.graphml;

import java.util.Map;

public class Node {
    public final String id;
    public final Map<String, String> data;

    public Node(final String id, final Map<String, String> data) {
        this.id = id;
        this.data = data;
    }
}
