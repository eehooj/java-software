package me.torissi.chapter1.coupling;


import me.torissi.chapter1.srp.BankTransaction;

import java.util.List;

public interface BankStatementParser {

    BankTransaction parseFrom(String line);
    List<BankTransaction> parseLinesFrom(List<String> lines);
}
