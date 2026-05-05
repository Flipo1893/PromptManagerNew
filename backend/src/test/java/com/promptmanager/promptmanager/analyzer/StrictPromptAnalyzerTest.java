package com.promptmanager.promptmanager.analyzer;

import com.promptmanager.promptmanager.entity.Prompt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StrictPromptAnalyzerTest {

    private StrictPromptAnalyzer analyzer;

    @BeforeEach
    void setUp() {
        analyzer = new StrictPromptAnalyzer();
    }

    @Test
    void analyze_nullPrompt_returnsZero() {
        assertEquals(0, analyzer.analyze(null));
    }

    @Test
    void analyze_nullText_returnsZero() {
        Prompt prompt = new Prompt();
        prompt.setText(null);
        // all three criteria return 0 for null
        assertEquals(0, analyzer.analyze(prompt));
    }

    @Test
    void analyze_shortTextNoRoleNoQuestion_returnsZero() {
        Prompt prompt = new Prompt();
        prompt.setText("Hi");
        // length < 30 -> 0, no role -> 0, no "?" -> 0
        assertEquals(0, analyzer.analyze(prompt));
    }

    @Test
    void analyze_mediumTextWithRoleAndQuestion_returnsCombinedScore() {
        Prompt prompt = new Prompt();
        // 49 chars (>= 30 < 80): length=10, "du bist ein": role=30, "?": question=20 => total=60
        prompt.setText("Du bist ein Experte. Kannst du mir helfen damit??");
        assertEquals(60, analyzer.analyze(prompt));
    }

    @Test
    void analyze_longTextRoleAndQuestion_returnsEighty() {
        Prompt prompt = new Prompt();
        // >= 150 chars -> length=30, role=30, question=20 => total=80
        String text = "Du bist ein erfahrener Java-Entwickler. " + "Bitte erklaere mir das Thema ausfuehrlich. ".repeat(4) + "?";
        prompt.setText(text);
        assertEquals(80, analyzer.analyze(prompt));
    }

    @Test
    void analyze_longTextNoRoleNoQuestion_returnsThirty() {
        Prompt prompt = new Prompt();
        prompt.setText("a".repeat(200));
        // length >= 150 -> 30, no role -> 0, no "?" -> 0
        assertEquals(30, analyzer.analyze(prompt));
    }

    @Test
    void analyze_emptyText_returnsZero() {
        Prompt prompt = new Prompt();
        prompt.setText("");
        assertEquals(0, analyzer.analyze(prompt));
    }

    @Test
    void analyze_mediumTextWithOnlyQuestion_returnsThirty() {
        Prompt prompt = new Prompt();
        // 36 chars: length->10, no role->0, "?"->20 => total 30
        prompt.setText("Kannst du mir bitte etwas erklaeren?");
        assertEquals(30, analyzer.analyze(prompt));
    }

    @Test
    void analyze_actAsKeyword_includesRoleScore() {
        Prompt prompt = new Prompt();
        prompt.setText("Act as a senior developer and help me!");
        // 38 chars -> 10, "act as" -> 30, no "?" -> 0 => 40
        assertEquals(40, analyzer.analyze(prompt));
    }
}
