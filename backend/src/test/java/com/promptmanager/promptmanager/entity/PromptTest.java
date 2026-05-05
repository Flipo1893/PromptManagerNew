package com.promptmanager.promptmanager.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PromptTest {

    private Prompt prompt;

    @BeforeEach
    void setUp() {
        prompt = new Prompt();
    }

    @Test
    void newPrompt_idIsNull() {
        assertNull(prompt.getId());
    }

    @Test
    void setId_andGetId_returnsCorrectValue() {
        prompt.setId(42L);
        assertEquals(42L, prompt.getId());
    }

    @Test
    void setAiModel_andGetAiModel_returnsCorrectValue() {
        prompt.setAiModel("GPT-4");
        assertEquals("GPT-4", prompt.getAiModel());
    }

    @Test
    void setTitle_andGetTitle_returnsCorrectValue() {
        prompt.setTitle("My Prompt");
        assertEquals("My Prompt", prompt.getTitle());
    }

    @Test
    void setCategory_andGetCategory_returnsCorrectValue() {
        prompt.setCategory("coding");
        assertEquals("coding", prompt.getCategory());
    }

    @Test
    void setText_andGetText_returnsCorrectValue() {
        prompt.setText("Du bist ein Experte.");
        assertEquals("Du bist ein Experte.", prompt.getText());
    }

    @Test
    void setScore_andGetScore_returnsCorrectValue() {
        prompt.setScore(75);
        assertEquals(75, prompt.getScore());
    }

    @Test
    void setId_null_returnsNull() {
        prompt.setId(1L);
        prompt.setId(null);
        assertNull(prompt.getId());
    }

    @Test
    void setAiModel_null_returnsNull() {
        prompt.setAiModel(null);
        assertNull(prompt.getAiModel());
    }

    @Test
    void setTitle_null_returnsNull() {
        prompt.setTitle(null);
        assertNull(prompt.getTitle());
    }

    @Test
    void setCategory_null_returnsNull() {
        prompt.setCategory(null);
        assertNull(prompt.getCategory());
    }

    @Test
    void setText_null_returnsNull() {
        prompt.setText(null);
        assertNull(prompt.getText());
    }

    @Test
    void setScore_null_returnsNull() {
        prompt.setScore(null);
        assertNull(prompt.getScore());
    }

    @Test
    void setScore_zero_returnsZero() {
        prompt.setScore(0);
        assertEquals(0, prompt.getScore());
    }

    @Test
    void setScore_negativeValue_returnsNegativeValue() {
        prompt.setScore(-1);
        assertEquals(-1, prompt.getScore());
    }
}
