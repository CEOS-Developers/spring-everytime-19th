# week2: spring-everytime-19th
CEOS 19th BE study - everytime clone coding

## ERD Overview
<div align="center">
  <img src="imgs/erd_overview.png" alt="drawing" width=700"/>
</div>

<div align="center">
  <img src="imgs/erd.png" alt="drawing" width=700"/>
</div>

## ERD Details
Study 이후 개선점들! (User Entity example)

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
    private Boolean isBoardManager;

    // TemporalType.TIMESTAMP
    @Column(nullable = true, length = 20)
    private LocalDateTime createdAt;

    @Column(nullable = true, length = 20)
    private LocalDateTime lastLogin;

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

### UUID(Universally Unique Identifier : 범용 고유 식별자)

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




### Hibernate: ddl-auto

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


### ignore가 필요한 파일이 remote에 업로드 됐을 때
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
- [ddl-auto 옵션 관련 주의할 점](https://smpark1020.tistory.com/140)
- [이미 commit 이후에 gitignore 적용이 안될 때](https://junlab.tistory.com/237)