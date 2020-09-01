package com.albertoventurini.graphino;

import com.albertoventurini.graphino.graph.GraphMLGraphBuilder;
import com.albertoventurini.graphino.graphml.GrammarGraphMLParser;
import com.albertoventurini.graphino.queries.GetConnectedCities;
import com.albertoventurini.graphino.queries.GetConnectedCountries;

public class Main {

    public static void main(final String[] args) {
        final var parser = new GrammarGraphMLParser();
        final var parseResult = parser.parse("air-routes-latest.graphml");
        final var airRouteGraph = new GraphMLGraphBuilder().build(parseResult);

        System.out.println(GetConnectedCities.execute(airRouteGraph, "CPT"));
        System.out.println(GetConnectedCountries.execute(airRouteGraph, "CPT"));
    }
}
