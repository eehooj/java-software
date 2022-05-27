package me.torissi.chapter3;

import me.torissi.chapter2.srp.BankTransaction;

@FunctionalInterface
public interface BankTransactionSummarizer {

    double summarize(double accumulator, BankTransaction bankTransaction);
}
