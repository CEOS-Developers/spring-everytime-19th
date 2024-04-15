# spring-everytime-19th

CEOS 19th BE study - everytime clone coding

# Week 2

## DB 모델링

![image](https://github.com/CEOS-Developers/spring-everytime-19th/assets/116694226/9a5f7bdb-fc47-4253-a952-d818aad82133)

### 학교

<img src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/116694226/30e09e4a-cd85-448e-ac4a-2eda6a09be09" width=700>

- 학교마다 다른 게시판을 만들 수 있습니다.
- 회원은 학교에 소속되어야 합니다.

### 회원/메시지

<img src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/116694226/791c136f-c387-4d25-8fd7-e2be54301225" width=700>

- 회원은 여러 개의 메시지를 주고 받을 수 있습니다.

### 회원/댓글

<img src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/116694226/fe6f36f6-7847-4d39-a6e0-79ca20429c36" width=700>

- 회원은 댓글을 여러 개 달 수 있습니다.
- 회원은 댓글에 좋아요를 누를 수 있습니다.
- 회원은 대댓글을 달 수 있습니다.

### 게시글/댓글

<img src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/116694226/be705f67-b66c-44f7-a211-1ddce9eca8f6" height=500>

- 하나의 게시글에 여러 개의 댓글을 달 수 있습니다.

### 게시판/게시글

<img src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/116694226/0c63d084-f75f-4f53-8d04-13827cc536a1" width=700>

- 하나의 게시판에 여러 개의 게시글을 작성할 수 있습니다.

### 회원/게시글

<img src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/116694226/ce4986ee-eb39-47d3-bf93-b6a952bac63d" width=700>

- 회원은 여러 게시글에 좋아요를 누를 수 있습니다.
- 회원은 여러 게시글을 작성할 수 있습니다.

### 게시글/이미지

<img src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/116694226/7d4211f6-e13a-40db-bd98-e440d6bbf9cc" width=700>

- 게시글에 여러 개의 이미지를 올릴 수 있습니다.

## Repository 단위 테스트

저는 `@DataJpaTest`를 사용해서 `PostRepository`에 대한 단위 테스트를 작성했습니다.

```java

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private User user;
    private Post post1;
    private Post post2;
    private Post post3;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("test")
                .password("qwe123!")
                .nickname("nick")
                .build();
        post1 = Post.builder()
                .title("title1")
                .user(user)
                .content("content1")
                .build();
        post2 = Post.builder()
                .title("title1")
                .user(user)
                .content("content1")
                .build();
        post3 = Post.builder()
                .title("title1")
                .user(user)
                .content("content1")
                .build();
        userRepository.save(user);
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
    }

    @Test
    void 게시글_조회_테스트() {
        // given & when
        final Post post1 = postRepository.findById(1L).get();
        final Post post2 = postRepository.findById(2L).get();
        final Post post3 = postRepository.findById(3L).get();

        // then
        assertAll(
                () -> assertThat(post1).usingRecursiveComparison().isEqualTo(this.post1),
                () -> assertThat(post2).usingRecursiveComparison().isEqualTo(this.post2),
                () -> assertThat(post3).usingRecursiveComparison().isEqualTo(this.post3)
        );
    }
}
```

하지만 테스트를 실행하면 다음과 같은 에러가 발생합니다.

```text
Caused by: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'dataSourceScriptDatabaseInitializer' defined in class path resource [org/springframework/boot/autoconfigure/sql/init/DataSourceInitializationConfiguration.class]: Unsatisfied dependency expressed through method 'dataSourceScriptDatabaseInitializer' parameter 0: Error creating bean with name 'dataSource': Failed to replace DataSource with an embedded database for tests. If you want an embedded database please put a supported one on the classpath or tune the replace attribute of @AutoConfigureTestDatabase.
	at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:798)
	at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:542)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(AbstractAutowireCapableBeanFactory.java:1335)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1165)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:562)
	...
```

`@DataJpaTest`는 기본적으로 내장 데이터베이스를 사용하기 때문에 `DataSource` 빈을 찾을 수 없다는 에러가 발생했습니다.
이 문제를 해결하기 위해 `@AutoConfigureTestDatabase`를 사용해서 기본 `DataSource` 빈을 교체하지 않는 설정을 했습니다.

```text
org.springframework.dao.DataIntegrityViolationException: not-null property references a null or transient value : com.ceos19.everytime.domain.User.createdAt
	at org.springframework.orm.jpa.vendor.HibernateJpaDialect.convertHibernateAccessException(HibernateJpaDialect.java:307)
	at org.springframework.orm.jpa.vendor.HibernateJpaDialect.translateExceptionIfPossible(HibernateJpaDialect.java:241)
	at org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.translateExceptionIfPossible(AbstractEntityManagerFactoryBean.java:550)
	at org.springframework.dao.support.ChainedPersistenceExceptionTranslator.translateExceptionIfPossible(ChainedPersistenceExceptionTranslator.java:61)
	at org.springframework.dao.support.DataAccessUtils.translateIfNecessary(DataAccessUtils.java:335)
	at org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:152)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:184)
	at org.springframework.data.jpa.repository.support.CrudMethodMetadataPostProcessor$CrudMethodMetadataPopulatingMethodInterceptor.invoke(CrudMethodMetadataPostProcessor.java:164)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:184)
```

`@DataJpaTest`는 Repository와 관련된 빈만 등록하기 때문에 `@EnableJpaAuditing`이 있는 JpaAuditingConfig는 등록되지 않아 `@EnableJpaAuditing`이
적용되지 않았습니다.
이 문제를 해결하기 위해 `@Import`를 사용해서 `JpaAuditingConfig`를 등록했습니다.

```java

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaAuditingConfig.class)
class PostRepositoryTest {
    ...
}
```

이렇게 설정하고 테스트를 실행하면 테스트가 성공하고 다음과 같은 쿼리가 출력됩니다.

```sql
Hibernate: 
    insert 
    into
        users
        (created_at, nickname, password, school_id, updated_at, username) 
    values
        (?, ?, ?, ?, ?, ?)
Hibernate: 
    insert 
    into
        post
        (board_id, content, created_at, is_anonymous, title, updated_at, user_id) 
    values
        (?, ?, ?, ?, ?, ?, ?)
Hibernate: 
    insert 
    into
        post
        (board_id, content, created_at, is_anonymous, title, updated_at, user_id) 
    values
        (?, ?, ?, ?, ?, ?, ?)
Hibernate: 
    insert 
    into
        post
        (board_id, content, created_at, is_anonymous, title, updated_at, user_id) 
    values
        (?, ?, ?, ?, ?, ?, ?)
````

## 어떻게 data jpa는 interface만으로도 함수가 구현이 되는가?

이렇게 인터페이스로 구현할 수 있다는 것이 놀랍지 않은가요?

![image](https://github.com/CEOS-Developers/spring-everytime-19th/assets/116694226/145d4dcb-dfd9-498b-9534-e0e691eaa85f)

우리는 어떻게 구현체도 없이 findByName을 사용할 수 있을까요?

MemberService에서 memberRepository에 주입되는 빈을 살펴봅시다.
디버깅을 했더니 memberRepository에는 SimpleJpaRepository를 프록시로 주입하고 있었습니다.

![img](https://github.com/CEOS-Developers/spring-everytime-19th/assets/116694226/9418d160-3cca-4768-8657-0d4428c381f2))

Spring data jpa는 JpaRepositoryFactory에서 개발자가 정의한 MemberRepository를 참조하여 JpaRepository를 대신 구현해주는 역할을 합니다.

```java
public class JpaRepositoryFactory extends RepositoryFactorySupport {
    ...
}
```

RepositoryFactorySupport는 전달받은 Repository 인터페이스로 프록시 인스턴스를 생성하는 추상 클래스입니다.
Spring data XXX는 `RepositoryFactorySupport`를 확장해서 Repository를 생성합니다.
getRepository를 통해 Repository 인터페이스를 구현할 프록시를 생성합니다.

```java
public abstract class RepositoryFactorySupport implements BeanClassLoaderAware, BeanFactoryAware {
    ...

    // 첫 번째 파라미터 repositoryInterface로 MemberRepository가 전달됩니다.
    public <T> T getRepository(Class<T> repositoryInterface, RepositoryComposition.RepositoryFragments fragments) {
        ...

        // repositoryInterface로 전달된 MemberRepository의 인터페이스를 구현합니다.
        ProxyFactory result = new ProxyFactory();
        result.setTarget(target);
        result.setInterfaces(new Class[]{repositoryInterface, Repository.class, TransactionalProxy.class});
        
        ...

        T repository = result.getProxy(this.classLoader);
        
        ...
        
        // 프록시 인스턴스를 반환합니다.
        return repository;
    }
}
```

Spring data jpa에서는 `JpaRepositoryFactory`가 추상 메서드인 getRepositoryBaseClass가 `SimpleJpaRepository.class`를 반환하도록 오버라이드했습니다.

```java
protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
    return SimpleJpaRepository.class;
}
```

정리하자면 빈으로 생성되는 MemberRepository는 프록시 인스턴스이고 
JpaRepository는 프록시 인스턴스를 생성해 SimpleJpaRepsitory에 주입합니다.
그리고 프록시 인스턴스 내부에서 SimpleJpaRepsitory가 구현체를 실행합니다.

## References
- [https://brunch.co.kr/@anonymdevoo/40](https://brunch.co.kr/@anonymdevoo/40)

## Week 3
이번 주는 Service 계층을 구현하고 테스트 코드를 작성해보았습니다. 우선 기능 목록은 다음과 같습니다.

- 회원 가입
- 회원 탈퇴
- 게시글 생성 
- 게시글 댠건 조회 
- 모든 게시글 조회 
- 댓글 작성 
- 댓글 삭제 
- 댓글 좋아요 
- 댓글 좋아요 취소 
- 게시글 좋아요 
- 게시글 좋아요 취소 
- 쪽지 전송 
- 쪽지 읽기

### Repository 단위 테스트

Repository를 테스트할 때마다 @DataJpaTest, @AutoConfigureTestDatabase 그리고 @Import를 사용해야 했기 때문에 다음과 같이 커스텀 애노테이션으로 만들었습니다.

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaAuditingConfig.class)
public @interface RepositoryTest {
}
```

그래서 Repository에 대한 단위 테스트는 `@RepositoryTest`를 사용해서 테스트할 수 있습니다.

```java
@RepositoryTest
class XXRepositoryTest {
    ...
}
```

### Service 단위 테스트

Service 계층의 단위 테스트는 Mockito를 사용해서 Repository 계층을 Mocking해서 테스트했습니다.

```java
@ExtendWith(MockitoExtension.class)
class XXServiceTest {
    
    @Mock
    private XXRepository xxRepository;
    
    @InjectMocks
    private XXService xxService;
    
    // 테스트 코드
}
```

### 고민한 부분

항상 Service 계층을 구현할 때 조회하는 로직을 Repository에서 바로 가져올지, Service 계층에서 로직을 구현할지 고민이 됩니다.

```java
public class PostService {

    private final userRepository userRepository;

    public PostService findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        ...
    }
}
```

```java
public class PostService {

    private final UserService userService;

    public Post findById(Long id) {
        User user = userService.findUser(id);
        ...
        
    }
}

public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
    }
}
```

회원이 존재하는지 검증하고 조회하는 로직은 필요한 곳이 많기 때문에 Service 계층에서는 여러 군데에서 사용이 됩니다.
만약, 첫 번째 방법처럼 구현을 한다면 조회 로직을 private 메서드로 분리할 수 있습니다. 하지만 다른 XXXService에서도 사용할 수 있기 때문에 동일한 로직이 중복이 됩니다.

두 번째 방법처럼 구현을 한다면 조회 로직을 UserService로 분리할 수 있습니다. 이 방법은 조회 로직을 재사용할 수 있지만 Service 계층이 Repository 계층에 의존하게 됩니다.
그렇지만 Service 계층을 직접 호출하게 되면 순환 참조 문제가 발생할 수 있기 때문에 아직은 Repository에서 직접 조회하는 방법을 사용하고 있습니다.

그러나 이 방법이 옳은지에 대해서는 고민을 더 해봐야 될 것 같습니다.

# Week 4
## 리팩토링
### 클래스 레벨에 Transactional(readOnly = true) 적용

![image](https://github.com/CEOS-Developers/spring-everytime-19th/assets/116694226/968227d2-9714-4944-bb10-0200bc0f9577)

![image](https://github.com/CEOS-Developers/spring-everytime-19th/assets/116694226/0b4eb524-f308-4f45-8b57-111cb2b96ed7)

이렇게 두 분에게 같은 리뷰를 받고, `@Transactional(readOnly = true)`를 클래스 레벨에 적용했습니다.

### 디비에 저장된 id로 테스트하도록 수정

기존의 코드는 다음과 같습니다.

```java
@Test
void 댓글을_삭제한다() {
    // given
    final User user = User.builder()
            .nickname("nickname")
            .password("password")
            .username("username")
            .build();
    userRepository.save(user);

    final Post post = Post.builder()
            .title("test")
            .content("content")
            .isAnonymous(false)
            .writer(user)
            .build();
    postRepository.save(post);

    final Comment comment = Comment.builder()
            .content("content")
            .isAnonymous(false)
            .user(user)
            .post(post)
            .build();
    commentRepository.save(comment);

    // when
    commentRepository.deleteById(1L);

    // then
    final Boolean result = (Boolean) em.createNativeQuery("SELECT is_deleted FROM comment WHERE comment_id = 1",
                    Boolean.class)
            .getSingleResult();
    assertThat(result).isTrue();
}
```

하지만 위의 테스트 경우에는 특정 ID에 의존하고 있기 때문에, 데이터베이스에 특정 ID의 댓글이 존재하지 않을 경우 테스트가 실패할 수 있습니다.
그래서 저장된 Comment 객체를 받아 이 객체의 id로 테스트하도록 수정했습니다.

```java
@Test
void 댓글을_삭제한다() {
    // given
    ...
    final Comment savedComment = commentRepository.save(comment);

    // when
    commentRepository.deleteById(savedComment.getId());

    // then
    final Boolean result = (Boolean) em.createNativeQuery("SELECT is_deleted FROM comment WHERE comment_id = ?",
                    Boolean.class)
            .setParameter(1, savedComment.getId())
            .getSingleResult();
    assertThat(result).isTrue();
}
```

### Posts 클래스 삭제

Posts 클래스는 Post의 리스트를 감싸는 일급 컬렉션으로, 이 클래스는 dto로 변환하는 메서드만 가지고 있었습니다.

```java
public class Posts {

    private final List<Post> posts;

    public Posts(final List<Post> posts) {
        this.posts = posts;
    }

    public List<BoardPostsResponseDto> toResponseDto() {
        return posts.stream()
                .map(BoardPostsResponseDto::from)
                .toList();
    }
}
```


일급 컬렉션은 도메인에 더 가깝다는 생각이 들어서 dto 리스트로 변환하는 로직을 서비스 레이어로 옮기고 Posts 클래스를 삭제했습니다.

## API 만들기
### 회원 가입 API

- URL: `/api/users`
- Method: POST
- Request Body: 
    ```json
    {
        "username": String,
        "password": String,
        "nickname": String,
        "schoolName": String,
         "department": String
    }
    ```
  
### 회원 탈퇴 API

- URL: `/api/users/{userId}`
- Method: DELETE
- Path Variable: userId(Long)

### 게시글 작성 API

- URL: `/api/post`
- Method: POST
- Request Body:
    ```json
    {
        "title": String,
        "content": String,
        "isAnonymous": boolean,
        "boardId": Long,
        "writerId": Long
    }
    ```
  
### 모든 게시글 조회 API

- URL: `/api/posts`
- Method: GET

### 게시글 조회 API

- URL: `/api/post/{postId}`
- Method: GET

### 게시글 좋아요 등록 API

- URL: `/api/post/likes/{postId}`
- Method: POST
- Path Variable: postId(Long)
- Request Body:
    ```json
    {
        "userId": Long
    }
    ```
  
### 게시글 좋아요 취소 API

- URL: `/api/post/likes/{postId}`
- Method: DELETE
- Path Variable: postId(Long)
- Request Body:
    ```json
    {
        "userId": Long
    }
    ```
  
### 쪽지 전송 API

- URL: `/api/messages`
- Method: POST
- Request Body:
    ```json
    {
        "senderId": Long,
        "receiverId": Long,
        "content": String
    }
    ```
  
### 쪽지 읽기 API

- URL: `/api/messages`
- Method: GET
- Request Body:
    ```json
    {
        "senderId": Long,
        "receiverId": Long
    }
    ```
  
## 컨트롤러 테스트

이번 기회에 컨트롤러 테스트 작성까지 적용해보면 좋을 것 같다는 생각에 테스트 코드까지 작성했습니다. 
우선 `@WebMvcTest`를 사용했습니다. `@WebMvcTest`는 웹 계층에 집중할 수 있게 해주는 애노테이션입니다. 
웹 계층과 관련된 빈만 등록되기 때문에 `@WebMvcTest`를 사용하면 컨트롤러는 등록이 되지만, `@Repository`나 `@Service` 빈은 등록되지 않습니다.
따라서, `@WebMvcTest`에서 리포지토리와 서비스를 사용하기 위해서는 `@MockBean`을 사용하여 리포지토리와 서비스를 Mock 객체에 빈으로 등록해줘야 합니다.

```java
@WebMvcTest(UserController.class)
class UserControllerTest {
    
    private static final String USER_DEFAULT_URL = "/api/users";

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void 회원_가입에_성공한다() throws Exception {
        // given
        final UserSaveRequestDto request = new UserSaveRequestDto("username", "password", "nickname", "홍익대학교", "컴퓨터공학과");

        doNothing().when(userService).saveUser(request);

        // when & then
        mockMvc.perform(post(USER_DEFAULT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
```

## 정적 팩토리 메서드 사용해보기

```java
public record PostResponseDto(String title, String content, String username, String boardName, List<Comment> comments) {

    public static PostResponseDto of(final Post post, final List<Comment> comments) {
        return new PostResponseDto(post.getTitle(), post.getContent(), post.getWriterNickname(),
                post.getBoard().getName(), comments);
    }
}
```

## Swagger
### Swagger 애노테이션

- `@Tag`: API에 대한 설명을 추가할 수 있습니다.
- `@Operation`: API에 대한 설명을 추가할 수 있습니다.
- `@ApiResponses`: API 응답에 대한 설명을 추가할 수 있습니다.
- `@Schema`: API 요청/응답에 대한 스키마를 정의할 수 있습니다.
- `@Parameter`: API 요청 파라미터에 대한 설명을 추가할 수 있습니다.

### Swagger 테스트

Swagger를 사용해서 회원 가입 테스트를 진행했습니다.

![image](https://github.com/CEOS-Developers/spring-everytime-19th/assets/116694226/7e4be793-f334-4e56-91c4-494639ece4f2)

![image](https://github.com/CEOS-Developers/spring-everytime-19th/assets/116694226/bd35c7af-5f69-4f79-abc0-ddd72fde422a)

![image](https://github.com/CEOS-Developers/spring-everytime-19th/assets/116694226/66893a3f-c90f-4830-9046-0dca65146eae)

만약 존재하지 않은 회원이 회원 탈퇴를 요청하면 다음과 같이 에러가 발생합니다.

![image](https://github.com/CEOS-Developers/spring-everytime-19th/assets/116694226/dae820ef-d379-49c2-b02d-ee361d6962e1)

# Week 5
## 인증(Authentication) 방법에 대해서 알아보기
### 쿠키를 이용한 방식

쿠키는 사용자와 사용자의 선호 사항을 식별하는 데 사용되는 데이터 조각입니다. 브라우저는 페이지가 요청될 때마다 서버에 쿠키를 반환합니다.
HTTP 쿠키와 같은 특정 쿠키는 쿠키 기반 인증을 수행하여 각 사용자의 세션을 유지하는 데 사용됩니다.
쿠키 기반 인증 방식은 다음과 같이 동작합니다.

사용자 계정에 연결된 특수 식별자가 있는 쿠키는 사용자가 로그인할 때 웹사이트에 의해 생성됩니다. 
그런 다음 사용자의 장치는 이 쿠키를 수신하여 브라우저에 저장합니다. 
웹사이트는 사용자를 인식하고 후속 방문 시 웹사이트에 쿠키를 다시 전송함으로써 사용자가 다시 로그인할 필요 없이 사용자를 인증할 수 있습니다.
사용자는 자신의 계정에 액세스하기 위해 반복적으로 로그인할 필요가 없으므로 쿠키 인증을 사용하여 간단하고 원활한 사용자 경험을 제공할 수 있습니다. 
사용자 계정의 보안을 위협하지 않으려면 인증에 사용되는 쿠키가 안전하고 조작하기 어려운지 확인하는 것이 중요합니다. 
또한 항상 충분한 보안을 제공할 수 없기 때문에 쿠키 인증이 모든 웹사이트나 애플리케이션에 적합하지 않을 수도 있습니다.

#### 장점

- 편의성: 쿠키 기반 인증을 사용하면 사용자가 브라우저를 닫거나 장치 전원을 끈 후에도 로그인 정보를 계속 입력할 필요가 없기 때문에 사용자가 웹사이트나 애플리케이션에 더 쉽게 액세스할 수 있습니다.
- 확장성: 서버는 각 사용자의 활성 세션만 추적하면 되므로 쿠키 기반 인증을 확장하여 엄청난 수의 사용자를 처리할 수 있습니다.
- 개인화: 쿠키 기반 인증을 통해 웹사이트나 앱에서 사용자의 선호도와 행동을 수집함으로써 웹사이트나 애플리케이션이 사용자 경험을 맞춤화할 수 있습니다.

#### 단점

- 보안 위험: XSS 공격과 세션 하이재킹
- 개인 정보 보호 문제: 웹사이트나 애플리케이션이 사용자에 대한 개인 정보를 수집하고 저장하기 때문에 개인 정보 보호 문제가 발생할 수 있습니다.
- 공용 컴퓨터를 사용하는 경우 장치에 로그인 정보가 저장되어 있다면 사용자가 로그인 정보에 접근할 수 있습니다.

### 세션을 이용한 방식

사용자가 애플리케이션이나 웹 사이트에 로그인하면 일종의 토큰 기반 인증인 세션 인증이 해당 사용자를 위한 특수 세션 ID를 생성합니다. 
이 세션 ID의 서버측 저장소는 해당 시점 이후에 이루어진 사용자 요청을 확인하는 데 사용됩니다.
서버는 로그인할 때마다 새로운 세션 ID를 생성하고 이를 사용자의 계정에 연결합니다. 그러면 사용자의 브라우저는 이 세션 ID를 쿠키로 수신하여 사용자의 장치에 저장됩니다. 
연속된 요청이 있을 때마다 사용자의 브라우저는 세션 ID를 서버로 다시 전송하여 사용자의 신원을 확인하고 보안 리소스에 대한 액세스 권한을 부여할 수 있습니다.
웹 앱과 웹사이트는 세션 인증을 활용하여 사용자가 페이지를 변경하거나 다른 작업을 수행할 때마다 비밀번호를 다시 입력하지 않고도 계정에 액세스할 수 있도록 하는 경우가 많습니다.
강력하고 신뢰할 수 있는 액세스 제어 솔루션을 제공하기 위해 다단계 인증 및 암호화와 같은 다른 보안 조치와 함께 작동하는 경우가 많습니다.

#### 장점

- 보안: 사용자에게 각 세션에 대한 로그인 정보를 입력하도록 요청함으로써 사용자 계정에 대한 원치 않는 액세스를 방지하는 데 도움이 됩니다.
- 사용자는 한 번만 로그인하면 되고 해당 세션은 한동안 활성 상태로 유지되므로 세션 기반 인증을 통해 시스템 사용을 더 쉽게 만들 수 있습니다.
- 확장성: 버는 각 사용자의 로그인 정보를 보관하는 대신 활성 세션만 추적하면 되므로 세션 기반 인증을 쉽게 확장하여 엄청난 수의 사용자를 처리할 수 있습니다.

#### 단점

- 공격자가 사용자 세션을 제어하고 사용자의 신원을 가장하는 세션 하이재킹 공격이 발생할 수 있습니다.
- 사용자가 로그인하기 전에 공격자가 사용자의 세션 ID를 설정하여 사용자가 로그인한 후 공격자가 사용자 세션에 대한 제어권을 부여할 때 문제가 발생합니다.
- 서버는 모든 활성 세션을 추적해야 하기 때문에 세션 기반 인증은 리소스를 매우 많이 소모할 수 있습니다.

### OAuth를 이용한 방식

OAuth는 `Open Authorization`을 의미하며 웹사이트나 애플리케이션이 사용자를 대신하여 다른 웹 앱에 의해 호스팅되는 리소스에 액세스할 수 있도록 설계된 표준입니다.
그래서 OAuth는 사용자의 비밀번호를 공유하지 않고도 서드파티 애플리케이션에 대한 접근 권한을 안전하게 부여할 수 있습니다.
OAuth 인증의 핵심은 액세스 토큰을 사용한다는 점입니다. 사용자가 서드파티 애플리케이션에 로그인할 때, 이 애플리케이션은 사용자를 인증 서버로 리디렉션하고, 사용자가 인증 후 인증 서버는 애플리케이션에 액세스 토큰을 발급합니다. 
왜냐하면 이 토큰을 통해 애플리케이션은 사용자의 데이터에 접근할 수 있기 때문입니다.

OAuth 인증 프로세스의 이해는 개발자가 보안성 높은 애플리케이션을 구축하는 데 있어 중요한 기초 지식을 제공합니다. 이를 통해 사용자 데이터의 보안을 유지하면서도 편리한 사용자 경험을 제공할 수 있습니다.

## References
- [Cookies-Based Authentication Vs Session-Based Authentication](https://dev.to/emmykolic/cookies-based-authentication-vs-session-based-authentication-1f6)
- []()
