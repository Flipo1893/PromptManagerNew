package com.promptmanager.promptmanager.criteria;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LengthCriteriumTest {

    private final LengthCriterium criterion = new LengthCriterium();

    @Test
    void evaluate_TextUnter30Zeichen_GibtNull() {
        assertEquals(0, criterion.evaluate("Kurz"));
    }

    @Test
    void evaluate_TextZwischen30Und80_Gibt10() {
        String text = "Das ist ein Text der zwischen 30 und 80 Zeichen lang ist ja";
        assertEquals(10, criterion.evaluate(text));
    }

    @Test
    void evaluate_NullText_GibtNull() {
        assertEquals(0, criterion.evaluate(null));
    }
}