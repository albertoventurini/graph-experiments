package com.albertoventurini.graphino.graph.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static com.albertoventurini.graphino.graph.parser.Grammar.*;

public class GrammarTest {

    @Test
    public void xmlTagWithoutAttributes() {
        final Grammar.Rule tag = new Grammar.Sequence(
                new MatchCharacter('<'),
                new TakeWhileCharacter(java.lang.Character::isLetterOrDigit),
                new Grammar.OneOf( new Grammar.MatchString("/>"), new MatchCharacter('>')));

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
        final Rule tag = sequence(
                character('<'),
                takeWhile(Character::isLetterOrDigit),
                oneOf(string("/>"), character('>')));

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

}
