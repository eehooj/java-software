package me.torissi.chapter6;

import java.util.Objects;
import javax.swing.text.Position;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;

public class SenderEndPoint {

  private final User user;
  private final Twootr twootr;

  SenderEndPoint(final User user, final Twootr twootr) {
      Objects.requireNonNull(user, "user");
      Objects.requireNonNull(twootr, "twootr");

      this.user = user;
      this.twootr = twootr;
  }

  public FollowStatus onFollow (final String userIdToFollow) {
    Objects.requireNonNull(userIdToFollow, "userIdToFollow");

    return null;
  }
}
