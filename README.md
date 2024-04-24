# 연관관계 매핑

이번에 게시판 서비스를 만들면서 설계한 엔티티간의 연관관계 구조는 다음과 같다.
![img_4](https://github.com/riceCakeSsamanKo/spring-everytime-19th/assets/121627245/87fd119c-859c-4c6f-9773-a4bf7e4891d3)

# 개발 중 문제점 및 고민점

1. 이번에 User와 Post 사이에 좋아요 기능을 추가하기 위해서 Like 엔티티를 설계했었다. 하지만 like 키워드는 DB에서 이미 사용중인 예약어로 테이블 명으로 사용할 수 없었다. 따라서 Heart로
   테이블명을 교체하였다.
2. 매핑시 필연적으로 중간 테이블들이 존재하게 되는데 중간테이블의 생성에 대해서 고민했었다. 일반적으로 중간테이블에 해당하는 엔티티의 관리를 외부에 노출하게 된다면 조인 정보가 잘못될 수 있기 때문에 최대한 노출을
   하지 않도록 설계하고자 하였다.

~~~java

@Getter
@NoArgsConstructor(access = PROTECTED)  // 기본생성자 protected
@Entity
@ToString(exclude = {"user", "post"})
public class Heart { // 좋아요  
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "heart_id")
    private Long id;

    @ManyToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = "user_id")
    @Setter(value = PROTECTED)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    @Setter(value = PROTECTED)
    private Post post;

    protected static Heart createHeart(User user) {  // 생성 메서드
        Heart heart = new Heart();
        user.addHearts(heart);

        return heart;
    }
}
~~~

결과적으로 중간테이블에 해당하는 엔티티(예: Heart)들의 생성자는 외부에서 따로 생성할 수 없도록 하였고, 별도의 create 메소드를 선언하였다. 해당 엔티티와 연관된 다른 테이블에서 create 메소드를
호출하여 객체를 생성 후 연관관계를 매핑하도록 설계하였다.

 ~~~java

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@ToString(exclude = {"user", "hearts", "comments", "images"})
public class Post extends BaseTimeEntity {  //게시물  
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")  //select * from post p join user u on p.user_id = u.id;  
    @Setter(value = PROTECTED)
    private User user;

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private List<Heart> hearts = new ArrayList<>();


    /**
     * 연관관계 편의 메서드  
     */

    // User, Heart, Post의 연관관계는 Post에서 관리함  
    public void addHeartFromUser(User user) {
        Heart heart = Heart.createHeart(user);
        heart.setPost(this);
        hearts.add(heart);
    }

    // 생략 ...
}
~~~

3. 중간테이블의 엔티티를 연결된 양 엔티티들 중 어느쪽에서 라이프 사이클 관리를 할지 고민이 많았다.
   예를 들어 User - Comment - Post 세개의 엔티티가 있을 때, Comment의 라이프 사이클 관리을 User에서 할지 Post에서 할지가 문제였다. 코드를 작성하기에는 User에서 Comment의
   관리를 하는 것이 편리했을 것 같긴 한데, 그래도 댓글(Comment)는 게시물(Post)에 작성을 하는 것이니 일단 Post에서 Comment의 라이프 사이클을 관리하는 방향으로 설계하였다. 이 점은 잘한건지
   아직은 잘 모르겟다...
4. 중간 테이블에 해당하는 엔티티들은 외부에 노출을 최대한 피하는 방향으로 설계하였기에 해당 엔티티들에 대한 리포지토리 계층은 구현하지 않았다. 대신 더티체킹 기능을 이용해 연결된 테이블이 저장될 때 함께 저장되는
   방식으로 구성했다.
5. 유저간의 쪽지 기능을 구현하기 위해서 Message 엔티티에는 sender, receiver 두개의 User 필드를 두었다. 처음에는 sendMessage, receiveMessage라는 중간테이블을 두어
   기능을 구현하여 보려 했지만 데이터를 저장하는 과정에서 문제가 발생하여 구조를 변경하였다.

~~~java
package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
@Entity
@ToString(exclude = {"sender", "receiver"})
public class Message { // 쪽지, message는 user의 비즈니스 연관관계 편의 메서드로 저장함. 별도의 리포지토리 없다.  
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @Column(nullable = false)
    private String title;
    private String content;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    @ManyToOne(fetch = LAZY)
    @Setter(value = PROTECTED)
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(fetch = LAZY)
    @Setter(value = PROTECTED)
    @JoinColumn(name = "receiver_id")
    private User receiver;


    public Message(String title, String content) {
        this.title = title;
        this.content = content;
        this.sentAt = LocalDateTime.now();
    }

    public void setSenderAndReceiver(User sender, User receiver) {
        sender.addSendMessage(this);
        receiver.addReceiveMessage(this);
    }
}
~~~

6. 유지 보수간의 문제를 최소화하기 위해서 setter의 사용은 최대한 배제하였다.

## fetch join 할 때 distinct를 안하면 생길 수 있는 문제

일대다 관계에서 다에 속한 테이블을 fetch join으로 조회시에 각각의 컬렉션 엔티티에 속한 엔티티가 중복 조회되는 문제 발생
Team : Member = 일대다
query = "select t from Team t join fetch t.members"라는 쿼리로 조회를 하는 경우 만일 해당 team에 3개의 member가 조인된 경우
team이 세번 조회되는 문제가 발생함.
따라서 이 경우 query = "select t from Team t distinct join fetch t.members"와 같이 distinct 키워드를 통해서 중복 조회가 되는 문제를 해결한다.

# 3주차 서비스 계층 구현

총 10개의 엔티티에 대해 서비스를 구현했다.

### UserService

유저 가입, 조회 로직을 구현했다.

~~~java
public Long join(User user) {
    if (userRepository.findByUsername(user.getUsername()).isPresent()) {
        log.error("에러 내용: 유저 가입 실패 " +
                "발생 원인: 이미 존재하는 아이디로 가입 시도");
        throw new AppException(DATA_ALREADY_EXISTED, "이미 존재하는 아이디입니다");
    }
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
        log.error("에러 내용: 유저 가입 실패 " +
                "발생 원인: 이미 존재하는 이메일로 가입 시도");
        throw new AppException(DATA_ALREADY_EXISTED, "이미 사용중인 이메일입니다");
    }
    if (userRepository.findBySchoolIdAndStudentNo(user.getSchool().getId(), user.getStudentNo()).isPresent()) {
        log.error("에러 내용: 유저 가입 실패 " +
                "발생 원인: 이미 존재하는 학번으로 가입 시도");
        throw new AppException(DATA_ALREADY_EXISTED, "이미 사용중인 학번입니다");
    }

    userRepository.save(user);
    return user.getId();
}
~~~

가입시에 이미 존재하는 아이디이거나, 이메일 혹은 가입하려는 학교에 동일 학번이 존재하는 경우 가입이 불가하도록 구현했다.

find 로직은 아래와 같은 식이다.

~~~java

@Transactional(readOnly = true)
public User findById(Long userId) {
    Optional<User> optionalUser = userRepository.findById(userId);
    if (optionalUser.isEmpty()) {
        log.error("에러 내용: 유저 조회 실패 " +
                "발생 원인: 존재하지 않는 PK 값으로 조회");
        throw new AppException(NO_DATA_EXISTED, "존재하지 않는 유저입니다");
    }
    return optionalUser.get();
}
~~~

유저 서비스 코드를 보면 AppException이라는 예외를 던지는 것을 볼 수 있는데, 이는 사용자 정의 예외로 따로 정의하여 이번 어플리케이션에서 사용할 계획이다.

~~~java

@AllArgsConstructor
@Getter
public class AppException extends RuntimeException {
    private ErrorCode errorCode;
    private String message;
}
~~~

~~~java

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DATA_ALREADY_EXISTED(CONFLICT, ""),
    NO_DATA_EXISTED(NOT_FOUND, ""),
    NOT_NULL(NO_CONTENT, ""),

    ID_DUPLICATED(CONFLICT, ""),
    INVALID_PASSWORD(UNAUTHORIZED, ""),

    NO_DATA_ALLOCATED(FAILED_DEPENDENCY, ""),

    KEYWORD_TOO_SHORT(BAD_REQUEST, ""),
    INVALID_VALUE_ASSIGNMENT(BAD_REQUEST, ""),
    INVALID_URI_ACCESS(NOT_FOUND, "");


    private final HttpStatus httpStatus;
    private final String message;
}
~~~

AppException은 예외가 발생시 ErrorCode enum을 통해서 그에 맞는 HttpStatus와 메시지를 반환해주도록 설정하였다.

UserService를 구현하는데 사용한 UserRepository에는 @EntityGraph 어노테이션을 통해서 fetch join을 적용하였다.
이를 통해 User를 조회하는 동시에 연관된 school 필드또한 함께 가져와서 N+1 문제를 해결하였다.

~~~java
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"school"})
    Optional<User> findById(long userId);

    @EntityGraph(attributePaths = {"school"})
    Optional<User> findByUsername(String username);

    @EntityGraph(attributePaths = {"school"})
    Optional<User> findByUsernameAndPassword(String username, String password);

    @EntityGraph(attributePaths = {"school"})
    Optional<User> findBySchoolIdAndStudentNo(long schoolId, String studentNo);

    @EntityGraph(attributePaths = {"school"})
    Optional<User> findByEmail(String email);

    List<User> findBySchoolId(Long schoolId);

    @EntityGraph(attributePaths = {"school"})
    List<User> findByName(String name);
}
~~~

하지만 모든 메서드에 fetch join을 적용하지는 않았는데

~~~java
List<User> findBySchoolId(Long schoolId);
~~~

와 같이 연관된 필드로 조회하는 경우, 해당 필드를 User에서 다시 get을 할 일은 없을 것 같아서 이 경우 조인을 적용하지 않았다.

### PostService

게시물 등록, 조회, 제거 기능을 구현했다.

~~~java

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    public Long save(Post post, Attachment... attachments) {
        postRepository.save(post);
        for (Attachment attachment : attachments) {
            if (attachment.getId() != null) {
                log.error("에러 내용: 게시물 등록 실패 " +
                        "발생 원인: 이미 다른 곳에 등록된 파일을 해당 게시물에 재 등록함");
                throw new AppException(DATA_ALREADY_EXISTED, "이미 등록된 파일입니다");
            }
            post.addAttachment(attachment);
        }
        return post.getId();
    }

    public Post findById(Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            log.error("에러 내용: 게시물 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 게시물입니다");
        }
        return optionalPost.get();
    }

    public List<Post> findByAuthorId(Long authorId) {
        return postRepository.findByAuthorId(authorId);
    }

    public void deletePost(Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            log.error("에러 내용: 게시물 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 게시물입니다");
        }

        // 연관관계 제거
        List<Comment> comments = commentRepository.findByPostId(postId);
        for (Comment comment : comments) {
            comment.removeParentComment();
        }
        commentRepository.deleteAll(comments);

        // 연관관계 제거
        postLikeRepository.deleteAllByPostId(postId);

        postRepository.deleteById(postId);
    }
}
~~~

postService에는 deletePost()라는 제거 메서드가 존재한다. 게시물에는 연관된 유저, 댓글, 게시판, 첨부파일이 존재한다.
![img_5](https://github.com/riceCakeSsamanKo/spring-everytime-19th/assets/121627245/87c84813-01ad-4384-b91e-5de9965f4a07)
올바른 제거를 위해서 deletePost() 내부에는 먼저 해당 게시물의 외래키를 갖고있는 다른 엔티티들을 먼저 제거하였다.
그리고 첨부파일(Attachement) 엔티티는 Post에 cascade 설정을 통해서 Post를 제거시 자동으로 같이 제거가 되도록 하였다.

## N+1 문제 테스트 코드

N+1 문제는 조인을 사용하지 않는 경우에 발생할 수 있는 문제로, 연관된 엔티티를 조회 시 별도의 쿼리가 나가는 문제를 말한다.  
EAGER 로딩이든 LAZY 로딩이든 연관된 엔티티를 조회시 별도의 쿼리가 나가는 것은 동일하므로, 조인을 사용하지 않는다면 로딩 전략에는 관계 없이 해당 문제가 발생한다.
다만 LAZY 로딩의 경우 연관된 엔티티를 사용하는 경우에 쿼리가 나가서 즉시 별도의 쿼리가 발생하는 EAGER와는 쿼리 발생 시점이 다르다는 차이점이 존재한다.

~~~java

@Test
public void nPlus1() throws Exception {
    //given
    Attachment attachment1 = Attachment.builder()
            .originFileName("file1")
            .storePath("~/storage")
            .attachmentType(AttachmentType.GENERAL)
            .build();

    Attachment attachment2 = Attachment.builder()
            .originFileName("file2")
            .storePath("~/storage")
            .attachmentType(AttachmentType.GENERAL)
            .build();

    Post post1 = Post.builder()
            .board(board).author(user)
            .title("게시물1").content("내용...")
            .isAnonymous(false).isQuestion(true)
            .build();

    // post1 저장시 attachment1도 해당 게시물에 저장함
    post1.addAttachment(attachment1);
    post1.addAttachment(attachment2);
    postRepository.save(post1);

    em.flush();
    em.clear();

    //when
    Post post = postRepository.findById(post1.getId()).get();
    for (Attachment attachment : post.getAttachments()) {
        attachment.getId();
    }
}
~~~

위의 예시에서는 하나의 Post에 두개의 Attachment를 넣어서 저장한다. 따라서 만일 @EntityGraph나 join fetch를 사용하지 않는다면
두개의 Attachment 조회 쿼리가 발생할 것임을 예상할 수 있다.

### @EntityGraph나 join fetch 전략을 사용하지 않은 경우

~~~
2024-03-24T19:29:18.771+09:00 DEBUG 9712 --- [           main] org.hibernate.SQL                        : 
    select
        p1_0.post_id,  
        p1_0.user_id,
        p1_0.board_id,
        p1_0.content,
        p1_0.created_date,
        p1_0.is_anonymous,
        p1_0.is_question,
        p1_0.modified_date,
        p1_0.title 
    from
        post p1_0    // Post 조회
    where
        p1_0.post_id=?   
        
2024-03-24T19:29:18.790+09:00 DEBUG 9712 --- [           main] org.hibernate.SQL                        : 
    select
        a1_0.post_id,
        a1_0.attachment_id,
        a1_0.attachment_type,
        a1_0.original_file_name,
        a1_0.stored_path 
    from
        attachment a1_0   // Attachments 조회
    where
        a1_0.post_id=?
~~~

그러나 실제로는 한개의 Attachment 쿼리가 나간 것을 알 수 있는데 이는 jpa의 컬렉션 관리 메커니즘에 의해서라고 한다.
hibernate에서는 @OneToMany 또는 @ManyToOne과 같은 연관 관계 매핑에 대해 컬렉션의 모든 항목을 한 번의 쿼리로 조회할 수 있도록 최적화하고 있어 하나의 쿼리만 나간다고 한다.
즉, 위의 예시에서는 총 두개의 Attachment 모두가 조회되었으나 두개의 Attachment를 하나의 쿼리만을 사용하여 가져온 것이다.

### @EntityGraph, join fetch 전략을 사용한 경우

만일 위의 경우와 달리 @EntityGraph를 적용한 경우 발생하는 쿼리는 다음과 같다.

~~~
2024-03-24T19:39:09.329+09:00 DEBUG 13048 --- [           main] org.hibernate.SQL                        : 
    select
        p1_0.post_id,
        a1_0.user_id,
        a1_0.email,
        a1_0.name,
        a1_0.password,
        a1_0.school_id,
        a1_0.student_no,
        a1_0.username,
        p1_0.board_id,
        p1_0.content,
        p1_0.created_date,
        p1_0.is_anonymous,
        p1_0.is_question,
        p1_0.modified_date,
        p1_0.title,
        a2_0.post_id,
        a2_0.attachment_id,
        a2_0.attachment_type,
        a2_0.original_file_name,
        a2_0.stored_path 
    from
        post p1_0 
    left join                // attachment1 조인
        user a1_0 
            on a1_0.user_id=p1_0.user_id 
    left join
        attachment a2_0      // attachment2 조인
            on p1_0.post_id=a2_0.post_id 
    where
        p1_0.post_id=?
~~~

하나의 쿼리만으로 조인을 통해 연관된 모든 Attachment를 조회해 옴을 볼 수 있다.

# 4주차 CRUD API 설계하기

## Parameter 받기

url을 설계하면서 어떤식으로 프론트에서 정보를 받는 것이 적합한지 의문이 들어 알아보았다.  
특히나 @RequestParam은 쿼리 스트링을 통해서, @RequestBody는 json, xml 데이터를 통해서 한번에 여러 값을 받을 수 있다는 공통점이 존재하여 더욱 헷갈렸었다.  
조사한 결과는 다음과 같았다.

- @PathVariable: 사용자의 PK로 특정 정보를 조회하는 경우 사용
- @RequestParam: 검색, 필터링, 정렬 등의 기능을 구현, 적은 양의 데이터를 전달할 때 적합
- @RequestBody: POST, PUT 요청 시 적용. 한번에 여러 데이터를 보내야할 때 적합

<br/>

해당 결과를 바탕으로 조회시에는 @RequestParam과 @PathVariable을 적용하기로 하였다.  
그중 PK 정보로 조회하는 경우에는 @PathVariable을 여러개의 값을 통해서 검색하거나 필터링, 정렬 기능을 사용시에는 @RequestParam을 적용하였다.    
그리고 POST, PUT 메서드를 사용할 만한 상황 ***(예: 수정)*** 에서는 @RequestBody를 통해서 Json 데이터를 받아 처리하는 방식으로 구현했다.

## Rest Api 구현

### 학교(SchoolController)

- 등록
    1. 학교 등록(/school) O
    2. 게시판 등록(/school/{school_id}/board) O
    3. 수업 등록(/school/{school_id}/course) O
- 조회
    - 단건 조회
        1. PK (/school/{school_id}) O
        2. 학교 명 (/school?name={학교명}) O
    - 다중 조회
        1. 모든 학교 조회 (/schools) O
    - 학교에 속한 게시판 조회
        1. 해당 학교에 있는 모든 게시판 조회(/school/{school_id}/boards) O
        2. 학교 FK + 게시판 명(/school/{school_id}/board?name={게시판명}) O
    - 학교에 속한 수업 조회
        1. 해당 학교에 있는 모든 수업 조회 (/school/{school_id}/courses) O
        2. 학교 FK + 수업명 (/school/{school_id}/courses?name={수업명}) O
        3. 학교 FK + 교수명 (/school/{school_id}/courses?professorName={교수명}) O
        4. 학교 FK + 수업명 + 교수명 (/school/{school_id}/courses?name={수업명}&professorName={교수명}) O
- 수정 (/school/{school_id}) O
    1. 학교명

### 게시판(BoardController)

- 등록
    1. 게시물 등록(POST /board/{board_id}/post) O
- 조회
    - 단건 조회
        1. PK (/board/{board_id})
    - 게시판에 속한 게시물 조회
        1. 해당 게시판의 모든 게시물 조회  (/board/{board_id}/posts) O
        2. 게시물 등록일자로 조회 (/board/{board_id}/posts?date={xxxx-xx-xx}) O
        3. 게시물 명으로 조회 (/board/{board_id}/posts?title={게시물명}) O
- 수정 (/board/{board_id}) O
    1. 게시판 이름

### 게시물(PostController)

- 등록
    1. 좋아요 등록 (/post/{post_id}/postLike) O
    2. 해당 게시물에 댓글 달기 (/post/{post_id}/comment) O
    3. 해당 게시물에 첨부 파일 추가(/post/{post_id}/attachment) O
- 조회
    - 단건 조회
        1. PK (/post/{post_id})  O
    - 게시물에 속한 댓글 조회
        1. 해당 게시물에 속한 모든 댓글 조회 (/post/{post_id}/comments) O
    - 게시물에 속한 좋아요 조회
        1. 해당 게시물에 속한 좋아요 개수 조회(/post/{post_id}/postLike) O
    - 게시물에 속한 첨부파일 조회
        1. 해당 게시물에 속한 모든 첨부파일 조회(/post/{post_id}/attachments) O
- 수정 (/post/{post_id}) O
    1. 내용
    2. 질문 여부
    3. 익명 여부
- 제거
    1. 게시물 (/post/{post_id}) O

### 첨부파일(AttachmentController)

- 제거
    1. 첨부파일 (/attachment/{attachment_id}) O

### 댓글(CommentController

- 등록
    1. 대댓글 달기 (/comment/{comment_id}/replies)
- 조회
    - 다중 조회
        1. 대댓글 전체 조회 (/comment/{comment_id}/replies)
- 댓글 수정 (/comment/{comment_id})
- 댓글 제거 (/comment/{comment_id})

### 게시물 좋아요(PostLikeController)

- 제거 (/postLike/{plid})

### 수업(CourseController)

- 조회
    - 단건 조회
        1. PK (/course/{course_id})
- 제거 (/courses/{course_id})

### 유저(UserController)

- 등록
    1. 시간표 등록(/user/{user_id}/timeTables)
- 조회
    - 단건 조회
        - 구현 예정 ...
    - 다중 조회
        - 구현 예정 ...
    - 유저가 쓴 게시물 조회
        1. 유저가 쓴 게시물 전체 조회(/user/{user_id}/posts)
    - 유저가 속해 있는 채팅방 조회
        1. 유저가 속해 있는 채팅방 전체 조회 (/users/{user_id}/chattingRooms)
    - 유저가 쓴 댓글 조회
        1. 유저가 쓴 댓글 전체 조회 (/users/{user_id}/comments)
    - 유저의 시간표 조회
        1. 유저의 시간표 전체 조회 (/users/{user_id}/timeTables)
        2. 연도, 학기를 조건으로 유저의 시간표 전체 조회 (/users/{user_id}/timeTables?year={연도}&학기={semester})
        3. 연도, 학기, 시간표 명을 조건으로 유저의 시간표 전체 조회 (/users/{user_id}/timeTables?year={연도}&학기={semester}&name={시간표명})
- 제거

### 채팅방(ChattingRoomController)

- 등록
    1. 채팅방 등록(/chattingRooms) ({"participant1":fk1, "participant2":fk2...})
    2. 채팅 등록(/chattingRooms/{crid}/chats) ({"author":fk, "content":"내용..."})
- 조회
    - 단건 조회
        1. PK (/chattingRooms/{crid})
    - 채팅방에 속해 있는 채팅 전체 조회(/chattingRooms/{crid}/chats)
- 제거 (/chattingRooms/{crid})

### 채팅(ChatController)

- 채팅 제거(chats/{cid})

### 시간표(TimeTableController)

- 등록
    1. 시간표에 수업 추가(/timeTables/{tid}/courses)
- 조회
    - 단건 조회
        1. PK(/timeTables/{tid})
    - 시간표에 등록된 수업 조회
        1. 시간표상의 모든 수업 조회(/timeTables/{tid}/courses)

- 제거 (/timeTables/{tid})
    1. 시간표의 수업 제거(/timeTables/{tid}/courses/{cid})

## 컨트롤러 구현

### 1) BaseResponse

~~~java

@GetMapping("/{sid}")
public BaseResponse<ReadSchoolResponse> readSchool(@PathVariable("sid") Long schoolId) {
    try {
        School school = schoolService.findSchoolById(schoolId);
        ReadSchoolResponse readSchoolResponse = ReadSchoolResponse.from(school);
        return new BaseResponse<>(HttpStatus.OK, null, readSchoolResponse, 1);
    } catch (AppException e) {
        return new BaseResponse<>(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
    }
}
~~~

readSchool() 메서드는 "/api/schools/{sid}" url로 request가 들어온 경우 해당 sid(= PK)에 맞는 학교 정보를 찾아서 반환해주는 메서드이다.
이때 반환형을 보면 BaseResponse<ReadSchoolResponse>인 것을 확인할 수 있는데, 모든 컨트롤러 메서드에 BaseResponse 메서드를 wrapping하여서 반환 데이터가 규격화되도록
구현했다.

~~~java

@Getter
public class BaseResponse<T> {
    private HttpStatus httpStatus;
    private String message;
    private T value;
    private int count;

    public BaseResponse(HttpStatus httpStatus, String message, T value, int count) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.value = value;
        this.count = count;
    }
}
~~~

BaseResponse는 기본적으로 Http 상태, 상태 메시지, 반환값, 반환 값 개수를 response 반환시에 함께 반환하도록 구현된 wrapper 클래스이다.
따라서 어떠한 데이터를 반환할 경우, 반환 데이터의 형태는 다음과 같다.

~~~
{
  "httpStatus": "100 CONTINUE",
  "message": "string",
  "value": {},
  "count": 0
}
~~~

### 2) 정적 팩토리 메서드 적용

그동안 Dto에 데이터를 넣을때마다 별도의 생성자를 통해서 Dto를 생성하는 방식으로 진행했다.  
그러다 스터디를 통해서 정적팩토리 메서드를 통해서 데이터를 주입 받는 방식을 배우게 되어 적용해 보았다.

~~~java

@Data
@AllArgsConstructor
public class ReadSchoolResponse {
    private Long id;
    private String name;

    // 정적 팩토리 메서드
    public static ReadSchoolResponse from(School school) {
        return new ReadSchoolResponse(school.getId(), school.getName());
    }
}
~~~

from() 정적 팩토리 메서드를 통해서 번거롭게 dto에 값을 하나하나 넣지 않고 엔티티를 넣는다면 자동으로 반환값이 들어가도록 구현할 수 있었다.  
이를 통해서 훨씬 개발 생산성 뿐만이 아니라 반환값을 넣는 과정에서 발생할 수 있는 실수도 줄이는 장점을 볼 수 있었다.  
정적 팩토리 메서드 패턴은 굉장히 좋은 방식이라고 생각했다!

### 3) Global Exception

알고 보니 운이 좋게도 과제가 나오기 전에 이미 Global Exception을 구현해서 사용하고 있었다.  
나는 AppException이라는 별도의 예외 클래스를 생성하여 예외 발생시 상황에 맞는 HttpStatus와 메시지를 반환 할 수 있도록 구현하였다.

~~~java

@AllArgsConstructor
@Getter
public class AppException extends RuntimeException {  // global exception
    private ErrorCode errorCode;
    private String message;
}

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DATA_ALREADY_EXISTED(HttpStatus.CONFLICT, ""),
    NO_DATA_EXISTED(HttpStatus.NOT_FOUND, ""),
    NOT_NULL(HttpStatus.NO_CONTENT, ""),

    ID_DUPLICATED(HttpStatus.CONFLICT, ""),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, ""),

    NO_DATA_ALLOCATED(HttpStatus.FAILED_DEPENDENCY, ""),

    KEYWORD_TOO_SHORT(HttpStatus.BAD_REQUEST, ""),
    INVALID_VALUE_ASSIGNMENT(HttpStatus.BAD_REQUEST, ""),
    INVALID_URI_ACCESS(HttpStatus.NOT_FOUND, "");


    private final HttpStatus httpStatus;
    private final String message;
}
~~~

사용중에 잘못된 경우에 대한 예외처리를 하기 위해서 RunTimeException을 상속받도록 하였다.  
AppException은 ErrorCode를 반환하도록 하였는데, ErrorCode는 HttpStatus와 message로 구성이되는 방식을 택했다.   
하지만 상황에 따라서 별도의 메시지를 반환하도록 중간에 수정을 거쳐서 ErrorCode 내부의 메시지는
따로 사용하지 않고 일단은 비워놓았다. 하지만 나중에 사용될 경우를 염두해서 제거를 하진 않았다.
<br/>
<br/>
AppException의 사용 예시는 다음과 같다. 서비스 단에서 예외가 발생되도록 구현했다.

~~~java
public School findSchoolById(Long schoolId) {
    return schoolRepository.findById(schoolId)
            .orElseThrow(() -> {
                log.error("에러 내용: 학교 조회 실패 " +
                        "발생 원인: 존재하지 않는 PK 값으로 조회");
                return new AppException(NO_DATA_EXISTED, "존재하지 않는 학교입니다");
            });
}
~~~

만일 잘못된 PK로 조회를 하는 경우 ErrorCode중 NO_DATA_EXISTED라는 값과 "존재하지 않는 학교입니다"라는 메시지를 담은 예외가 발생한다.
<br/>
<br/>

~~~java

@GetMapping("/{sid}")
public BaseResponse<ReadSchoolResponse> readSchool(@PathVariable("sid") Long schoolId) {
    try {
        School school = schoolService.findSchoolById(schoolId);
        ReadSchoolResponse readSchoolResponse = ReadSchoolResponse.from(school);
        return new BaseResponse<>(HttpStatus.OK, null, readSchoolResponse, 1);
    } catch (AppException e) {
        return new BaseResponse<>(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);  // 여기서 예외 CATCH
    }
}
~~~

예외가 발생하는 경우, 컨트롤러에서 catch되어 BaseResponse에 해당 에러의 HttpStatus와 message가 담겨 반환된다.
<br/>
<br/>
![img_6](https://github.com/riceCakeSsamanKo/spring-everytime-19th/assets/121627245/23d53438-bcab-4a06-af2c-0634125623bd)  
포스트 맨으로 테스트해본 결과 정상적으로 작동됨을 알 수 있었다.

### 4) swagger 연동

이번에 처음으로 swagger를 접하였는데 굉장히 유용하였다. api에 대한 데이터 구조를 직관적으로 볼 수 있다는 점에서 굉장히 앞으로도 애용할 것 같다!
![img_7](https://github.com/riceCakeSsamanKo/spring-everytime-19th/assets/121627245/99772109-cd3f-430b-8bb8-b3a69b895fdb)
![img_8](https://github.com/riceCakeSsamanKo/spring-everytime-19th/assets/121627245/95f71023-671e-41de-9456-33f642935b0b)
json 데이터 구조 뿐만 아니라 쿼리 파라미터등도 쉽게 볼 수 있어 굉장히 유용하였다!

# 5주차

## 리팩토링 진행

1) **ResponseEntity 추가**</br>
   기존에는 와일드 카드를 이용한 BaseResponse 엔티티를 사용하여 반환을 진행했다. 이 과정에서 HttpStatus가 클라이언트에게 반환되지 않는다는 문제를 발견하여
   ResponseEntity를 통해서 HttpStatus를 명시해줬다.

   ![수정전](https://github.com/riceCakeSsamanKo/spring-everytime-19th/assets/121627245/ef19026b-38da-4fab-b0df-b2bf5bb85a8f)

   -수정전-</br>

   ![수정후](https://github.com/riceCakeSsamanKo/spring-everytime-19th/assets/121627245/47045cb9-ebe0-4071-a614-77f1a5b09520)

   -수정후-</br>
3) **Controller 리팩토링**</br>
   코드 리뷰를 통해 받은 피드백 중에서 Service단과 Controller 단의 분리가 부족하다는 리뷰가 있었다. 이번에 리팩토링을 하면서 로직들은 서비스 단에 모두 몰아놓도록 다시 구현했다.
   추가적으로 나중에는 Service에서 바로 DTO를 반환하도록 구현하는 것도 고민해봐야겠다.</br>
   이렇게 구현하면 예외 발생에 따른 dto 생성도 모두 서비스 내부에서 처리하고 컨트롤러는 정말 메서드 호출만 하면 되어서 더욱 분리가 확실해질 것 같다.
4) **Service 리팩토링**</br>
   lambda 메서드를 적극 사용하여 코드를 좀 더 보기 좋도록 리팩토링 했다. 
