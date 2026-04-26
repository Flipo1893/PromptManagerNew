package com.promptmanager.promptmanager.criteria;

import org.springframework.stereotype.Component;

@Component
public class RoleCriterion extends AbstractCriterium {

    public RoleCriterion() {
        super("role");
    }

    @Override
    public int evaluate(String text) {
        if (text == null || text.isBlank()) {
            return 0;
        }

        String lowerText = text.toLowerCase();


        if (lowerText.contains("du bist eine") ||
                lowerText.contains("du bist ein") ||
                lowerText.contains("ich gebe dir die rolle") ||
                lowerText.contains("handle als") ||
                lowerText.contains("act as")) {
            return 30;
        }

        return 0;
    }
}