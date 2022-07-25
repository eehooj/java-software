package me.torissi.chapter7;

import java.util.Optional;

public interface AbstractRepository<T> {

  void add(T value);

  Optional<T> get(String id);

  void update(T value);

  void delete(T value);
}

/*
* 다양한 저장소를 추상화 할 때 사용하면 좋은 추상화 인터페이스
* */

