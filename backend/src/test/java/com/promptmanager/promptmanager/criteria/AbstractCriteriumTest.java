package com.promptmanager.promptmanager.criteria;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbstractCriteriumTest {

    private AbstractCriterium criterium;

    @BeforeEach
    void setUp() {
        criterium = new AbstractCriterium("test-name") {
            @Override
            public int evaluate(String text) {
                return text == null ? 0 : text.length();
            }
        };
    }

    // --- getName() ---

    @Test
    void getName_returnsNameFromConstructor() {
        assertEquals("test-name", criterium.getName());
    }

    // --- normalize(int value, int max) ---

    @Test
    void normalize_valueWithinRange_returnsValue() {
        assertEquals(5, criterium.normalize(5, 10));
    }

    @Test
    void normalize_valueAtMax_returnsMax() {
        assertEquals(10, criterium.normalize(10, 10));
    }

    @Test
    void normalize_valueAboveMax_clampsToMax() {
        assertEquals(10, criterium.normalize(15, 10));
    }

    @Test
    void normalize_valueZero_returnsZero() {
        assertEquals(0, criterium.normalize(0, 10));
    }

    @Test
    void normalize_negativeValue_clampsToZero() {
        assertEquals(0, criterium.normalize(-5, 10));
    }

    @Test
    void normalize_maxZero_returnsZero() {
        assertEquals(0, criterium.normalize(5, 0));
    }

    @Test
    void normalize_negativeMax_returnsZero() {
        assertEquals(0, criterium.normalize(5, -1));
    }

    @Test
    void normalize_valueOneMaxOne_returnsOne() {
        assertEquals(1, criterium.normalize(1, 1));
    }

    // --- evaluate() via anonymous subclass ---

    @Test
    void evaluate_nonNullText_returnsLength() {
        assertEquals(5, criterium.evaluate("hello"));
    }

    @Test
    void evaluate_null_returnsZero() {
        assertEquals(0, criterium.evaluate(null));
    }

    @Test
    void evaluate_emptyString_returnsZero() {
        assertEquals(0, criterium.evaluate(""));
    }
}
