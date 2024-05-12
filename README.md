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


# JWT

### ✅ 왜 세션과 쿠키를 사용하는가?

HTTP 프로토콜의 약점을 보완하기 위해 사용한다.

HTTP는 기본적으로 Connectionless, StateLess한 특성을 가진다.

> 💡 Connectionless(비연결성) : 클라이언트가 요청을 보내고 서버가 그 요청에 대한 **응답을 하고나면 연결이 즉시 종료된다.**
>
>그러나 여러 요청에 대해 매번 연결을 하는 것은 비효율적이므로
>header에 keep-alive라는 값을 줘서 연결을 재활용한다.

>💡 Stateless(무상태성) : 서버는 클라이언트의 요청을 처리한 후 **요청에 대한 정보를 저장하지 않는다**. 자원의 효율성을 위함.

즉 웹은 클라이언츠에 대한 정보를 따로 저장하지 않는다!
이를 보완하기 위해 세션과 쿠키를 사용한다.

### ✅ 세션과 쿠키의 차이점?

두 개념은 클라이언트의 상태 정보를 유지하기 위한 기술이다.

#### 세션
세션은 서버에서 관리하는 클라이언트 정보이다.

클라이언트를 구분하기 위해 서버는 고유 세션ID를 발급하며
웹 브라우저가 서버에 접속하여 브라우저를 종료할 때까지 유지된다
+ 더불어 설정된 시간이 지나면 자동으로 세션이 만료된다.

서버에서 저장하기 때문에 **접속자가 많을 경우 성능 저하의 원인이 된다.**

로그인과 같은 중요 보안에 사용된다.

#### 쿠키
쿠키는 브라우저가 관리하는 클라이언트 정보이다.

인증이 유효한 시간이 기록되어 있으며 브라우저가 종료되어도 시간이 남아있다면 삭제되지 않고 인증이 유효하다는 특징이 있다.

클라이언트가 request를 보낼 때 자동으로 header에 넣어 전송된다.

예시 : 쇼핑몰 장바구니, 아이디 자동 저장, 당일 팝업창 유무


#### 세션과 쿠키의 주요 차이점

+ 보관되는 장소 (클라이언트 vs 서버)
+ 보안성 (쿠키는 스니핑 우려있음)
+ 속도 (세션은 서버를 통해 처리되어야 하므로 상대적 느림)

### ✅ 실제 통신에서 어떻게 사용될까?
![image](https://github.com/CEOS-Developers/spring-everytime-19th/assets/63999019/0d0e08c4-cae7-4108-a50e-13df1b815433)

1. 클라이언트가 서버에 request를 요청한다.

2. 서버는 request-header에 쿠키가 존재하는지 확인한다.

3. 세션ID가 없을 경우, 서버에서 생성해 쿠키에 저장한다.

4. 생성된 쿠키와 함께 response를 전송

5. 클라이언트는 쿠키를 통해 세션ID를 저장한다.

6. 재요청을 할 때, 세션ID와 함께 request 요청!

7. 별다른 작업없이 세션ID만 확인하고 저장된 세션 정보를 사용

8. 클라이언트 세션 정보를 통해 request 전송

## JWT Token

세션을 통해 통신하면 서버에 부하가 크다!.

ex) 1억명의 유저가 접속하려면 세션 ID를 어느 세월에 다 비교할까.. ***(서버의 부하가 너무 커짐!!!)***

JWT Token은 이러한 점을 보완하기 위해 사용한다.

### Spring Security 동작원리

![image](https://github.com/CEOS-Developers/spring-everytime-19th/assets/63999019/71b1b225-ecdc-4330-99e2-e7e1df6e48df)

WAS의 필터에 Custom된 필터를 만들어 넣고 해당 필터에서 요청을 가로챈다.

해당 요청은 스프링 컨테이너 내부에 구현되어 있는 Security 로직을 거친다.

모든 로직이 종료되면 다시 WAS의 다음 필터로 넘어간다.

우리는 가로챈 요청을 우리만의 인증, 인가 로직을 거치게 할 것이다

![image](https://github.com/CEOS-Developers/spring-everytime-19th/assets/63999019/e17819ab-994e-44a2-95f2-da744f732fed)

> Spring provides a FilterApplicationContext. The Servlet container allows registering Filter instances by using its own standards, but it is not aware of Spring-defined Beans. You can register DelegatingFilterProxy through the standard Servlet container mechanisms but delegate all the work to a Spring Bean that implements Filter.

spring 공식 문서에 따르면 spring에서 제공하는 Servlet container에는 spring bean을 등록할 수 없다.

그렇기 때문에 `DelegatingFilterProxy`를 사용해 요청을 `SecurityFilterChain`에 위임하는 과정을 거친다.

`SecurityFilterChain`은 일련의 FilterChain들의 모음으로 여러 개의 chain을 순서대로 거친다.

우리의 관심사는 Custom된 FilterChain을 저 사이에 추가해 인증, 인가 과정을 요청이 거치도록 하는 것이다.

### JWT 동작원리

![image](https://github.com/CEOS-Developers/spring-everytime-19th/assets/63999019/ea71b3ad-d944-4c85-b151-4926cd58e19c)

우리는 JWT Token에 입장하기 위한 정보 (이름, 아이디 , 비밀번호, etc...)를 입력하고 로그인을 시도한다.

![image](https://github.com/CEOS-Developers/spring-everytime-19th/assets/63999019/0ba2067d-3e69-4cc2-ab90-33c2b11be41b)

각각의 Chain들은 본연의 기능만 수행하도록 분업화되어 있기 때문에 Chain을 통과할 때마다 상태를 저장하기 위한 저장소가 필요하다.

예를 들어 인가 필터가 작업을 수행하려면 유저의 권한 정보가 필요하다.

앞단의 필터가 부여한 권한을 인가 필터에게 공유해야 이를 확인할 수 있다.

![image](https://github.com/CEOS-Developers/spring-everytime-19th/assets/63999019/eb29469f-d42e-4f87-ab01-f8d6fb713f41)

해당 정보(아이디, 로그인 여부, Role 정보)들은 `Authentication`이라는 객체에 저장된다.

이 객체들은 `SecurityContext`에 포함되어 관리되며 멀티쓰레드 환경에서는 유저 당 하나 씩 할당된다.

`SecurityContextHolder`는 `SecurityContext`들을 관리하는 주체이다.

---

이제 Filter들의 동작원리에 대해 자세히 살펴보자

`UsernamePasswordAuthenticationFilter`는 form login 방식으로 전달된 id, password를 추출하고 `UsernamePasswordAuthenticationToken`을 생성한다.

이제 인증을 위해 이 토큰을 `AuthenticationManager`에게 전달한다.

```java
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
                throws AuthenticationException {
    
            String username = obtainUsername(request);
            String password = obtainPassword(request);
            
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password,
                    null);
    
            return authenticationManager.authenticate(authToken);
        }
```

```java
public class UsernamePasswordAuthenticationToken extends AbstractAuthenticationToken {
```
로 `AbstractAuthenticationToken`를 상속하며

```java
public abstract class AbstractAuthenticationToken implements Authentication, CredentialsContainer {
```
결국은 `Authentication`을 상속한다는 것을 참고하자

현재 `UsernamePasswordAuthenticationToken`는 아직 인증되지 않은 `Authentication`이라고 생각하면 된다.

`AuthenticationManager`는 `AuthenticationProvider`의 구현체에 `UsernamePasswordAuthenticationToken`를 전달한다.

`AuthenticationProvider`는 `UserDetailsService`에 다시 `UsernamePasswordAuthenticationToken`를 넘겨 DB에서 가져온 `UserDetails`과 정보를 비교한다.

실질적으로 인증 로직이 진행되는 곳은 `UserDetailsService`으로 우리는 이를 Custom해 수행하면 된다.

```java
public class UserService implements UserDetailsService 
...
@Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        User userData = userRepository.findUserByLoginId(loginId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        if (userData != null) {

            //UserDetails에 담아서 return하면 AutneticationManager가 검증 함
            return new CustomUserDetails(userData);
        }

        return null;
    }
```

`UserDetails`도 Entity에 맞게 Custom했다.

```java
public class CustomUserDetails implements UserDetails {
    private final User user;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority(){
                return user.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }
    ...
}
```

인증이 됐다면 인증이 된 `Authentication`을 생성해 `UsernamePasswordAuthenticationFilter`에게 다시 전달된다.

두 가지 상황이 존재하는데 ***인증 성공***과 ***인증 실패***일 것이다.

인증이 성공했다면 Access Token과 Refresh Token을 발급한다.

```java
@Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String userName = userDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        String access = jwtUtil.createJwt("access", userName, role, 600000L);
        String refresh = jwtUtil.createJwt("refresh", userName, role, 86400000L);

        addRefreshEntity(userName, refresh,  86400000L);

        response.setHeader("access", access);
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
    }
```
인증 실패 시에는 아무것도 발급하지 않고 에러코드를 출력한다.

그럼 JWT Token을 가지고 있고 이를 통해 인가하는 과정은 어떨까?

```java
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 헤더에서 access키에 담긴 토큰을 꺼냄
        String accessToken = request.getHeader("access");

        // 토큰이 없다면 다음 필터로 넘김
        if (accessToken == null) {
            
            // 해당 필터를 종료하고 다음 필터로 넘어가라는 의미
            filterChain.doFilter(request, response);

            return;
        }

        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음 (재발급 로직을 처음부터 새로 수행해야함)
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");

            //response status code
            // 더이상 필터를 통과하는 게 아니라 바로 response해서 재발급 하도록 유도
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 토큰이 access인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("access")) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // username, role 값을 획득
        String username = jwtUtil.getUserName(accessToken);
        String role = jwtUtil.getRole(accessToken);

        User user = User.builder()
                .name(username)
                .role(role)
                .build();
        
        // dto에 담아 전달하는 방식
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        //다음 필터로 이동하자
        filterChain.doFilter(request, response);
    }
}
```

## ❓ 궁금했던 내용

### 대체 `AuthenticationProvider`의 구현체를 직접 상속해 구현한 기억이 없는데 어디서 자동으로 등록되는 걸까?

> `AuthenticationProvider`의 구현체는 `@EnableWebSecurity`에 의해 config의 내용을 자동으로 등록한다.

```java
@EnableGlobalAuthentication
public @interface EnableWebSecurity {

	/**
	 * Controls debugging support for Spring Security. Default is false.
	 * @return if true, enables debug support with Spring Security
	 */
	boolean debug() default false;

}
```
`@EnableGlobalAuthentication`을 어노테이션으로 포함하고 있으며

```java
@Import(AuthenticationConfiguration.class)
public @interface EnableGlobalAuthentication {

}
```
다시 `AuthenticationConfiguration`를 import하고 있는데

```java
public class AuthenticationConfiguration {

    ...

	private AuthenticationManager authenticationManager;

    ...

	@Bean
	public AuthenticationManagerBuilder authenticationManagerBuilder(ObjectPostProcessor<Object> objectPostProcessor,
```

`AuthenticationManager`를 생성하는 Builder가 포함되어 있다.

이러한 관계때문에 우리는 `@EnableWebSecurity`를 선언하면 설정파일의 코드를 자동으로 빌드할 수있다.

### Access 토큰과 Refresh 토큰의 차이점?

Access 토큰은 쉽게 이야기하면 인증된 입장권이다.

토큰이 만약 탈취당하면 어떻게 될까?

이러한 보안적 위험을 줄이기 위해 Access 토큰에 아주 짧은 만료기간을 부여한다.

Access 토큰이 만료되면 재발급을 위해 Refresh 토큰이 함께 발급된다.

Refresh 토큰은 Access 토큰에 비해 만료기간이 매우 길며, 재발급 요청시에 필요하다.

---

또한 탈취에 저항하기 위해 저장소의 위치도 다르다.

* Access 토큰은 주로 로컬 스토리지에 저장된다. 권한이 필요한 모든 경로에 사용되기 때문에 CSRF 공격의 위험보다는 XSS 공격을 받는 게 더 나은 선택일 수 있다.
* Refresh 토큰은 주로 쿠키에 저장된다. CSRF는 Access 토큰이 접근하는 회원 정보 수정, 게시글 CRUD에 취약하지만 토큰 재발급 경로에서는 크게 피해를 입힐 만한 로직이 없기 때문이다.

### Refresh Rotate가 뭐고 왜 사용하는걸까?

위에서 Refresh 토큰은 Access 토큰의 재발급을 위해 필요한 요소라고 설명했다.

그런데 여전히 Refresh 토큰도 탈취당할 위험이 있다.

이러한 점도 보완하기 위해 Rotate 방식을 사용한다.

Refresh 토큰을 일회용으로 사용하는 것이다.

Refresh 토큰으로 재발급 로직을 수행하게 될 때 Access 토큰과 Refresh 토큰 둘 다 새로 발급해 전달한다.

---
💡 추가로 알면 좋을 내용
> 로그아웃을 구현하면 프론트측에 존재하는 Access/Refresh 토큰을 제거합니다. 그럼 프론트측에서 요청을 보낼 JWT가 없기 때문에 로그아웃이 되었다고 생각하지만 이미 해커가 JWT를 복제 했다면 요청이 수행됩니다.
>
> 위와 같은 문제가 존재하는 이유는 단순하게 JWT를 발급해준 순간 서버측의 주도권은 없기 때문입니다. (세션 방식은 상태를 STATE하게 관리하기 때문에 주도권이 서버측에 있음)
>
>
> 위 문제의 해결법은 생명주기가 긴 Refresh 토큰은 발급과 함께 서버측 저장소에도 저장하여 요청이 올때마다 저장소에 존재하는지 확인하는 방법으로 서버측에서 주도권을 가질 수 있습니다.
>
> 만약 로그아웃을 진행하거나 탈취에 의해 피해가 진행되는 경우 서버측 저장소에서 해당 JWT를 삭제하여 피해를 방어할 수 있습니다.
> (Refresh 토큰 블랙리스팅이라고도 부릅니다.)


출처 : [개발자 유미 docs 모음](https://substantial-park-a17.notion.site/Docs-002024551c294889863d0c7923590568)

### Docker

이번 과제는 도커 이미지를 빌드하고 컨테이너를 생성해 배포하는 것이다.

```java
FROM openjdk:18 // openjdk 18버전을 기본 이미지로 사용한다.
ARG JAR_FILE=/build/libs/*.jar // 변수를 정의한다. 여기서는 이미지로 생성할 파일의 경로이다.
COPY ${JAR_FILE} app.jar // 정의한 변수를 통해 도커 이미지의 app.jar로 복사한다.
LABEL authors="imhyeongun" // 이런 메타데이터도 선언할 수 있다.
EXPOSE 8080 // 포트 넘버는 8080이다.
ENTRYPOINT ["java","-jar","/app.jar"] // app.jar의 jar파일을 실행한다.
```

위와 같이 `Dockerfile`을 생성했다.

터미널에 다음과 같은 명령어를 입력하자.

`docker build -t spring-everytime:0.0 ./`

0.0 버전으로 이미지를 생성했다.
![image](https://github.com/CEOS-Developers/spring-everytime-19th/assets/63999019/852aabca-7de0-41df-a1b0-b68282642785)

생성된 이미지들을 확인하고 싶다면

`docker images` 명령어를 사용할 수 있다.
![image](https://github.com/CEOS-Developers/spring-everytime-19th/assets/63999019/73e0c628-bf21-4e7e-a4c8-97f43c997903)

현재 생성된 이미지들을 전부 확인할 수 있다.

이제 이미지를 통해 컨테이너를 실행해보자

```text
2024-05-12 21:12:44 com.mysql.cj.jdbc.exceptions.CommunicationsException: Communications link failure
2024-05-12 21:12:44
2024-05-12 21:12:44 The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
```

위와 같은 에러가 발생한다.

서버가 패킷을 받지 못한다고 나오는데 검색해보니 sql 설정 문제가 가장 많았다.

`application.yml`에서 다음과 같이 수정했다.

```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/ceos


// 수정

spring:
  datasource:
    url: jdbc:mysql://ceos:3306/ceos
```

그 후 실행해도 여전히 똑같은 에러가 발생한다.

에러창을 내려보니 다음과 같은 내용도 있다

```text
2024-05-12 21:12:44 Caused by: org.hibernate.HibernateException: Unable to determine Dialect without JDBC metadata (please set 'jakarta.persistence.jdbc.url' for common cases or 'hibernate.dialect' when a custom Dialect implementation must be provided)
```

구글링을 통해 다음 설정을 추가했다.

```yaml
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: update
    properties:
      hibernate.format_sql: true
      hibernate.dialect : org.hibernate.dialect.MySQLDialect // 추가
```

그래도 같은 에러가 발생한다. 즉 위의 내용은 에러의 원인이 아니였다.

아마 docker-compose를 통해 db의 이미지도 컨테이너로 같이 띄워야 하는 것으로 추측된다.

그래서 까짓거 하나 새로 만들기로 결심했다

![image](https://github.com/CEOS-Developers/spring-everytime-19th/assets/63999019/18fcb258-5f90-44fb-9e9c-22ab368753c2

최신 mysql 이미지를 다운받았다. 이를 통해 컨테이너만 run하면 될 것이다.

`docker run -d -p 8080:8080 -e MYSQL_ROOT_PASSWORD=root --name mysql_container mysql`

이렇게 포트 포워딩도 설정하고 실행했지만 이미 점유된 포트라고 에러가 발생했다.

우리의 로컬 mysql workbench가 실행되고 있으니 당연한 말이다.

그러므로 비어있는 포트를 아무거나 찾아서 연결에 사용해보자

`sudo ss -tuln | grep :3308` 명령어는 3308 포트에 연결되어 있는 프로세스를 출력해준다.

![image](https://github.com/CEOS-Developers/spring-everytime-19th/assets/63999019/cc7dc480-675f-499e-9b8a-b25ebd120805)

결과가 없는 것을 보니 비어있는 포트임을 알 수 있다.

`docker run -d -p 3308:3308 -e MYSQL_ROOT_PASSWORD=root --name mysql_container mysql` 

이제 포트넘버를 수정하고 ps를 실행해보면

![image](https://github.com/CEOS-Developers/spring-everytime-19th/assets/63999019/2d7c0d0b-3157-4de2-84e9-a5e4bb74ae69)

위와 같이 실행이 됨을 확인할 수 있다 (3308이 뭔가 맘에 안 들어서 3307로 연결했다)

이제 docker-compose 파일을 작성해 여러 이미지들 한꺼번에 각각의 컨테이너로 띄울 수 있다.

```yaml
version: "3"
services: # 이 항목 밑에 실행하려는 컨테이너 들을 정의 ( 컴포즈에서 컨테이너 : 서비스 )
  db: # 서비스 명
    image: mysql:latest # 사용할 이미지
    restart: always
    container_name: ceos # 컨테이너 이름 설정
    ports:
      - "3307:3306" # 접근 포트 설정
    environment: # -e 옵션
      - MYSQL_DATABASE=ceos
      - MYSQL_ROOT_PASSWORD= "root"  # MYSQL 패스워드 설정 옵션
      - TZ=Asia/Seoul
    volumes:
      - app:/app
  web:
    container_name: spring-everytime
    build: src/main/java
    ports:
      - "8080:8080"
    depends_on:
      - db
    environment:
      mysql_host: db
    restart: always

volumes:
  dbdata:
  app:
```

다음과 같이 작성하고 실행하자
`failed to solve: failed to read dockerfile: open Dockerfile: no such file or directory`

이런 에러가 발생하는데 분명 같은 폴더에 있는데 왜 못 찾는 지 모르겠다..

여기서부터는 구글링해도 정확한 내 상황을 모르겠어서 제출 후 추가로 작성하겠다......

### 추가 리팩토링 
(추가 예정)
