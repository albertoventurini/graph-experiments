package com.albertoventurini.graphino.graph.graphml;

import com.albertoventurini.graphino.graph.parser.Grammar;
import com.albertoventurini.graphino.graph.parser.ParseTree;
import com.albertoventurini.graphino.graph.parser.Rule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.albertoventurini.graphino.graph.parser.Grammar.*;

public class GrammarGraphMLParser implements GraphMLParser {

    private final Grammar grammar;

    public GrammarGraphMLParser() {

        final Rule comment = sequence(string("<!--"), until("-->")).as("comment");
        final Rule comments = zeroOrMore(comment).as("comments");

        final Rule xmlHeader = string("<?xml version='1.0' ?>").as("header");

        final Rule attribute = sequence(
                takeWhile(c -> c != '=' && c != '>'),
                character('='),
                character('\''),
                takeWhile(c -> c != '\''),
                character('\'')).as("attribute");

        final Function<String, Rule> tagFunc = (s) -> sequence(
                character('<'),
                string(s).as("tagName"),
                zeroOrMore(attribute).as("attributes"),
                character('>')).as("tag:" + s);

        final Function<String, Rule> closingTagFunc = (s) -> sequence(
                string("</"),
                string(s),
                character('>')).as("closingTag:" + s);

        final BiFunction<String, Rule, Rule> elementFunc = (tagName, content) -> sequence(
                tagFunc.apply(tagName).as("tag:" + tagName),
                content.as("content"),
                closingTagFunc.apply(tagName).as("closingTag:" + tagName));

        final Rule data = elementFunc.apply("data", takeWhile(c -> c != '<')).as("dataElem");

        final Rule node = elementFunc.apply("node", zeroOrMore(data)).as("nodeElem");

        final Rule edge = elementFunc.apply("edge", zeroOrMore(data)).as("edgeElem");

        final Rule graph = sequence(
                tagFunc.apply("graph"),
                oneOrMore(node).as("nodes"),
                oneOrMore(edge).as("edges"),
                closingTagFunc.apply("graph")).as("graphElem");

        final Rule key = sequence(tagFunc.apply("key"), closingTagFunc.apply("key"))
                .as("keyElem");

        final Rule graphml = sequence(
                tagFunc.apply("graphml"),
                oneOrMore(key).as("keys"),
                graph,
                closingTagFunc.apply("graphml")).as("graphmlElem");

        final Rule graphmlFile = sequence(
                xmlHeader,
                comments.discard(),
                graphml).as("graphmlFile");

        grammar = new Grammar(graphmlFile, null);
    }

    @Override
    public GraphMLParseResult parse(final String filePath) {
        try {
             return grammar
                     .parse(Files.readString(Path.of(filePath)))
                     .map(this::processParseTree)
                     .orElseThrow(() -> new RuntimeException("Unable to parse graphml file: " + filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private GraphMLParseResult processParseTree(final ParseTree parseTree) {

        final List<Node> nodes = parseTree.getFirstDescendantByName("nodes").map(n ->
            n.getChildren()
                    .stream()
                    .map(this::processNodeElem)
                    .collect(Collectors.toList())
        ).orElseThrow();

        final List<Edge> edges = parseTree.getFirstDescendantByName("edges").map(n ->
                n.getChildren()
                        .stream()
                        .map(this::processEdgeElem)
                        .collect(Collectors.toList())
        ).orElseThrow();

        return new GraphMLParseResult(nodes, edges);
    }

    private Node processNodeElem(final ParseTree nodeElem) {
        final String id = extractAttribute(nodeElem.child("tag:node"), "id");

        final Map<String, String> data = nodeElem.child("content").getChildren()
                .stream()
                .map(this::extractDataElemContent)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return new Node(id, data);
    }

    private Edge processEdgeElem(final ParseTree edgeElem) {
        final String id = extractAttribute(edgeElem.child("tag:edge"), "id");
        final String source = extractAttribute(edgeElem.child("tag:edge"), "source");
        final String target = extractAttribute(edgeElem.child("tag:edge"), "target");

        final Map<String, String> data = edgeElem.child("content").getChildren()
                .stream()
                .map(this::extractDataElemContent)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return new Edge(id, source, target, data);
    }

    private Map.Entry<String, String> extractDataElemContent(final ParseTree dataElem) {
        final var attribute = extractAttribute(dataElem.child("tag:data"), "key");
        final var content = dataElem.child(1).getText();
        return Map.entry(attribute, content);
    }

    private String extractAttribute(final ParseTree tag, final String attributeKey) {
        return tag.child("attributes").getChildren()
                .stream()
                .filter(a -> a.child(0).getText().equals(attributeKey))
                .map(a -> a.child(1).getText())
                .findFirst()
                .orElseThrow();
    }

    public static void main(String[] args) {
        var result = new GrammarGraphMLParser().parse("/Users/alberto/Devel/graphino/air-routes-latest.graphml");
        int i = 0;
    }


}
