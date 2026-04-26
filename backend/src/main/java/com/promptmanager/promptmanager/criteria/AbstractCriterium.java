// file: backend/src/main/java/com/promptmanager/promptmanager/criteria/AbstractCriterium.java
package com.promptmanager.promptmanager.criteria;

import com.promptmanager.promptmanager.analyzer.Criterion;

public abstract class AbstractCriterium implements Criterion {
    protected String name;

    protected AbstractCriterium(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }


    protected int normalize(int value, int max) {
        if (max <= 0) return 0;
        return Math.max(0, Math.min(value, max));
    }

    public abstract int evaluate(String text);
}
