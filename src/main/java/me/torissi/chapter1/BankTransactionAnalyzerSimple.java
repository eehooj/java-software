package me.torissi.chapter1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class BankTransactionAnalyzerSimple {

    private static final String RESOURCES = "src/main/resources";

    public static void main(String[] args) throws IOException {
        final Path path = Paths.get(RESOURCES + args[0]); // 파일 시스템 경로
        final List<String> lines = Files.readAllLines(path); // 행목록 반환
        double total = 0d;

        for (final String line: lines) {
            final String[] columns = line.split(","); // 콤마로 열 분리
            final double amount = Double.parseDouble(columns[1]); // 금액 추출
            total += amount; // 총 금액
        }

        System.out.println("The total for all transactions is " + total);
    }
}
