package com.albertoventurini.graphino.graph.graphml;

import java.util.Map;

public class Edge {
    public final String id;
    public final String source;
    public final String target;
    public final Map<String, String> data;

    public Edge(final String id, final String source, final String target, final Map<String, String> data) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.data = data;
    }
}
