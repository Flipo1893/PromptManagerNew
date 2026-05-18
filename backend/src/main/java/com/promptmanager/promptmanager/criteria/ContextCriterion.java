package com.promptmanager.promptmanager.criteria;

import org.springframework.stereotype.Component;

@Component
public class ContextCriterion extends AbstractCriterium {

    public ContextCriterion() {
        super("context");
    }

    @Override
    public int evaluate(String text) {
        if (text == null || text.isBlank()) return 0;

        String lower = text.toLowerCase();

        if (lower.contains("kontext") ||
                lower.contains("hintergrund") ||
                lower.contains("background") ||
                lower.contains("context") ||
                lower.contains("ausgangslage") ||
                lower.contains("situation")) {
            return 20;
        }

        return 0;
    }
}
