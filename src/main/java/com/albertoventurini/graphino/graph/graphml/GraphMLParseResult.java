package com.albertoventurini.graphino.graph.graphml;

import java.util.Set;

public class GraphMLParseResult {
    public final Set<Node> nodes;
    public final Set<Edge> edges;

    public GraphMLParseResult(
            final Set<Node> nodes,
            final Set<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

}
