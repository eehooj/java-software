package me.torissi.chapter6;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.swing.text.Position;

public class User {

  private final String id;
  private final byte[] password;
  private final byte[] salt;
  private final Set<User> followers = new HashSet<>();
  private final Set<String> following = new HashSet<>();

  private Position lastSeenPosition;
  private ReceiverEndPoint receiverEndPoint;

  public User(
      final String id,
      final byte[] password,
      final byte[] salt,
      final Position lastSeenPosition) {

    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(password, "password");

    this.id = id;
    this.password = password;
    this.salt = salt;
    this.lastSeenPosition = lastSeenPosition;
  }

}
