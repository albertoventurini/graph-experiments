package com.albertoventurini.graphino.graph;

public interface GraphBuilder<T> {
    Graph build(T source);
}
