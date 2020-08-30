package com.albertoventurini.graphino.graph.exceptions;

public class NodeNotFoundException extends RuntimeException {

    public NodeNotFoundException(final String nodeId) {
        super("Node not found: " + nodeId);
    }
}
