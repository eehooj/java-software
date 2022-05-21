package me.torissi.chapter1.cohesion;


import me.torissi.chapter1.srp.BankTransaction;

import java.time.Month;
import java.util.List;

public class BankStatementProcessor {

    private final List<BankTransaction> bankTransactions;

    public BankStatementProcessor(List<BankTransaction> bankTransactions) {
        this.bankTransactions = bankTransactions;
    }

    public double calulateTotalAmount() {
        double total = 0d;

        for (final BankTransaction bankTransaction : bankTransactions) {
            total += bankTransaction.getAmount();
        }

        return total;
    }

    public double calulateTotalInMonth(final Month month) {
        double total = 0d;

        for (final BankTransaction bankTransaction : bankTransactions) {
            if (bankTransaction.getDate().getMonth() == month) {
                total += bankTransaction.getAmount();
            }
        }

        return total;
    }

    public double calulateTotalForCategory(final String category) {
        double total = 0d;

        for (final BankTransaction bankTransaction : bankTransactions) {
            if (bankTransaction.getDescription().equals(category)) {
                total += bankTransaction.getAmount();
            }
        }

        return total;
    }
}
