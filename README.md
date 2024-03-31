# spring-everytime-19th
CEOS 19th BE study - everytime clone coding

## Everytime 파악!
...한 5년만에 들어가본듯 
![img_1.png](img_1.png)
주요 기능은 게시판 / 시간표 / 강의실 / 학점계산기 / 친구 등등....

주요 Domain : 유저 / 게시판 / 시간표

위의 3개를 세분화하기 위해서 총 15개의 table로 구성

### Board
-> 게시판들의 종류를 나타냄
- 게시판 종류 이름
- 게시판 세팅
### Post
-> 하나의 게시판에 속한 게시물
- 게시물 이름
- 게시물 내용
- 익명
- 조회수
- 좋아요 수
### PostLike
-> 게시물에 좋아요를 누를 때 저장
### Comment
-> 하나의 게시물 안에 있는 댓글들
- 내용
- 좋아요 수
### CommentLike
-> 댓글에 좋아요를 누를때 저장
### Reply
-> 댓글에 대한 좋아요를 누를 때 저장
- 내용
### User
-> 사용자
- 유저이름
- 아이디
- 비밀번호
- 닉네임
### Friend
-> 사용자들간의 친구 관계를 저장
### School
-> 학교에 대한 정보
- 학교 이름
### Lecture
-> 강의에 대한 정보
- 이름
- 코드
- 시작시간
- 끝나는 시간
- 요일
- 학점 수
### CourseLecture
-> 강의에 대한 정보 + 학점을 시간표에 넣기 위한 중간 매체
- 강의 당 학점
### TableCourse
-> 각각의 학기에 따른 시간표 + 평균 학점
- 평균 학점
### TimeTable
-> 종합적인 시간표
- 총 학점
- 시간표 이름
### Message
-> 유저들끼리의 메시지
- 보낸 내용
- 내용
- 삭제 유무(보낸사람)
- 삭제 유무(받은 사람)
### Common
-> enum 셋등에 대한 정의



![img_3.png](img_3.png)
<연관관계는 발표 시에 설명... 위의 다이어 그램을 확인해 주세요>
-> 자동으로 만들어주는 건 DBeaver라는 것을 참고 해주세요

## 어떻게 data jpa는 interface만으로도 함수가 구현되는가?
- Repository의 실제 객체에는 Proxy가 주입되어 있다
- 그렇다면 Proxy가 뭘까???
- 대략적으로 누군가를 **대신하여**  뭔가를 수행하는 권한 자체 또는 그 권한을 받은 주체

- 프록시 패턴
  - 원래 객체를 감싸고 있는 같은 타입의 객체 -> 프록시 객체가 원래 객체를 감싸서 client의 요청을 처리하기 하는 패턴
    - 접근을 제어하고 싶거나, 부가 기능을 추가하고 싶을 때 주로 사용
      - ex)
      - DisplayImage()

        ```java
        public interface Image {
                  void displayImage();
                  }
        ```
      - RealImage
      ```java
      public class RealImage implements Image {

            private String fileName;
    
            public RealImage(String fileName) {
            this.fileName = fileName;
            loadFromDisk(fileName);
            }
    
            private void loadFromDisk(String fileName) {
            System.out.println("Loading " + fileName);
            }
    
            @Override
            public void displayImage() {
            System.out.println("Displaying " + fileName);
    
            }
            }
    - 
    - ProxyImage
```java
          
    
      public class ProxyImage implements Image {
      private RealImage realImage;
      private String fileName;

      public ProxyImage(String fileName) {
      this.fileName = fileName;
      }

      @Override
      public void displayImage() {
      if (realImage == null) {
      realImage = new Real_Image(fileName);
      }
      realImage.displayImage();
      }
      }
  ```
  - Main
  ```java
  public class Main {
  public static void main(String[] args) {
    Image image1 = new Proxy_Image("test1.png");
    Image image2 = new Proxy_Image("test2.png");

    image1.displayImage();
    System.out.println();
    image2.displayImage();
  }
}
```
  - 상대적으로 오래걸리는 이미지 로딩 전에 로딩 텍스트를 먼저 출력할 수 있도록 프록시 객체가 흐름을 제어

  - 프록시 : 어떠한 클래스(빈)가 AOP 대상이면 원본 클래스 대신 프록시가 감싸진 클래스가 자동으로 만들어져 프록시 클래스가 빈에 등록이 된다
  - 이렇게 빈에 등록된 프록시 클래스는 원본 클래스가 호출되면 자동으로 바꿔서 사용!! -> OCP(계방 폐쇄의 원칙 : 확장에는 개방 / 변경에는 폐쇄)만족
    - RealImage 클래스가 원본 / ProxyImage 클래스가 프록시 역할을 한다.
    - ProxyImage 클래스가 RelaImage 인스턴스를 감싸고 있으며 displayImage()메소드 호출 시 부가기능을 수행한 뒤 RelaIamge의 displayImage()를 호출!!

![img.png](img.png)
고로 Spring Data JPA를 사용할 때, Repository도 스프링이 적절한 프록시를 생성해준다

How??? 동적으로 프록시를 생성할까??
- 리플렉션
- 클래스나 메서드의 메타 정보를 동적으로 획득 / 코드도 동적으로 호출가능 / private 접근 제어자가 붙어있는 메서드에도 접근 가능
- 내부의 ProxyGenerator 클래스 내부에서 프록시 클래스를 바이트코드(.class file)로 직접 만든다
  - 즉 리플렉션을 이용해서 **동적으로** 메소드와 클래스를 .class 바이트 파일로 만들어준다
    
      - ex) RepositoryFactorySupport
        ```java
        public <T> T getRepository(Class<T> repositoryInterface, RepositoryFragments fragments) {
    
        // Create proxy
        StartupStep repositoryProxyStep = onEvent(applicationStartup, "spring.data.repository.proxy", repositoryInterface);
        ProxyFactory result = new ProxyFactory();
        result.setTarget(target);
        result.setInterfaces(repositoryInterface, Repository.class, TransactionalProxy.class);**
    
        T repository = (T) result.getProxy(classLoader);
    
        return repository;
        }```
      - 'Create Proxy' 부분에서 ProxyFactory()를 통해 ProxyFactory를 만들고 target설정, interface 설정을 하고 있다
      - getProxy()를 통해서 repository를 생성한다

- Fetch Join의 조건 : ToOne은 몇개든 사용 / ToMany는 1 개만 가능

- fetch join 할 때 distinct를 안하면 생길 수 있는 문제
  - fetch join은 주로 연관된 엔티티를 함께 로딩하기 위해서 사용된다
  - What if ? 연관된 엔티티들이 중복 -> ex) 하나의 주문에 대해 여러개의 주문 상품이 중복되어 반환되므로 distinct가 필요하다

- fetch join 을 할 때 생기는 에러가 생기는 3가지 에러 메시지
  - `HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!`
    - 원인 : 컬렉션 fetch에서 firstResult나 maxResults를 사용하려고 할 때 발생
    - 해결 : 컬렉션 fetch에 firstResult나 maxResults를 사용하지 않도록 하거나, 쿼리를 변경하여 데이터베이스에서 이러한 제한을 적용
    - ex)
  
    ```java
      List<Order> orders = entityManager.createQuery(
      "SELECT o FROM Order o JOIN FETCH o.orderItems",
      Order.class)
      .setFirstResult(0)
      .setMaxResults(10)
      .getResultList();```
    
  - `query specified join fetching, but the owner of the fetched association was not present in the select list`
    - 원인 : fetch join을 사용하여 연관된 엔티티를 함께 가져오려고 했지만, 기본 엔티티가 select 목록에 포함되지 않은 경우 발생
    - fetch join에 사용된 모든 엔티티를 select 목록에 포함하도록 쿼리를 수정
    - ex)

    ```
    List<OrderItem> orderItems = entityManager.createQuery(
    "SELECT oi FROM OrderItem oi JOIN FETCH oi.order",
    OrderItem.class)
    .getResultList();`
  
  - `org.hibernate.loader.MultipleBagFetchException: cannot simultaneously fetch multiple bags`
    - 원인 : 하이버네이트는 한 번에 여러 개의 컬렉션을 fetch join하는 것을 허용하지 않는다 - 2개 이상의 OneToMany 자식 테이블에 Fetch Join을 선언했을 때
    - 주로 N+1(조회된 부모의 수만큼 자식 테이블의 쿼리가 추가 발생) 문제에 대한 해결책으로 Fetch Join을 사용하다보면 나온다
      - 부모 엔티티와 연관 관계가 있는 자식 엔티티들의 조회 쿼리가 문제
      - 부모 엔티티 key하나하나를 자식 엔티티 조회로 사용한다 -> How about 1개씩 사용되는 조건문을 in절로 묶어서 조회?
      - 엔티티에서 여러 개의 컬렉션을 fetch join하려고 시도

    ```java
        @Entity
        public class Order {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
    
        @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
        private List<OrderItem> orderItems = new ArrayList<>();
    
        @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
        private List<Payment> payments = new ArrayList<>();
    
        // Getters and setters
        }
    
        @Entity
        public class OrderItem {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        
            @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn(name = "order_id")
            private Order order;
        
            // Other fields, getters and setters
        }
        
        @Entity
        public class Payment {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        
            @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn(name = "order_id")
            private Order order;
        
            // Other fields, getters and setters
    }
    
----------------------------------------------------------------------------------------------------------
- test 조금 더 공부하자

## 통합 테스트 - @SpringBootTest
- 모든 빈을 등록하여 테스트를 진행 -> 애플리케이션 규모가 크면 테스트가 많이 느려진다
- @RunWith : 해당 어노테이션을 사용하면 JUnit의 러너를 사용하는게 아니라 지정된 SpringRunner 클래스를 사용한다.
- @SpringBootTest
- @EnableConfigurationProperties : Configuration으로 사용하는 클래스를 빈으로 등록할 수 있게 해줌.
```java
    @RunWith(SpringRunner.class)
@SpringBootTest(
        properties = {
                "property.value=propertyTest",
                "value=test"
        },
        classes = {TestApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@EnableConfigurationProperties(StayGolfConfiguration.class)
public class TestApplicationTests {

  @Value("${value}")
  private String value;

  @Value("${property.value}")
  private String propertyValue;

  @Test
  public void contextLoads() {
    assertThat(value, is("test"));
    assertThat(propertyValue, is("propertyTest"));
  }

}
```
## 컨트롤러 테스트 - @WebMvcTest
- 웹상에서 요청과 응답에 대한 테스트
```java
      @RunWith(SpringRunner.class)
    @WebMvcTest(BookApi.class)
    public class BookApiTest {

      @Autowired
      private MockMvc mvc;

      @MockBean
      private BookService bookService;

      @Test
      public void getBook_test() throws Exception {
          //given
          final Book book = new Book(1L, "title", 1000D);

          given(bookService.getBook()).willReturn(book);

          //when
          final ResultActions actions = mvc.perform(get("/books/{id}", 1L)
                  .contentType(MediaType.APPLICATION_JSON_UTF8))
                  .andDo(print());

          //then
          actions
                  .andExpect(status().isOk())
                  .andExpect(jsonPath("id").value(1L))
                  .andExpect(jsonPath("title").value("title"))
                  .andExpect(jsonPath("price").value(1000D))
          ;

      }
    }
```

## JPA 관련 테스트 - @DataJpaTest
- JPA 관련된 설정만 로드
- @Entity 클래스를 스캔하여 스프링 데이터 JPA 저장소를 구성
기본적으로 인메모리 데이터베이스를 이용
- 데이터소스의 설정이 정상적인지, JPA를 사용하서 데이터를 제대로 생성, 수정, 삭제하는지 등의 테스트가 가능
- @AutoConfigureTestDataBase : 데이터 소스를 어떤 걸로 사용할지에 대한 설정
- Replace.Any : 기본적으로 내장된 데이터소스를 사용
- Replace.NONE : @ActiveProfiles 기준으로 프로파일이 설정됨
- @DataJpaTest : 테스트가 끝날 때마다 자동으로 테스트에 사용한 데이터를 롤백
```java
        /**
     * What the test database can replace.
     */ 
    enum Replace {
    
      /**
       * Replace any DataSource bean (auto-configured or manually defined).
       */
      ANY,
    
      /**
       * Only replace auto-configured DataSource.
       */
      AUTO_CONFIGURED,
    
      /**
       * Don't replace the application default DataSource.
       */
      NONE
    
    }
```
## REST 관련 테스트 - @RestClientTest
- Rest 통신의 JSON 형식이 예상대로 응답을 반환하는지 등을 테스트
- @RestClientTest : 테스트 대상이 되는 빈을 주입받음
- @Rule
- MockRestServiceServer : 클라이언트와 서버 사이의 REST 테스트를 위한 객체. 내부에서 RestTemplate을 바인딩하여 실제로 통신이 이루어지게끔 구성할 수 있음. 이 코드에서는 목 객체와 같이 실제 통신이 이루어지지는 않지만 지정한 경로에 예상되는 반환값을 명시
```java
        @RunWith(SpringRunner.class)
    @RestClientTest(BookRestService.class)
    public class BookRestServiceTest {
    
        @Rule
        public ExpectedException thrown = ExpectedException.none();
    
        @Autowired
        private BookRestService bookRestService;
    
        @Autowired
        private MockRestServiceServer server;
    
        @Test
        public void rest_test() {
    
            server.expect(requestTo("/rest/test"))
                    .andRespond(
                            withSuccess(new ClassPathResource("/test.json", getClass()), MediaType.APPLICATION_JSON));
    
            Book book = bookRestService.getRestBook();
    
            assertThat(book.getId(), is(notNullValue()));
            assertThat(book.getTitle(), is("title"));
            assertThat(book.getPrice(), is(1000D));
    
        }
    }
```
-----------------------------------------------------------------
# Service 구현해보기

## 로그인 기능
## 글 쓰기
- user정보를 가져오고 boardId로 boardRepository에서 Board를 가져오고 postRequestDto를 통하여 값들을 받아와서 post에 값을 넣어주고 postRepository를 통해 저장해줍니다
## 글 조회
- postId를 통해 postRepository에서 post를 조회하여서 글의 내용을 postResponseDto를 통하여 가져옵니다
## 댓글 달기
- user정보와 postId를 통해 postRepository에서 post를 조회하고 commentRequestDto를 통하여 정보를 가져와서 Comment를 작성합니다
## 글 좋아요
- user정보와 PostId를 통해 PostLikeRepository에 해당 데이터가 있는지 확인하고 없으면 저장해준다
## 댓글 좋아요
- user정보와 CommnetId를 통해 CommentLikeRepository에 해당데이터가 있는지 확인하고 없으면 저장해준다
## 학기에 시간표 추가
## 시간표를 table에 추가
## 메시지 보내기

를 하려고 했으니 마지막 3개와 test코드 검증들을 시간상으로 진행을 거의 못했습니다.........ㅠㅠㅠㅠㅠㅠㅠㅠ
spring security가 그 뒤에 한다는걸 못보고 이걸 login을 구현해나?해서 이거에 시간을 많이 쏟았네요 ㅠㅠ

## 댓글 달기 서비스
```java
        insert
        into
        post
        (anonymous, board_id, content, created_date, likes, modified_date, title, user_id, view)
        values
        (?, ?, ?, ?, ?, ?, ?, ?, ?)
        Hibernate:
        select
        p1_0.post_id,
        p1_0.anonymous,
        p1_0.board_id,
        p1_0.content,
        p1_0.created_date,
        p1_0.likes,
        p1_0.modified_date,
        p1_0.title,
        p1_0.user_id,
        p1_0.view
        from
        post p1_0
        where
        p1_0.post_id=?
        Hibernate:
        insert
        into
        comment
        (content, content_like, created_date, modified_date, post_id, user_id)
        values
        (?, ?, ?, ?, ?, ?)
        Hibernate:
        select
        c1_0.comment_id,
        c1_0.content,
        c1_0.content_like,
        c1_0.created_date,
        c1_0.modified_date,
        c1_0.post_id,
        c1_0.user_id
        from
        comment c1_0
        Hibernate:
        insert
        into
        post
        (anonymous, board_id, content, created_date, likes, modified_date, title, user_id, view)
        values
        (?, ?, ?, ?, ?, ?, ?, ?, ?)
        Hibernate:
        insert
        into
        comment
        (content, content_like, created_date, modified_date, post_id, user_id)
        values
        (?, ?, ?, ?, ?, ?)
        Hibernate:
        insert
        into
        comment
        (content, content_like, created_date, modified_date, post_id, user_id)
        values
        (?, ?, ?, ?, ?, ?)
        Hibernate:
        select
        c1_0.comment_id,
        c1_0.content,
        c1_0.content_like,
        c1_0.created_date,
        c1_0.modified_date,
        c1_0.post_id,
        c1_0.user_id
        from
        comment c1_0
```

데이터가 잘 넣어지는 것을 확인가능합니다.!

- PostServiceTest 작성

```java
@BeforeEach
    void setUp() {
            MockitoAnnotations.openMocks(this);
            }
```
- Mockito test 세팅
```java
    @Test
    void postLikeCreate_Success() {

            User user = new User();
            user.setUsername("testUser");

            UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");

        Post post = new Post();
        post.setPostId(1L);

        // Mocking repositories
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postLikeRepository.findByUserAndComment(user, post)).thenReturn(Optional.empty());

        ApiResponseDto<SuccessResponse> response = postLikeService.postLikeCreate(userDetails, 1L);

        verify(postLikeRepository, times(1)).save(any(postLike.class));

        assertEquals(HttpStatus.OK, response.getResponse().getStatus());
        assertEquals("commentLike Create Success", response.getError().getMessage());
        }
```
- 사용자가 유효하고 존재하는 경우에 좋아요를 성공적으로 만드는지 확인
- 이를 위해 모의 객체를 사용하여 사용자, UserDetails 및 게시물을 설정하고, 해당 사용자가 게시물을 좋아요했는지 확인
```java
    @Test
    void postLikeCreate_UserNotFound() {

            UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("nonExistingUser");

        when(userRepository.findByUsername("nonExistingUser")).thenReturn(Optional.empty());

        assertThrows(RestApiException.class, () -> postLikeService.postLikeCreate(userDetails, 1L),
        "Expected RestApiException to be thrown");

        verify(postLikeRepository, never()).save(any(postLike.class));
        }
```
- 사용자가 존재하지 않는 경우, 즉 userRepository가 빈 Optional을 반환할 때 RestApiException이 발생하는지 확인
```java
    @Test
    void postLikeCreate_PostNotFound() {

            UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");

        User user = new User();
        user.setUsername("testUser");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RestApiException.class, () -> postLikeService.postLikeCreate(userDetails, 1L),
        "Expected RestApiException to be thrown");

        verify(postLikeRepository, never()).save(any(postLike.class));
        }
```
- 게시물이 존재하지 않는 경우, 즉 postRepository가 빈 Optional을 반환할 때 RestApiException이 발생하는지 확인
```java

@Test
    void postLikeCreate_AlreadyExists() {

            User user = new User();
            user.setUsername("testUser");

            UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");

        Post post = new Post();
        post.setPostId(1L);

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postLikeRepository.findByUserAndComment(user, post)).thenReturn(Optional.of(new postLike()));

        assertThrows(RestApiException.class, () -> postLikeService.postLikeCreate(userDetails, 1L),
        "Expected RestApiException to be thrown");

        verify(postLikeRepository, never()).save(any(postLike.class));
        }
```
- 이미 사용자가 게시물에 좋아요를 한 경우, 즉 postLikeRepository가 비어 있지 않은 Optional을 반환할 때 RestApiException이 발생하는지 확인

*** 코드에러(뭘 잘못 작성했나봐요)...로 결과 값 도출은 못했습니다.. 시간이슈로 다시 작성을 못했는데 추후에 하겠습니다. 죄송합니다.

-----------------------------------------------------------------------------------------------------------------------------------------------------------------
# Todo

## Post 관련
- 게시글 만들기
- 게시글 삭제
- 게시글 수정
- 게시글 정보 가져오기 -> 댓글 대댓글 모두다 가져오기 <댓글과 대댓글에서 조회를 굳이 하지 않아도 될거 같아서 변경>

- 댓글 만들기
- 댓글 삭제

- 대댓글 만들기
- 대댓글 삭제

- 게시글 좋아요
- 게시글 좋아요 취소
- 댓글 좋아요
- 댓글 좋아요 취소
- 대댓글 좋아요
- 대댓글 좋아요 취소

## 댓글 삭제는 어떻게 진행이 될까??

### what is 고아 객체
- 부모 엔티티와 연관관계가 끊어진 자식 엔티티
  - 부모가 제거될 때, 부모와  연관되어 있는 모든 자식 엔티티들은 고아객체가 된다
  - 부모 엔티티와 자식 엔티티 사이의 연관관계를 삭제할때, 해당 자식 엔티티는 고아 객체가 된다
  - ex_)
- Member Entity 코드
```java
        @Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    // custructor

    // 연관관계 편의 메서드
    public void setTeam(Team team) {

        // 기존 팀과 연관관계를 제거
        if (this.team != null) {
            this.team.getMembers().remove(this);
        }

        // 새로운 연관관계 설정
        this.team = team;
        if (team != null) {
            team.getMembers().add(this);
        }
    }

}
```
- Team Entity 코드
```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id
    @Column(name = "TEAM_ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @OneToMany(
            mappedBy = "team",
            cascade = CascadeType.PERSIST
    )
    private List<Member> members = new ArrayList<>();

    // custructor

}
```
- 부모엔티티가 자식엔티티에게 영속성을 전달해주기 위해 cascade = CascadeType.PERSIST 옵션 지정

- 테스트 코드
```java
// 내장 DB (가짜 DB)로 테스트를 수행 - 단위 테스트
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) 
@DataJpaTest // @Transactional 포함하고 있기 때문에, 각 테스트 종료 시 Rollback
public class JpaTest {

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void initTest() {
        Team team = new Team(0L, "팀1");
        entityManager.persist(team);

        Member member1 = new Member(0L, "회원1");
        Member member2 = new Member(1L, "회원2");

        // 연관관계의 주인에 값 설정
        member1.setTeam(team);
        member2.setTeam(team);

        // CascadeType.PERSIST 로 인하여 영속성 전이
//        entityManager.persist(member1);
//        entityManager.persist(member2);

        // 영속성 컨텍스트의 변경 내용을 DB에 반영
        entityManager.flush();
    }
}
```
- @BeforeEach 를 사용하여 각 테스트에 필요한 데이터를 사전에 추가
- 부모(Team) 엔티티에 설정해둔 CascadeType.PERSIST 옵션으로 인하여, Team 엔티티 영속화시 하위 엔티티인 Member 엔티티[member1, member2] 역시 영속화
- entityManager.flush(); 를 통해, 영속성 컨텍스트의 변경 내용을 DB에 반영

## CascadeType.REMOVE
- 부모 엔티티가 삭제되면 자식 엔티티도 삭제됩니다. 즉, 부모가 자식의 삭제 생명 주기를 관리
- 부모 엔티티와 자식 엔티티 사이의 연관관계를 제거해도, 자식 엔티티는 삭제되지 않고 그대로 DB에 남아있다.

```java
public class Team {

    @Id
    @Column(name = "TEAM_ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @OneToMany(
            mappedBy = "team",
            cascade = {CascadeType.REMOVE, CascadeType.PERSIST}
    )
    private List<Member> members = new ArrayList<>();

    // custructor

}
```
- 부모 엔티티 삭제
```java
@DisplayName("부모 엔티티(Team)을 삭제하는 경우")
@Test
public void cascadeType_REMOVE_Parent() {
    // when
    Team team = entityManager.find(Team.class, 0L);
    entityManager.remove(team); // 부모 엔티티 삭제

    entityManager.flush();

    // then
    List<Team> teamList = entityManager.createQuery("select t from Team t", Team.class).getResultList();
    Assertions.assertEquals(0, teamList.size());

    List<Member> memberList = entityManager.createQuery("select m from Member m", Member.class).getResultList();
    Assertions.assertEquals(0, memberList.size());
}
```
- 부모 엔티티(Team)를 삭제하게 되면, 이와 연관된 자식 엔티티(member1, member2)도 삭제
- 부모 엔티티와 자식 엔티티 사이의 연관관계 제거
```java
@DisplayName("고아객체 - 부모 엔티티(Team)에서 자식 엔티티(Member)와 연관관계를 끊는 경우")
@Test
public void cascadeType_REMOVE_Persistence_Remove() {
    // when
    Team team = entityManager.find(Team.class, 0L);
    team.getMembers().get(0).setTeam(null);

    entityManager.flush();

    // then
    List<Team> teamList = entityManager.createQuery("select t from Team t", Team.class).getResultList();
    Assertions.assertEquals(1, teamList.size());

    List<Member> memberList = entityManager.createQuery("select m from Member m", Member.class).getResultList();
    Assertions.assertEquals(2, memberList.size());
}
```
부모 엔티티(Team)와 자식 엔티티(Member) 사이의 연과관계를 끊게 되어도, 자식 엔티티는 삭제되지 않는다
부모 엔티티와 자식 엔티티 사이의 연관관계 변경
```java
@DisplayName("자식 엔티티의 연관관계 변경 시")
@Test
public void change_persistence_child() {
    // given
    Team team = new Team(1L, "팀2");
    entityManager.persist(team);

    // when
    Member member1 = entityManager.find(Member.class, 0L);
    member1.setTeam(team); // UPDATE 쿼리 수행
    entityManager.flush();

    // then
    Team team1 = entityManager.createQuery("select t from Team t where t.id = 0", Team.class).getSingleResult();
    Assertions.assertEquals(1L, team1.getMembers().get(0).getId());

    Team team2 = entityManager.createQuery("select t from Team t where t.id = 1", Team.class).getSingleResult();
    Assertions.assertEquals(0L, team2.getMembers().get(0).getId());

    List<Member> memberList = entityManager.createQuery("select m from Member m", Member.class).getResultList();
    Assertions.assertEquals(2, memberList.size());
}
```
부모 엔티티(Team)과 자식 엔티티(Member) 사이의 연관관계가 잘 변경

## orphanRemoval=true
- 부모 엔티티가 삭제되면 자식 엔티티도 삭제됩니다. 즉, 부모가 자식의 삭제 생명 주기를 관리
- 부모 엔티티와 자식 엔티티 사이의 연관관계를 제거하면, 자식 엔티티는 고아 객체로취급되어 DB에서 삭제
```java
public class Team {

    @Id
    @Column(name = "TEAM_ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @OneToMany(
            mappedBy = "team",
            orphanRemoval = true,
            cascade = CascadeType.PERSIST
    )
    private List<Member> members = new ArrayList<>();

    // custructor
```
- 부모 엔티티 삭제
```java
@DisplayName("부모 엔티티(Team)을 삭제하는 경우")
@Test
public void orphanRemoval_true_Parent() {
    // when
    Team team = entityManager.find(Team.class, 0L);
    entityManager.remove(team);

    entityManager.flush();

    // then
    List<Team> teamList = entityManager.createQuery("select t from Team t", Team.class).getResultList();
    Assertions.assertEquals(0, teamList.size());

    List<Member> memberList = entityManager.createQuery("select m from Member m", Member.class).getResultList();
    Assertions.assertEquals(0, memberList.size());

}
```
부모 엔티티(Team)를 삭제하게 되면, 이와 연관된 자식 엔티티(member1, member2)도 삭제

- 부모 엔티티와 자식 엔티티 사이의 연관관계 제거
```java
@DisplayName("고아객체 - 부모 엔티티(Team)에서 자식 엔티티(Member)와 연관관계를 끊는 경우")
@Test
public void orphanRemoval_true_Persistence_Remove() {
    // when
    Team team = entityManager.find(Team.class, 0L);
    team.getMembers().get(0).setTeam(null);

    entityManager.flush();

    // then
    List<Team> teamList = entityManager.createQuery("select t from Team t", Team.class).getResultList();
    Assertions.assertEquals(1, teamList.size());

    List<Member> memberList = entityManager.createQuery("select m from Member m", Member.class).getResultList();
    Assertions.assertEquals(1, memberList.size());
    }
```
부모 엔티티(Team)와 자식 엔티티(Member) 사이의 연과관계를 끊게 되어도, 해당 자식 엔티티가 고아객체로 취급되어 삭제

- 부모 엔티티와 자식 엔티티 사이의 연관관계 변경 시
```java
@DisplayName("자식 엔티티의 연관관계 변경 시")
@Test
public void change_persistence_child() {
    // given
    Team team = new Team(1L, "팀2");
    entityManager.persist(team);

    // when
    Member member1 = entityManager.find(Member.class, 0L);
    member1.setTeam(team); // DELETE, INSERT 쿼리 수행
    entityManager.flush();

    // then
    Team team1 = entityManager.createQuery("select t from Team t where t.id = 0", Team.class).getSingleResult();
    Assertions.assertEquals(1L, team1.getMembers().get(0).getId());

    Team team2 = entityManager.createQuery("select t from Team t where t.id = 1", Team.class).getSingleResult();
    Assertions.assertEquals(0L, team2.getMembers().get(0).getId());

    List<Member> memberList = entityManager.createQuery("select m from Member m", Member.class).getResultList();
    Assertions.assertEquals(2, memberList.size());
}
```
부모 엔티티(Team)과 자식 엔티티(Member) 사이의 연관관계가 잘 변경

# 결과
- 부모 엔티티 삭제
  - CascadeType.REMOVE와 orphanRemoval = true 옵션 모두
  - 부모 엔티티를 삭제하면, 자식 엔티티도 삭제됩니다.
  
- 부모 엔티티와 자식 엔티티 사이의 연관관계 제거
  - CascadeType.REMOVE 옵션은 자식 엔티티가 DB에 삭제되지 않고 남아있으며, 외래키 값만 변경됩니다.
  - orphanRemoval = true 옵션은 자식 엔티티가 고아 객체로 취급되어 DB에서 삭제됩니다.

- 부모 엔티티와 자식 엔티티 사이의 연관관계 변경
  - CascadeType.REMOVE와 orphanRemoval = true 옵션 모두
  - 자식 엔티티가 DB에 삭제되지 않고 남아있으며, 외래키 값만 변경됩니다.'

- 회원가입 완료
![img_2.png](img_2.png)

- 특정 post에 대한 글 내용 댓글 대댓글 모두 확인 (like 포함)
```java
{
    "success": true,
    "response": {
        "postId": 1,
        "title": "tes1",
        "anonymous": true,
        "view": 0,
        "likes": 0,
        "commentResponseDtoList": [
            {
                "content": "testest1",
                "username": "thoja45hw",
                "contentLike": 0,
                "replyResponseDtoList": [
                    {
                        "commentId": 1,
                        "content": "testesttest1",
                        "likes": 0
                    },
                    {
                        "commentId": 1,
                        "content": "testesttest4",
                        "likes": 23
                    }
                ]
            },
            {
                "content": "testest2",
                "username": "thoja45hw",
                "contentLike": 1,
                "replyResponseDtoList": [
                    {
                        "commentId": 2,
                        "content": "testesttest2",
                        "likes": 0
                    },
                    {
                        "commentId": 2,
                        "content": "testesttest5",
                        "likes": 2
                    }
                ]
            }
        ]
    },
    "error": null
}
```

- comment를 삭제할 시 CascadeType.REMOVE / orphanRemoval 비교 
- cascade remove
```java
Hibernate: 
    select
        u1_0.user_id,
        u1_0.login_type,
        u1_0.nick_name,
        u1_0.password,
        u1_0.role,
        u1_0.school_id,
        u1_0.time_table_id,
        u1_0.username 
    from
        user u1_0 
    where
        u1_0.username=?
Hibernate: 
    insert 
    into
        user
        (login_type, nick_name, password, role, school_id, time_table_id, username) 
    values
        (?, ?, ?, ?, ?, ?, ?)
Hibernate: 
    select
        u1_0.user_id,
        u1_0.login_type,
        u1_0.nick_name,
        u1_0.password,
        u1_0.role,
        u1_0.school_id,
        u1_0.time_table_id,
        u1_0.username 
    from
        user u1_0 
    where
        u1_0.username=?
Hibernate: 
    select
        u1_0.user_id,
        u1_0.login_type,
        u1_0.nick_name,
        u1_0.password,
        u1_0.role,
        u1_0.school_id,
        u1_0.time_table_id,
        u1_0.username 
    from
        user u1_0 
    where
        u1_0.username=?
Hibernate: 
    select
        c1_0.comment_id,
        c1_0.content,
        c1_0.content_like,
        c1_0.created_date,
        c1_0.modified_date,
        p1_0.post_id,
        p1_0.anonymous,
        p1_0.board_id,
        p1_0.content,
        p1_0.created_date,
        p1_0.likes,
        p1_0.modified_date,
        p1_0.title,
        p1_0.user_id,
        p1_0.view,
        u2_0.user_id,
        u2_0.login_type,
        u2_0.nick_name,
        u2_0.password,
        u2_0.role,
        u2_0.school_id,
        u2_0.time_table_id,
        u2_0.username 
    from
        comment c1_0 
    left join
        post p1_0 
            on p1_0.post_id=c1_0.post_id 
    left join
        user u2_0 
            on u2_0.user_id=c1_0.user_id 
    where
        c1_0.comment_id=?
Hibernate: 
    select
        rl1_0.comment_id,
        rl1_0.reply_id,
        rl1_0.content,
        rl1_0.likes,
        rl1_0.user_id 
    from
        reply rl1_0 
    where
        rl1_0.comment_id=?
Hibernate: 
    delete 
    from
        reply 
    where
        reply_id=?
Hibernate: 
    delete 
    from
        reply 
    where
        reply_id=?
Hibernate: 
    delete 
    from
        comment 
    where
        comment_id=?
```
- orphan = True를 사용할 떄`
```java
Hibernate: 
    select
        u1_0.user_id,
        u1_0.login_type,
        u1_0.nick_name,
        u1_0.password,
        u1_0.role,
        u1_0.school_id,
        u1_0.time_table_id,
        u1_0.username 
    from
        user u1_0 
    where
        u1_0.username=?
Hibernate: 
    select
        u1_0.user_id,
        u1_0.login_type,
        u1_0.nick_name,
        u1_0.password,
        u1_0.role,
        u1_0.school_id,
        u1_0.time_table_id,
        u1_0.username 
    from
        user u1_0 
    where
        u1_0.username=?
Hibernate: 
    select
        c1_0.comment_id,
        c1_0.content,
        c1_0.content_like,
        c1_0.created_date,
        c1_0.modified_date,
        p1_0.post_id,
        p1_0.anonymous,
        p1_0.board_id,
        p1_0.content,
        p1_0.created_date,
        p1_0.likes,
        p1_0.modified_date,
        p1_0.title,
        p1_0.user_id,
        p1_0.view,
        u2_0.user_id,
        u2_0.login_type,
        u2_0.nick_name,
        u2_0.password,
        u2_0.role,
        u2_0.school_id,
        u2_0.time_table_id,
        u2_0.username 
    from
        comment c1_0 
    left join
        post p1_0 
            on p1_0.post_id=c1_0.post_id 
    left join
        user u2_0 
            on u2_0.user_id=c1_0.user_id 
    where
        c1_0.comment_id=?
Hibernate: 
    select
        rl1_0.comment_id,
        rl1_0.reply_id,
        rl1_0.content,
        rl1_0.likes,
        rl1_0.user_id 
    from
        reply rl1_0 
    where
        rl1_0.comment_id=?
Hibernate: 
    delete 
    from
        reply 
    where
        reply_id=?
Hibernate: 
    delete 
    from
        reply 
    where
        reply_id=?
Hibernate: 
    delete 
    from
        comment 
    where
        comment_id=?
```
..... delete쿼리가 1번 나가야하는데 왜 이렇게 나올까요.....?