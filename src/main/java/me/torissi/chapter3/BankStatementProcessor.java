package me.torissi.chapter3;


import me.torissi.chapter2.srp.BankTransaction;

import java.time.Month;
import java.util.ArrayList;
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

    // 특정 금액 이상의 은행 거래 내역 찾기
    /*
    * 따로 클래스를 만들지 않고 여기에 추가한 이유는?
    * => 새로 클래스를 추가하면 여러 이름이 생기면서 다양한 동작 간의 관계를 이해하기 어려워짐
    * => 전체 프로젝트가 복잡해짐
    * */
/*    public List<BankTransaction> findTransactionsGreaterThenEqual (final int amount) {
        final List<BankTransaction> result = new ArrayList<>();

        for (final BankTransaction bankTransaction : bankTransactions) {
            if (bankTransaction.getAmount() >= amount) {
                result.add(bankTransaction);
            }
        }

        return result;
    }

    public List<BankTransaction> findTransactionsInMonth (final Month month) {
        final List<BankTransaction> result = new ArrayList<>();

        for (final BankTransaction bankTransaction : bankTransactions) {
            if (bankTransaction.getDate().getMonth() == month) {
                result.add(bankTransaction);
            }
        }

        return result;
    }

    public List<BankTransaction> findTransactionsInMonthAndGreaterThenEqual (final Month month, final int amount) {
        final List<BankTransaction> result = new ArrayList<>();

        for (final BankTransaction bankTransaction : bankTransactions) {
            if (bankTransaction.getDate().getMonth() == month
                    && bankTransaction.getAmount() >= amount) {
                result.add(bankTransaction);
            }
        }

        return result;
    }*/

    public List<BankTransaction> findTransactions(final BankTransactionFilter bankTransactionFilter) {
        final List<BankTransaction> result = new ArrayList<>();

        for (final BankTransaction bankTransaction : bankTransactions) {
            if (bankTransactionFilter.test(bankTransaction)) {
                result.add(bankTransaction);
            }
        }

        return result;
    }
}
