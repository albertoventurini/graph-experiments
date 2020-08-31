package com.albertoventurini.graphino.graph.graphml;

import com.albertoventurini.graphino.graph.parser.Grammar;
import com.albertoventurini.graphino.graph.parser.Rule;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.albertoventurini.graphino.graph.parser.Grammar.*;

public class GrammarGraphMLParser implements GraphMLParser {

    private final Grammar grammar;

    public GrammarGraphMLParser() {

        final Rule comment = sequence(string("<!--"), until("-->"));
        final Rule comments = zeroOrMore(comment);

        final Rule xmlHeader = string("<?xml version='1.0' ?>");

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
                content,
                closingTagFunc.apply(tagName));

        final Rule data = sequence(
                tagFunc.apply("data"),
                takeWhile(c -> c != '<'),
                closingTagFunc.apply("data"));

        final Rule node = sequence(
                tagFunc.apply("node"),
                zeroOrMore(data),
                closingTagFunc.apply("node"));

        final Rule edge = sequence(
                tagFunc.apply("edge"),
                zeroOrMore(data),
                closingTagFunc.apply("edge"));

        final Rule graph = sequence(
                tagFunc.apply("graph"),
                oneOrMore(node),
                oneOrMore(edge),
                closingTagFunc.apply("graph"));

        final Rule key = sequence(
                tagFunc.apply("key"),
                closingTagFunc.apply("key"));

        final Rule graphml = sequence(
                tagFunc.apply("graphml"),
                oneOrMore(key),
                graph,
                closingTagFunc.apply("graphml"));

        final Rule graphmlFile = sequence(xmlHeader, graphml);

        grammar = new Grammar(graphmlFile, comments);
    }

    @Override
    public GraphMLParseResult parse(final String filePath) {
        try {
            var p = grammar.parse(Files.readString(Path.of("air-routes-latest.graphml")));
            int i = 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public static void main(String[] args) {
        new GrammarGraphMLParser().parse("");
    }


}
