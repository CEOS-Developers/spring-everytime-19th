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


