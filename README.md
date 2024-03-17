# week2: spring-everytime-19th
CEOS 19th BE study - everytime clone coding

## ERD Overview
<div align="center">
  <img src="imgs/erd_overview.png" alt="drawing" width=700"/>
</div>


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