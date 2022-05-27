package me.torissi.chapter3;


import me.torissi.chapter2.coupling.BankStatementParser;
import me.torissi.chapter2.srp.BankTransaction;
import me.torissi.chapter3.exception.CSVSyntaxException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BankStatementCSVParser3 implements BankStatementParser {

    final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    final Long EXPECTED_ATTRIBUTES_LENGTH = 100L;

    @Override
    public BankTransaction parseFrom(String line) {
        final String[] columns = line.split(",");

        if (columns.length < EXPECTED_ATTRIBUTES_LENGTH) {
            throw new CSVSyntaxException("길이 에러");
        }

        final LocalDate date = LocalDate.parse(columns[0], DATE_PATTERN);
        final double amount = Double.parseDouble(columns[1]);
        final String description = columns[2];

        return new BankTransaction(date, amount, description);
    }

    @Override
    public List<BankTransaction> parseLinesFrom(List<String> lines) {
        final List<BankTransaction> bankTransactions = new ArrayList<>();

        for (final String line: lines) {
            bankTransactions.add(this.parseFrom(line));
        }

        return bankTransactions;
    }
}
