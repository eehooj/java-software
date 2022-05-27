package me.torissi.chapter3;

import me.torissi.chapter2.srp.BankTransaction;

@FunctionalInterface
public interface BankTransactionFilter {

    boolean test(BankTransaction bankTransaction);
}

/* @FunctionalInterface
* 추상 메소드가 딱 하나만 존재하는 인터페이스
* 람다식은 함수형 인터페이스로만 접근이 가능
* default method 또는 static method 는 여러 개 존재해도 상관 없음
* 해당 인터페이스가 함수형 인터페이스 조건에 맞는지 검사
* 없어도 함수형 인터페이스 사용 가능.. (인터페이스 검증과 유지보수를 위해 붙이는 것이 좋음)
* */