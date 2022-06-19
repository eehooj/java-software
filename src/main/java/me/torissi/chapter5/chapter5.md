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
- 