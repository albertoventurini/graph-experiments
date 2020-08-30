package com.albertoventurini.graphino.graph.graphml;

import com.albertoventurini.graphino.graph.parser.Grammar;

import java.nio.file.Files;
import java.nio.file.Path;

import static com.albertoventurini.graphino.graph.parser.Grammar.*;

public class GrammarGraphMLParser implements GraphMLParser {

    private final Grammar grammar;

    public GrammarGraphMLParser() {

        final Rule xmlHeader = new Sequence(
                new Grammar.MatchString("<?xml version='1.0' ?>"));

        final Rule comment = new Sequence(
                new Grammar.MatchString("<!--"),
                new UntilString("-->"));

        final Rule comments = new ZeroOrMore(comment);

        final Rule attribute = new Sequence(
                new TakeWhileCharacter(c -> c != '='),
                new MatchCharacter('='),
                new TakeWhileCharacter()
        );

        final Grammar.Rule graphmlTag = new Grammar.Sequence(
                new Grammar.MatchString("<graphml"),
                new UntilString(">"));

        final Grammar.Rule keyTag = new Grammar.Sequence(
                new Grammar.MatchString("<key"),
                new Grammar.ZeroOrMore(attribute),
                new MatchCharacter('>'),
                new Grammar.MatchString("</key>"));

        final Grammar.Rule tag = new Grammar.Sequence(
                new MatchCharacter('<'),
                new TakeWhileCharacter(),
                new Grammar.ZeroOrMore(attribute),
                new OneOf(new Grammar.MatchString("/>"), new MatchCharacter('>')));

        final Grammar.Rule closingTag = new Grammar.Sequence(
                new MatchCharacter('<'),
                new TakeWhileCharacter(),
                new Grammar.MatchString("/>"));

        final Wrapper elementContentWrapper = new Wrapper();

        final Grammar.Rule element = new Sequence(
                tag,
                elementContentWrapper,
                new ZeroOrOne(closingTag)
        );

        final Grammar.Rule elementContent = new Grammar.ZeroOrMore(new OneOf(
                new Grammar.ZeroOrMore(element),
                new TakeWhileCharacter()
        ));

        elementContentWrapper.setChildRule(elementContent);

        final Grammar.Rule graphml = new Grammar.Sequence(
                xmlHeader,
                element);

        grammar = new Grammar(graphml, comments);
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
