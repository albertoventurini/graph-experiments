package com.albertoventurini.graphino.graph.graphml;

import java.util.Map;

public record Edge(String id, String source, String target, Map<String, String> data) { }
