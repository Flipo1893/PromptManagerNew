package com.promptmanager.promptmanager.service;

import com.promptmanager.promptmanager.analyzer.PromptAnalyzer;
import com.promptmanager.promptmanager.entity.Prompt;
import com.promptmanager.promptmanager.repository.PromptRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PromptService {

    private final PromptRepository repo; //Aggregation Lebt ohne Besitzer
    private final PromptAnalyzer analyzer;

    public PromptService(PromptRepository repo, PromptAnalyzer analyzer) {
        this.repo = repo;
        this.analyzer = analyzer;
    }

    public Prompt save(Prompt p) {
        Integer score = analyzer.analyze(p);
        p.setScore(score);
        return repo.save(p);
    }

    public List<Prompt> getAll() {
        return repo.findAll();
    }

    public List<Prompt> getByModel(String model) {
        return repo.findByAiModel(model);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Prompt nicht gefunden");
        }
        repo.deleteById(id);
    }
}