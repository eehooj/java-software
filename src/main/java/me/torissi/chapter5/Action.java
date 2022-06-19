package me.torissi.chapter5;

@FunctionalInterface
interface Action{

    void execute(Facts facts);
}


/*
* Action 인터페이스를 이용하여 비즈니스 규칙 엔진과 구체적 액션의 결합을 제거
* */