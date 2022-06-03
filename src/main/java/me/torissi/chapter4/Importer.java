package me.torissi.chapter4;

import java.io.File;
import java.io.IOException;

public interface Importer {

    Document importFile(File file) throws IOException;
}

/*
* 강한형식 (strong typed) 원칙사용
* - String 대신 파일을 가리키는 전용 형식을 이용하여 오류가 발생할 범위를 줄임
* */