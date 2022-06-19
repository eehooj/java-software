package me.torissi.chapter5;

public class RuleBuilder {

    private Condition condition;

    private RuleBuilder(Condition condition) {
        this.condition = condition;
    }

    public static RuleBuilder when(Condition condition) {
        return new RuleBuilder(condition);
    }

    public Rule then(Action action) {
        return new Rule(condition, action);
    }
}

/*
* 1. 사용자가 명시적으로 생성자를 호출하지 못하도록 생성자를 비공개로 설정
* 2. when()를 정적 메소드로 만들어서 이 메소드를 사용자가 직접 호출하면 예전 생성자를 호출하도록 함
* 3. then()이 DefaultRule 객체의 최종 생성을 책임짐짐
*
 */
