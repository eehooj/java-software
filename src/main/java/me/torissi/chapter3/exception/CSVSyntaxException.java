package me.torissi.chapter3.exception;

public class CSVSyntaxException extends RuntimeException {

    public CSVSyntaxException(String message) {
        super(message);
    }
}
