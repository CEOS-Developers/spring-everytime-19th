# week2: spring-everytime-19th
CEOS 19th BE study - everytime clone coding

## ERD Overview
<div align="center">
  <img src="imgs/erd_overview.png" alt="drawing" width=600"/>
</div>

<div align="center">
  <img src="imgs/erd.png" alt="drawing" width=600"/>
</div>

### User
```java
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    // skip...

    @Column(nullable = false)
    private Boolean isBoardManager = False;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "school_id")
    private School school;

    @Builder
    public User(final String username,
                
                // skip...

                final LocalDateTime createdAt,
                final LocalDateTime lastLogin) {
        this.username = username;
        
        // skip...

        this.lastLogin = lastLogin;
        this.school = school;
    }
}

```

# 3주차 - JPA 심화

## Service Business Logic 목록
**User**
- 유저 회원가입
- 유저 회원탈퇴
- 유저 로그인 (ToDo)
- 유저 로그아웃 (ToDo) 
- 대학교 인증 (ToDo)

**Board**
- 게시판 생성
- 게시판 관리자 변경
- 게시판 삭제
  - 하위 테이블(Post, Comment) 삭제

**Post**
- 게시글 작성
- 게시글 조회
- 게시글 수정
- 게시글 삭제
- 게시글 좋아요 및 취소

**Comment**
- 댓글 작성
- 댓글 수정
- 댓글 삭제
- 대댓글 기능
- 댓글 좋아요 및 취소

**Message**
- 쪽지 전송 (최초 방 생성 및 전송)
- 쪽지 조회
- 쪽지방 나가기 (방 삭제)

**Image**
- 이미지 업로드 (DB 저장)
- 이미지 삭제 (DB 삭제)

### Spring naming convention

서비스 클래스 안에서 메서드 명을 작성 할 때는 아래와 같은 접두사를 붙인다.

- findOrder() - 조회 유형의 service 메서드
- addOrder() - 등록 유형의 service 메서드
- modifyOrder() - 변경 유형의 service 메서드
- removeOrder() - 삭제 유형의 service 메서드
- saveOrder() – 등록/수정/삭제 가 동시에 일어나는 유형의 service 메서드

[Reference](https://cocobi.tistory.com/27)


### Service 생성자 주입 (DI)

생성자 주입 장점 요약
- 객체의 불변성을 확보할 수 있다.
- 테스트 코드의 작성이 용이해진다.
- final 키워드를 사용할 수 있고, Lombok과의 결합을 통해 코드를 간결하게 작성할 수 있다.
- 스프링에 침투적이지 않은 코드를 작성할 수 있다.
- 순환 참조 에러를 애플리케이션 구동(객체의 생성) 시점에 파악하여 방지할 수 있다.


# 스터디 이후 개선점들!

## UUID(Universally Unique Identifier : 범용 고유 식별자)

- 중복이 되지 않는 유일한 값을 구성하고자 할때 주로 사용이 되는 고유 식별자
- 주로 세션 식별자, 쿠키 값, 무작위 데이터베이스 키 등에 사용
- 100년 동안 생성했을 때 최소 1개가 중복 및 충돌 될 확률
- 버전 5까지 있지만, 버전 5의 보안 취약점으로 인해 버전4가 대중적으로 사용됨

<div align="center">
  <img src="imgs/uuid.png" alt="drawing" width=600"/>
</div>

16바이트(128비트) 형태의 구조를 가지며 하나의 <U>**UUID 길이는 36자리이며 “4개의 하이픈(-)”과 “32개의 16진수 문자열”로 구성**</U>

**Pros and Cons**
- Pros
  - 보안성
  - 정렬했을 때 서비스 사이즈 규모 추정이 힘듬
- Cons
  - increment PK보다 더 많은 storage를 필요로 한다. (UUID: 128 bits)
    -> DB와 메모리를 많이 사용한다.

> Result: 애플리케이션 내부용 키로는 자동증가 pk, 외부에 공개할 키로는 uuid를 사용하는 것을 권장
- 애플리케이션 내부에서 자동증가 pk를 사용하면 성능과 저장 장소 측면에서 이점이 있다.
- 만약 식별 값이 외부로 노출될 수도 있는 서비스라면 UUID로 데이터를 식별하는 것이 좋다.
- 어떤 이유로든(외부 노출 등) UUID가 손상된다면 UUID를 변경해야 한다.
    - PK를 변경하는 작업은 매우 값비싼데, UUID가 PK와 별개로 사용되는 경우 UUID를 변경하는 작업은 훨씬 저렴하다.


## Refactoring: SQL 예약어 유의
<div align="center">
  <img src="imgs/post.png" alt="drawing" width=400"/>
</div>

<div align="center">
  <img src="imgs/post_error.png" alt="drawing" width=400"/>
</div>

`like`와 같은 SQL 예약어를 사용하게 되면 table이 정상적으로 만들어지지 않는다.

-> Post, Comment, CommentReply 테이블의 '좋아요' 변수명을 like가 아닌 likeCount 등으로 변경


## BaseEntity
대부분 엔티티에 필요할 경우, BaseEntity로 관리할 수 있다.
everytime 프로젝트의 경우, TimeEntity가 중복되어, 이를 BaseTimeEntity로 분리해주었다.

```java
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)  // Auditing 기능 포함
public abstract class BaseTimeEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;
}

```

아래와 같이 extends 하여 BaseEntity의 속성을 가져올 수 있다.

```java
@Entity
public class User extends BaseTimeEntity{
  // skip... 
}
```

## Fetch 전략
- 한 사람이 여러 개의 메세지를 보낼 수 있다.
- FetchType이란 JPA가 하나의 Entity를 조회할 때, 연관관계에 있는 객체들을 어떻게 가져올지에 대한 설정값이다.
- 이 경우 Message 클래스는 @ManyToOne으로 User와 연관되며, @ManyToOne의 default FetchType 은  EAGER 이다.

```java
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
```

대부분의 FetchType은 LAZY가 권장된다.

하지만 xToOne의 경우, LAZY FetchType은 N+1 문제를 야기할 수 있다.
- ex. Message를 불러온 뒤, 각 User에 대해 무언가를 하는 for Loop가 있을 경우

### How to prevent N+1 problem?
N+1 이 발생하는 Entity 연관관계를 발견하였다면, 한 Entity 가 Managed 상태로 올라올 때 N+1 문제를 일으키는 Entity Collections 들도 동시에 Managed 상태로 올라오게 하면 된다.

**Detail Suggestions**
1. JPQL 의 Fetch Join 을 이용한다. (QueryDSL 과 같은 쿼리빌더의 도움을 받을 수도 있다.)
2. ManyToOne, OneToOne 의 FetchType = LAZY → EAGER 로 변경한다.
3. @EntityGraph 를 이용해, 한 쿼리에 대해서만 EAGER load 를 지정한다.


## Hibernate: ddl-auto

ddl-auto 옵션은 DB 테이블 관리 옵션

- create: 기존테이블 삭제 후 다시 생성 (DROP + CREATE)
- create-drop: create와 같으나 종료시점에 테이블 DROP
- update: 변경분만 반영(운영DB에서는 사용하면 안됨)
- validate: 엔티티와 테이블이 정상 매핑되었는지만 확인
- none: 사용하지 않음(사실상 없는 값이지만 관례상 none이라고 한다.)

**주의할 점**

- 운영 장비에서는 절대 crate, create-drop, update 사용하면 안된다.
- 개발 초기 단계는 create 또는 update
- 테스트 서버는 update 또는 validate
- 스테이징과 운영 서버는 validate 또는 none

[ ! ] 로컬 환경을 제외한 나머지 서버에서는 최대한 직접 쿼리를 날려서 적용하는 것이 가장 좋다.

-> 가능하면 update로 개발할 것!


## ignore가 필요한 파일이 remote에 업로드 됐을 때
  - .gitignore에 yml 파일 추가
  - `git rm -r --cached .` 명령어로 캐시 제거

```zsh
git rm -r --cached .
git add .
git commit -m 'clear git cache'
git push
```

<div align="center">
  <img src="imgs/yml-ignore.png" alt="drawing" width=500"/>
</div>

하지만 Load diff로 커밋 내용으로는 확인할 수 있어, 기록 전부를 삭제하고 싶으면 rebase 해주어야 함.

## Unit Test

```java
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositryTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        System.err.println("=============================== setup ==============================");
    }

    @Test
    @DisplayName("정상 회원 가입 케이스 테스트")
    @Transactional
    void joinUser() {
        // given
        LocalDateTime currentDateTime = LocalDateTime.now();

        School univ = new School("Yonsei");

        User insertUser = userRepository.save(User.builder()
                .username("yeonghwan")
                .password("testpw")
                .nickname("yeonghwan")
                .isAdmin(true)
                .userLast("Jang")
                .userFirst("Yeonghwan")
                .email("jghff700@naver.com")
                .isBoardManager(false)
                .board(null)
                .isBanned(false)
                .createdAt(currentDateTime)
                .lastLogin(currentDateTime)
                .school(univ).build()
        );

        // when
        User saveUser = userRepository.findByUsername("yeonghwan").get();

        // then
        assertThat(insertUser).isEqualTo(saveUser);
    }
}

```
<div align="center">
  <img src="imgs/user_repository_test.png" alt="drawing" width=500"/>
</div>


## References
- [UUID 이해 및 사용방법](https://adjh54.tistory.com/142)
- [UUID 장단점](https://mr-popo.tistory.com/199)
- [빌더 패턴을 사용해보자](https://velog.io/@haerong22/Java-%EB%B9%8C%EB%8D%94-%ED%8C%A8%ED%84%B4%EC%9D%84-%EC%8D%A8%EB%B3%B4%EC%9E%90)
- [BaseTimeEntity](https://europani.github.io/spring/2021/10/05/027-baseTimeEntity.html)
- [N+1 problem with LAZY FetchType](https://jaynewho.com/post/39)
- [ddl-auto 옵션 관련 주의할 점](https://smpark1020.tistory.com/140)
- [이미 commit 이후에 gitignore 적용이 안될 때](https://junlab.tistory.com/237)