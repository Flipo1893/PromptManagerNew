import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

class StrictPromptAnalyzerTest {
    private StrictPromptAnalyzer strictPromptAnalyzer;
    private LengthCriterium lengthCriterium;
    private RoleCriterion roleCriterion;
    private QuestionCriterion questionCriterion;

    @BeforeEach
    void setUp() {
        lengthCriterium = mock(LengthCriterium.class);
        roleCriterion = mock(RoleCriterion.class);
        questionCriterion = mock(QuestionCriterion.class);
        strictPromptAnalyzer = new StrictPromptAnalyzer(Arrays.asList(lengthCriterium, roleCriterion, questionCriterion));
    }

    @Test
    void testAnalyze_NullPrompt() {
        assertThrows(IllegalArgumentException.class, () -> strictPromptAnalyzer.analyze(null));
    }

    @Test
    void testAnalyze_EmptyText() {
        Prompt prompt = new Prompt("");
        EvaluationResult result = strictPromptAnalyzer.analyze(prompt);
        assertEquals(0, result.getScore());
        assertFalse(result.getCriteriaMet().contains(lengthCriterium));
    }

    @Test
    void testAnalyze_SingleCriteriaMet() {
        Prompt prompt = new Prompt("Valid text");
        when(lengthCriterium.evaluate(prompt)).thenReturn(true);
        EvaluationResult result = strictPromptAnalyzer.analyze(prompt);
        assertEquals(50, result.getScore());
        assertTrue(result.getCriteriaMet().contains(lengthCriterium));
        assertFalse(result.getCriteriaMet().contains(roleCriterion));
        assertFalse(result.getCriteriaMet().contains(questionCriterion));
    }

    @Test
    void testAnalyze_MultipleCriteriaMet() {
        Prompt prompt = new Prompt("Another valid text");
        when(lengthCriterium.evaluate(prompt)).thenReturn(true);
        when(roleCriterion.evaluate(prompt)).thenReturn(true);
        EvaluationResult result = strictPromptAnalyzer.analyze(prompt);
        assertEquals(100, result.getScore());
        assertTrue(result.getCriteriaMet().contains(lengthCriterium));
        assertTrue(result.getCriteriaMet().contains(roleCriterion));
    }

    @Test
    void testAnalyze_NonCriteriaMet() {
        Prompt prompt = new Prompt("Invalid text");
        when(lengthCriterium.evaluate(prompt)).thenReturn(false);
        when(roleCriterion.evaluate(prompt)).thenReturn(false);
        EvaluationResult result = strictPromptAnalyzer.analyze(prompt);
        assertEquals(0, result.getScore());
        assertTrue(result.getCriteriaMet().isEmpty());
    }

    @Test
    void testAnalyze_ScoreCalculationWithAllCriteria() {
        Prompt prompt = new Prompt("Text meeting all criteria");
        when(lengthCriterium.evaluate(prompt)).thenReturn(true);
        when(roleCriterion.evaluate(prompt)).thenReturn(true);
        when(questionCriterion.evaluate(prompt)).thenReturn(true);
        EvaluationResult result = strictPromptAnalyzer.analyze(prompt);
        assertEquals(100, result.getScore());
        assertEquals(3, result.getCriteriaMet().size()); // all criteria met
    }

    @Test
    void testAnalyze_ScoreCalculationWithSomeCriteria() {
        Prompt prompt = new Prompt("Text meeting some criteria");
        when(lengthCriterium.evaluate(prompt)).thenReturn(true);
        when(roleCriterion.evaluate(prompt)).thenReturn(false);
        when(questionCriterion.evaluate(prompt)).thenReturn(true);
        EvaluationResult result = strictPromptAnalyzer.analyze(prompt);
        assertEquals(67, result.getScore()); // 2/3 criteria met
        assertTrue(result.getCriteriaMet().contains(lengthCriterium));
        assertTrue(result.getCriteriaMet().contains(questionCriterion));
        assertFalse(result.getCriteriaMet().contains(roleCriterion));
    }

    @Test
    void testAnalyze_ScoreWhenNoCriteriaMet() {
        Prompt prompt = new Prompt("Text with no criteria");
        when(lengthCriterium.evaluate(prompt)).thenReturn(false);
        when(roleCriterion.evaluate(prompt)).thenReturn(false);
        when(questionCriterion.evaluate(prompt)).thenReturn(false);
        EvaluationResult result = strictPromptAnalyzer.analyze(prompt);
        assertEquals(0, result.getScore());
    }

    @Test
    void testAnalyze_HandleNullPromptInCriteria() {
        Prompt prompt = new Prompt("Text");
        when(lengthCriterium.evaluate(any(Prompt.class))).thenThrow(new IllegalArgumentException());
        assertThrows(IllegalArgumentException.class, () -> strictPromptAnalyzer.analyze(prompt));
    }
}