package com.albertoventurini.graphino.graph;

import java.util.HashMap;
import java.util.Map;

public class Edge {

    public final String label;
    public final Node fromNode;
    public final Node toNode;

    public final Map<String, Object> properties;

    public Edge(
            final String label,
            final Node fromNode,
            final Node toNode) {
        this.label = label;
        this.fromNode = fromNode;
        this.toNode = toNode;

        properties = new HashMap<>();
    }
}
