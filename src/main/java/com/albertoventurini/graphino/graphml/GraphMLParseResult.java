package com.albertoventurini.graphino.graphml;

import java.util.List;

public record GraphMLParseResult(List<Node> nodes,
                                 List<Edge> edges) {

}
