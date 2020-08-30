package com.albertoventurini.graphino.graph.graphml;

import com.albertoventurini.graphino.graph.parser.Grammar;
import com.albertoventurini.graphino.graph.parser.MatchCharacter;
import com.albertoventurini.graphino.graph.parser.MatchString;
import com.albertoventurini.graphino.graph.parser.OneOf;
import com.albertoventurini.graphino.graph.parser.Rule;
import com.albertoventurini.graphino.graph.parser.Sequence;
import com.albertoventurini.graphino.graph.parser.TakeWhileCharacter;
import com.albertoventurini.graphino.graph.parser.UntilString;
import com.albertoventurini.graphino.graph.parser.Wrapper;
import com.albertoventurini.graphino.graph.parser.ZeroOrMore;
import com.albertoventurini.graphino.graph.parser.ZeroOrOne;

import java.nio.file.Files;
import java.nio.file.Path;

public class GrammarGraphMLParser implements GraphMLParser {

    private final Grammar grammar;

    public GrammarGraphMLParser() {

        final Rule xmlHeader = new Sequence(
                new MatchString("<?xml version='1.0' ?>"));

        final Rule comment = new Sequence(
                new MatchString("<!--"),
                new UntilString("-->"));

        final Rule comments = new ZeroOrMore(comment);

        final Rule attribute = new Sequence(
                new TakeWhileCharacter(c -> c != '='),
                new MatchCharacter('='),
                new TakeWhileCharacter()
        );

        final Rule graphmlTag = new Sequence(
                new MatchString("<graphml"),
                new UntilString(">"));

        final Rule keyTag = new Sequence(
                new MatchString("<key"),
                new ZeroOrMore(attribute),
                new MatchCharacter('>'),
                new MatchString("</key>"));

        final Rule tag = new Sequence(
                new MatchCharacter('<'),
                new TakeWhileCharacter(),
                new ZeroOrMore(attribute),
                new OneOf(new MatchString("/>"), new MatchCharacter('>')));

        final Rule closingTag = new Sequence(
                new MatchCharacter('<'),
                new TakeWhileCharacter(),
                new MatchString("/>"));

        final Wrapper elementContentWrapper = new Wrapper();

        final Rule element = new Sequence(
                tag,
                elementContentWrapper,
                new ZeroOrOne(closingTag)
        );

        final Rule elementContent = new ZeroOrMore(new OneOf(
                new ZeroOrMore(element),
                new TakeWhileCharacter()
        ));

        elementContentWrapper.setChildRule(elementContent);

        final Rule graphml = new Sequence(
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
