package me.torissi.chapter3.notification;

import me.torissi.chapter3.exception.DateInTheFutureException;
import me.torissi.chapter3.exception.DescriptionTooLongException;
import me.torissi.chapter3.exception.InvalidAmountException;
import me.torissi.chapter3.exception.InvalidDateFormat;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class OverlySpecificBankStatementValidator2 {

    private String description;
    private String date;
    private String amount;

    public OverlySpecificBankStatementValidator2(String description, String date, String amount) {
        this.description = description;
        this.date = date;
        this.amount = amount;
    }

    public Notification validate() {
        final Notification notification = new Notification();

        if (this.description.length() > 100) {
            notification.addError("디스크립션이 너무 길다");
        }

        final LocalDate parsedDate;

        try {
            parsedDate = LocalDate.parse(this.date);

            if (parsedDate.isAfter(LocalDate.now())) {
                notification.addError("날짜는 미래로 설정할 수 없음");
            }
        } catch (DateTimeParseException e) {
            notification.addError("부적절한 날짜 포맷");
        }

        final Double amount;

        try {
            amount = Double.parseDouble(this.amount);
        } catch (NumberFormatException e) {
            notification.addError("부적절한 수량 포맷");
        }

        return notification;
    }
}
