package com.promptmanager.promptmanager.analyzer;

import com.promptmanager.promptmanager.criteria.LengthCriterium;
import com.promptmanager.promptmanager.criteria.QuestionCriterion;
import com.promptmanager.promptmanager.criteria.RoleCriterion;
import com.promptmanager.promptmanager.entity.Prompt;

import java.util.ArrayList;
import java.util.List;

public class StrictPromptAnalyzer {
    private final List<Criterion> criteria;

    public StrictPromptAnalyzer() { //Komposition Lebt nur mit Besitzer
        this.criteria = new ArrayList<>();
        this.criteria.add(new LengthCriterium());
        this.criteria.add(new RoleCriterion());
        this.criteria.add(new QuestionCriterion());
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

