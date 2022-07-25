package me.torissi.chapter7;

import java.util.Optional;
import me.torissi.chapter6.FollowStatus;
import me.torissi.chapter6.User;

public interface UserRepository extends AutoCloseable {
  boolean add(User user);

  Optional<User> get(String userId);

  void update(User user);

  void clear();

  FollowStatus follow(User follower, User userToFollow);
}
