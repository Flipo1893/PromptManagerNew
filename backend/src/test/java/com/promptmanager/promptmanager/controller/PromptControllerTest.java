package com.promptmanager.promptmanager.controller;

import com.promptmanager.promptmanager.entity.Prompt;
import com.promptmanager.promptmanager.service.PromptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PromptControllerTest {

    @Mock
    private PromptService service;

    @InjectMocks
    private PromptController controller;

    private MockMvc mockMvc;
    private Prompt prompt;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        prompt = new Prompt();
        prompt.setId(1L);
        prompt.setAiModel("GPT-4");
        prompt.setTitle("Test Prompt");
        prompt.setText("Du bist ein Experte.");
        prompt.setScore(50);
    }

    @Test
    void getAll_returnsOkWithListOfPrompts() throws Exception {
        when(service.getAll()).thenReturn(List.of(prompt));

        mockMvc.perform(get("/api/prompts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].aiModel").value("GPT-4"))
                .andExpect(jsonPath("$[0].score").value(50));

        verify(service).getAll();
    }

    @Test
    void getAll_emptyList_returnsEmptyArray() throws Exception {
        when(service.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/prompts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getAll_multiplePrompts_returnsAll() throws Exception {
        Prompt second = new Prompt();
        second.setId(2L);
        second.setAiModel("Claude");
        when(service.getAll()).thenReturn(Arrays.asList(prompt, second));

        mockMvc.perform(get("/api/prompts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void getByModel_returnsFilteredPrompts() throws Exception {
        when(service.getByModel("GPT-4")).thenReturn(List.of(prompt));

        mockMvc.perform(get("/api/prompts/GPT-4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].aiModel").value("GPT-4"));

        verify(service).getByModel("GPT-4");
    }

    @Test
    void getByModel_noMatch_returnsEmptyArray() throws Exception {
        when(service.getByModel("unknown")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/prompts/unknown"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void create_savesAndReturnsCreatedPrompt() throws Exception {
        when(service.save(any(Prompt.class))).thenReturn(prompt);

        mockMvc.perform(post("/api/prompts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"aiModel\":\"GPT-4\",\"title\":\"Test Prompt\",\"category\":null,\"text\":\"Du bist ein Experte.\",\"score\":50}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.score").value(50));

        verify(service).save(any(Prompt.class));
    }

    @Test
    void create_callsServiceSaveExactlyOnce() throws Exception {
        when(service.save(any(Prompt.class))).thenReturn(prompt);

        mockMvc.perform(post("/api/prompts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"aiModel\":\"GPT-4\",\"title\":\"Test Prompt\",\"category\":null,\"text\":\"Du bist ein Experte.\",\"score\":50}"));

        verify(service, times(1)).save(any(Prompt.class));
    }

    @Test
    void delete_callsServiceDeleteAndReturnsOk() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/api/prompts/1"))
                .andExpect(status().isOk());

        verify(service).delete(1L);
    }

    @Test
    void delete_differentId_passesCorrectIdToService() throws Exception {
        doNothing().when(service).delete(99L);

        mockMvc.perform(delete("/api/prompts/99"))
                .andExpect(status().isOk());

        verify(service).delete(99L);
        verify(service, never()).delete(1L);
    }
}
