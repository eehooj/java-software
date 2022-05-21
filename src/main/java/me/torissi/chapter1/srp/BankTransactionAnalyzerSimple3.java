package me.torissi.chapter1.srp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class BankTransactionAnalyzerSimple3 {

    private static final String RESOURCES = "src/main/resources";

    public static void main(String[] args) throws IOException {
        final BankStatementCSVParser bankStatementCSVParser = new BankStatementCSVParser();

        final String fileName = args[0];
        final Path path = Paths.get(RESOURCES + fileName);
        final List<String> lines = Files.readAllLines(path);
        final List<BankTransaction> bankTransactions = bankStatementCSVParser.parseLinesFromCSV(lines);

        System.out.println("계좌의 총 사용 금액은 " + calulateTotalAmount(bankTransactions));
        System.out.println("1월 달 총 사용 금액은 " + selectInMonth(bankTransactions, Month.JANUARY));
    }

    public static double calulateTotalAmount(final List<BankTransaction> bankTransactions) {
        double total = 0d;

        for (final BankTransaction bankTransaction : bankTransactions) {
            total += bankTransaction.getAmount();
        }

        return total;
    }

    public static List<BankTransaction> selectInMonth(
            final List<BankTransaction> bankTransactions, final Month month) {
        final List<BankTransaction> bankTransactionsInMonth = new ArrayList<>();

        for (final BankTransaction bankTransaction : bankTransactions) {
            if (bankTransaction.getDate().getMonth() == month) {
                bankTransactionsInMonth.add(bankTransaction);
            }
        }

        return bankTransactionsInMonth;
    }
}

/*
* 이 클래스에 정의된 계산 로직은 클래스와 직접적인 관련이 없음 -> 응집도가 떨어짐
* */
