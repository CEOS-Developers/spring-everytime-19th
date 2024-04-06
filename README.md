### 💡에브리타임 분석
모든 기능을 분석하기 보단 요구사항에 대한 내용을 중점으로 확인해보자!

<p align="center">
  <img src="https://github.com/CEOS-Developers/spring-tutorial-19th/assets/63999019/1dcfede7-e4e4-49b7-8cb9-28dcf30c925b">
</p>

게시물은 내용을 작성할 수 있으며, 사진을 첨부 할 수 있다.
<p align="center">
  <img src="https://github.com/CEOS-Developers/spring-tutorial-19th/assets/63999019/a899b3b4-846d-4ead-b4f9-4507a02fbcda">
</p>

각 게시물은 좋아요를 가지며, 댓글을 작성할 수 있다.

<p align="center">
  <img src="https://github.com/CEOS-Developers/spring-tutorial-19th/assets/63999019/5aa5b549-6dc8-478e-a871-d47af45032ff">
</p>

각 댓글에는 대댓글을 작성할 수 있다.

<p align="center">
  <img src="https://github.com/CEOS-Developers/spring-tutorial-19th/assets/63999019/1c6096b3-cc93-4ec2-98ac-b8a30cde13ec">
</p>

각 회원들끼리 1대1 쪽지를 주고 받을 수 있다.

위와 같은 요구조건들을 중점으로 DB 설계 및 리포지토리를 구현해보자

### Database Schema
<p align="center">
  <img src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/63999019/c6a7b44c-085e-445e-a82a-a079e249521c">
</p>

위와 같이 연관관계 및 DB 설계를 완료하였다.

`Domain`과 `Repository`는 `Comment`를 기준으로 설명하겠다.

### Domain
<p align="center">
  <img src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/63999019/5c6bfdf0-f073-48f3-997d-3ea280b60d63">
</p>

작성한 도메인 목록은 위와 같다.


```java
public class Comment{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parentComment;

    private String content;
    private LocalDateTime createdAt;
}
```
위에서 부터 하나씩 살펴보자

* id : 각각의 댓글 객체의 식별자로 `@Id`를 통해 기본 키로 설정했다. `@GeneratedValue`를 통해 객체가 생성되면 자동으로 `IDENTITY`하게 생성된다. `@Column`을 통해 DB 테이블에서 어떤 컬럼과 매핑되는 지를 명시했다.
* post : 댓글이 달린 게시물을 의미한다. `@ManyToOne`을 통해 다대일 연관관계로 설정했으며 이는 즉 외래 키임을 의미한다. 또한 `@JoinColumn`을 통해 join 할 때 어떤 컬럼과 매핑되어야 하는 지를 명시한다.
* user : 어떠한 유저가 작성한 댓글인 지를 명시한다.
* parentComment : 대댓글인지 여부를 명시한다. `@JoinColumn`의 `Nullable` 옵션의 디폴트 값은 Null이므로 따로 값을 설정하지 않는다면, 댓글이고 값이 존재한다면 대댓글임을 알 수 있다.
* content : 댓글의 내용을 의미한다.
* createdAt : 댓글의 작성일자를 의미한다.

### Repository
<p align="center">
  <img src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/63999019/a8631801-fa75-4eaa-9c6f-e8fc181100f3">
</p>

각 엔티티에 대한 리포지토리를 전부 생성해주었다.

```java
@Repository
@RequiredArgsConstructor
public class CommentRepository {
    private final EntityManager em;

    public void save(Comment comment){
        em.persist(comment);
    }

    public Optional<Comment> findOne(Long id){
        return Optional.ofNullable(em.find(Comment.class,id));
    }

    public List<Comment> findAll(){
        return em.createQuery("select c from Comment c", Comment.class)
                .getResultList();
    }

    public List<Comment> findByUser(Long id){
        return em.createQuery("select c "
                + "from Comment c "
                + "join fetch c.user u "
                + "where u.id =: userId",
                Comment.class)
                .setParameter("userId",id)
                .getResultList();
    }

    public List<Comment> findByPost(Long id){
        return em.createQuery("select c "
                + "from Comment c "
                + "join fetch c.post p "
                + "where p.id =: postId", Comment.class)
                .setParameter("postId",id)
                .getResultList();
    }

    public void delete(Comment comment){
        em.remove(comment);
    }

}
```
가장 많은 메소드가 구현되어 있는 CommentRepository의 코드 내용이다.

리포지토리에서는 영속성 컨택스트인 EntityManger를 통해 영속성을 부여하고 관리한다.

CRUD 연산을 수행하는 메서드들이 작성되어 있으며

연관관계로 매핑된 값들을 찾을 수 있는 조회 메서드들이 특징이다.

* save : create 연산을 수행한다. `EntityManger`에 `persist`하여 영속성을 부여하고 관리한다.
* delete : delete 연산을 수행하며 `EntityManger`에 `remove`하여 영속성을 제거하고 삭제한다.
* findBy~ : 특정 연관관계로 매핑된 댓글들만 조회한다. JPQL을 통해 작성했으며 fetch join을 통해 쿼리 최적화가 적용되어 있다.
* findOne : 하나의 댓글을 조회한다. 이때 존재하지 않는다면 `find` 메서드는 null을 반환하므로 `Optional`을 통해 null 값을 관리하도록 했다.

### Test
```java
@Transactional
@SpringBootTest
class CommentRepositoryTest {

    @Autowired private CommentRepository commentRepository;
    @Autowired private EntityManager em;

    @Test
    void FindOne() {
        //given
        Comment comment = new Comment();
        //when
        commentRepository.save(comment);
        //then
        Assertions.assertEquals(comment, commentRepository.findOne(comment.getId()));
    }

    @Test
    void findAll() {
        //given
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        //when
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        List<Comment> allComments = commentRepository.findAll();
        //then

        assertEquals(2, allComments.size());
    }

    @Test
    void findByUser() {
        //given
        User user = new User();
        User nonTargetUser = new User();
        em.persist(user);
        em.persist(nonTargetUser);
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        Comment comment3 = new Comment();
        comment1.setUser(user);
        comment2.setUser(user);
        comment3.setUser(nonTargetUser);

        //when
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);

        //then
        List<Comment> allCommentsByUser = commentRepository.findByUser(user.getId());

        assertEquals(2, allCommentsByUser.size());
    }

    @Test
    void findByPost() {
        //given
        Post post = new Post();
        em.persist(post);
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        comment1.setPost(post);
        comment2.setPost(post);

        //when
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        //then
        List<Comment> allCommentsByPost = commentRepository.findByPost(post.getId());

        assertEquals(2, allCommentsByPost.size());
    }

    @Test
    void delete() {
        //given
        Comment comment = new Comment();
        //when
        commentRepository.save(comment);
        commentRepository.delete(comment);

        //then
        assertThrows(EntityNotFoundException.class, () -> commentRepository.findOne(comment.getId())
                .orElseThrow(() -> new EntityNotFoundException("Not found")));
    }
}
```
* `@Transactional` : 하나의 트랙잭션으로 테스트를 수행하도록 하였다.
* `@SpringBootTest` : 의존성 주입을 위해 사용하였으며 통합테스트를 지원한다.
* given, when, then : 테스트 상황을 세 가지로 나눠 가독성을 높혔다.

간단한 테스트는 생략하고 중요 테스트만 설명하겠다.

* `delete` : 리포지토리에서 `Optional`을 사용하고 있기 때문에 댓글을 저장하고 삭제했을 때 `findOne`을 호출하면 null 객체가 반환된다. 이를 `assertThrow`를 통해 `EntityNotFoundException`을 반환하는 지 확인하도록 작성했다.
* `findByPost` : 해당 게시물의 댓글들을 전부 조회하는 지 확인하는 테스트이다. 메서드를 실행했을 때 Post에 저장된 2개의 Comment가 반환되는 지를 테스트했다.
* `findByUser` : 해당 유저가 작성한 댓글들을 전부 조회하는 지 확인하는 테스트이다. `targetUser`가 작성한 세 개중 두개의 댓글만 조회되는 지를 확인한다.

### Service

이번 주차 과제를 진행하면서 도메인형 패키지 구조로 수정했다.
<p align="center">
  <img src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/63999019/4bb55200-9ce4-4486-8a99-78f84348eab0">
</p>

게시물을 의미하는 post 도메인을 위주로 설명하겠다.
<p align="center">
  <img src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/63999019/fc7a039d-dade-4510-a421-7fb8d5d50c76">
</p>

dto를 사용해 계층간 데이터 통신을 수행했다.

***외부 api가 없는 구조***라면 엔티티를 사용해도 크게 문제가 되진 않지만

만약 스펙이 다른 api가 연결되어 있고 서비스를 호출하는 구조라면

서비스에 맞는 스펙으로 정보를 전달해야하기 때문에 dto를 활용해 확장성을 높혔다.

```java
package com.ceos19.springeverytime.post.dto;

import com.ceos19.springeverytime.Image.domain.Image;
import com.ceos19.springeverytime.postcategory.domain.PostCategory;
import com.ceos19.springeverytime.post.domain.Post;
import com.ceos19.springeverytime.user.domain.User;
import java.util.List;
import lombok.Data;

@Data
public class CreatePostDto {
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private Long categoryId;
    private List<Long> imageId;

    public Post toEntity(User user, PostCategory postCategory, List<Image> images) {
        return Post.builder()
                .title(title)
                .content(content)
                .author(user)
                .image(images)
                .category(postCategory)
                .likeCount(0)
                .build();
    }
}
```

위 dto는 `@Data`를 선언해 사용한다.

<p align="center">
  <img src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/63999019/8045a80e-f1ce-4403-bb01-27fab600bc7b">
</p>

`@Data` 소스 코드를 보면 `Getter` , `Setter` 같은 어노테이션을 포함하는 것을 알 수 있다.

이를 통해 다양한 어노테이션들을 붙이지 않고 사용할 수 있어 가독성을 높힐 수 있다.

또한 해당 Dto에는 `toEntity` 메서드가 포함되어 있다.

이는 Dto를 Entity로 변환하는 메서드로 리포지토리에 저장하거나 컨트롤러에 반환할 때 사용하도록 했다.

정확하게는 Response와 Request에 대한 Dto를 각각 가지는 것이 가장 좋은 방식이나,

아직 컨트롤러가 구현되어 있지 않아 추후에 리팩토링할 예정이다.

```java
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private PostRepository postRepository;
    private UserService userService;
    private PostCategoryService postCategoryService;
    private ImageService imageService;

    @Transactional
    public void createPost(CreatePostDto postDto) {
        User user = userService.getUser(postDto.getUserId());
        PostCategory category = postCategoryService.getPostCategoryByCategoryId(postDto.getCategoryId());
        List<Image> images = imageService.getImagesByImageIds(postDto.getImageId());

        postRepository.save(postDto.toEntity(user, category, images));
    }

    public Post getPost(Long postId) {
        return postRepository.findPostById(postId)
                .orElseThrow(IllegalStateException::new);
    }

    @Transactional
    public void deletePost(Long postId) {
        postRepository.deletePostById(postId);
    }

    @Transactional
    public void increaseLike(Long postId){
        Post post = getPost(postId);
        post.increaseLikeCount();
    }
    @Transactional
    public void decreaseLike(Long postId){

        Post post = getPost(postId);
        post.decreaseLikeCount();
    }

}
```

`PostService`에는 비지니스 로직을 구현한다

포스트를 생성, 수정, 삭제하는 CRUD 연산 뿐 아니라 좋아요를 집계하는 로직도 포함되어 있다.

Service에서는 트랜잭션을 관리하기 때문에 `@Transactional`을 통해 트랙잭션으로 작업을 처리할 것을 명시했다.

단 `readOnly = true` 옵션을 통해 db에서 단순 조회만 하도록 디폴트 값을 설정했다.

이러한 방식의 장점은 DB 성능 최적화에 있다.

데이터베이스는 dirty checking을 통해 영속성 컨텍스트의 객체가 변경되었는 지를 감지하고 추적한다.

하지만 `readOnly = true`가 적용된 transaction은 변경이 발생하지 않음을 알기 때문에 이러한 기능을 수행하지 않는다.

그렇기에 자연스럽게 이에 대한 오버헤드가 줄면서 성능 최적화가 이루어진다.

각 메소드에 대한 설명은 다음과 같다.

* createPost : 새로운 게시물을 생성한다. 글쓴이와 카테고리에 대한 정보를 각 도메인의 서비스에서 불러와 리포지토리에 저장한다.
여기서 dto에 `user`와 `category`를 직접 저장하지 않고 id에 대한 정보만 담고 있다.
이는 사용하지 않을 객체를 들고 있는 것 자체가 오버헤드라고 생각해 `toEntity`를 통해 직접 사용할 때 조회해 저장하도록 로직을 구현했다.
* getPost : 게시물의 id를 통해 리포지토리에 객체를 조회하도록 한다. Optional을 통해 null 일 때의 처리를 동반한다.
* deletePost : 게시물의 id를 통해 리포지토리에 객체를 삭제하도록 한다.
* increaseLike,decreaseLike : 좋아요가 생성, 삭제됨에 따라 값을 증가시키거나 감소시킨다.
좋아요 수를 조회 쿼리를 날리지 않고 알 수 있도록 하는 로직이며, Post 내부의 메소드를 통해 이를 관리하도록 했다.
```java
@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @OneToOne
    @JoinColumn(name = "category_id")
    private PostCategory category;

    @OneToMany(mappedBy = "post")
    private List<Comment> Comments;

    @OneToMany(mappedBy = "post")
    private List<Image> image;

    private int likeCount;
    private String title;
    private String content;

    public void increaseLikeCount(){
        likeCount++;
    }

    public void decreaseLikeCount() {
        likeCount--;
    }
}
```

핵심은 setter를 사용하지 않는 것이다. 해당 로직의 의미가 명확하게 드러나도록 메소드를 구현했다.

### Controller

이번 주 과제는 컨트롤러를 구현하고 swagger를 통한 통합 테스트의 구현이다.

User 도메인의 예시를 통해 설명해보도록 하겠다.

```java
@Tag(name = "User Controller", description = "유저 컨트롤러")
@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping()
    @Operation(summary = "유저 회원가입", description = "새로운 유저를 DB에 등록")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 형식입니다."
                    // content, schema 옵션들을 통해 상세한 에러 정보를 view에 전달할 수 있다.
            ),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 회원입니다.")
    })
    public ResponseEntity<Void> userAdd(@RequestBody final UserRequestDto request){
        userService.createUser(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping()
    @Operation(summary = "유저 목록 조회", description = "존재하는 모든 유저의 목록을 조회")
    public ResponseEntity<List<User>> userList(){
        List<User> users = userService.readAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "유저 회원탈퇴", description = "존재하는 유저의 정보를 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원탈퇴 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 회원정보")
    })
    public ResponseEntity<Void> userRemove(@PathVariable Long userId){
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{userId}")
    @Operation(summary = "단건 회원 조회", description = "특정 유저의 정보를 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 회원정보")
    })
    public ResponseEntity<ResponseUserDto> userDetails(@PathVariable Long userId){
        return ResponseEntity.ok(userService.readUser(userId));
    }

    @PatchMapping("/{userId}")
    @Operation(summary = "유저정보 수정", description = "존재하는 회원정보를 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원정보 수정 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 회원정보")
    })
    public ResponseEntity<Void> userUpdate(@RequestBody UserRequestDto userRequestDto
            , @PathVariable Long userId){
        userService.updateUser(userRequestDto, userId);
        return ResponseEntity.status(HttpStatus.OK).build();

    }
}
```

swagger는 springdoc을 통한 메타데이터를 활용해 메소드에 대한 요약과 설명을 작성할 수 있도록 돕는다.

이를 위해 class에는 `@Tag` 어노테이션으로 이름과 설명을 작성할 수 있다.

method는 `@Operation` 어노테이션을 통해 메소드의 요약과 설명을 작성할 수 있다.

또한 `@ApiResponse`를 통해 반환되는 https code에 대한 설명을 code 별로 작성할 수 있다.

`@RequestMapping`은 각 메소드의 디폴트 url을 설정해 가독성을 높힌다.

또한 url에 있는 값을 변수로 사용하기 위해

`{}`으로 사용할 변수를 덮고 `@PathVariable`을 통해 파라미터에 그 값을 담아 사용할 수 있다.

> 💡 `@RequestBody`와 `@RequestParam`의 차이점
> `@RequestBody`는 JSON 데이터의 KEY값과 자바 객체의 필드 변수명을 매핑해 자동으로 값을 넣어준다
> 이에 반해 `@RequestParam`은 KEY와 변수명의 매핑을 직접 명시해줘야 한다는 차이점이 있다.

또한 `ResponseUserDto`를 사용해 노출되지 말아야 할 정보를 감추고 필요한 정보만 반환하도록 했다.

```java
@Data
@Builder
public class ResponseUserDto {
    private Long id;
    private String LoginId;
    private String name;

    public static ResponseUserDto of (User user){
        return ResponseUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .LoginId(user.getLoginId())
                .build();
    }
}
```

정적 팩토리 메서드 방식을 통해 `User` 객체를 `ResponseUserDto`로 생성하는 `of` 메소드를 구현했다.

이를 통해 좀 더 직관적으로 메소드의 역할을 알 수 있다.

또한 Http code를 custom에 `UserErrorCode`를 통해 관리했다.

```java
@Getter
public enum UserErrorCode{
    USER_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 회원 정보입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    INVALID_USER_INFO(HttpStatus.BAD_REQUEST, "잘못된 회원 정보입니다.");


    private final HttpStatus httpStatus;
    private final String message;

    UserErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
```

이러한 ErrorCode는 `UserException`을 통해 `throw`할 수 있다.

```java
public class UserException extends ResponseException {

    public UserException(UserErrorCode userErrorCode) {
        super(userErrorCode.getMessage(), userErrorCode.getHttpStatus());
    }
}
```

각각 도메인 마다 Exception을 전부 구현하기에 번거로워 `ResponseException`이라는 추상 클래스를 생성했다.

```java
public abstract class ResponseException extends RuntimeException {
    private final HttpStatus status;

    protected ResponseException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
```
이러한 `ResponseException` 객체들은 `ResponseTemplate`에 담겨 RestApi에서 반환하는 `ResponseEntity`로 변환할 수 있다.

```java
@Builder
@AllArgsConstructor
public class ResponseTemplate {
    public int status;

    public String message;

    private final LocalDateTime timestamp = LocalDateTime.now();

    public static ResponseEntity<ResponseTemplate> toResponseEntity(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(ResponseTemplate.builder()
                        .message(message)
                        .status(status.value())
                        .build());
    }
}
```

`ResponseTemplate`을 `ResponseEntity`로 변환하는 로직은 `ExceptionHandler`가 처리한다.

```java
@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler({UserException.class})
    public ResponseEntity<ResponseTemplate> handleCustomException(ResponseException exception){
        return ResponseTemplate.toResponseEntity(exception.getStatus(),exception.getMessage());
    }
}
```

`@RestControllerAdvice`를 통해 custom화된 exception을 관리해 직렬화하여 사용자에게 전달할 수 있다.

```java
    public ResponseUserDto readUser(Long userId) {
        User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        return ResponseUserDto.of(user);
    }
```

위 메소드는 `UserService`에 있는 `readUser`로 유저를 조회해 반환한다.

만약 유저를 찾지 못할 경우 `UserErrorCode`인 `USER_NOT_FOUND`를 Exception으로 던진다.

이제 swagger를 통한 통합 테스트를 살펴보자

`http://localhost:8080/swagger-ui/index.html#/` 을 통해 local swagger ui에 접속할 수 있다.

<p align="center">
  <img src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/63999019/cc6db44e-4784-4cfc-9fd5-7e1be4b2c565">
</p>

`UserController`의 http method들과 `@Operation`에서 `summary`로 등록한 내용이 노출되어 쉽게 내용을 파악할 수 있다.

<p align="center">
  <img src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/63999019/8a03f4fa-03bc-41fb-9111-80c48f2fdbe4">
</p>

유저 회원가입의 상세 정보인 `description`의 내용이 여기에 노출되며 request로 보내야 할 body의 내용을 확인할 수 있다.

`Try it out` 버튼을 통해 원하는 값을 body에 넣고 response body를 확인해보자.

<p align="center">
  <img src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/63999019/e6af9f8f-321f-46e9-93b8-cefaa56a6123">
</p>

다음과 같이 값을 설정해 execute를 누르면

<p align="center">
  <img src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/63999019/a6baf8c8-2363-4800-ba3a-be15411fa307">
</p>

실행에 대한 http code을 알 수 있으며 그 밑에 `@ApiResponse`로 작성한 각 코드 값에 대한 설명을 알 수 있다.

<p align="center">
  <img src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/63999019/219269f0-3719-4238-b53e-fbadfb6e8a30">
</p>

## 🥲 번외편

원격 브런치에 커밋하지 않고 계속 새로 생성된? 로컬 브런치에 커밋해 깃허브에 반영되지 않는 문제 발생

<p align="center">
  <img src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/63999019/50992471-d1e8-48ed-a7ea-f39d068c4b41">
</p>

이렇게 커밋했으나

<p align="center">
  <img src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/63999019/18ae8f2a-a91e-44a9-bf88-79de2b610029">
</p>

실제로는 반영되어 있지 않음.

자동으로 Merge 했다는 커밋이 발생해서 원상복구 된 줄 알고 `git fetch` 적용

근데 사실 반영이 안 된 원격 브런치를 로컬 브런치에 붙여넣었고,,,

<p align="center">
  <img src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/63999019/8e783182-e652-4333-b86e-09d138790f2f">
</p>

약 25개의 커밋이 공중분해되었다...

✔️ 앞으로 커밋을 꼼꼼하게 확인하고 GIT에 대해 자세하게 공부하자...
