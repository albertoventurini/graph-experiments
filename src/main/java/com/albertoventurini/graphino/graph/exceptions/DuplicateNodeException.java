package com.albertoventurini.graphino.graph.exceptions;

public class DuplicateNodeException extends RuntimeException {

    public DuplicateNodeException(final String nodeId) {
        super("Duplicate node found: " + nodeId);
    }
}
