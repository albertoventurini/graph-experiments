package com.albertoventurini.graphino.graph.parser;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.albertoventurini.graphino.graph.parser.Grammar.*;
import static com.albertoventurini.graphino.graph.parser.Grammar.sequence;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GraphMLGrammarTest {

    private final Grammar grammar;

    public GraphMLGrammarTest() {
        final Rule comment = sequence(string("<!--"), until("-->"));
        final Rule comments = zeroOrMore(comment);

        final Rule xmlHeader = string("<?xml version='1.0' ?>").as("header");

        final Rule attribute = sequence(
                takeWhile(c -> c != '=' && c != '>'),
                character('='),
                character('\''),
                takeWhile(c -> c != '\''),
                character('\''));

        final Function<String, Rule> tagFunc = (s) -> sequence(
                character('<'),
                string(s),
                zeroOrMore(attribute),
                character('>'));

        final Function<String, Rule> closingTagFunc = (s) -> sequence(
                string("</"),
                string(s),
                character('>'));

        final BiFunction<String, Rule, Rule> elementFunc = (tagName, content) -> sequence(
                tagFunc.apply(tagName),
                content.as("content"),
                closingTagFunc.apply(tagName));

        final Rule data = elementFunc.apply("data", takeWhile(c -> c != '<')).as("dataElem");

        final Rule node = elementFunc.apply("node", zeroOrMore(data)).as("nodeElem");

        final Rule edge = elementFunc.apply("edge", zeroOrMore(data)).as("edgeElem");

        final Rule graph = sequence(
                tagFunc.apply("graph"),
                oneOrMore(node).as("nodes"),
                oneOrMore(edge).as("edges"),
                closingTagFunc.apply("graph")).as("graphElem");

        final Rule key = sequence(
                tagFunc.apply("key"),
                closingTagFunc.apply("key")).as("keyElem");

        final Rule graphml = sequence(
                tagFunc.apply("graphml"),
                oneOrMore(key).as("keys"),
                graph,
                closingTagFunc.apply("graphml")).as("graphmlElem");

        final Rule graphmlFile = sequence(xmlHeader, graphml).as("graphmlFile");

        grammar = new Grammar(graphmlFile, comments);
    }

    @Test
    public void graphmlGrammar_shouldParseGraphmlFile() throws Exception {
        final var result =
                grammar.parse(Files.readString(Path.of("src/test/resources/graphml/graphA.graphml")));

        assertTrue(result.isPresent());
    }

    @Test
    public void graphmlGrammar_shouldParseLargeGraphmlFile() throws Exception {
        final var result =
                grammar.parse(Files.readString(Path.of("src/test/resources/graphml/air-routes-latest.graphml")));

        assertTrue(result.isPresent());
    }
}
