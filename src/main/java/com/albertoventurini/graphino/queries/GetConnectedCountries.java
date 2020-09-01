package com.albertoventurini.graphino.queries;

import com.albertoventurini.graphino.graph.Graph;
import com.albertoventurini.graphino.graph.Query;

import java.util.Set;
import java.util.stream.Collectors;

public class GetConnectedCountries {

    public static Set<String> execute(final Graph graph, final String sourceAirport) {

        return new Query(graph).withLabel("airport")
                .where("code", String.class, c -> c.equals(sourceAirport))
                .out("route")
                .toNodes()
                .in("contains")
                .fromNodes("country")
                .stream()
                .map(n -> (String) n.properties.get("desc"))
                .collect(Collectors.toSet());
    }
}
