package com.promptmanager.promptmanager.controller;

import com.promptmanager.promptmanager.entity.Prompt;
import com.promptmanager.promptmanager.service.PromptService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prompts")
@CrossOrigin(origins = "*")
public class PromptController {

    private final PromptService service;

    public PromptController(PromptService service) {
        this.service = service;
    }

    @GetMapping
    public List<Prompt> getAll() {
        return service.getAll();
    }

    @GetMapping("/{model}")
    public List<Prompt> getByModel(@PathVariable String model) {
        return service.getByModel(model);
    }

    @PostMapping
    public Prompt create(@RequestBody Prompt p) {
        return service.save(p);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
