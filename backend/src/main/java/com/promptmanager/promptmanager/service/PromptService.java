package com.promptmanager.promptmanager.service;

import com.promptmanager.promptmanager.analyzer.PromptAnalyzer;
import com.promptmanager.promptmanager.entity.Prompt;
import com.promptmanager.promptmanager.repository.PromptRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromptService {

    private final PromptRepository repo;
    private final PromptAnalyzer analyzer;

    public PromptService(PromptRepository repo,PromptAnalyzer analyzer) {
        this.repo = repo;
        this.analyzer = analyzer;
    }

    public Prompt save(Prompt p) {
        int score = analyzer.analyze(p);
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
        repo.deleteById(id);
    }
}
