package me.torissi.chapter5;

import java.util.ArrayList;
import java.util.List;

public class BusinessRuleEngine {

    private final List<Action> actions;

    public BusinessRuleEngine() {
        this.actions = new ArrayList<>();
    }

    public void addAction(final Action action) {
        this.actions.add(action);
    }

    public int count() {
        return this.actions.size();
    }

    public void run() {
        // this.actions.forEach(Action::execute()); // perform 메소드를 언제 만듦..?

    }
}


// 기본 기능을 구현한 기본 API