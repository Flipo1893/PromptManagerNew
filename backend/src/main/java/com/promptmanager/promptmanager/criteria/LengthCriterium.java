package com.promptmanager.promptmanager.criteria;

import com.promptmanager.promptmanager.analyzer.Criterion;
import org.springframework.stereotype.Component;

@Component
public class LengthCriterium implements Criterion {

    @Override
    public int evaluate(String text) {
        if (text == null) return 0;

        int len = text.trim().length();

        if (len < 30) return 0;
        if (len < 80) return 10;
        if (len < 150) return 20;
        return 30;
    }
}
