package com.promptmanager.promptmanager.analyzer;

import com.promptmanager.promptmanager.entity.Prompt;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PromptAnalyzer {

    private final List<Criterion> criteria;

    public PromptAnalyzer(List<Criterion> criteria) {
        this.criteria = criteria;
    }

    public Integer analyze(Prompt prompt) {
        if (prompt == null) return 0;

        String text = prompt.getText();
        int total = 0;

        for (Criterion c : criteria) {
            total += c.evaluate(text);
        }

        return total;
    }
}
