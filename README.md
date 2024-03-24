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

  
