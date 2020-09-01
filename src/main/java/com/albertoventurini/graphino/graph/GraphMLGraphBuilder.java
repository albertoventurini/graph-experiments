package com.albertoventurini.graphino.graph;

import com.albertoventurini.graphino.graphml.GraphMLParseResult;

public class GraphMLGraphBuilder implements GraphBuilder<GraphMLParseResult> {

    @Override
    public Graph build(final GraphMLParseResult source) {
        final Graph graph = new Graph();

        source.nodes().forEach(n -> addNode(graph, n));
        source.edges().forEach(e -> addEdge(graph, e));

        return graph;
    }

    private void addNode(final Graph graph, final com.albertoventurini.graphino.graphml.Node graphmlNode) {
        final Node node = graph.addNode(graphmlNode.id(), graphmlNode.data().get("labelV"));

        graphmlNode.data().forEach((key, value) -> {
            if (!key.equals("labelV")) {
                node.properties.put(key, value);
            }
        });
    }

    private void addEdge(final Graph graph, final com.albertoventurini.graphino.graphml.Edge graphmlEdge) {
        final String label = graphmlEdge.data().get("labelE");
        final Edge edge = graph.addEdge(label, graphmlEdge.source(), graphmlEdge.target());

        graphmlEdge.data().forEach((key, value) -> {
            if (!key.equals("labelE")) {
                edge.properties.put(key, value);
            }
        });
    }
}
