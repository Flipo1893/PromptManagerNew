package com.promptmanager.promptmanager.criteria;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LengthCriteriumTest {

    private LengthCriterium criterium;

    @BeforeEach
    void setUp() {
        criterium = new LengthCriterium();
    }


    @Test
    void evaluate_nullText_returnsZero() {
        assertEquals(0, criterium.evaluate(null));
    }


    @Test
    void evaluate_shortText_returnsZero() {
        String text = "Hallo";
        assertEquals(0, criterium.evaluate(text));
    }


    @Test
    void evaluate_mediumText_returnsTen() {
        String text = "a".repeat(50);
        assertEquals(10, criterium.evaluate(text));
    }


    @Test
    void evaluate_longText_returnsTwenty() {
        String text = "a".repeat(100);
        assertEquals(20, criterium.evaluate(text));
    }


    @Test
    void evaluate_veryLongText_returnsThirty() {
        String text = "a".repeat(200);
        assertEquals(30, criterium.evaluate(text));
    }


    @Test
    void evaluate_exactlyThirtyChars_returnsTen() {
        String text = "a".repeat(30);
        assertEquals(10, criterium.evaluate(text));
    }


    @Test
    void evaluate_exactlyEightyChars_returnsTwenty() {
        String text = "a".repeat(80);
        assertEquals(20, criterium.evaluate(text));
    }


    @Test
    void evaluate_exactlyHundredFiftyChars_returnsThirty() {
        String text = "a".repeat(150);
        assertEquals(30, criterium.evaluate(text));
    }


    @Test
    void evaluate_textWithWhitespace_trimsBeforecounting() {
        String text = "   " + "a".repeat(5) + "   ";
        assertEquals(0, criterium.evaluate(text));
    }
}