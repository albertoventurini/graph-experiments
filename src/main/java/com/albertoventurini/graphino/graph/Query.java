package com.albertoventurini.graphino.graph;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class Query {

    private final Graph graph;

    public Query(final Graph graph) {
        this.graph = graph;
    }

    public Nodes match(final String id) {
        return new Nodes(graph.getOptionalNode(id).stream());
    }

    public static class Nodes {
        final Stream<Node> nodes;

        public Nodes(final Stream<Node> nodes) {
            this.nodes = nodes;
        }

        public Relationships out(final String relationshipLabel) {
            return new Relationships(nodes.flatMap(n ->
                    n.getOutgoingEdges().stream().filter(r -> r.label.equals(relationshipLabel))));
        }

        public Relationships in(final String relationshipLabel) {
            return new Relationships(nodes.flatMap(n ->
                    n.getIncomingEdges().stream().filter(r -> r.label.equals(relationshipLabel))));
        }

        public Nodes where(final Predicate<Node> predicate) {
            return new Nodes(nodes.filter(predicate));
        }
    }

    public static class Relationships {
        final Stream<Edge> relationships;

        public Relationships(final Stream<Edge> relationships) {
            this.relationships = relationships;
        }

        public Nodes toNodes() {
            return new Nodes(relationships.map(r -> r.target));
        }

//        public <T> Nodes toNodes(final Class<T> clazz) {
//            return new Nodes(relationships.filter(r -> clazz.isInstance(r.toNode.value)).map(r -> r.toNode));
//        }

        public Nodes fromNodes() {
            return new Nodes(relationships.map(r -> r.source));
        }

//        public <T> Nodes fromNodes(final Class<T> clazz) {
//            return new Nodes(relationships.filter(r -> clazz.isInstance(r.fromNode.value)).map(r -> r.fromNode));
//        }

        public Relationships where(final Predicate<Edge> predicate) {
            return new Relationships(relationships.filter(predicate));
        }
    }

}
