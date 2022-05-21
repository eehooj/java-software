package me.torissi.chapter1.coupling;

import java.io.IOException;

public class MainApplication {

    public static void main(String[] args) throws IOException {
        final BankTransactionAnalyzer2 bankTransactionAnalyzer = new BankTransactionAnalyzer2();
        final BankStatementParser bankStatementParser = new BankStatementCSVParser2();

        bankTransactionAnalyzer.analyze(args[0], bankStatementParser);
    }
}
