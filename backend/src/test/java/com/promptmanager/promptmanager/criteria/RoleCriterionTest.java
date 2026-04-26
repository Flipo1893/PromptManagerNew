package com.promptmanager.promptmanager.criteria;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoleCriterionTest {

    private RoleCriterion criterion;

    @BeforeEach
    void setUp() {
        criterion = new RoleCriterion();
    }


    @Test
    void evaluate_nullText_returnsZero() {
        assertEquals(0, criterion.evaluate(null));
    }


    @Test
    void evaluate_blankText_returnsZero() {
        assertEquals(0, criterion.evaluate("   "));
    }


    @Test
    void evaluate_emptyText_returnsZero() {
        assertEquals(0, criterion.evaluate(""));
    }


    @Test
    void evaluate_noRoleKeyword_returnsZero() {
        assertEquals(0, criterion.evaluate("Erkläre mir wie Java funktioniert"));
    }


    @Test
    void evaluate_duBistEin_returnsThirty() {
        assertEquals(30, criterion.evaluate("Du bist ein erfahrener Entwickler"));
    }


    @Test
    void evaluate_duBistEine_returnsThirty() {
        assertEquals(30, criterion.evaluate("Du bist eine erfahrene Lehrerin"));
    }


    @Test
    void evaluate_ichGebeDirDieRolle_returnsThirty() {
        assertEquals(30, criterion.evaluate("Ich gebe dir die Rolle eines Coaches"));
    }


    @Test
    void evaluate_handleAls_returnsThirty() {
        assertEquals(30, criterion.evaluate("Handle als professioneller Arzt"));
    }


    @Test
    void evaluate_actAs_returnsThirty() {
        assertEquals(30, criterion.evaluate("Act as a senior software engineer"));
    }


    @Test
    void evaluate_uppercaseKeyword_returnsThirty() {
        assertEquals(30, criterion.evaluate("DU BIST EIN Experte"));
    }
}