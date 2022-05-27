#입출금 내역 분석기 확장판

## 요구사항
1. 특정 입출금 내역을 검색할 수 있는 기능. ex) 주어진 날짜 범위 또는 특정 범주의 입출금 내역
2. 검색 결과의 요약 통계를 텍스트, HTML 다양한 형식으로 만들기

## 개방/폐쇄 원칙
> ```java
> public class BankStatementProcessor {
> 
>     // 특정 금액 이상의 은행 거래 내역 찾기
>     /*
>     * 따로 클래스를 만들지 않고 여기에 추가한 이유는?
>     * => 새로 클래스를 추가하면 여러 이름이 생기면서 다양한 동작 간의 관계를 이해하기 어려워짐
>     * => 전체 프로젝트가 복잡해짐
>     * */
>     public List<BankTransaction> findTransactionasGreaterThenEqual (final int amount) {
>         final List<BankTransaction> result = new ArrayList<>();
> 
>         for (final BankTransaction bankTransaction : bankTransactions) {
>             if (bankTransaction.getAmount() >= amount) {
>                 result.add(bankTransaction);
>             }
>         }
> 
>         return result;
>     }
> 
>     public List<BankTransaction> findTransactionasInMonth (final Month month) {
>         final List<BankTransaction> result = new ArrayList<>();
> 
>         for (final BankTransaction bankTransaction : bankTransactions) {
>             if (bankTransaction.getDate().getMonth() == month) {
>                 result.add(bankTransaction);
>             }
>         }
> 
>         return result;
>     }
> 
>     public List<BankTransaction> findTransactionasInMonthAndGreaterThenEqual (final Month month, final int amount) {
>         final List<BankTransaction> result = new ArrayList<>();
> 
>         for (final BankTransaction bankTransaction : bankTransactions) {
>             if (bankTransaction.getDate().getMonth() == month
>                     && bankTransaction.getAmount() >= amount) {
>                 result.add(bankTransaction);
>             }
>         }
> 
>         return result;
>     }
> }
> ```
> 위 코드의 문제점
> * 요구사항이 늘수록 조합할 수록 코드가 길어지고 복잡해짐
> * 반복 로직과 비즈니스 로직이 결합되어 분리가 어려워짐
> * 코드가 계속 반복됨
> 
> 이럴 때 개방/폐쇄 원칙을 적용!
> * 코드를 직접 바꾸지 않고 해당 메소드나 클래스의 동작을 바꿀 수 있음
> * 코드 복사, 파라미터 추가 등 코드 수정 없이 동작 확장이 가능
> * BankTransactionFilter 인터페이스를 만들어서 사용자!
> ```java
> @FunctionalInterface
> public interface BankTransactionFilter {
> 
>     boolean test(BankTransaction bankTransaction);
> }
>```
> ```java
> public class BankStatementProcessor {
> 
>     public List<BankTransaction> findTransactionas (final BankTransactionFilter bankTransactionFilter) {
>         final List<BankTransaction> result = new ArrayList<>();
> 
>         for (final BankTransaction bankTransaction : bankTransactions) {
>             if (bankTransactionFilter.test(bankTransaction)) {
>                 result.add(bankTransaction);
>             }
>         }
> 
>         return result;
>     }
> }
>```
> * 새로운 인터페이스를 사용하여 반복 로직과 비즈니스 로직의 결합을 제거
> * 변경 없이도 확장성은 개방된다!
> 
> ```java
> public class BankTransactionIsInFebruaryAndExpensive implements BankTransactionFilter {
> 
>     @Override
>     public boolean test(BankTransaction bankTransaction) {
>         return bankTransaction.getDate().getMonth() == Month.FEBRUARY
>                 && bankTransaction.getAmount() >= 1000;
>     }
> }
> ```
> ```java
> public class BankTransactionAnalyzer3 {
> 
>     private static void collectSummary(BankStatementProcessor bankStatementProcessor) {
>         List<BankTransaction> transactions
>                 = bankStatementProcessor.findTransactionas(new BankTransactionIsInFebruaryAndExpensive());
>     }
> }
> ```
> * 요구사항에 맞는 필터를 구현한 후 인수로 필터의 인스턴스를 전달
> * 그러나 이러면 요구마다 클래스를 만들어야 하는 수고로움이....
> 
> 람다로 만들어 보자!
> ```java
> public class BankTransactionAnalyzer3 {
>
>     private static void collectSummary(BankStatementProcessor bankStatementProcessor) {
>         List<BankTransaction> transactions
>                 = bankStatementProcessor.findTransactionas(
>                         bankTransaction -> bankTransaction.getDate().getMonth() == Month.FEBRUARY
>                                 && bankTransaction.getAmount() >= 1000);
>     }
> }
> ```
> ### 개방/폐쇄 원칙의 장점
> 1. 기존 코드를 바꾸지 않으므로 기존 코드가 잘 못될 가능성이 줄어든다
> 2. 코드가 중복되지 않으므로 기존 코드의 재사용성이 높아진다
> 3. 결합도가 낮아지므로 코드 유지보수성이 좋아진다

## 인터페이스 문제
> 갓 인터페이스 (한 인터페이스에 모든 기능을 추가)
> * 인터페이스가 복잡해짐
> * 인터페이스는 모든 구현이 지켜야 할 규칙을 정의함 
>   * 구현 클래스는 인터페이스에서 정의한 모든 연산의 구현 코드를 제공해야 함
>   * 인터페이스를 바꾸면 구현한 코드들도 바꿔야함
> * 인터페이스가 도메인 객체의 특정 접근자에 종속
>   * calulateTotalForCategory(final String category)
>   * calulateTotalInMonth(final Month month)
> * 위의 문제를 피하기 위해 작은 인터페이스 권장!
>   * 도메인 객체의 다양한 내부 연산으로의 의존을 최소화
> 
> 인터페이스는 작을 수록 좋은가?
> ```java
> interface CalculateTotalAmount{ double calculateTotalAmount(); }
> interface CalculateTotalCategory{ double calculateTotalCategory(); }
> interface CalculateTotalInMonth{ double calculateTotalInMonth(); }
>```
> * 지나치게 인터페이스가 세밀해도 코드 유지보수에 방해가 됨
> * 위의 코드는 안티응집도(anti-cohesion) 문제 발생
>   * 기능이 여러 인터페이스로 분산
>   * 필요한 기능 찾기 어려움
> * 인터페이스가 너무 세밀하면 복잡도가 높아짐

## 명시적 API vs 암묵적 API
> * 개방/폐쇄 원칙을 적용하면 연산에 유연성을 추가하고 가장 공통적인 상황을 클래스로 정의할 수 있음
> * findTransactionsGreaterThenEqual()
>   * 어떤 동작을 수행하는지 명시되어 있고, 사용하기 쉬움
>   * API의 가독성을 높이고 쉽게 이해하도록 메소드 이름을 서술적으로 만듦
>   * 특정 상황에 국한되어 각 상황에 맞는 메소드를 계속 만들어야 함
> * findTransactions()
>   * 처음 사용하기 어려움
>   * 문서화 필요
>   * 거래 내역을 검색하는 데 필요한 모든 상황을 단순한 API로 처리 가능
> * 둘 중 어떤 방식을 쓰는 지 답은 정해져 있지 않음
> * findTransactionsGreaterThenEqual()이 자주 쓰이는 연산이라면 명시적으로 API 로 만드는 것이 합리적
> ```java
> @FunctionalInterface
> public interface BankTransactionSummarizer {
> 
>     double summarize(double accumulator, BankTransaction bankTransaction);
> }
> ```
> ```java
> public class BankStatementProcessor2 {
> 
>     private final List<BankTransaction> bankTransactions;
> 
>     public BankStatementProcessor2(List<BankTransaction> bankTransactions) {
>         this.bankTransactions = bankTransactions;
>     }
> 
>     public double summarizeTransaction(final BankTransactionSummarizer bankTransactionSummarizer) {
>         double result = 0;
> 
>         for (final BankTransaction bankTransaction : bankTransactions) {
>             result = bankTransactionSummarizer.summarize(result, bankTransaction);
>         }
> 
>         return result;
>     }
>       //.....
> 
>     public List<BankTransaction> findTransactions(final BankTransactionFilter bankTransactionFilter) {
>         final List<BankTransaction> result = new ArrayList<>();
> 
>         for (final BankTransaction bankTransaction : bankTransactions) {
>             if (bankTransactionFilter.test(bankTransaction)) {
>                 result.add(bankTransaction);
>             }
>         }
> 
>         return result;
>     }
> }
> ```
> 
> 도메인 클래스 vs 원싯값
> * bankTransactionSummarizer.summarize()는 double이라는 원싯값을 결과로 반환
> * 일반적으로 좋은 방법은 아님
> * 원싯값으로는 다양한 결과를 반환할 수 없어서 유연성이 떨어짐

## 다양한 형식으로 내보내기
> * 가장 먼저 어떤 형식으로 내보낼 지 정하기
>   * public class SummaryStatistics {} 도메인 객체 구현
> * 적절하게 인터페이스를 정의하고 구현하기
> ```java
> public interface Exporter {
> 
>     void export(SummaryStatistics summaryStatistics);
> }
> ```
> * 위 코드의 문제점
>   * void 반환 형식은 아무 도움이 되지 않고, 기능 파악도 어려움 
>       * 인터페이스로 부터 얻을 수 있는 정보가 없음
>   * Assertions로 테스트하기도 어려움
>       * 예상한 값과 실제 결과 값을 비교할 수 없음
>   * 반환 값을 String으로 바꿔주자

## 예외처리
> 만약 오류가 난다면 어떻게 처리할래?
> * 데이터를 적절하게 파싱하지 못한다면?
> * 입출금 내역을 포함하는 CSV 파일을 읽을 수 없다면?
> * 응용프로그램을 실행하는 하드웨어에 램이나 저장 공간이 부족하다면?
> 
> 예외를 사용해야 하는 이유
> * 자바에서 제공하는 예외 장점
>   * 문서화 : 메소드 시그니처 자체에 예외를 지원
>   * 형식 안정화 : 개발자가 예외 흐름을 처리하고 있는지를 형식 시스템이 파악
>   * 관심사 분리 : 비즈니스 로직과 예외 회복이 각각 try/catch 블록으로 구분됨
> * 자바에서 지원하는 예외 종류
>   * 확인된 예제
>       * 회복해야 하는 대상의 예외
>       * 자바에서는 메소드가 던질 수 있는 확인된 예외 목록을 선언해야 함 (throws)
>       * 또는 try/catch로 예외를 처리해야함
>       * Exception
>       * 컴파일러 체크 예외
>   * 미확인 예제
>       * 프로그램을 실행하면서 언제든 발생할 수 있는 예외
>       * 메소드에 명시적으로 오류를 선언하지 않으면 굳이 호출자도 처리하지 않아도 됨
>       * Error, RuntimeException
>       * 컴파일러 넌체크 예외
> 
> 예외의 패턴과 안티 패턴
> * csv 파싱시 고려 사항
>   * 정해진 문법 파싱
>   * 데이터 검증
> * 미확인 예외와 확인된 예외에서 선택하기
> ```java
> public class BankStatementCSVParser3 implements BankStatementParser {
> 
>     final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd");
>     final Long EXPECTED_ATTRIBUTES_LENGTH = 100L;
> 
>     @Override
>     public BankTransaction parseFrom(String line) {
>         final String[] columns = line.split(",");
> 
>         if (columns.length < EXPECTED_ATTRIBUTES_LENGTH) {
>             throw new CSVSyntaxException("길이 에러");
>         }
> 
>         final LocalDate date = LocalDate.parse(columns[0], DATE_PATTERN);
>         final double amount = Double.parseDouble(columns[1]);
>         final String description = columns[2];
> 
>         return new BankTransaction(date, amount, description);
>     }
>   // ...
> }
> ```
>    
> CSVSyntaxException를 확인된 예제로 또는 미확인 예제로?
> * 예외가 발생했을 때 프로그램이 회복되도록 강제할 것인가를 고민
> * 미확인 예외
>   * 비즈니스 로직 검증시 발생하는 예외는 불필요한 try/catch문을 줄일 때 가용
>   * 예외가 발생했을 때 응용프로그램을 어떻게 회복시킬 것인지 애매할 떄
>       * 이런 상황에 사용자에게 오류를 복구하라고 강제 할 필요가 없음
>   * 시스템 오류가 발생(저장공간 꽉참 등)했을 때 사용자가 할 수 있는 일이 없으므로 시스템 오류도 미확인 예외로 지정
> * 대다수의 예외를 미확인 예외로 지정하고 꼭 필요한 상황에만 확인된 예외로 지정하여 불필요한 코드를 줄여야 함
> 
> 과도하게 세밀함
>   * 데이터 검증 로직을 만들 때 전용 Validator를 만드는 것이 좋음
>       * 검증 로직을 재상용해 코드를 중복하지 않음
>       * 시스템의 다른 부분도 같은 방법으로 검증할 수 있음
>       * 로직을 독립적으로 유닛 테스트 할 수 있음
>       * 프로그램 유지보수와 이해하기 쉬운 SRP를 따름
> ```java
> public class OverlySpecificBankStatementValidator {
> 
>     private String description;
>     private String date;
>     private String amount;
> 
>     public OverlySpecificBankStatementValidator(String description, String date, String amount) {
>         this.description = description;
>         this.date = date;
>         this.amount = amount;
>     }
> 
>     public boolean validate() throws DescriptionTooLongException,
>             InvalidDateFormat, DateInTheFutureException, InvalidAmountException {
>         if (this.description.length() > 100) {
>             throw new DescriptionTooLongException();
>         }
>         
>         final LocalDate parsedDate;
>         
>         try {
>             parsedDate = LocalDate.parse(this.date);
>         } catch (DateTimeParseException e) {
>             throw new InvalidDateFormat();
>         }
>         
>         if (parsedDate.isAfter(LocalDate.now())) {
>             throw new DateInTheFutureException();
>         }
>         
>         try {
>             Double.parseDouble(this.amount);
>         } catch (NumberFormatException e) {
>             throw new InvalidAmountException();
>         }
>         
>         return true;
>     }
> }
> ```
> * 각각의 예외에 적합하고 정확한 회복 기법 구현 가능
> * 너무 많은 설정 작업이 필요
> * 여러 예외를 선언하고 사용자가 모든 예외를 처리해야 해서 생산성이 떨어짐
> * 여러 오류가 발생했을 때 모든 오류 목록을 모아 사용자에게 제공할 수 없음
> 
> 과도하게 덤덤함
> * 모든 예외를 IllegalArgumentException 등의 미확인 예외로 지정
> ```java
> public class OverlySpecificBankStatementValidator {
> 
>     private String description;
>     private String date;
>     private String amount;
> 
>     public OverlySpecificBankStatementValidator(String description, String date, String amount) {
>         this.description = description;
>         this.date = date;
>         this.amount = amount;
>     }
> 
>     public boolean validate2() throws DescriptionTooLongException,
>             InvalidDateFormat, DateInTheFutureException, InvalidAmountException {
>         if (this.description.length() > 100) {
>             throw new IllegalArgumentException("The Description Too Long");
>         }
> 
>         final LocalDate parsedDate;
> 
>         try {
>             parsedDate = LocalDate.parse(this.date);
>         } catch (DateTimeParseException e) {
>             throw new IllegalArgumentException("부적절한 날짜 포맷", e);
>         }
> 
>         if (parsedDate.isAfter(LocalDate.now())) {
>             throw new IllegalArgumentException("날짜는 미래로 설정할 수 없음");
>         }
> 
>         try {
>             Double.parseDouble(this.amount);
>         } catch (NumberFormatException e) {
>             throw new IllegalArgumentException("부적절한 amount 포맷", e);
>         }
> 
>         return true;
>     }
> }
> ```
> * 구체적인 회복 로직을 만들 수 없음
> * 여러 오류가 발생했을 때 모든 오류 목록을 모아 사용자에게 제공할 수 없음
> 
> 노티피케이션 패턴
> * 너무 많은 미확인 예외를 사용하는 상황에 적합한 해결책 제공
> * 도메인 클래스로 오류를 수집
> * 오류를 수집할 Notification class
> ```java
> public class Notification {
> 
>     private final List<String> errors = new ArrayList<>();
> 
>     public void addError(final String message) {
>         errors.add(message);
>     }
> 
>     public boolean hasErrors() {
>         return !errors.isEmpty();
>     }
> 
>     public String errorMessage() {
>         return errors.toString();
>     }
> 
>     public List<String> getErrors() {
>         return this.errors;
>     }
> }
> ```
> ```java
> public class OverlySpecificBankStatementValidator2 {
> 
>     private String description;
>     private String date;
>     private String amount;
> 
>     public OverlySpecificBankStatementValidator2(String description, String date, String amount) {
>         this.description = description;
>         this.date = date;
>         this.amount = amount;
>     }
> 
>     public Notification validate() {
>         final Notification notification = new Notification();
>         
>         if (this.description.length() > 100) {
>             notification.addError("디스크립션이 너무 길다");
>         }
> 
>         final LocalDate parsedDate;
> 
>         try {
>             parsedDate = LocalDate.parse(this.date);
> 
>             if (parsedDate.isAfter(LocalDate.now())) {
>                 notification.addError("날짜는 미래로 설정할 수 없음");
>             }
>         } catch (DateTimeParseException e) {
>             notification.addError("부적절한 날짜 포맷");
>         }
>         
>         final Double amount;
>         
>         try {
>             amount = Double.parseDouble(this.amount);
>         } catch (NumberFormatException e) {
>             notification.addError("부적절한 수량 포맷");
>         }
> 
>         return notification;
>     }
> }
> ```
> 
> 응용프로그램에서의 예외 사용 가이드 라인
> * 예외를 무시하지 않음
>   * 문제의 근본 원인을 알 수 없다고 예외를 무시하면 안됨
>   * 예외 처리 방법이 명확하지 않으면 미확인 예외를 던짐
>       * 확인된 예외를 처리할 때 런타임에서 어떤 문제가 발생했는지 먼저 확인한 다음 이전 문제로 돌아와 필요한 작업을 다시 시장
> * 일반적인 예외는 잡지 않음
>   * 구체적으로 예외를 잡으면 가독성이 높아지고 더 세밀하게 예외를 처리할 수 있음
> * 예외 문서화
>   * API 수준에서 미확인 예외를 포함한 예외를 문서화
>   * API 사용자에게 문제 해결의 실마리를 제공
> * 특정 구현에 종속된 예외를 주의할 것
>   * 특정 구현에 종속된 예외를 던지면 API의 캡슐화가 깨질 수 있음 (특정 구현에 종속됨)
> * 예외 vs 제어 흐름
>   * 예외로 흐름을 제어하지 않음
> ```java
> // 예외를 이용하여 읽기 작업을 수행하는 루프에서 탈출
> public class ex {
>     
>     public void example() {
>         try {
>             while (true) {
>                 System.out.println(source.read());
>             }
>         } catch (NoDataException e) {
>             
>         }
>     }
> }
> ```
> * 예외를 처리하기 위해 불필요한 try/catch 문법이 추가되어 가독성이 떨어짐
> * 코드의 의도를 이해하기 어려움
> * 예외가 정말 필요한 상황이 아니라면 예외를 만들지 말아야 함!
> * 예외가 발생하면 스택트레이스 생성, 보존과 관련된 부담이 생김
> 
> 예외 대안 기능
> * null 사용
>   * throw 대신 return null;
>   * 절대 사용 하면 안됨
>   * 호출자에게 아무 정보도 제공하지 않음
>   * 많은 NullPointerException이 발생하여 불필요한 디버깅 발생
> 
> * null 객체 패턴
>   * 객체가 존재하지 않을 때 null 레퍼런스를 반환하는 대신 필요한 인터페이스를 구현하는 객체를 반환
>   * 의도치 않은 NullPointerException 과 긴 null 확인 코드를 피할 수 있음
>   * 빈 객체는 아무것도 하지 않기 때문에 동작을 예측하기 쉬움
>   * 단점 - 데이터에 문제가 있어도 빈 객체를 이용해 실제 문제를 무시할 수 있어 나중에 문제를 해결하기가 더 어려워질 수 있음
> 
> * Optional<T>
>   * 값이 없는 상태를 명시적으로 처리하는 다양한 메소드 집합을 제공
>   * 다른 API에서 반환한 Optional 형식을 다른 Optional 형식과 조합할 수 있음
> 
> * Try<T>
>   * 성공하거나 실패할 수 있는 연산을 가리킴
>   * Optional과 비슷하지만 값이 아니라 연산에 적용한다는 점이 다름
>   * 코드 조합 가능
>   * 코드에서 발생하는 오류 범위를 줄여줌
>   * JDK에서는 제공하지 않으므로 외부 라이브러리 사용해야 함

## 빌드 도구 사용
> 왜 빌드 도구를 사용할까?
> * 빌드 도구는 응용프로그램 빌드, 테스트, 배포 등 소프트웨어 개발 생명 주기를 자동화할 수 있도록 도와줌
> * 프로젝트에 적용되는 공통적인 구조를 제공하
> * 응용프로그램을 빌드하고 실행하는 반복적이고, 표준적인 작업을 설정
> * 저수준 설정과 초기화에 들이는 시간을 줄일 수 있음
> * 잘못된 설정이나 일부 빌드 과정 생략 등으로 발생하는 오류의 범위를 줄여줌
> * 공통 빌드 작업을 재사용해 이를 다시 구현할 필요가 없으므로 시간을 절약
> 
> 메이븐
> * 메이븐을 이용해 소프트웨어의 디펜던시와 빌드 과정을 작성
> * XML 기반으로 빌드 과정을 정의
> * 유지보수에 도움되는 구조를 처음부터 제공
> * pom.xml 파일을 만들어 응용프로그램 빌드에 필요한 과정을 다양한 XML 정의로 지정해 빌드 프로세스를 정의
> 
> 그레이들
> * 그루비, 코틀린 프로그래밍 언어 등을 이용해 친근한 도메인 특화 언어를 적용
> * 더 자연스럽게 빌드를 지정하
> * 쉽게 커스터마이징 할 수 있으며 쉽게 이해 할 수 있음
> * 캐시, 점진적 컴파일 등 빌드 시간을 단축하는 기능도 지원
> * 