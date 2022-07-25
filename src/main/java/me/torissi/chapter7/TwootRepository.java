package me.torissi.chapter7;

import java.util.Optional;
import java.util.function.Consumer;
import me.torissi.chapter6.Twoot;

public interface TwootRepository {
  Twoot add(String id, String userId, String content);

  Optional<Twoot> get(String id);

  void delete(Twoot twoot);

  void query(TwootQuery twootQuery, Consumer<Twoot> callback);

  void clear();
}
