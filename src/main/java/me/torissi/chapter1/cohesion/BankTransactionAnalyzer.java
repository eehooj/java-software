package me.torissi.chapter1.cohesion;


import me.torissi.chapter1.srp.BankStatementCSVParser;
import me.torissi.chapter1.srp.BankTransaction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Month;
import java.util.List;

public class BankTransactionAnalyzer {

    private static final String RESOURCES = "src/main/resources";
    private static final BankStatementCSVParser bankStatementParser = new BankStatementCSVParser();

    public static void main(String[] args) throws IOException {
        final String fileName = args[0];
        final Path path = Paths.get(RESOURCES + fileName);
        final List<String> lines = Files.readAllLines(path);
        final List<BankTransaction> bankTransactions = bankStatementParser.parseLinesFromCSV(lines);
        final BankStatementProcessor bankStatementProcessor = new BankStatementProcessor(bankTransactions);

        collectSummary(bankStatementProcessor);
    }

    private static void collectSummary(BankStatementProcessor bankStatementProcessor) {
        System.out.println("계좌의 총 사용 금액은 " + bankStatementProcessor.calulateTotalAmount());
        System.out.println("1월 달 총 사용 금액은 " + bankStatementProcessor.calulateTotalInMonth(Month.JANUARY));
        System.out.println("2월 달 총 사용 금액은 " + bankStatementProcessor.calulateTotalInMonth(Month.FEBRUARY));
        System.out.println("총 월급 받은 금액은 " + bankStatementProcessor.calulateTotalForCategory("Salary"));
    }

}
