package me.torissi.chapter3;

import me.torissi.chapter3.exception.DateInTheFutureException;
import me.torissi.chapter3.exception.DescriptionTooLongException;
import me.torissi.chapter3.exception.InvalidAmountException;
import me.torissi.chapter3.exception.InvalidDateFormat;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class OverlySpecificBankStatementValidator {

    private String description;
    private String date;
    private String amount;

    public OverlySpecificBankStatementValidator(String description, String date, String amount) {
        this.description = description;
        this.date = date;
        this.amount = amount;
    }

    public boolean validate() throws DescriptionTooLongException,
            InvalidDateFormat, DateInTheFutureException, InvalidAmountException {
        if (this.description.length() > 100) {
            throw new DescriptionTooLongException();
        }

        final LocalDate parsedDate;

        try {
            parsedDate = LocalDate.parse(this.date);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormat();
        }

        if (parsedDate.isAfter(LocalDate.now())) {
            throw new DateInTheFutureException();
        }

        try {
            Double.parseDouble(this.amount);
        } catch (NumberFormatException e) {
            throw new InvalidAmountException();
        }

        return true;
    }

    public boolean validate2() throws DescriptionTooLongException,
            InvalidDateFormat, DateInTheFutureException, InvalidAmountException {
        if (this.description.length() > 100) {
            throw new IllegalArgumentException("The Description Too Long");
        }

        final LocalDate parsedDate;

        try {
            parsedDate = LocalDate.parse(this.date);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("부적절한 날짜 포맷", e);
        }

        if (parsedDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("날짜는 미래로 설정할 수 없음");
        }

        try {
            Double.parseDouble(this.amount);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("부적절한 amount 포맷", e);
        }

        return true;
    }
}
