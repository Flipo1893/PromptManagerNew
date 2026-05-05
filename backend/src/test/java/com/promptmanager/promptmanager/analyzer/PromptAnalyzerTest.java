package com.promptmanager.promptmanager.analyzer;

import com.promptmanager.promptmanager.entity.Prompt;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromptAnalyzerTest {

    @Mock
    private Criterion criterion1;

    @Mock
    private Criterion criterion2;

    @Test
    void analyze_nullPrompt_returnsZeroWithoutCallingCriteria() {
        PromptAnalyzer analyzer = new PromptAnalyzer(List.of(criterion1, criterion2));

        Integer result = analyzer.analyze(null);

        assertEquals(0, result);
        verifyNoInteractions(criterion1, criterion2);
    }

    @Test
    void analyze_sumsCriteriaScores() {
        PromptAnalyzer analyzer = new PromptAnalyzer(Arrays.asList(criterion1, criterion2));
        Prompt prompt = new Prompt();
        prompt.setText("Du bist ein Experte.");

        when(criterion1.evaluate("Du bist ein Experte.")).thenReturn(10);
        when(criterion2.evaluate("Du bist ein Experte.")).thenReturn(20);

        Integer result = analyzer.analyze(prompt);

        assertEquals(30, result);
        verify(criterion1).evaluate("Du bist ein Experte.");
        verify(criterion2).evaluate("Du bist ein Experte.");
    }

    @Test
    void analyze_nullTextInPrompt_passesNullToCriteria() {
        PromptAnalyzer analyzer = new PromptAnalyzer(Arrays.asList(criterion1, criterion2));
        Prompt prompt = new Prompt();
        prompt.setText(null);

        when(criterion1.evaluate(null)).thenReturn(0);
        when(criterion2.evaluate(null)).thenReturn(0);

        Integer result = analyzer.analyze(prompt);

        assertEquals(0, result);
        verify(criterion1).evaluate(null);
        verify(criterion2).evaluate(null);
    }

    @Test
    void analyze_noCriteria_returnsZero() {
        PromptAnalyzer analyzer = new PromptAnalyzer(Collections.emptyList());
        Prompt prompt = new Prompt();
        prompt.setText("some text");

        assertEquals(0, analyzer.analyze(prompt));
    }

    @Test
    void analyze_singleCriterion_returnsItsScore() {
        PromptAnalyzer analyzer = new PromptAnalyzer(List.of(criterion1));
        Prompt prompt = new Prompt();
        prompt.setText("hello");

        when(criterion1.evaluate("hello")).thenReturn(15);

        assertEquals(15, analyzer.analyze(prompt));
    }

    @Test
    void analyze_allCriteriaReturnZero_returnsZero() {
        PromptAnalyzer analyzer = new PromptAnalyzer(Arrays.asList(criterion1, criterion2));
        Prompt prompt = new Prompt();
        prompt.setText("text");

        when(criterion1.evaluate("text")).thenReturn(0);
        when(criterion2.evaluate("text")).thenReturn(0);

        assertEquals(0, analyzer.analyze(prompt));
    }

    @Test
    void analyze_eachCriterionCalledExactlyOnce() {
        PromptAnalyzer analyzer = new PromptAnalyzer(Arrays.asList(criterion1, criterion2));
        Prompt prompt = new Prompt();
        prompt.setText("test");

        when(criterion1.evaluate(anyString())).thenReturn(5);
        when(criterion2.evaluate(anyString())).thenReturn(5);

        analyzer.analyze(prompt);

        verify(criterion1, times(1)).evaluate("test");
        verify(criterion2, times(1)).evaluate("test");
    }
}
