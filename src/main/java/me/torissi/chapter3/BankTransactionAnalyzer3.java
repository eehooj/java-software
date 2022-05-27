package me.torissi.chapter3;


import me.torissi.chapter2.coupling.BankStatementParser;
import me.torissi.chapter2.srp.BankTransaction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Month;
import java.util.List;

public class BankTransactionAnalyzer3 {

    private static final String RESOURCES = "src/main/resources";

    public void analyze(final String fileName, final BankStatementParser bankStatementParser) throws IOException {
        final Path path = Paths.get(RESOURCES + fileName);
        final List<String> lines = Files.readAllLines(path);
        final List<BankTransaction> bankTransactions = bankStatementParser.parseLinesFrom(lines);
        final BankStatementProcessor bankStatementProcessor = new BankStatementProcessor(bankTransactions);

        collectSummary(bankStatementProcessor);
    }

    private static void collectSummary(BankStatementProcessor bankStatementProcessor) {
        System.out.println("계좌의 총 사용 금액은 " + bankStatementProcessor.calulateTotalAmount());
        System.out.println("1월 달 총 사용 금액은 " + bankStatementProcessor.calulateTotalInMonth(Month.JANUARY));
        System.out.println("2월 달 총 사용 금액은 " + bankStatementProcessor.calulateTotalInMonth(Month.FEBRUARY));
        System.out.println("총 월급 받은 금액은 " + bankStatementProcessor.calulateTotalForCategory("Salary"));
        /*List<BankTransaction> transactions
                = bankStatementProcessor.findTransactions(new BankTransactionIsInFebruaryAndExpensive());*/
        List<BankTransaction> transactions
                = bankStatementProcessor.findTransactions(
                        bankTransaction -> bankTransaction.getDate().getMonth() == Month.FEBRUARY
                                && bankTransaction.getAmount() >= 1000);
    }
}
