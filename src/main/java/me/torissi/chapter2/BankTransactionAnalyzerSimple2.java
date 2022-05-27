package me.torissi.chapter2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BankTransactionAnalyzerSimple2 {

    private static final String RESOURCES = "src/main/resources";

    public static void main(String[] args) throws IOException {
        final Path path = Paths.get(RESOURCES + args[0]); // 파일 시스템 경로
        final List<String> lines = Files.readAllLines(path); // 행목록 반환
        double total = 0d;
        final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (final String line: lines) {
            final String[] columns = line.split(","); // 콤마로 열 분리
            final LocalDate date = LocalDate.parse(columns[0], DATE_PATTERN);

            if (date.getMonth() == Month.JANUARY) {
                final double amount = Double.parseDouble(columns[1]); // 금액 추출
                total += amount; // 총 금액
            }
        }

        System.out.println("The total for all transactions is " + total);
    }
}
