package com.promptmanager.promptmanager.service;

import com.promptmanager.promptmanager.analyzer.PromptAnalyzer;
import com.promptmanager.promptmanager.entity.Prompt;
import com.promptmanager.promptmanager.repository.PromptRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromptServiceTest {

    @Mock
    private PromptRepository repo;

    @Mock
    private PromptAnalyzer analyzer;

    @InjectMocks
    private PromptService service;

    private Prompt prompt;

    @BeforeEach
    void setUp() {
        prompt = new Prompt();
        prompt.setId(1L);
        prompt.setAiModel("GPT-4");
        prompt.setTitle("Test Prompt");
        prompt.setText("Du bist ein erfahrener Entwickler. Erkläre mir Java.");
    }

    @Test
    void save_setsScoreFromAnalyzerAndSavesToRepo() {
        when(analyzer.analyze(prompt)).thenReturn(42);
        when(repo.save(prompt)).thenReturn(prompt);

        Prompt result = service.save(prompt);

        verify(analyzer).analyze(prompt);
        verify(repo).save(prompt);
        assertEquals(42, result.getScore());
        assertSame(prompt, result);
    }

    @Test
    void save_analyzerReturnsZero_setsScoreToZero() {
        when(analyzer.analyze(prompt)).thenReturn(0);
        when(repo.save(prompt)).thenReturn(prompt);

        Prompt result = service.save(prompt);

        assertEquals(0, result.getScore());
    }

    @Test
    void save_analyzerReturnsMaxScore_setsCorrectScore() {
        when(analyzer.analyze(prompt)).thenReturn(80);
        when(repo.save(prompt)).thenReturn(prompt);

        Prompt result = service.save(prompt);

        assertEquals(80, result.getScore());
    }

    @Test
    void getAll_returnsAllPrompts() {
        Prompt second = new Prompt();
        second.setId(2L);
        List<Prompt> expected = Arrays.asList(prompt, second);
        when(repo.findAll()).thenReturn(expected);

        List<Prompt> result = service.getAll();

        verify(repo).findAll();
        assertEquals(2, result.size());
        assertEquals(expected, result);
    }

    @Test
    void getAll_emptyRepo_returnsEmptyList() {
        when(repo.findAll()).thenReturn(Collections.emptyList());

        List<Prompt> result = service.getAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void getByModel_returnsMatchingPrompts() {
        List<Prompt> expected = List.of(prompt);
        when(repo.findByAiModel("GPT-4")).thenReturn(expected);

        List<Prompt> result = service.getByModel("GPT-4");

        verify(repo).findByAiModel("GPT-4");
        assertEquals(1, result.size());
        assertSame(prompt, result.get(0));
    }

    @Test
    void getByModel_noMatch_returnsEmptyList() {
        when(repo.findByAiModel("unknown-model")).thenReturn(Collections.emptyList());

        List<Prompt> result = service.getByModel("unknown-model");

        assertTrue(result.isEmpty());
    }

    @Test
    void delete_callsDeleteByIdOnRepo() {
        doNothing().when(repo).deleteById(1L);

        service.delete(1L);

        verify(repo).deleteById(1L);
    }

    @Test
    void delete_differentId_passesCorrectId() {
        doNothing().when(repo).deleteById(99L);

        service.delete(99L);

        verify(repo).deleteById(99L);
        verify(repo, never()).deleteById(1L);
    }
}
