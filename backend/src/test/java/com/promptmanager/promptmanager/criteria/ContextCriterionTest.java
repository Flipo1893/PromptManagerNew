package com.promptmanager.promptmanager.criteria;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ContextCriterionTest {

    private ContextCriterion criterion;

    @BeforeEach
    void setUp() {
        criterion = new ContextCriterion();
    }

    @Test
    void evaluate_nullText_returnsZero() {
        assertEquals(0, criterion.evaluate(null));
    }

    @Test
    void evaluate_emptyText_returnsZero() {
        assertEquals(0, criterion.evaluate(""));
    }

    @Test
    void evaluate_blankText_returnsZero() {
        assertEquals(0, criterion.evaluate("   "));
    }

    @Test
    void evaluate_noContextKeyword_returnsZero() {
        assertEquals(0, criterion.evaluate("Erkläre mir wie Spring Boot funktioniert"));
    }

    @Test
    void evaluate_kontextKeyword_returnsTwenty() {
        assertEquals(20, criterion.evaluate("Kontext: Ich arbeite an einem Java-Projekt"));
    }

    @Test
    void evaluate_hintergrundKeyword_returnsTwenty() {
        assertEquals(20, criterion.evaluate("Hintergrund: Das System ist eine Web-Applikation"));
    }

    @Test
    void evaluate_backgroundKeyword_returnsTwenty() {
        assertEquals(20, criterion.evaluate("Background: I am building a REST API"));
    }

    @Test
    void evaluate_contextKeyword_returnsTwenty() {
        assertEquals(20, criterion.evaluate("Context: The application uses Spring Boot"));
    }

    @Test
    void evaluate_ausgangslageKeyword_returnsTwenty() {
        assertEquals(20, criterion.evaluate("Ausgangslage: Wir haben eine bestehende Datenbank"));
    }

    @Test
    void evaluate_situationKeyword_returnsTwenty() {
        assertEquals(20, criterion.evaluate("Situation: Der Kunde möchte eine neue Funktion"));
    }

    @Test
    void evaluate_uppercaseKeyword_returnsTwenty() {
        assertEquals(20, criterion.evaluate("KONTEXT: Das ist mein Projekt"));
    }

    @Test
    void evaluate_getName_returnsContext() {
        assertEquals("context", criterion.getName());
    }
}
