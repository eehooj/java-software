package me.torissi.chapter5;

import java.util.ArrayList;
import java.util.List;

public class BusinessRuleEngine {

    private final List<Action> actions;
    private final List<Rule> rules;
    private final Facts facts;

    public BusinessRuleEngine(Facts facts) {
        this.actions = new ArrayList<>();
        this.facts = facts;
        this.rules = new ArrayList<>();
    }

    public void addAction(final Action action) {
        this.actions.add(action);
    }

    public int count() {
        return this.actions.size();
    }

    public void addRule(Rule rule) {
        this.rules.add(rule);
    }

    public void run() {
        this.rules.forEach(rule -> rule.perform(facts));
    }
}


// 기본 기능을 구현한 기본 API