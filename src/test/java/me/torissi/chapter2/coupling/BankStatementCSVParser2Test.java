package me.torissi.chapter2.coupling;

import me.torissi.chapter2.srp.BankTransaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

class BankStatementCSVParser2Test {

    private final BankStatementParser statementParser = new BankStatementCSVParser2();

    @Test
    public void shouldParseOneCorrectLine() throws Exception {
        // Given
        final String line = "2022-01-21,-50,Testco";

        // When
        final BankTransaction result = statementParser.parseFrom(line);
        final BankTransaction expected = new BankTransaction(
                LocalDate.of(2022, Month.JANUARY, 21), -50, "Testco");
        final double tolerance = 0.0;

        // Then
        Assertions.assertEquals(expected.getDate(), result.getDate());
        Assertions.assertEquals(expected.getAmount(), result.getAmount(), tolerance);
        Assertions.assertEquals(expected.getDescription(), result.getDescription());
    }

}