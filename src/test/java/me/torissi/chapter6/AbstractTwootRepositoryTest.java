package me.torissi.chapter6;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;

public class AbstractTwootRepositoryTest {

  @Test
  @Description("유효한 사용자명으로 시스템에 로그인 했을 때 성공적으로 인증되는가")
  public void shouldBeAbleToAuthenticateUser() {
    /*
    * 사용자가 로그인하면 UI로 유효한 SenderEndPoint 객체를 반환
    * 로그인 이벤트가 발생했으며 테스트 코드가 컴파일 퇴도록 메소드 추가 필요
    * */

    //유효 사용자의 로그온 메시지 수신

    // 로그온 메소드는 새 엔드포인트 반환

    // 엔드포인트 유효성을 확인하는 어서션션
  }

  @Test
  @Description("로그인한 사용자가 정확한 비밀번호를 사용했는지 검사")
  public void shouldNotAuthenticateUserWithWrongPassword() {
    Twootr twootr = new Twootr();
    final ReceiverEndPoint receiverEndPoint = null;

    final Optional<SenderEndPoint> endPoint = twootr.onLogon(
        TestData.USER_ID, "bad password", receiverEndPoint
    );

    Assertions.assertFalse(endPoint.isPresent());
  }
}


