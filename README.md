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

```
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
```
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
