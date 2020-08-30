package com.albertoventurini.graphino.graph;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Node {

    private final String id;
    private final String label;
    private final Set<Edge> outgoingEdges;
    private final Set<Edge> incomingEdges;
    private final Map<String, Object> properties;

    public Node(
            final String id,
            final String label) {
        this.id = id;
        this.label = label;

        outgoingEdges = new LinkedHashSet<>();
        incomingEdges = new LinkedHashSet<>();

        properties = new HashMap<>();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Node node = (Node) o;
        return Objects.equals(id, node.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Node{" +
                "label='" + label + '\'' +
                ", id='" + id + '\'' +
                ", properties=" + properties +
                '}';
    }

    void addOutgoingEdge(final Edge r) {
        outgoingEdges.add(r);
    }

    void addIncomingEdge(final Edge r) {
        incomingEdges.add(r);
    }

    Set<Edge> getOutgoingEdges() {
        return outgoingEdges;
    }

    Set<Edge> getIncomingEdges() {
        return incomingEdges;
    }
}
