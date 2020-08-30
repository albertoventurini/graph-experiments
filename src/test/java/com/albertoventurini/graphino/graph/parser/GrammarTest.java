package com.albertoventurini.graphino.graph.parser;

import org.junit.jupiter.api.Test;

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

        final var parseTree1 = grammar.parse("<tag1 id=123>");

        assertTrue(parseTree1.isPresent());
        assertEquals("<", parseTree1.get().getChildren().get(0).getText());
        assertEquals("tag1", parseTree1.get().getChildren().get(1).getText());
        assertEquals("id", parseTree1.get().getChildren().get(2) // get the zeroOrMore rule
                .getChildren().get(0) // get the first attribute
                .getChildren() // get the sequence rule
                .get(0) // get the first child in the sequence
                .getText()
        );
        assertEquals("=", parseTree1.get().getChildren().get(2) // get the zeroOrMore rule
                .getChildren().get(0) // get the first attribute
                .getChildren() // get the sequence rule
                .get(1) // get the first child in the sequence
                .getText()
        );
        assertEquals("123", parseTree1.get().getChildren().get(2) // get the zeroOrMore rule
                .getChildren().get(0) // get the first attribute
                .getChildren() // get the sequence rule
                .get(2) // get the first child in the sequence
                .getText()
        );
        assertEquals(">", parseTree1.get().getChildren().get(3).getText());

        final var parseTree2 = grammar.parse("<tag1 hello=world something=else/>");

        assertTrue(parseTree2.isPresent());
        assertEquals("<", parseTree2.get().getChildren().get(0).getText());
        assertEquals("tag1", parseTree2.get().getChildren().get(1).getText());
        assertEquals("hello", parseTree2.get().getChildren().get(2) // get the zeroOrMore rule
                .getChildren().get(0) // get the first attribute
                .getChildren() // get the sequence rule
                .get(0) // get the first child in the sequence
                .getText()
        );
        assertEquals("=", parseTree2.get().getChildren().get(2) // get the zeroOrMore rule
                .getChildren().get(0) // get the first attribute
                .getChildren() // get the sequence rule
                .get(1) // get the first child in the sequence
                .getText()
        );
        assertEquals("world", parseTree2.get().getChildren().get(2) // get the zeroOrMore rule
                .getChildren().get(0) // get the first attribute
                .getChildren() // get the sequence rule
                .get(2) // get the first child in the sequence
                .getText()
        );
        assertEquals("something", parseTree2.get().getChildren().get(2) // get the zeroOrMore rule
                .getChildren().get(1) // get the second attribute
                .getChildren() // get the sequence rule
                .get(0) // get the first child in the sequence
                .getText()
        );
        assertEquals("=", parseTree2.get().getChildren().get(2) // get the zeroOrMore rule
                .getChildren().get(1) // get the second attribute
                .getChildren() // get the sequence rule
                .get(1) // get the first child in the sequence
                .getText()
        );
        assertEquals("else", parseTree2.get().getChildren().get(2) // get the zeroOrMore rule
                .getChildren().get(1) // get the second attribute
                .getChildren() // get the sequence rule
                .get(2) // get the first child in the sequence
                .getText()
        );
        assertEquals("/>", parseTree2.get().getChildren().get(3).getText());
    }

}
