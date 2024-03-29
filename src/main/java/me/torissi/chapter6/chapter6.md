# 트우터

## 목표
1. 큰 그림을 서로 다른 작은 아키텍처 문제로 쪼개는 방법
2. 테스트 더블(모킹)로 코드베이스의 다른 컴포넌트와의 상호작용을 고립하고 테스트 하는 방법
3. 요구 사항이 응용 프로그램 도메인 코어로 이어지도록 뒤집어 생각하는 방법

## 요구사항
- 트웃 : 사용자가 게시하는 각각의 마이크로블로그
- 스트림 : 사용자는 연속적인 트웃 스트림을 갖음
- 팔로우 : 다른 사용자를 팔로우해야 그 사용자가 무엇을 트우팅했는지 알 수 있음
1. 고유의 사용자 ID롸 비밀번호로 트우터에 로그인
2. 각 사용자는 자신이 팔로우하는 사용자 집합을 갖음
3. 사용자는 트웃을 전송할 수 있으며 로그인한 모든 팔로워는 이 트웃을 바로 볼 수 있음
4. 사용자가 로그인하면 최종 로그인한 이후로 게시된 팔로워의 모든 트웃을 볼 수 있음
5. 자신의 트웃을 삭제할 수 있으며, 팔로워는 삭제된 트웃을 볼 수 없음
6. 사용자는 모바일이나 웹사이트로 로그인할 수 있음

=> 핵심은 팔로워의 트웃을 바로 볼 수 있는 기능

## 설계 개요
> * 다양한 환경의 사용자가 어떻게 서로 통신할 수 있을까?
>   * 보통은 클라이언트 서버 모델로 해결
>       * 컴퓨터를 클라이언트와 서버 두 그룹으로 분리함
>       * 클라이언트 그룹 : 서비스를 사용하는 그룹
>       * 서버 그룹 : 관련 서비스를 제공하는 그룹
>       * 예제에서는 웹사이트나 스마트폰 응용프로그램이 클라이언트 그룹이고, 트우터 서버와 통신하는 UI를 제공
>       * 서버는 대부분의 비즈니스 로직을 처리하고, 다양한 클라이언트로부터 트웃을 받거나 전송
> 
> 1. 풀기반 (pull-based)
>   * 풀기반 통신은 점대점(point-to-point) 또는 요청 응답(request-response) 통신 형식으로 불림
>   * 클라이언트가 서버로 정보를 요청
>   * 대부분의 웹에서 이 형식을 사용
>   * 웹 사이트로 접속할 때 클라이언트는 서버로 HTTP 요청을 보내 페이지의 데이터를 가져옴
>   * 클라이언트가 어떤 정보를 가져올 지 결정하는 상항에서 유용
> 
> 2. 푸시기반 (push-based)
>   * 리액티브(reactive) 또는 이벤트 주도(event-driven) 통신이라 불림
>   * 작성자가 방출한 이벤트 스트림을 여러 구독자가 수신
>   * 일대일 통신 뿐만 아니라 일대다 통신도 지원
>   * 여러 컴포넌트 간에 다양한 이벤트의 의사소통이 발생하는 상황에서 유용
> 
> => 예시에서는 트웃의 스트림이 주를 이루기 때문에 푸시형식의 통신 방식이 적합
> => 풀기반은 사용자가 정기적으로 데이터를 요청해야함

## 이벤트에서 설계까지
> 1. 통신 (이벤트를 전송하고 수신하는 기능을 어떤 기술로 구현할거?)
>   * 웹소켓(WebSocket)
>       * TCP 스트림으로 양방향 이벤트 통신을 지원하는 가벼운 통신 프로토콜
>       * 웹 브라우저에서 지원하는 웹 서버와 웹 브라우저 사이의 이벤트 주도 통신에 주로 사용
>   * SQS(Simple Queue Service) - Message Queue
>       * 아마존 서비스
>       * 호스트된 클라우드 기반 메시지 큐를 메시지 송출이나 수신에 점점 많이 사용
>       * 메시지 큐는 그룹 내의 프로세스 중 한 프로세스가 전송된 메시지를 받아 처리하는 상호 프로세스 통신 방식
>       * 호스트된 서비스를 이용하면 안정적인 호스팅을 제공하기 위해 직접 관리하지 않아도 되어 편리
>   * 다양한 통신 방식을 혼합해서 사용할 수 있음
> 
> 2. GUI
>   * UI 통신 기술이나 UI를 서버의 비즈니스 로직과 결합하면 몇 가지 단점이 있음
>       * 테스트하기 어렵고 테스트 실행도 느려짐 
>           * 모든 테스트가 실행 중인 메인 서버로 이벤트를 발행하거나 수신해야 하기 때문
>       * 단일 책임 원칙을 위반
>   * 메시징을 코어 비즈니스 로직과 분리할 수 있도록 추상화 필요
>   * 클라이언트에세 메시지를 전송하고 클라이언트의 메시지를 수신하는 인터페이스가 필요
> 
> 3. 영구 저장
>   * 수신한 데이터를 어떻게 저장할까?
>       * 직접 인덱스하고 검색할 수 있는 텍스트 파일
>           * 기록된 데이터를 쉽게 볼 수 있으며 다른 응용프로그램과의 디펜던시를 줄일 수 있음
>       * 전통적 SQL 데이터베이스 
>           * 모두가 알고 있으며 잘 검증된 시스템으로 강력한 질의를 지원
>       * NoSQL 데이터베이스
>           * 다양한 유스케이스, 질의 언어, 데이터 저장 모델을 지원
>   * 개발을 진행하다 보면 어떤 기술이 적합한지, 요구사항 등이 계속 바뀌기 때문에 고민 많이 함
> 
> 4. 육각형 아키텍처
>   * 앨리스터 콕번이 정립한 포트와 어댑터(ports and adapters) 또는 육각형 아키텍처(hexagonal architecture)
>   * 이 아키텍처에서 응용프로그램의 코어는 우리가 구현하는 비즈니스 로직
>   * 다양한 구현은 코어와 분리되어 있음
>   * 코어 비즈니스 로직과 분리하고 싶은 기술이 있다면 포트를 이용해 코어 비즈니스 로직과 연결하여 전달
>   * 어댑터는 분리된 특정 기술을 구현한 코드
>   * 포트와 어댑터를 추상화해야함
>   * 어떤 기능을 포트로 하고, 어떤 기능을 코어 도메인으로 분리해야 하는 지에 대해서는 규칙이 따로 없음
>       * 개인적인 판단과 환경에 따라 응용프로그램에 맞는 결정을 하면 됨
>       * 보통 비즈니스 문제를 해결하는 데 꼭 필요한 기능을 응용프로그램의 코어로 분류
>       * 그 외 나머지 특정 기술에 종속된 기능이나 통신 관련 기능은 코어 의 외부 세계로 분류하는 것이 일반적
>   * 포트와 어댑터의 목표는 응용프로그램의 코어와 특정 어댑터 구현의 결합을 제거하는 것
>       * 즉, 인터페이스로 다양한 어댑터를 추상화해야 함

## 작업 순서
> - 설계를 더 구체화하고, 다이어그램을 세부적으로 완성하면서 어떤 클래스로 무슨 기능을 구현할지 결정할 차례
> - 설계를 충분히 고민하지 않고 바로 코딩을 시작하면 혼돈을 초래
> - 그러나 코딩 없는 아키텍처는 무너지기 쉽상이며 현실과 동떨어진 결과가 되기 쉬움
> - TDD는 소프트웨어 설계를 방해한다는 비판을 종종 받음
>   * 테스트 구현에 초점을 맞춰 빈약한 도메인 모델을 만들어 어느 시점에선 다시 코드를 구현해야 함
>   * 빈약한 도메인 모델이란 비즈니스 로직을 갖지 않으며 다양한 메소드에 절차적 형식으로 흩어져 정의된 도메인 객체를 말함

## 비밀번호와 보안
> * 일반 텍스트 (plain text)
>   * 문자열 형태로 비밀번호 저장
>   * 데이터베이스에 접근한 모든 사람이 사용자의 비밀번호를 확인할 수 있어 좋지 않음
>   * 그렇게 취득한 비밀번호를 악의적으로 사용할 수 있음
> * 암호화 해시 함수 (cryptographic hash function)
>   * 비밀번호에 암호화 해시 함수를 적용하면 데이터베이스에 접근한 사람이 비밀번호를 읽지 못함
>   * 임의의 길이의 문자열을 입력받아 다이제스트라는 출력으로 변환하는 기술
>   * 암호화 해시 함수는 항상 같은 결과를 출력
>   * 입력을 다이제스트로 변환하는 시간은 굉장히 빠름
>   * 되돌리는 작업은 매우 오래 걸리고, 많은 메모리를 사용해야 하므로 해커가 다이제스트를 되돌리는 시도를 무력화함
>   * 여전히 무차별 대입(Brute force)로 특정 길이 이내의 키를 맞추거나 레인보우 테이블(Rainbow Table)로 해싱된 값을 되돌릴 수 있음
>       * => salt로 이를 방지 할 수 있음
> * 바운시 캐슬 (Bouncy Castle)
>   * 오픈소스 자바 라이브러리
>   * Scrypt 해싱 함수 사용
> * salt
>   * 암호 해싱 함수에 적용하는 임의로 생성된 추가 입력
>   * 사용자가 입력하지 않은 임의의 값을 비밀번호에 추가하여 누군가 해싱을 되돌리는 기능을 만들지 못하게 막음
>   * 해싱을 되돌리려면 해싱함수롸 솔트 두 가지 모두 알아야 함
> * 전송되는 데이터 보안 신경 쓰기
>   * 이 프로젝트는 웹 소켓으로 로그인 메시지를 받으며, 중간자 공격(man-in-the-middie attack)으로 부터 웹소켓을 지켜야 함
>   * 가장 흔하면서 단순한 방법은 전송계층보안(Transport Layer Security) 방법
>       * 연결된 네트워크로 전달되는 데이터의 프라이버시와 무결성을 제공하는 암호화된 프로토콜 사용

## 팔로워와 트웃
> 소프트 웨어 설계
>   * 상향식 (bottom-up)
>       * 응용프로그램의 코어(테이터 저장 모델이나 코어 도메인 객체 간의 관계) 설뎨에서 시작해 전체를 만드는 방법
>       * 팔로우 기능을 상향식으로 만들 경우
>           * 팔로우를 할 때 발생하는 사용자의 관계를 어떻게 모델링할지 결정해야 함
>           * 사용자는 여러 팔로워를 가질 수 있으므로 다대다 관계 성립
>       * 위에 정의한 모델 위에 사용자에게 필요한 기능을 제공하는 비즈니스 기능을 구현
>   * 하향식 (up-down)
>       * 사용자 요구 사항이나 스토리에서 출발해 구현하는 데 필요한 동작이나 기능을 먼저 개발
>       * 점차 저장소나 데이터 모델을 추가
>       * 먼저 다른 사용자를 팔로우하는 이벤트 수신기 API를 만듦
>       * 그 다음에 이 동작에 필요한 저장소를 설계
>       * API를 먼저 구현하고 비즈니스 로직을 나중에 구현
> > 모델링을 먼저 하고 안하고의 차이인가? P158
> > 코어 상관 없이 스토리가 흐르는대로 개발?
> * 보통은 하향식을 주로 사용
> * 데이터 모델이나 코어 도메인을 먼저 설계하면 실질적으로 소프트웨어의 동작에 필요 없는 부분까지 만들 수 있기 때문
> * 하향식은 요구 사항과 스토리를 구현하면서 초기 버전의 설계 방식에 문제가 있음을 발견하게 된다는 단점
> 
> 오류 모델링
> * 성공했을 때에는 아무것도 반환하지 않고, 이외에는 예외를 던지는 방법
>   * 예외적인 제어 흐름에만 예외를 사용하는 것이 일반적이며, 잘 설계된 UI에서는 이런 방법을 지양함
> * boolean 사용
>   * 어떤 동작이 성공이나 실패 두 가지로 구분 되고 실패도 한 가지 원인일 때 좋음
>   * 여러 가지 이유로 실패 할 경우 '왜 이 동작이 실패했는지' 알려 줄 수 없음
> * int 상수 값 사용
>   * 그 자체로 오류가 발생할 수 있음
>   * 안전한 형식을 제공하지 못하며 가독성과 유지보수성이 낮아짐
> * enum 사용
>   * 안전한 형식과 좋은 문서화를 제공
>   * interface, class를 사용할 수 있는 모든 곳에 enum을 사용할 수 있음
>   * 선언된 값의 목록만 포함할 수 있음
>
> 트우팅
> * 사용자 스토리에서 모든 사용자가 트웃을 전송할 수 있으며, 이 때 로그인 되어 있는 모든 사용자는 즉시 이 트웃을 볼 수 있음
>
> 목(mock) 만들기
> * 목객체는 다른 객체인 척하는 객체
> * 원래 객체가 제공하는 메소드와 공개 API를 모두 제공함
> * 이를 이용해 특정 메소드가 실제 호출되었는지를 확인(verify)
> * 모키토라는 아주 편리한 라이브러리가 있음
> 
> 목으로 확인하기
> 
> 모킹 라이브러리

## Position 객체
> * 사용자가 마지막으로 확인한 트웃의 위치를 저장
>
> equals()와 hashCode() 메소드
> * Object에서 상속 받는 equals()와 hashCode() 메소드는 참조되는 값으로 두 객체가 같은지 판단
> * 값이 같더라도 주소가 다르면 다른 값이 됨
> * equals() 메소드를 오버라이딩해서 값 비교를 진행
>   * 같은 형식의 객체인지 먼저 비교한 후 각 필드의 값이 같은지 확인
> 
> equals()와 hashCode() 메소드 사이의 계약
> * equals() 뿐만 아니라 hashCode() 메소드도 오버라이딩 하는 이유는 자바의 equals()/hashCode() 계약 때문
> * 두 객체를 equals() 메소드로 같다고 판단할 떄 hashCode() 메소드 역시 같은 값을 반환해야 함
> 
> hashCode()메소드 구현 방법
> * 좋은 해시코드랑 계약을 준수할 뿐만 아니라 고르게 정수값이 퍼지도록 구현해야함
> * 그래야 HashMap, HashSet의 효율성이 좋아짐
> * 생성 규칙
>   * result 변수를 만들고 소수를 할당
>   * equals() 메소드가 사용하는 각 필드의 해시코드를 대표하는 int 값을 계산
>   * 기존 결과 값에 소수를 곱한 다음 필드의 해시코드와 합침
> * 각 필드의 해시코드 계산 법
>   * 원시값 : 컴패니언 클래스의 hashCode() 매소드 사용 ex) double -> Double.hashCode()
>   * null 이 아닌 값 : 객체의 hashCode() 또는 0 사용
>   * 배열 : 여기서 사용한 규칙을 그대로 적용하여 배열의 각 요소에 hashCode() 값을 합침
> * equals()와 hashCode() 메소드를 직접 구현하는 일은 드뭄
> * 최신 IDE는 이 코드를 자동으로 만들어줌