package me.torissi.chapter6;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class Twootr {

  public Optional<SenderEndPoint> onLogon(
      final String userId, final String password, final ReceiverEndPoint receiverEndPoint) {

    Objects.requireNonNull(userId, "userId");
    Objects.requireNonNull(password, "password");

    // tag::optional_onLogon[]
    /*var authenticatedUser = userRepository
        .get(userId)
        .filter(userOfSameId ->
        {
          var hashedPassword = KeyGenerator.hash(password, userOfSameId.getSalt());
          return Arrays.equals(hashedPassword, userOfSameId.getPassword());
        });

    authenticatedUser.ifPresent(user ->
    {
      user.onLogon(receiverEndPoint);
      twootRepository.query(
          new TwootQuery()
              .inUsers(user.getFollowing())
              .lastSeenPosition(user.getLastSeenPosition()),
          user::receiveTwoot);
      userRepository.update(user);
    });

    return authenticatedUser.map(user -> new SenderEndPoint(user, this));
    // end::optional_onLogon[]*/
    return null;
  }

  /*FollowStatus onFollow(final User follow, final String userIdToFollow) {
    return userRepository.get(userIdToFollow)
        .map(userToFollow -> userRepository.follow(follow, userToFollow))
        .orElse(INVALID_USER);
  }*/
}
