# spring-everytime-19th
CEOS 19th BE study - everytime clone coding
***
***
## 1. 에브리타임 서비스 분석
에브리타임에는 수많은 기능이 있다. 이번 과제에서는 다음의 6개에 집중하여 DB를 구성했다.  
- 게시글 조회
- 게시글에 사진과 함께 글 작성하기
- 게시글에 댓글 및 대댓글 기능
- 게시글에 좋아요 기능
- 게시글, 댓글, 좋아요 삭제 기능
- 1:1 쪽지 기능

다음은 작성한 ERD의 이미지이다.
<img width="1032" alt="스크린샷 2024-03-17 오후 4 46 43" src="https://github.com/The-Sculptor/Server/assets/97235034/235b8cc1-fdf4-43eb-b614-aeda15a0b30c">  

### 1.1 엔티티 설명
- Users(사용자)  
    사용자는 기본키로 사용자ID를 가진다. 기본적인 사용자 정보인 이름, 학교, 닉네임 등을 가지고 로그인을 위한 아이디와 비밀번호를 가진다.  
- Posts(게시글)  
  게시글은 기본키로 게시글ID를 가진다. 게시글은 어떠한 사용자가 작성했는지에 대한 정보가 있어야 하기 때문에 외래키로 작성자 정보를 가진다.  
또한 각 게시글에 표시될 좋아요 개수를 필드로 가지고, 사진과 같이 게시글을 업로드 할 수 있으므로 이미지의 url을 String으로 담는 필드를 가진다.  
  (이 부분은 수정이 필요할수도 있을 것 같다..!)
- PostLike(게시글에 좋아요)  
  게시글에 좋아요를 누를 수 있는데, 이때 어떠한 사용자가 어떠한 게시글에 좋아요를 누를지를 알아야 하기 때문에 게시글ID와 사용자ID를 외래키로 가진다.  
- Comment(댓글)  
  기본키로 댓글ID를 가진다. 어떠한 게시글에 어떠한 사용자가 댓글을 달지 알아야 하므로 각 기본키를 외래키로 가진다. 뿐만 아니라 대댓글 달 수 있는데, 이 부분은 특정 댓글의 상위 댓글 (부모 댓글)이 있다면 대댓글로 판단하게 했다.  
- Message(쪽지)  
  사용자끼리 주고받아야 하기 때문에, 보내는 사용자ID와 받는 사용자ID를 외래키로 가진다.  
***
### 1.2 연관관계 설정
이제 각 엔티티간의 연관관계를 보자.
1. 사용자 - 게시글  
   사용자 한명은 여러개의 게시글을 작성 가능하다. 게시글 하나는 하나의 사용자에 종속된다.  
   ->  따라서 1:N 의 관계가 형성된다.  
2. 게시글 - 댓글  
   게시글 하나에는 여러개의 댓글이 달릴 수 있다. 댓글 하나는 특정 게시글에 종속된다.  
   -> 따라서 1:N 의 관계가 형성된다.  
3. 사용자 - 댓글  
   사용자 한명은 여러개의 댓글을 작성할 수 있다. 특정 댓글 하나는 특정 사용자 하나에게 종속된다.  
   -> 따라서 1:N 의 관계가 형성된다.  
4. 게시글 - 좋아요  
   하나의 게시글에는 여러개의 좋아요가 달린다. 특정 좋아요 한개는 특정 게시글에 종속된다.  
   -> 따라서 1:N 의 관계가 형성된다.  
5. 사용자 - 좋아요  
   사용자 한명은 여러 게시글에 좋아요를 누를 수 있다. 특정 좋아요는 한명의 사용자에 종속된다.  
   -> 따라서 1:N 의 관계가 형성된다.  
6. 사용자 - 쪽지  
   한명의 사용자는 여러개의 쪽지가 작성가능하고, 쪽지 하나는 특정 사용자에 종속된다.  
   -> 1:N 의 관계가 형성된다.  
***  
## 2. 데이터 확인하기  
위에서 언급했던 6개의 주요 기능에 대해 각각 테스트 코드를 작성하였다.  
테스트 코드 작성시 JUnit5 와 AssertJ 를 사용하였고, 데이터의 확인은 DataGrip으로 하였다!
1. 사진과 함께 게시글 작성 + 게시글 조회하기  
   <img width="1440" alt="스크린샷 2024-03-17 오후 5 23 58" src="https://github.com/The-Sculptor/Server/assets/97235034/7af7a9d0-8efd-4bcf-9c5c-1b5ccfa2c2b6">  
```java
@Test
@DisplayName("게시글 조회 테스트")
public void searchPostsTest() throws Exception {
        //given
        Users user = Users.builder()
                .university("홍익대")
                .email("email")
                .loginId("아이디")
                .nickname("닉넴")
                .username("정기민")
                .password("비번")
                .build();
        
        Users saveUser = userRepository.save(user);
        
        //when
        Posts post = Posts.builder()
                .createdAt(LocalDateTime.now())
                .content("게시글1 내용")
                .imageUrl("file:///Users/jeong-kimin/Desktop/Everytime_clone_img/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202024-03-17%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%205.23.58.png")
                .title("게시글1 제목")
                .likes(0)
                .user(saveUser)
                .build();
        Posts post2 = Posts.builder()
                .createdAt(LocalDateTime.now())
                .content("게시글2 내용")
                .imageUrl("image url")
                .title("게시글2 제목")
                .likes(0)
                .user(saveUser)
                .build();
        Posts post3 = Posts.builder()
                .createdAt(LocalDateTime.now())
                .content("게시글3 내용")
                .imageUrl("image url")
                .title("게시글3 제목")
                .likes(0)
                .user(saveUser)
                .build();
        Posts savePost1 = postRepository.save(post);
        Posts savePost2 = postRepository.save(post2);
        Posts savePost3 = postRepository.save(post3);
        //then
        System.out.println("saveUser = " + saveUser);
}
```
사용자 한명을 생성하고, 해당 사용자가 3개의 게시글을 작성한 예시를 테스트 했다. 이미지를 보면 사용자 한명과 게시글 3개가 DB에 들어가있는 것을 볼 수 있다.  
2. 댓글 기능
   ```java
    @Test
    @DisplayName("댓글 작성 기능 테스트")
    public void createComment() throws Exception {
        //given
        Users user = Users.builder()
                .university("홍익대")
                .email("email")
                .loginId("아이디")
                .nickname("닉넴")
                .username("정기민")
                .password("비번")
                .build();

        Users commentUser = Users.builder()
                .university("홍익대")
                .email("email")
                .loginId("아이디")
                .nickname("닉네임")
                .username("홍길동")
                .password("비번")
                .build();
        Users savePostUser = userRepository.save(user);
        Users saveCommentUser = userRepository.save(commentUser);

        Posts post = Posts.builder()
                .createdAt(LocalDateTime.now())
                .content("게시글1 내용")
                .imageUrl("image url")
                .title("게시글1 제목")
                .likes(0)
                .user(user)
                .build();
        Posts savePost = postRepository.save(post);

        Comment comment = Comment.builder()
                .user(commentUser)
                .post(post)
                .content("댓글 내용")
                .build();
        //when
        Comment saveComment = commentRepository.save(comment);
        Comment findComment = commentRepository.findByContent(saveComment.getContent());
        //then
        Assertions.assertThat(saveComment.getContent()).isEqualTo(findComment.getContent());
    }
   ```  
   
   게시글을 작성할 사용자 한명과 게시글에 댓글을 달 사용자를 생성하고, 게시글을 생성한다. 그 이후에 해당 게시글에 댓글을 다는 로직을 추가하였다.  
   디비에 저장된 댓글 내용과 방금 디비에 저장한 댓글 내용이 같은지를 통해서 테스트를 진행했다.  
   <img width="1440" alt="스크린샷 2024-03-17 오후 5 35 54" src="https://github.com/The-Sculptor/Server/assets/97235034/0dde6afa-83df-4605-a283-b639edeef6cd">  
    이미지를 통해 댓글이 디비에 잘 저장된 것을 볼 수 있다.  

3. 대댓글 기능  
    ```java
    @Test
    @DisplayName("대댓글 기능 테스트")
    public void childCommentTest() throws Exception {
        //given
        Users user = Users.builder()
                .university("홍익대")
                .email("email")
                .loginId("아이디")
                .nickname("닉넴")
                .username("정기민")
                .password("비번")
                .build();
        Users commentUser = Users.builder()
                .university("홍익대")
                .email("댓글을 달 사용자")
                .loginId("아이디")
                .nickname("닉네임")
                .username("홍길동")
                .password("비번")
                .build();
        Users savePostUser = userRepository.save(user);
        Users saveCommentUser = userRepository.save(commentUser);

        Posts post = Posts.builder()
                .createdAt(LocalDateTime.now())
                .content("게시글1 내용")
                .imageUrl("image url")
                .title("게시글1 제목")
                .likes(0)
                .user(user)
                .build();
        Posts savePost = postRepository.save(post);

        Comment comment = Comment.builder()
                .user(commentUser)
                .post(post)
                .content("댓글 내용")
                .build();
        Comment saveComment = commentRepository.save(comment);

        Comment childComment = Comment.builder()
                .post(post)
                .parent(comment)
                .user(commentUser)
                .content("대댓글 내용")
                .build();
        //when
        Comment saveChildComment = commentRepository.save(childComment);
        comment.addChildComment(saveChildComment);
        //then
        Assertions.assertThat(saveComment.getChildren().size()).isEqualTo(1);
    }
    ```  
   댓글 기능 테스트에서와 같이 사용자들을 생성하고, 댓글을 생성한 뒤에 대댓글을 생성했다. 이때 대댓글은 parent 필드에 이미 작성되어있는 댓글을 넣어서 대댓글임을 알 수 있게 했다.  
   <img width="1440" alt="스크린샷 2024-03-17 오후 5 43 52" src="https://github.com/The-Sculptor/Server/assets/97235034/87e6c3d2-c754-4d8e-b9ab-7104d456a7e0">

4. 게시글에 좋아요 기능  
   ```java
    @Test
    @DisplayName("게시글에 좋아요 누르기 테스트")
    public void PostsLikeTest() throws Exception {
        //given
        Users user = Users.builder()
                .university("홍익대학교")
                .email("email")
                .loginId("아이디")
                .nickname("닉넴")
                .username("정기민")
                .password("비1번")
                .build();
        Posts post = Posts.builder()
                .createdAt(LocalDateTime.now())
                .content("게시글2 내용입니다")
                .imageUrl("image url")
                .title("게시글2 제목입니다")
                .likes(0)
                .user(user)
                .build();
        Users savedUser = userRepository.save(user);
        Posts savedPost = postRepository.save(post);
        //when
        Posts changedPost = savedUser.pressLike(savedPost);
        postRepository.save(changedPost);
        //then
        Assertions.assertThat(savedPost.getLikes()).isEqualTo(1);
    }
    ```
   사용자와 게시글을 생성하고, 엔티티 내의 핵심 비즈니스 메서드인 pressLike() 를 통해서 likes 필드의 값을 1 증가하는 식으로 작성했다.  
   <img width="1440" alt="스크린샷 2024-03-17 오후 5 46 17" src="https://github.com/The-Sculptor/Server/assets/97235034/316bb621-0262-43a7-8bfb-91c5bb16c7fa">  
5. 게시글 삭제 기능  
   ```java
    @Test
    @DisplayName("게시글 삭제 테스트")
    public void deletePost() throws Exception {
        //given
        Users user = Users.builder()
                .university("홍익대")
                .email("email")
                .loginId("아이디")
                .nickname("닉넴")
                .username("정기민")
                .password("비번")
                .build();
        Posts post = Posts.builder()
                .createdAt(LocalDateTime.now())
                .content("게시글1 내용")
                .imageUrl("image url")
                .title("게시글1 제목")
                .likes(0)
                .user(user)
                .build();

        Users saveUser = userRepository.save(user);
        Posts savePost = postRepository.save(post);
        saveUser.addPosts(savePost);

        System.out.println("saveUser.getPosts() = " + saveUser.getPosts());
        //when
        postRepository.delete(savePost);
        saveUser.removePost(savePost);
        //then
        System.out.println("saveUser.getPosts() = " + saveUser.getPosts());
    }
    ```  
   게시글을 삭제하면 사용자가 작성한 게시글 개수가 줄어든다. 이를 직접 콘솔에 찍어서 확인해보았다.  
   <img width="1131" alt="image" src="https://github.com/The-Sculptor/Server/assets/97235034/e43089e1-298d-4771-a94c-66f257fc8d0e">
    게시글이 없는것을 확인 할 수 있다!  
6. 댓글 삭제 기능  
   ```java
    @Test
    @DisplayName("댓글 삭제 기능 테스트")
    public void deleteCommentTest() throws Exception {
        //given
        Users user = Users.builder()
                .university("홍익대")
                .email("email")
                .loginId("아이디")
                .nickname("닉넴")
                .username("정기민")
                .password("비번")
                .build();

        Users commentUser = Users.builder()
                .university("홍익대")
                .email("댓글을 작성할 사용자")
                .loginId("아이디")
                .nickname("닉네임")
                .username("홍길동")
                .password("비번")
                .build();
        Users savePostUser = userRepository.save(user);
        Users saveCommentUser = userRepository.save(commentUser);

        Posts post = Posts.builder()
                .createdAt(LocalDateTime.now())
                .content("게시글 내용")
                .imageUrl("image url")
                .title("게시글 제목")
                .likes(0)
                .user(savePostUser)
                .build();
        Posts savePost = postRepository.save(post);

        Comment comment = Comment.builder()
                .user(commentUser)
                .post(savePost)
                .content("댓글 내용")
                .build();
        Comment saveComment = commentRepository.save(comment);
        savePost.addComment(saveComment);
        System.out.println("savePost.getComments() = " + savePost.getComments());
        //when
        commentRepository.delete(saveComment);
        savePost.removeComment(saveComment);
        //then
        System.out.println("savePost.getComments() = " + savePost.getComments());
    }
    ```  
   댓글또한 콘솔에 찍어서 확인해보았다.  
   <img width="1134" alt="image" src="https://github.com/The-Sculptor/Server/assets/97235034/938f7e0e-e140-4bd9-9e6c-26bda0818525">
    잘 삭제된 것을 확인 할 수 있다.  
7. 좋아요 삭제(취소) 하기  
   ```java
    @Test
    @DisplayName("좋아요 취소하기 테스트")
    public void unlikePostsTest() throws Exception {
        //given
        Users user = Users.builder()
                .university("홍익대학교")
                .email("email")
                .loginId("아이디")
                .nickname("닉넴")
                .username("정기민")
                .password("비1번")
                .build();
        Posts post = Posts.builder()
                .createdAt(LocalDateTime.now())
                .content("게시글2 내용입니다")
                .imageUrl("image url")
                .title("게시글2 제목입니다")
                .likes(0)
                .user(user)
                .build();
        Users savedUser = userRepository.save(user);
        Posts savedPost = postRepository.save(post);
        //when
        Posts pressLikePost = savedUser.pressLike(savedPost);
        Posts changedPost = savedUser.unlike(savedPost);
        //then
        Assertions.assertThat(changedPost.getLikes()).isEqualTo(0);
    }
    ```  
   원래 좋아요 개수가 0개였던 게시글에 좋아요를 누르고 (pressLike()), 좋아요를 바로 취소했다.(unlike())  
   <img width="1440" alt="image" src="https://github.com/The-Sculptor/Server/assets/97235034/b2311aaa-5194-466e-afd3-3b1716d61b4e">
    좋아요 개수가 0개가 된것을 볼 수 있다.  
8. 1:1 쪽지 기능  
    ```java
    @Test
    @DisplayName("쪽지 기능 테스트")
    public void MessageTest() throws Exception {
        //given
        Users sender = Users.builder()
                .university("홍익대")
                .email("sender_email")
                .loginId("아이디")
                .nickname("닉넴")
                .username("sender")
                .password("비번")
                .build();

        Users receiver = Users.builder()
                .university("홍익대")
                .email("receiver_email")
                .loginId("아이디")
                .nickname("닉넴")
                .username("receiver")
                .password("비번")
                .build();

        Users saveSender = userRepository.save(sender);
        Users saveReceiver = userRepository.save(receiver);
        //when
        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content("1:1 쪽지 내용")
                .build();

        Message saveMessage = messageRepository.save(message);
        //then
    }
    ```  
   사용자 두명을 생성하고, sender와 receiver를 설정하여 실행했다.  
   <img width="1440" alt="image" src="https://github.com/The-Sculptor/Server/assets/97235034/9e5e69e2-723e-498b-a238-6492f96c3c28">
    sender와 receiver가 설정된것을 볼 수 있다.  
***
***
## 3. JPA 관련 문제  
#### 1. 어떻게  data jpa는 interface만으로도 함수가 구현이 되는가?

```java
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    //...
}
```  
위와 같은 코드에서 MemberRepository는 **@Repository** 어노테이션을 작성해주지도 않았는데, 빈으로 등록되어 사용된다.  
결론부터 말하자면 다음과 같다.  
> Spring Data JPA가 사용자가 JpaRepository 인터페이스를 상속받는 repository 인터페이스를 만나면, 이를 구현한 클래스를 동적으로 생성하고 생성한 클래스를 빈으로 등록하여 의존성 주입을 해주기 때문이다.  
![image](https://github.com/The-Sculptor/Server/assets/97235034/90a7c958-29da-4882-85fb-efd88075cb39)  
  
이때, 동적으로 생성된 클래슨 **SimpleJpaRepository** 의 프록시 이다.  
```java
@Repository
@Transactional(readOnly = true)
public class SimpleJpaRepository<T, ID> implements JpaRepositoryImplementation<T, ID> {

	private final EntityManager em;
    
	@Override
	public List<T> findAll() {
		return getQuery(null, Sort.unsorted()).getResultList();
	}
    @Transactional
	@Override
	public <S extends T> S save(S entity) {

		Assert.notNull(entity, "Entity must not be null.");

		if (entityInformation.isNew(entity)) {
			em.persist(entity);
			return entity;
		} else {
			return em.merge(entity);
		}
	}
}
```  
**SimpleJpaRepository** 의 내부를 보면 위와 같다. 여기서는 EntityManager를 생성자 주입을 통해 주입 받는다.  

#### 2. Spring Data JPA 는 어떻게 EntityManager를 매번 주입 받는 것일까?  
이는 Spring에서 제공하는 proxy 기능과 관련이 있다고 한다.  
EntityManager 인스턴스는 실제로 각 데이터베이스 연결/트랜잭션에 대해 새로 생성되는 것이 아니라, Spring이 관리하는 proxy 객체를 통해 접근된다.  
이 프록시 객체는 실제 EntityManager의 메서드 호출을 가로채서 현재 실행 중인 트랜잭션과 관련된 실제 EntityManager 인스턴스로 연결하는 것이다.  
따라서, 각각의 데이터베이스 작업은 고유한 EntityManager를 사용하는 것처럼 보이지만, 실제로는 proxy를 통한 대리 접근 방식을 사용하는 것이다.  
***
***
***
## Service 계층 구현
이번 과제에도 저번에 소개했던 다음의 6가지 기능에 집중하여 구현을 했다.  
- 게시글 조회
- 게시글에 사진과 함께 글 작성하기
- 게시글에 댓글 및 대댓글 기능
- 게시글에 좋아요 기능
- 게시글, 댓글, 좋아요 삭제 기능
- 1:1 쪽지 기능

### 1. PostService 
```java
@Transactional
public Long savePost(Post post) {
    Post savePost = postRepository.save(post);
    return savePost.getPostId();
}
// 게시글 전체 조회
public List<Post> retrievePost() {
    return postRepository.findAll();
}

//게시글을 페이징 하여 조회
public Page<Post> retrievePostsPaged(int page, int size) {
    Pageable pageable = PageRequest.of(page, size); // page는 조회하고자 하는 페이지 번호, size는 페이지 당 게시글 수
    return postRepository.findAll(pageable);
}
```
가장 먼저 게시글을 저장하고 조회하는 로직을 구현했다.  
게시글을 조회하는 로직은 두가지로 나누어 구현했다.  
1. 게시글 전체를 조회함.
2. 게시글을 페이징 하여 조회함.  
   Pageable 을 사용하여 구현했고, 위와 같이 구현하면 원하는 개수만큼 게시글을 조회할 수 있다.

### Pageable
Pageable 인터페이스는 사용자가 한번에 너무 많은 데이터를 로드하여 애플리케이션의 성능이 저하되는 것을 방지하는 데 도움이 된다.  
```java
Pageable pageable = PageRequest.of(page, size);
Pageable sortedByLastName = PageRequest.of(0, 5, Sort.by("lastName"));
```
위와 같이 PageRequest.of() 를 통해 직접 인스턴스를 생성할 수 있다.  
PageRequest.of() 의 세번째 파라미터로 정렬 기준을 넘겨줄 수도 있다.  
<img width="1020" alt="image" src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/97235034/81bd3996-9431-44b1-b893-77b005cc5387">
JpaRepository의 ```Page<T> findAll(Pageable pageable);``` 는 위의 이미지와 같이 찾을 수 있다. 

```java
//게시글에 좋아요 누르기
@Transactional
public void pressLike(Post post) {
    Post findPost = postRepository.findById(post.getPostId())
            .orElseThrow(() -> new EntityNotFoundException("해당 ID의 Post를 찾을 수 없습니다"));
    findPost.plusLike();
}

@Transactional
public void unLike(Post post) {
    Post findPost = postRepository.findById(post.getPostId())
            .orElseThrow(() -> new EntityNotFoundException("해당 ID의 Post를 찾을 수 없습니다"));
    findPost.minusLike();
}

//게시글 삭제
@Transactional
public void deletePost(Post post) {
    commentRepository.deleteByPost(post);
    postLikeRepository.deleteByPost(post);

    postRepository.delete(post);
}
```
- Transactional 어노테이션 사용이유  
  데이터베이스 작업의 일관성과 무결성을 보장하기 위해서이다. **@Transactional** 어노테이션을 사용하는 것은 비즈니스 로직이 실행되는 동안 여러 데이터베이스 연산이 하나의 작업 단위로 묶이도록 하여, 모든 연산이 성공적으로 완료되거나 하나라도 실패할 경우 전체가 취소(롤백)되도록 하는 것입니다.  
  **readOnly = true** 를 설정하는 것은 주로 데이터를 조회하는 작업에 트랜잭션을 적용할 때 사용한다. 데이터 변경이 없음을 알기 때문에 성능상에서 이점이 있다고 한다.  
- deletePost() 메서드  
  해당 메서드에서는 게시글을 삭제하는 로직이 핵심로직이다. 하지만 다른 로직이 위에 추가적으로 있는것을 볼 수 있다.  
  게시글에 연관되어 있는 댓글과 좋아요를 전부 삭제하고 게시글을 삭제 하는 것이 맞기 때문에, **deleteByPost()** 를 생성하여 댓글과 좋아요를 먼저 삭제 했다.  

### 2. CommentService
```java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;

    // 댓글 저장 기능
    @Transactional
    public Long saveComment(Comment comment) {
        Comment saveComment = commentRepository.save(comment);
        return saveComment.getCommentId();
    }

    // 대댓글 기능
    @Transactional
    public void setChildComment(Comment parentComment, Comment childComment) {
        parentComment.addChildComment(childComment);
        childComment.setParent(parentComment);
    }
}
```
댓글 저장하는 기능과 대댓글을 작성하는 기능을 구현했다.  
대댓글은 자기 자신의 엔티티를 참조하는 방식으로 구현했기 때문에 양방향 연관관계가 되게끔 만들었다.  
## 3. UserService
```java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public Long saveUser(Users user) {
        Users saveUser = userRepository.save(user);
        return saveUser.getUserId();
    }

    @Transactional
    public void deleteUser(Users user) {
        /** 사용자 탈퇴 이전에 관련된 것들 모두 삭제*/
        postLikeRepository.deleteByUser(user);
        postRepository.deleteByUser(user);
        commentRepository.deleteByUser(user);

        userRepository.delete(user);
    }
}
```
UserService에서도 사용자를 저장하는 로직과 사용자가 탈퇴하는 로직을 구현했다.  
> deleteUser 에서는 사용자 탈퇴 이전에 사용자가 작성한 게시글, 누른 좋아요, 작성한 댓글을 모두 삭제 한 뒤에 탈퇴가 진행되도록 했다.  
## 4. MessageService

```java
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    @Transactional
    public Long sendMessage(String content, Users sender, Users receiver) {
        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(content)
                .build();
        Message saveMessage = messageRepository.save(message);
        return saveMessage.getMessageId();
    }
}
```
쪽지를 보낼 경우에는 사용자가 입력한 쪽지 내용(content)과, 보내는 사람(sender), 받는 사람(receiver)가 정해져 있기 때문에 위와 같이 작성했다.  
***
***
## 4주차 CRUD API 만들기
여태껏 만들었던 Entity와 Repository를 기반으로 한 Service를 토대로 Controller를 만들어 보았다.   

RESTful 한 API를 위해 각 컨트롤러에서 URI를 다음과 같이 구성하였다.
### 게시글(Post)
1. 게시글 생성 -> api/post + Post 방식
2. 여러 게시글 조회(게시판) -> api/post + Get 방식
3. 게시글 하나 조회 -> api/post{postId} + Get 방식
4. 특정 게시글 삭제 -> api/post/{postId} + Delete 방식
5. 게시글 수정 -> api/post/{postId} + Patch 방식
6. 좋아요 누르기 -> api/post/{postId}/like + Patch 방식

> 게시글에 수정사항이 있을때 어렴풋이 Patch 혹은 Put을 사용한다고 이해하고 있었는데 이번 기회에 검색을 통해 둘을 구분해보았다.  
Put : 타겟 리소스를 전체 교체할때 사용된다.  
Patch : 타겟 리소스의 일부 수정을 할때 사용된다. 클라이언트는 변경하고자 하는 필드만 따로 전송할 수 있다.  

따라서 Patch 방식을 사용하였다.  
### 댓글(Comment)  
1. 댓글 작성 -> api/comment + Post
2. 댓글 삭제 -> api/comment/{commentId} + Delete
3. 대댓글 작성 -> api/comment/{commentId} + Post 
4. 댓글에 좋아요 누르기 -> api/comment/{commentId}/like + Patch
5. 댓글에 좋아요 취소하기 -> api/comment/{commentId}/unlike + Patch

### 쪽지(Message)  
1. 쪽지 전송 -> api/message + Post
2. 쪽지 조회 -> api/message + Get  

### 사용자(User)  
사용자 로그인과 회원가입 관련해서는 추후에 Spring Security와 같이 진행한다고 알고 있어서 간단하게 회원을 저장하는 정도의 로직만 구현했다.  
1. 사용자 로그인 -> api/user/login + Post  


예시로 PostController의 코드를 보자.  
```java
/**
 * 목적 : 게시글 생성
 * 성공 : 생성된 게시글 정보 리턴
 * 실패 : 에러 메시지 출력
 */
@PostMapping("api/post")
public ResponseEntity<ApiResponse<PostResponseDto>> createPost(@RequestBody CombinedDto combinedDto) {
    try {
        Users user = userService.findUser(combinedDto.getUser().getUserId());
        Post createdPost = Post.builder()
            .createdAt(LocalDateTime.now())
            .content(combinedDto.getPost().getContent())
            .title(combinedDto.getPost().getTitle())
            .imageUrl(combinedDto.getPost().getImageUrl())
            .user(user)
            .likes(0)
            .build();
        Long savePostId = postService.savePost(createdPost);
        PostResponseDto postResponseDto = PostResponseDto.createFromPost(createdPost, savePostId);

        ApiResponse<PostResponseDto> response = ApiResponse.of(201, "게시글 생성 성공", postResponseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (Exception e) {
        ApiResponse<PostResponseDto> errorResponse = ApiResponse.of(500, e.getMessage(), null);
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
```
게시글을 생성할때 프론트측으로 부터 게시글 정보를 받아서 builder를 통해 생성하게 되는데 이때 내 코드 상에서는 컨트롤러단에서 생성한다.   

하지만 생각해보면 핵심 비즈니스 로직은 Service 계층에서 담당하는것이 맞다고 생각 하기 때문에, 단순히 savePost() 를 통해서 생성된 게시글 객체를 저장만 하는것이 아니라 createPost() 라는 메서드를 통해서 게시글 객체를 생성과 동시에 저장하는 것이 맞을 것 같다는 생각이 든다!!
        
### DTO
보통 데이터를 프론트로부터 전달 받을때 DTO를 사용하게끔 작성했고, 리턴해주는 값도 DTO에 담아서 전달하였다.  
정적 팩토리 메서드를 통해서 구현했다.  
```java
@Getter
public class PostResponseDto {
    private Long postId;
    private Long userId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private int likes;
    private String imageUrl;

    private PostResponseDto(Long postId, Long userId, String title, String content, LocalDateTime createdAt, int likes, String imageUrl) {
        this.postId = postId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.likes = likes;
        this.imageUrl = imageUrl;
    }

    // 정적 팩토리 메서드
    public static PostResponseDto createFromPost(Post post, Long postId) {
        return new PostResponseDto(
                postId,
                post.getUser().getUserId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getLikes(),
                post.getImageUrl());
    }

    public static PostResponseDto updateFromPost(Post post, Long postId) {
        return new PostResponseDto(
                postId,
                post.getUser().getUserId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getLikes(),
                post.getImageUrl());
    }
}
```
생성자를 private으로 만들어서 클래스 내부에서만 사용할 수 있게 만들고, 정적 팩토리 메서드를 생성하여 외부에서 이 메서드를 사용하게끔 만든다.  

### ApiResponse  
```java
@Getter
public class ApiResponse<T> {

    // API 응답 결과 Response
    private T result;

    // API 응답 코드 Response
    private int resultCode;

    // API 응답 코드 Message
    private String resultMsg;

    @Builder
    public ApiResponse(final T result, final int resultCode, final String resultMsg) {
        this.result = result;
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
    public static <T> ApiResponse<T> of(int resultCode, String resultMsg, T result) {
        return new ApiResponse<>(result,resultCode,resultMsg);
    }
}
```
매번 ResponseEntity에 다른 값을 담아서 리턴하는 것은 가독성이 떨어질 수 있기 때문에 위와 같이 응답 클래스를 생성하고 이를 활용하였다.

### Swagger
이전에 사용해본적은 있으나, 다른 팀원분이 설정해주신 것을 토대로 사용만 하였기 때문에 완전히 이해하지 못하고 있었다.  
단순히 적용 이외에도 여러 다양한 어노테이션을 사용해 설정할 수 있는 것 같다. 

나는 SpringDoc OpenAPI (Swagger 3) 를 사용했다.

- @Operation: 메서드 레벨에서 사용되며, API 작업에 대한 정보를 작성한다.
- @Parameter: 메서드의 파라미터에 사용되어, 파라미터에 대한 데이터를 제공한다.
- @ApiResponse: 메서드의 응답에 대한 정보를 나타낸다.
- @ApiResponses: 하나 이상의 @ApiResponse 어노테이션을 그룹화한다.
- @Tag: API를 그룹화한다.  

이번 작업시에는 위의 여러 어노테이션들을 직접 적용해보지는 못하였지만 다음 리팩토링때 적용해보고 싶다.  

### 실제 동작하는지  
<img width="723" alt="image" src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/97235034/b1559b71-50c8-4688-bd7d-7ae9bcfc9bab">  
위의 이미지는 내가 작성한 컨트롤러들이 Swagger에 의해 정리 되어있는 것을 보여주는 이미지이다.  

게시글을 생성하는 컨트롤러를 Swagger로 테스트 해보자.  
<img width="1440" alt="image" src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/97235034/bf8520dc-4e16-4f3a-8b46-1c76b83b4ba5">
위와 같이 Request 를 채워주고 실행한다.  
<img width="1440" alt="image" src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/97235034/2a560373-57c9-4bd1-be9e-72cf596c4728">
그러면 잘 생성된 것을 볼 수 있다. DB에 보면 잘 반영되어 있는 것을 볼 수 있다.  
<img width="1153" alt="image" src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/97235034/be2f0de7-5545-4fd4-beb2-86d6230435d4">  


### 수정하고 싶은 사항  
#### 1. 에러 코드에 대한 Global Exception 처리.  
이번 과제에서는 일단 컨트롤러 구현에 집중했기 때문에 글로벌 Exception 처리를 완전히 구현하지는 못했다. 추후에 추가해보자.  

#### 2. 어노테이션을 활용한 사용자 정보 가져오기  
예를들어 게시글을 생성할때, 어떤 사용자가 어떤 정보를 토대로 게시글을 생성하는지가 필요하기 때문에 DTO가 두가지 필요하다는 생각이 들었다.  
```java
@Getter
public class PostUserRequestDto {
    private Long userId;
    private String nickname;
}
```
이는 사용자 정보를 알기위한 DTO이다.  
```java
@Getter
public class PostRequestDto {
    private String title;
    private String content;
    private String imageUrl;
}
```
이는 게시글을 생성하기 위한 DTO이다.  
이 두가지 DTO를 모두 사용해야 했는데 @RequestBody는 한번만 사용가능하므로 다음과 같이 구성했었다.  
```java
@Getter
public class CombinedDto {
    private PostUserRequestDto user;
    private PostRequestDto post;
}
```
이렇게 하여 사용자 정보와 게시글 정보를 가져올 수 있었다.  
하지만 추후에 로그인과 회원가입 관련 로직이 구현되면 커스텀 어노테이션을 만들어 로그인되어있는 사용자 정보를 가져올 수 있게 만들고 싶다. 예를 들면 다음과 같다.  
```java
@PostMapping("api/post")
public ResponseEntity<ApiResponse<PostResponseDto>> createPost(@LoginUser Users user, @RequestBody PostRequestDto postRequestDto){
    //... 로직 구현
}
```
***
***
## 5주차 Spring Security & JWT  
### 5.1 Refactoring
일단 저번에 수행했던 과제에 대한 리팩토링을 먼저 진행했다.  
저번 주차에서는 Controller 마다 예외처리를 직접하여 코드의 양이 많아지고 가독성이 떨어진다는 피드백이 있었기에, 이를 반영하여 Global Exception을 추가하였다.  
```java
public class BusinessExceptionHandler extends RuntimeException {

    @Getter
    private final ErrorCode errorCode;

    @Builder
    public BusinessExceptionHandler(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    @Builder
    public BusinessExceptionHandler(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
```

### 5.2 Spring Security & JWT
#### 5.2.1 인증(Authentication) 방식
사용자를 인증하는 방식에는 세션과 토큰 방식이 있다고 알고있다. 먼저 세션 방식을 보자.
![image](https://github.com/CEOS-Developers/spring-everytime-19th/assets/97235034/e6b0534c-df58-4b54-a178-f02cc2f2f932)
사용자가 로그인을 진행하면 서버에서는 Session에 저장하고 해당 세션 ID를 넘겨준다.  
이후에 사용자가 세션ID를 담아서 데이터를 요청하면 해당 세션 ID를 검사(?) 하여 데이터를 보내준다.  

**문제점**
1. 세션 ID를 관리하는 Session store는 서버에서 관리해야 하기 때문에 사용자 수가 많아지면 서버에 부담이 된다.
2. 사용자가 요청을 보낼 때 마다 세션 스토어에서 세션 ID를 찾아보아야 하므로 부담이 될 수 있다.

반면, JWT를 사용한 방식에서는 위와같은 서버 부담이 적다. 이를 _stateless_ 하다고 부른다.  
  
이제 JWT 방식을 보자.  
JWT에서는 우리가 배웠듯이 사용자의 정보를 토대로 토큰값을 만들어서 이를 사용자에게 전달하는 방식으로 사용된다.  
![image](https://github.com/CEOS-Developers/spring-everytime-19th/assets/97235034/a4f10893-e978-4177-82a5-7320026d0ee7)
1. 사용자 로그인
2. 사용자를 DB에서 확인 (혹은 회원가입 시킴)
3. Access token 발급 -> 이때 사용자의 정보(ex. 이메일)를 사용하여 토큰을 생성한다.
4. 사용자는 이후에 요청을 보낼때마다 해당 토큰을 담아서 보낸다.
5. 서버에서 이를 검증하여 요청을 처리한다.
  
세션 방식과 다르게, 서버에서 따로 저장소를 사용하여 무언가를 관리할 필요가 없다. 단지 사용자가 보내는 토큰이 유효한지를 검증하는 로직이 필요할 뿐이다.  

다만 문제점은 있다.  
**문제점**
1. access token이 탈취 당하면 막을 수 없다.  
   사용자의 access token을 누가 탈취해서 맘대로 사용하면 서버입장에서는 알 방법이 없다.  
   > 따라서 access token의 유효기간을 매우 짧게 (1시간 정도) 로 설정해둔다. + Refresh token을 통해 Access token을 재발급하는 과정을 거친다.  
2. JWT의 payload는 디코딩을 통해 암호화한 내용을 확인할 수 있기 때문에 중요정보는 담지 못한다.  

#### 5.2.2 구현  
```java
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
            .csrf(CsrfConfigurer::disable)
            .sessionManagement(configurer -> configurer.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(
                    requests -> requests.requestMatchers("/", "/api/user/join"
                            ,"/api/user/login"
                            ,"/swagger-ui/index.html"
                            ,"/swagger-ui/**"
                            ,"/css/**"
                            ,"/img/**"
                            ,"/swagger-resources/**"
                            ,"/v3/api-docs/**"
                    ).permitAll()
                            .anyRequest().hasRole("USER")
            );
    http.addFilterBefore(jwtExceptionHandlerFilter(), JwtAuthenticationFilter.class);
    http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
}
```  
WebSecurityConfig 파일의 securityFilterChain 메서드를 위와 같이 작성했다.  
스웨거 관련 url들을 넣어주지 않으면 스웨거가 실행되긴하는데 흰색창만 뜬다. 이것 때문에 막혀서 시간을 좀 썼다...  
이외의 url은 USER 라는 ROLE을 가지고있어야 접근이 가능하게 설정했다.  
```java
public String createAccessToken(String username, Authentication authentication) {
    String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

    long now = (new Date()).getTime();
    Date validity = new Date(now + ACCESS_TOKEN_VALIDITY_SECONDS * 1000);

    return Jwts.builder()
            .setSubject(username)
            .claim(AUTHORITIES_KEY, authorities)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .compact();
}
```  
액세스 토큰을 만드는 부분은 위와 같다. 사용자 이름을 사용해서 만들도록 하였다.  
#### 5.2.3 실행
- 회원가입
<img width="951" alt="image" src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/97235034/62185566-a0b6-4fbe-b429-204599aa5db5">
  <img width="1152" alt="image" src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/97235034/7c25a2f9-23ce-4e3b-ae3e-007967224cc2">
  

- 로그인  
  <img width="950" alt="image" src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/97235034/47ce939a-ca6c-485a-9f27-18ede8846967">
로그인 결과로 액세스토큰이 발급되어있는 것을 볼 수 있다.    
  

- 액세스 토큰이 필요한 API
  <img width="949" alt="image" src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/97235034/9f1a0c83-7038-4e9a-b7f0-fdd7694794e4">
액세스 토큰을 넣어서 요청을 보내야 수행된다. 액세스 토큰이 없다면 에러가 발생하도록 했다.  


## 6주차 Docker  
### 6.1 Docker 명령어 써보기  
<img width="1440" alt="image" src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/97235034/8b81d596-d606-4587-8615-409ef8502eed">
위의 이미지처럼 terminal 에서 여러 명령어들을 수행하면서 도커와 친해져보았다.  

### 6.2 겪었던 어려움  
도커를 통해서 배포를 진행해보면서 크게 두번의 막힘과정이 있었다.  
첫번째는 DB관련 이슈이다.  
  
  

1. DB 에 연결이 안됨.  
Dockerfile을 만들고, 생성한 파일을 토대 docker 이미지를 만든 뒤 도커 이미지를 실행하여 컨테이너를 생성했다.  
   <img width="905" alt="image" src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/97235034/ffc2752b-5e40-4f81-84aa-cc28aaeffd80">  
이 이미지에서 build 명령어로 도커 이미지를 생성함.  
   <img width="1087" alt="image" src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/97235034/8c6eba7f-bfdc-4040-b177-f0bc89939a77">
이 이미지에서는 생성한 이미지를 실행시켜 컨테이너를 만듦.  
    근데 에러가 발생했다. 에러 내용은 mysql과 연결이 안된다는 것.  
도커를 통해 이미지를 만들고 해당 이미지로 실행하면, localhost:3306으로 설정했던 DB에 대한 정보를 도커가 읽을 수 없다는 것이 문제였다.  
   <img width="591" alt="image" src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/97235034/8574a05b-f759-42ea-895d-f98cff7b2c29">
따라서 application.yml 에서 127.0.0.1 의 부분을 **host.docker.internal** 로 설정해주어 해결했다.  
  
  
2. Error creating bean 에러  
해당 에러는 보통 특정 클래스가 빈으로 등록되지 않은 상태에서 사용되거나 할때 생기는 문제이다. 근데 로컬에서는 잘 돌아가는 코드가 도커 컨테이너를 통해서 실행하면 계속 에러가 발생했다.  
나의 경우에는 TokenProvider 가 빈으로 등록되지 않았다는 에러가 발생해서 해당 코드를 다시 봤다. 근데 코드상에는 문제가 없었다.  
<img width="872" alt="image" src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/97235034/b69aeb52-b673-45ac-b990-79d827408388">  
에러를 자세히 보니 jwt.secret.key 부분에서 뭔가 안된다는 것을 알았고 이때 깨달았다.  
  
    지금까지 JWT_SECRET_KEY 는 유출되면 안되는 키 라고 생각하여 다음과 같이 저장했다.  
<img width="230" alt="image" src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/97235034/7550d370-fad2-4c2e-850c-efe3e9e0c466">  
인텔리제이 내부에서 환경변수를 설정하여 사용하고 있었다. 하지만 이렇게 되면 도커는 해당 변수를 이해하지 못하기 때문에 발생하는 문제였던 것이다.  
  
    해결하는 방법은 여러개가 있지만 다음의 방법이 좋은 듯 하다.  
    ```yaml
   version: '3'
   services:
     spring-app:
       image: everytime-ceos
       environment:
         - JWT_SECRET_KEY=your_secret_key
       ports:
         - "8080:8080"
    ```
    docker-compse.yml 에 위와 같이 설정할 수 있고, 루트 디렉토리에 .env 파일을 생성하여 거기에서 키 값을 입력해두면 된다.  

### 6.3 실행  
위의 에러들을 해결하고 나니 제대로 실행이 됐다.
<img width="1440" alt="image" src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/97235034/db008b34-6fc9-49de-bb9c-fcf599c999fd">





