# 비즈니스 규칙 엔진

## 요구사항
1. 프로그래머가 아닌 사람도 자신의 워크플로에 비즈니스 로직을 추가하거나 바꿀 수 있음
2. 비즈니스 규칙 엔진은 간단한 맞춤 언어를 사용해 한 개 이상의 비즈니스 규칙을 실행하는 소프트웨어로 다양한 컴포넌트를 동시에 지원
    * 팩트 : 규칙이 확이할 수 있는 정보
    * 액션 : 수행하려는 동작
    * 조건 : 액션을 언제 발생시킬지 지정
    * 규칙 : 실행하려는 비즈니스 규칙을 지정. 보통 팩트/액션/조건을 한 그룹으로 묶어 규칙으로 만듦

## 테스트 주도 개발
> 아직 요구 사항이 확정되지 않은 유동적인 상태
> 사용자가 수행할 기본 기능 : 액션추가, 액션실행, 기본보고
> 
> - 기본 기능을 구현한 기본 API 
> ```java
> public class BuinessRuleEngine {
> 
>     public void addAction(final Action action) {
>         throw new UnsupportedOperationException();
>     }
> 
>     public int count() {
>         throw new UnsupportedOperationException();
>     }
> 
>     public void run() {
>         throw new UnsupportedOperationException();
>     }
> }
> ```
> 
> - Action 인터페이스를 이용하여 비즈니스 규칙 엔진과 구체적 액션의 결합을 제거
> ```java
> @FunctionalInterface
> interface Action{
> 
>     void execute(Facts facts);
> }
> ```
> 
> 왜 TDD를 사용할까?
> - 테스트를 따로 구현하므로 테스트에 대응하는 요구 사항을 한 개씩 구현할 때마다 필요한 요구사항에 집중하고 개선할 수 있음
> - 코드를 올바르게 조직할 수 있음
>     ex) 테스트를 먼저 구현하면서 코드에 어떤 공개 인터페이스를 만들어야 하는지 신중히 검토 가능
> - TDD 주기에 따라 요구 사항 구현을 반복하면서 종합적인 테스트 스위트를 완성할 수 있어 요구 사항을 만족시켰다는 사실을 조금 더 확신할 수 있으며, 버그 발생 위험을 줄일 수 있음
> - 테스트를 통과하기 위한 코드를 구현하기 때문에 필요하지 않은 테스트를 구현하는 일을 줄일 수 있음
> 
> TDD 주기
> 1. 실패하는 테스트 구현
> 2. 모든 테스트 실행
> 3. 기능이 동작하도록 코드 구현
> 4. 모든 테스트 실행
> 
> - 실생활에서는 코드를 항상 리팩토링 해야 하며, 그렇지 않을 경우 유지보수할 수 없는 코드가 될 수 있음
> - 리팩토링하면 코드를 바꿨을 때 뭔가 잘못되어도 의지할 수 있는 테스트 스위트를 갖음
> - 테스트 추가 -> 테스트 실행(실패) -> 코드 구현 -> 테스트 실행(통과) <-> 리팩토링 -> 테스트 추가 -> 실행 -> ....반복
> 

## 모킹 (mocking)
> - run()이 실행됐을 때 확인하는 기법 (반환값이 없는 void 인 경우 테스트 하기 위해...)
> - 비즈니스 규칙에 액션을 추가할 때마다 run()이 실행되었는지 확인
> - 모킹 사용 방법
>    * mock 생성
>    * 메소드가 호출되었는지 확인
> ```java
> class InspectorTest {
>  //.....
> 
>    @Test
>    void shouldExecuteOneAction() {
>        final BusinessRuleEngine businessRuleEngine = new BusinessRuleEngine();
>        final Action mockAction = mock(Action.class);
>
>        businessRuleEngine.addAction(mockAction);
>        businessRuleEngine.run();
>
>        verify(mockAction, atLeastOnce());
>    }
> }
> ```   
> - mock()으로 필요한 목 객체를 만들고 verify()로 특정 메소드가 호출됐는지 확인
> - mock() 메소드에 Action 객체를 인수로 전달하면 유닛 테스트로 Action의 목객체를 만들 수 있음

## 조건 추가하기
> - TDD는 처음엔 테스트를 실패하는데, 이를 항상 확인해야함
> - 그렇지 않으면 우연히 통과하는 테스트를 만들 수 있기 때문
> - API를 바꾸고 코드를 구현해 테스트를 통과해야 함
> - 지역 변수 형식 추론
>  * 컴파일러가 정적 형식을 자동으로 추론해 결정하는 기능
>  * 사용자는 더 이상 명시적으로 형식을 지정할 필요가 없음
>  * var 키워드와 형식추론으로 구현할 수 있음 (자바10)
>  * 사용하면 코드 구현 시간을 단축할 수 있음
>  * var를 사용해도 가독성에 문제가 없다면 사용하고, 있다면 사용하지 않는 것이 좋음
>> ```java
>> public class ex {
>>    var env = new Facts();
>>    var businessRuleEngine = new BusinessRuleEngine();
>> } 
>> ```
> - switch문
>  * 다양한 액션을 추가하고 싶은 경우
>  * 자바 12에 추가된 switch문은 break문을 사용하지 않아도 fall-through를 방지할 수 있음
>  * 새로운 Switch를 이용하면 가독성이 좋아지고 모든 가능성을 확인하는 소모 검사도 이루어짐
>  * enum에 switch를 사용하면 자바 컴파일러가 모든 enum 값을 switch에서 소모했는지 확인해줌
>> ```java
>> public class ex {
>> 
>>    var dealStage = Stage.valueOf(facts.getFact("stages"));
>>    var amount = Double.parseDouble(facts.getFact("amount"));
>>    var forecastedAmount = amount * switch (dealStage) {
>>       case LEAD -> 0.2;
>>       case EVALUATING -> 0.5;
>>       case INTERESTED -> 0.8;
>>       case CLOSED -> 1;
>>    };
>> }
>> ```
>
> 인터페이스 분리 원칙
> - 비즈니스 규칙 엔진 사용자가 사용할 수 있는 액션과 조건을 검사할 수 있도록 인스펙터도구를 개발해 제공 예정
> - ex) 실제 액션을 수행하지 않고도 각 액션과 관련된 조건을 기록해야 함
> - 어떤 클래스도 사용하지 않는 메소드에 의존성을 갖지 않아야 함 -> 불필요한 결합 방지 위해
> - ISP는 설계가 아닌 사용자 인터페이스에 초점
> - 즉 인터페이스가 커지면 인터페이스 사용자는 결국 사용하지 않는 기능을 갖게 되며 이는 불필요한 결합도를 만듦
> - 응집도 UP

## 플루언트 API 설계 (Fluent API)
> - 특정 문제를 더 직관적으로 해결할 수 있도록 특정 도메인에 맞춰진 API
> - 플루언트 API의 메소드 체이닝을 이용하면 더 복잡한 연산도 지정할 수 있음
> - 도메인 모델링
>  * 조건 : 어떤 팩트에 적용할 조건(참이나 거짓으로 평가됨)
>  * 액션 : 실행할 연산이나 코드 집합
>  * 규칙 : 조건과 액션을 합친 것. 조건이 참일 때만 액션을 실행
> 
> 빌더 패턴
> - 빌더 패턴으로 Rule 객체와 필요한 조건, 액션을 만드는 과정을 개선
> - 빌더 패턴은 단순하게 객체를 만드는 방법을 제공
> - 생성자의 파라미터를 분해해서 각각의 파라미터를 받는 여러 메소드로 분리
> - 각 메소드는 도메인이 다루는 문제와 비슷한 이름을 갖음
> 
