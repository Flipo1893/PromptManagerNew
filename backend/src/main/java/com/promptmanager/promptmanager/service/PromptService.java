package com.promptmanager.promptmanager.service;

import com.promptmanager.promptmanager.entity.Prompt;
import com.promptmanager.promptmanager.repository.PromptRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromptService {

    private final PromptRepository repo;

    public PromptService(PromptRepository repo) {
        this.repo = repo;
    }

    public Prompt save(Prompt p) {
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
