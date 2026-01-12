package com.promptmanager.promptmanager.criteria;

import com.promptmanager.promptmanager.analyzer.Criterion;
import org.springframework.stereotype.Component;

@Component
public class RoleCriterion implements Criterion {

    @Override
    public int evaluate(String text) {
        if (text == null || text.isBlank()) {
            return 0;
        }

        String lowerText = text.toLowerCase();

        // typische Rollen-Formulierungen
        if (lowerText.contains("du bist ein") ||
                lowerText.contains("du bist eine") ||
                lowerText.contains("ich gebe dir die rolle") ||
                lowerText.contains("handle als") ||
                lowerText.contains("act as")) {
            return 30;
        }

        return 0;
    }
}