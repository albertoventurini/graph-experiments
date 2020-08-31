package com.albertoventurini.graphino.graph.parser;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static com.albertoventurini.graphino.graph.parser.Grammar.*;

public class GrammarTest {

    @Test
    public void xmlTagWithoutAttributes() {
        final Rule tag = new Sequence(
                new MatchCharacter('<'),
                new TakeWhileCharacter(java.lang.Character::isLetterOrDigit),
                new OneOf( new MatchString("/>"), new MatchCharacter('>')));

        final Grammar grammar = new Grammar(tag, null);

        final var parseTree1 = grammar.parse("<tag1>");

        assertTrue(parseTree1.isPresent());
        assertEquals("<", parseTree1.get().getChildren().get(0).getText());
        assertEquals("tag1", parseTree1.get().getChildren().get(1).getText());
        assertEquals(">", parseTree1.get().getChildren().get(2).getText());

        final var parseTree2 = grammar.parse("<tag1 />");

        assertTrue(parseTree2.isPresent());
        assertEquals("<", parseTree2.get().getChildren().get(0).getText());
        assertEquals("tag1", parseTree2.get().getChildren().get(1).getText());
        assertEquals("/>", parseTree2.get().getChildren().get(2).getText());
    }

    @Test
    public void xmlTagWithOptionalAttributes() {
        final Rule token = takeWhile(Character::isLetterOrDigit);

        final Rule attribute = sequence(token, character('='), token);

        final Rule tag = sequence(
                character('<'),
                token,
                zeroOrMore(attribute),
                oneOf(string("/>"), character('>')));

        final Grammar grammar = new Grammar(tag, null);

        assertTrue(grammar.parse("<tag1>").isPresent());

        final var parseTree1 = grammar.parse("<tag1 id=123>");

        assertTrue(parseTree1.isPresent());
        assertEquals("<", parseTree1.get().child(0).getText());
        assertEquals("tag1", parseTree1.get().child(1).getText());
        assertEquals("id", parseTree1.get().child(2) // get the zeroOrMore rule
                .child(0) // get the first attribute
                .child(0) // get the first element in the sequence rule
                .getText()
        );
        assertEquals("=", parseTree1.get().child(2) // get the zeroOrMore rule
                .child(0) // get the first attribute
                .child(1) // get the first child in the sequence
                .getText()
        );
        assertEquals("123", parseTree1.get().child(2) // get the zeroOrMore rule
                .child(0) // get the first attribute
                .child(2) // get the second child in the sequence
                .getText()
        );
        assertEquals(">", parseTree1.get().getChildren().get(3).getText());

        final var parseTree2 = grammar.parse("<tag1 hello=world something=else/>");

        assertTrue(parseTree2.isPresent());
        assertEquals("<", parseTree2.get().child(0).getText());
        assertEquals("tag1", parseTree2.get().child(1).getText());
        assertEquals("hello", parseTree2.get().child(2) // get the zeroOrMore rule
                .child(0) // get the first attribute
                .child(0) // get the first child in the sequence
                .getText()
        );
        assertEquals("=", parseTree2.get().child(2) // get the zeroOrMore rule
                .child(0) // get the first attribute
                .child(1) // get the second child in the sequence
                .getText()
        );
        assertEquals("world", parseTree2.get().child(2) // get the zeroOrMore rule
                .child(0) // get the first attribute
                .child(2) // get the third child in the sequence
                .getText()
        );
        assertEquals("something", parseTree2.get().child(2) // get the zeroOrMore rule
                .child(1) // get the second attribute
                .child(0) // get the first child in the sequence
                .getText()
        );
        assertEquals("=", parseTree2.get().child(2) // get the zeroOrMore rule
                .child(1) // get the second attribute
                .child(1) // get the second child in the sequence
                .getText()
        );
        assertEquals("else", parseTree2.get().child(2) // get the zeroOrMore rule
                .child(1) // get the second attribute
                .child(2) // get the third child in the sequence
                .getText()
        );
        assertEquals("/>", parseTree2.get().getChildren().get(3).getText());
    }

    @Test
    public void xmlTagWithAttributeSurroundedByQuotes() {
        final Rule attribute = sequence(
                takeWhile(c -> c != '='),
                character('='),
                character('\''),
                takeWhile(c -> c != '\''),
                character('\''));

        final Rule tag = sequence(
                character('<'),
                token(),
                zeroOrMore(attribute),
                oneOf(string("/>"), character('>')));

        final Grammar grammar = new Grammar(tag, null);

        assertTrue(grammar.parse("<graphml xmlns='http://graphml.graphdrawing.org/xmlns'>").isPresent());
    }

    @Test
    public void elementWithoutContent() {
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
                string(">"));

        final Rule key = sequence(
                tagFunc.apply("key"),
                closingTagFunc.apply("key"));

        final Grammar grammar = new Grammar(key, null);

        assertTrue(grammar.parse("<key id='type'    for='node' attr.name='type'    attr.type='string'></key>").isPresent());
    }

}
