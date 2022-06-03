package me.torissi.chapter4;

import java.util.Map;

public class Document {

    private final Map<String, String> attributes;

    Document(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getAttribute(final String attributeName) {
        return attributes.get(attributeName);
    }
}

/*
* 생성자를 public으로 만들면 프로젝트 어디서나 그 형식의 객체를 만들 수 있는 문제가 발생
* 오직 문서 관리 시스템에서만 Document를 만들 수 있어야 하므로 패키지 영역으로 접근을 제한함
* */
