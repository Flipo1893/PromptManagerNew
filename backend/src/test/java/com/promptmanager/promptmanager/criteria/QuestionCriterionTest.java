package com.promptmanager.promptmanager.criteria;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestionCriterionTest {

    private QuestionCriterion criterion;

    @BeforeEach
    void setUp() {
        criterion = new QuestionCriterion();
    }

    // --- evaluate(String text) ---

    @Test
    void evaluate_nullText_returnsZero() {
        assertEquals(0, criterion.evaluate(null));
    }

    @Test
    void evaluate_emptyString_returnsZero() {
        assertEquals(0, criterion.evaluate(""));
    }

    @Test
    void evaluate_textWithoutQuestionMark_returnsZero() {
        assertEquals(0, criterion.evaluate("Erklaere mir Java"));
    }

    @Test
    void evaluate_textWithQuestionMark_returnsTwenty() {
        assertEquals(20, criterion.evaluate("Wie funktioniert Java?"));
    }

    @Test
    void evaluate_textWithMultipleQuestionMarks_returnsTwenty() {
        assertEquals(20, criterion.evaluate("Was? Wie? Warum?"));
    }

    @Test
    void evaluate_onlyQuestionMark_returnsTwenty() {
        assertEquals(20, criterion.evaluate("?"));
    }

    @Test
    void evaluate_questionMarkEmbedded_returnsTwenty() {
        assertEquals(20, criterion.evaluate("Bitte erklaere mir das Konzept? Danke."));
    }

    // --- evaluate(String text, String language) ---

    @Test
    void evaluateWithLanguage_nullText_returnsZero() {
        assertEquals(0, criterion.evaluate(null, "de"));
    }

    @Test
    void evaluateWithLanguage_nullTextNullLanguage_returnsZero() {
        assertEquals(0, criterion.evaluate(null, null));
    }

    @Test
    void evaluateWithLanguage_textWithQuestionMark_returnsTwenty() {
        assertEquals(20, criterion.evaluate("Was ist das?", "de"));
    }

    @Test
    void evaluateWithLanguage_textWithoutQuestionMark_returnsZero() {
        assertEquals(0, criterion.evaluate("Bitte erklaere mir etwas", "en"));
    }

    @Test
    void evaluateWithLanguage_emptyText_returnsZero() {
        assertEquals(0, criterion.evaluate("", "en"));
    }

    // --- getName() inherited from AbstractCriterium ---

    @Test
    void getName_returnsQuestion() {
        assertEquals("question", criterion.getName());
    }
}
