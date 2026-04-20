package com.promptmanager.promptmanager.criteria;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoleCriterionTest {

    private final RoleCriterion criterion = new RoleCriterion();

    @Test
    void evaluate_MitDuBistEin_Gibt30() {
        assertEquals(30, criterion.evaluate("Du bist ein erfahrener Entwickler"));
    }

    @Test
    void evaluate_MitActAs_Gibt30() {
        assertEquals(30, criterion.evaluate("Act as a senior software engineer"));
    }

    @Test
    void evaluate_OhneRolle_GibtNull() {
        assertEquals(0, criterion.evaluate("Schreib mir ein Programm"));
    }
}