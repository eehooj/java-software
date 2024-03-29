# 트우터 확장판

## 목표
1. 의존관계 역전 원칙(DIP)와 의존관계 주입(DI)로 의존 관계 피하기
2. 저장소 패턴, 쿼리 객체 패턴으로 데이터 영구 저장하기
3. 함수형 프로그래밍이란 무엇이며 자바로 구현한 실제 응용프로그램에 이를 적용하는 방법 소개

## 영구 저장과 저장소 패턴
> * 저장소 패턴 (Repository pattern) 
>   * 도메인 로직과 저장소 백엔드 간의 인터페이스 정의
>   * 저장소 패턴을 이용하면 나중에 응용 프로그램의 저장소를 다른 저장소 백엔드로 쉽게 갈아탈 수 있음
>   * 장점
>       * 저장소 백엔드를 도메인 모델 데이터로 매핑하는 로직을 중앙화 함
>       * 실제 데이터베이스 없이도 코어 비즈니스 로직을 유닛 테스트하므로 빠르게 테스트를 실행할 수 있음
>       * 각 클래스가 하나의 책임을 가지므로 유지보수성과 가독성이 좋아짐
> 
> 저장소 설계
> * 예제에서는 하향식 테스트 주도로 저장소를 설계
> * 불필요한 코드는 일종의 부채
> * YGNI (You ain't gonna need it, 당신은 그 기능이 필요하지 않을 거에요.)
>   * 미래에 사용할 것 같은 기능은 구현하지 말고 정말로 사용해야 할 때 그 기능을 구현하라는 것
> 
> 쿼리 객체
> 

## 함수형 프로그래밍
> * 메소드를 수학 함수처럼 취급하는 컴퓨터 프로그래밍 형식
> * 자바 8부터 복잡한 컬렉션 처리 알고리즘을 구현할 수 있으며, 메소드 호출 방식을 살짝 바꿔 멀티 코어 cpu를 효율적으로 활용할 수 있음
> * 함수형 프로그래밍은 동작 추상화에 초점
> 
> 람다 표현식
> * 익명 함수를 람다 표현식으로 줄여서 정의
> * 람다 메소드 파라미터는 불필요한 코드를 제거하면서도 정적 형식을 유지
> * 가독성, 친밀성이 중요하거나 컴파일러가 형식을 판단하기 어려울 때는 명시적으로 형식을 선언할 수 있음
> 
> 메소드 레퍼런스
> * 람다 표현식은 제공된 파라미터의 메소드를 호출
> * twoot -> twoot.getContent()를 Twoot::getContent로 쓰는게 메소드 레퍼런스
> * 클래스 이름::메소드 이름
> * 람다 표현식 대신 메소드 레퍼런스 사용 가능
> 
> 실행 어라운드 (execute around)
> * 함수형 디자인 패턴에서 주로 사용
> * 항상 비슷한 작업을 수행하는 초기화, 정리 코드가 있음
> * 초기화, 정리 코드에서 동퉁 메소드를 추출
> * 같은 전체 패턴의 유스 케이스에 따라 다른 동작을 수행하도록 파라미터를 받음
> 
> 스트림
> * map()
>   * 어떤 형식의 값을 포함하는 스트림을 다른 형식의 값의 스트림으로 변환
>   * 오직 한 개의 인수를 받음
> * forEach()
>   * 스트림 값에 부작용을 일으키는 작업을 수행할 때 사용
>   * 반복문
> * filter()
>   * == if문
>   * 한 개의 함수를 인수로 받음
> * reduce()
>   * 값의 컬랙션을 한 개의 결과로 만들기 위해 사용 
>   * 연산용..?
> 
> Optional
> * null을 대신하는 코어 자바 라이브러리 데이터 형식
> * 버그를 피하기 위해 변수의 값이 있는지 개발자가 확인하도록 장려
> * 클래스의 API에서 값이 없을 수 있다는 사실을 문서화
> 

## 사용자 인터페이스
>

## 의존관계 역전과 의존관계 주입
> * 의존관계 역전
>   * SOLID 패턴의 마지막 원칙
>   * 높은 수준의 모듈은 낮은 수준의 모듈에 의존하지 않아야 함
>   * 두 모듈은 모두 추상화에 의존해야 함
>   * 추상화는 세부 사항에 의존하지 않아야 함
>   * 세부사항은 추상화에 의존해야 함
> * 의존관계 주입
>   * 명시적으로 디펜던시나 팩토리를 만들 필요가 없음
>   * 필요한 인수를 제공하면 디펜던시에 필요한 책임을 담당하는 객체를 알아서 인스턴스화함
>   * 생성자 주입 -> 객체를 쉽게 테스트할 수 있고 객체 생성을 외부로 위임
> 

## 패키지와 빌드 시스템
> * 큰 프로젝트를 다양한 패키지로 쪼개면 개발자가 쉽게 기능을 찾을 수 있도록 코드를 조작할 수 있음
> * 관련된 클래스를 패키지로 묶음
> * 패키지는 정보 은닉 기능도 제공
>   * 패키지로 클래스와 메소드 접근 범위를 제한해 외부 패키지에서 내부 패키지의 세부 구현에 접근하지 못하도록 막음
>   * 결합도를 낮춤
> * 패키지 구조화
>   * 패키지를 계층으로 구분
>       * views, controller, service 등등..
>       * 구조를 잘 세우지 않으면 결합도와 응집도에 문제가 생김
>   * 패키지를 기능으로 코드를 그룹화
>       * cart, product 등등..
>       * 응집도를 높일 수 있음
>

## 한계의 단순화
> 