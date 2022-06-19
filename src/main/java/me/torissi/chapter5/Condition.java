package me.torissi.chapter5;

@FunctionalInterface
public interface Condition {

    boolean evaluate(Facts facts);

}
