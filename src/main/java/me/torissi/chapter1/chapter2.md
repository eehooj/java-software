###### 좋은 소프트웨어 기반이 무엇인지 배우자

## 요구 사항
1. 자신의 입출금 목록이 담긴 텍스트 파일을 읽어 분석하기
2. 텍스 파일은 콤마로 분리된 csv 형식
3. 은행 입출금 내역의 총 수입과 총 지출 얼마? 결과는 양수 음수?
4. 특정 달엔 몇 건의 입출금 내역 발생?
5. 지출이 가장 높은 상위 10건은?
6. 돈을 가장 많이 소비한 항목은?

## KISS (Keep It Short and Simple)
> 은행 입출금 내역의 총 수입과 총 지출 얼마?
> ```java
> public class BankTransactionAnalyzerSimple {
> 
>     private static final String RESOURCES = "src/main/resources";
> 
>     public static void main(String[] args) throws IOException {
>         final Path path = Paths.get(RESOURCES + args[0]); // 파일 시스템 경로
>         final List<String> lines = Files.readAllLines(path); // 행목록 반환
>         double total = 0d;
> 
>         for (final String line: lines) {
>             final String[] columns = line.split(","); // 콤마로 열 분리
>             final double amount = Double.parseDouble(columns[1]); // 금액 추출
>             total += amount; // 총 금액
>         }
> 
>         System.out.println("The total for all transactions is " + total);
>     }
> }
> ```
> 고민 사항 
> - 파일이 빈파일이라면?
> - 데이터 형식이 다르거나 문제가 있어 데이터 파싱이 안된다면?
>
> 특정 달엔 몇 건의 입출금 내역 발생?
> ```java
> public class BankTransactionAnalyzerSimple2 {
> 
>     private static final String RESOURCES = "src/main/resources";
> 
>     public static void main(String[] args) throws IOException {
>         final Path path = Paths.get(RESOURCES + args[0]); // 파일 시스템 경로
>         final List<String> lines = Files.readAllLines(path); // 행목록 반환
>         double total = 0d;
>         final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd");
> 
>         for (final String line: lines) {
>             final String[] columns = line.split(","); // 콤마로 열 분리
>             final LocalDate date = LocalDate.parse(columns[0], DATE_PATTERN);
> 
>             if (date.getMonth() == Month.JANUARY) {
>                 final double amount = Double.parseDouble(columns[1]); // 금액 추출
>                 total += amount; // 총 금액
>             }
>         }
> 
>         System.out.println("The total for all transactions is " + total);
>     }
> }
> ```
> final 변수
> - fianl로 선언한 변수는 값을 다시 대입할 수 없음
> - 어떤 변수가 바뀔 수 있고, 없는지를 명확하게 구분할 수 있음
> - but, final 필드를 쓰더라도 가변상태를 포함하기 때문에 객체가 바뀌지 못하도록 강요하지 않음
> - 추상화 메소드의 메소드는 실제 구현이 없기 때문에 final을 사용해도 의미가 없음

## 코드의 유지보수성과 안티 패턴
> 코드가 가져야할 덕목
> 1. 특정 기능을 담당하는 코드를 쉽게 찾을 수 있어야 함
> 2. 코드가 어떤 일을 수행하는지 쉽게 이해할 수 있어야 함
> 3. 새로운 기능을 쉽게 추가/삭제 할 수 있어야 함
> 4. 캡슐화가 잘되어 있어야함 
> 
> 복붙 하는 코드? (DRY 원칙 - Do not Repeat Yourself)
> 1. 코드 중복 때문에 코드가 불안정하고 변화에 쉽게 망가짐
> 2. 한 개의 거대한 god class 때문에 코드 이해가 어려움
>   - god class란
>       1. 한 개의 파일 안에 모든 코드를 구현하는 것
>       2. 만약 이 로직을 수정해야 한다면 많은 분석이 필요함 -> 갓클래스 안티패턴
>       3. <strong>단일 책임의 원칙을 지키자</strong>
> 3. 어떤 부분을 바꿔야 한다면 중복 소스 모두를 바꿔야 하고, 사이드 이팩트가 발생할 가능성이 커짐
> 
> 
> 결론
> - 코드를 간결하게 유지하되 KISS 원칙 남발은 금물!
> - 전체 설계를 보고 한 문제를 작은 개별 문제로 쪼갤 수 있는지 파악해야 한다!
> 
## SRP (Single Responsibility Principle - 단일 책임의 원칙)
> SRP 란?
> 1. 한 클래스는 하나의 기능만 책임
> 2. 클래스가 바뀌는 이유는 오직 하나
> 3. 일반적으로 클래스와 메소드에 적용되는 원칙
> 4. 코드가 바뀌는 이유가 한 가지가 아니라면 여러 장소에서 코드 변경이 발생
> 
> 어떻게 적용?
> - 입력읽기, 주어진 입력 파싱, 결과 처리, 결과 요약 리포트 분리하기
> ```html
> [소스 링크](src/me/torissi/chapter1/srp)
> ```
> - 메인에서의 파싱 로직을 다른 클래스와 메소드에 위임
> - 새 요구 사항이 들어오면 BankStatementCSVPaser를 캡슐화 하여 재사용 로직 구현
> - BankTransaction을 통해 다른 코드가 특정 데이터 형식에 의존하지 않음
>   1. 문서의 데이터 형식이 다르더라도 코드에서 BankTransaction에 담아 언제나 같은 형식의 데이터 사용 가능
> - 메소드를 구현할 때는 놀람 최소화 원칙을 지켜야 함
>   1. 메소드가 수행하는 일을 바로 알 수 있도록 메소드명을 직관적이게 명시해야함
>   2. 파라미터의 상태를 바꾸지 않는다. -> 사이드 이팩트 가능성 UP
>   3. 주관적인 원칙으로, 회사 마다 조금씩 다름!

## 응집도
> 응집도란 ?
> 1. 코드 구현에 있어 중요한 특성
>   * 소프트 웨어의 복잡성을 유추하는데 좋음
> 2. 클래스 또는 메소드의 책임이 서로 얼마나 강하게 연결되어 있는지를 나타내는 특성
>   * 여기 저기  모든 곳에 속해 있는지
> 3. 높은 응집도를 갖는 것이 목표
> 4. 클래스, 패키지, 메소드 등의 동작이 얼마나 관련되어 있는가
> ```html
> [소스 링크](src/me/torissi/chapter1/cohesion)
> ```
> 클래스 수준의 응집도
> - 실무에서는 기능, 정보, 유틸리티, 논리, 순차, 시간으로 그룹화
> - 그룹화하는 메소드의 관련성이 적으면 응집도도 낮아짐
> 1. 기능
>   * 함께 사용하는 메소드를 그룹화하면 찾기도 쉽고 이해하기도 쉬워 응집도 Up
>   * ex) BankStatementCSVPaser.java
>   * 다만, 한 개의 메소드를 갖는 클래스를 너무 과도하게 만들면 생각해야 클래스가 많아지고, 장황해짐
> 2. 정보
>   * 같은 데이터나 도메인 객체를 처리하는 메소드들을 그룹화
>   * ex) BankTransaction을 crud 하는 class 만들기
>   * 테이블이나 특정 도메인 객체를 저장하는 데이터베이스와 상호작용할 때 많이 씀
>   * 이 패턴을 보통 DAO(Data Access Object - 데이터 접근 객체)라 부름
>   * 여러 기능으로 그룹화하면서, 필요한 일부 기능을 주입해야하는 약점이 있음
> 3. 유틸리티
>   * 때로 관련성 없는 메소드를 한 클래스로 포함 시킬때 
>   * 유틸리티 클래스의 사용은 낮은 응집도로 이어짐
> 4. 논리
>   * 본질적으로는 관련이 없으나 논리적으로 그룹화 할 수 있는 것
>   * SRP 위배 
>   * 비추천
> 5. 순차
>   * 파일을 읽고, 파싱, 처리, 정보 저장 메소드를 한 클래스로 그룹화
>   * 입출력이 순차적으로 흐으는 것을 순차 응집이라함
>   * SRP 위배
> 6. 시간
>   * 시간과 관련된 연산을 그룹화
>   * ex) 어떤 작업을 하기 전에 초기화, 뒷정리를 담당하는 메소드
> 
> |응집도 수준|장점|단점|
> |---|---|---|
> |기능(높은 응집도)|이해하기 쉬움|너무 단순한 클래스 생성|
> |정보(중간 응집도)|유지보수하기 쉬움|불필요한 디팬던시|
> |순차(중간 응집도)|관련 동작을 찾기 쉬움|SRP 위배할 수 있음|
> |논리(중간 응집도)|높은 수준의 카테고리화 제공|SRP 위배할 수 있음|
> |유틸리티(낮은 응집도)|간단히 추가 가능|클래스의 책임을 파악하기 어려움|
> |시간(낮은 응집도)|판단 불가|각 동작을 이해하고 사용하기 어려움|

## 결합도
> 결합도란 ? 
> 1. 한 기능이 다른 클래스에 얼마나 의존하고 있는가
> 2. 어떤 클래스를 구현하는데 얼마나 많은 클래스를 참조했는가
> 3. 하나의 클래스를 수정하면 이 클래스에 의존하고 있는 모든 클래스가 영향을 받음
> 4. ex) BankTransactionAnalyzer는 BankStatementCSVPaser에 의존함
> 5. 결합도를 줄이기 위해서는 인터페이스와 구현을 분리하는 것이 좋음
>   * 코드의 다양한 컴포넌트가 내부와 세부 구현에 의존하지 않아야함
> ```html
> [소스 링크](src/me/torissi/chapter1/cohesion)
> ```
> 질문......
> > 인터페이스화를 한다는 것이 애매하게 이해됨 <br/>
> > 만약 csv에서 폼을 json으로 바꿨을 경우 mainApplication에서 new 객체만 바꿔주면 됨으로 영향을 적게 받음 <br/>
> > 전의 경우와 후의 경우 어쨌든 csv를 바꿔줘야 하는건데 똑같지 않나..?

## 테스트
> 테스트 자동화
> 1. 사람 조작 없이 여러 테스트를 한 번에 실행
>   * 코드를 수정하고 난 뒤 테스트를 실행하여 코드에 이상이 없는지 빠르게 파악 가능
> 2. 확신
>   * 소프트웨어가 규격 사양과 일치하며 동작하는지를 테스트 하여 고객의 요구 사항을 충족하고 있는지 빠르게 파악 가능
>   * 고객에게 제시할 증거 사항이 될 수 있음
> 3. 변화에도 튼튼함 유지
>   * 사이드 이펙트가 발생했는지 확인 가능
> 4. 프로그램 이해도
>   * 다양한 컴포넌트가 어떻게 동작하는지 이해하는데에 도움을 줌
>   * 소프트웨어의 전체 개요를 빠르게 파악할 수 있음
> 
> 제이유닛 사용하기
> 1. 유닛 테스트 class 명에는 Test라는 접미어를 붙여준다.
> 2. 테스트 메소드 명은 직관적으로 만든다.
> 3. 테스트 메소드 위에 반드시 @Test 어노테이션을 붙여준다.
> 
> |어서션 구문|용도|
> |---|---|
> |Assertions.fail(message)|메소드 실행 결과를 실패로 만듦|
> |Assertions.assertEquals(expected, actual)|두 값이 같은지 테스트|
> |Assertions.assertEquals(expected, actual, delta)|두 float 또는 double이 delta 범위 내에서 같은지 테스트|
> |Assertions.assertNotNull(object)|객체가 null인지 테스트|
> 
> 코드 커버리지
> 1. 테스트 집합이 소스코드를 얼마나 테스트 했는가를 가리키는 척도
> 2. 커버리지가 높을 수록 예상치 못한 버그가 발생할 확률이 낮아짐
> 3. 보통 70 ~ 90퍼센트를 목료로 잡는 것이 좋음
> 4. 커버리지는 테스트 품질과는 관련이 없음
> 5. 분기 커버리지를 사용하는 것이 좋음
>   * 구문 커버리지는 if, while, for 등을 한 구문으로 취급하기 때문에





























