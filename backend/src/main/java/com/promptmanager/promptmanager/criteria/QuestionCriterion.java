package com.promptmanager.promptmanager.criteria;

import org.springframework.stereotype.Component;

@Component
public class QuestionCriterion extends AbstractCriterium {

    public QuestionCriterion() {
        super("question");
    }

    @Override
    public int evaluate(String text) {
        if (text == null) return 0;
        return text.contains("?") ? 20 : 0;
    }

    public int evaluate(String text, String language) {
        if (text == null) return 0;
        return text.contains("?") ? 20 : 0;
    }
}