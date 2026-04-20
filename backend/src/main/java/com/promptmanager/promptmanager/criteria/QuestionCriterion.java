package com.promptmanager.promptmanager.criteria;

import com.promptmanager.promptmanager.analyzer.Criterion;
import org.springframework.stereotype.Component;

@Component
public class QuestionCriterion implements Criterion {

    @Override
    public int evaluate(String text) {
        if (text == null) return 0;
        return text.contains("?") ? 20 : 0;
    }
}