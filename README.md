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
    1. 학교 등록(/school)
    2. 게시판 등록(/school/{school_id}/board)
    3. 수업 등록(/school/{school_id}/course)
- 조회
    - 단건 조회
        1. PK (/school/{school_id})
        2. 학교 명 (/school?name={학교명})
    - 다중 조회
        1. 모든 학교 조회 (/schools)
    - 학교에 속한 게시판 조회
        1. 해당 학교에 있는 모든 게시판 조회(/school/{school_id}/boards)
        2. 학교 FK + 게시판 명(/school/{school_id}/board?name={게시판명})
    - 학교에 속한 수업 조회
        1. 해당 학교에 있는 모든 수업 조회 (/school/{school_id}/courses)
        2. 학교 FK + 수업명 (/school/{school_id}/courses?name={수업명})
        3. 학교 FK + 교수명 (/school/{school_id}/courses?professorName={교수명})
        4. 학교 FK + 수업명 + 교수명 (/school/{school_id}/courses?name={수업명}&professorName={교수명})
- 수정 (/school/{school_id})
    1. 학교명

### 게시판(BoardController)

- 등록
    1. 게시물 등록(POST /board/{board_id}/post)
- 조회
    - 단건 조회
        1. PK (/board/{board_id})
    - 게시판에 속한 게시물 조회
        1. 해당 게시판의 모든 게시물 조회  (/board/{board_id}/posts)
        2. 게시물 등록일자로 조회 (/board/{board_id}/posts?date={xxxx-xx-xx})
        3. 게시물 명으로 조회 (/board/{board_id}/posts?title={게시물명})
- 수정 (/board/{board_id})
    1. 게시판 이름

### 게시물(PostController)

- 등록
    1. 좋아요 등록 (/post/{post_id}/postLike)
    2. 해당 게시물에 댓글 달기 (/post/{post_id}/comment)
    3. 해당 게시물에 첨부 파일 추가(/post/{post_id}/attachment)
- 조회
    - 단건 조회
        1. PK (/post/{post_id})
    - 게시물에 속한 댓글 조회
        1. 해당 게시물에 속한 모든 댓글 조회 (/post/{post_id}/comments)
    - 게시물에 속한 좋아요 조회
        1. 해당 게시물에 속한 좋아요 개수 조회(/post/{post_id}/postLike)
    - 게시물에 속한 첨부파일 조회
        1. 해당 게시물에 속한 모든 첨부파일 조회(/post/{post_id}/attachments)
- 수정 (/post/{post_id})
    1. 내용
    2. 질문 여부
    3. 익명 여부
- 제거
    1. 게시물 (/post/{post_id})

### 첨부파일(AttachmentController)

- 제거
    1. 첨부파일 (/attachment/{attachment_id})

### 댓글(CommentController)

- 등록
    1. 대댓글 달기 (/comment/{comment_id}/reply)
- 조회
    - 다중 조회
        1. 대댓글 전체 조회 (/comment/{comment_id}/replies)
- 댓글 수정 (/comment/{comment_id})
    1. 콘텐츠 수정
- 댓글 제거 (/comment/{comment_id})

### 게시물 좋아요(PostLikeController)

- 제거 (/postLike/{postlike_id})

### 수업(CourseController)

- 조회
    - 단건 조회
        1. PK (/course/{course_id})
- 제거 (/course/{course_id})

### 유저(UserController)

- 등록
    1. 회원가입(/join)
    2. 시간표 등록(/user/{user_id}/timeTable)

- 조회
    - 단건 조회
        1. PK(/user/{user_id})
        2. email(/user?email={이메일})
        3. 학번(/user?school_id={학교PK}&studentNo={학번})
    - 다중 조회
        1. 이름(/users?name={이름})
    - 유저가 쓴 게시물 조회
        1. 유저가 쓴 게시물 전체 조회(/user/{user_id}/posts)
    - 유저가 속해 있는 채팅방 조회
        1. 유저가 속해 있는 채팅방 전체 조회 (/user/{user_id}/chattingRooms)
    - 유저가 쓴 댓글 조회
        1. 유저가 쓴 댓글 전체 조회 (/user/{user_id}/comments)
    - 유저의 시간표 조회
        - 단건 조회
            1. 연도, 학기를 조건으로 유저의 시간표 전체 조회 (/user/{user_id}/timeTable?year={연도}&학기={semester})
        - 다중 조회
            1. 유저의 시간표 전체 조회 (/user/{user_id}/timeTables)
            2. 연도, 학기, 시간표 명을 조건으로 유저의 시간표 전체 조회 (/user/{user_id}/timeTables?year={연도}&학기={semester}&name={시간표명})
- 제거
  1. 유저 제거 (/user/{user_id})

### 채팅방(ChattingRoomController)

- 등록
    1. 채팅방 등록(/chattingRoom)
    2. 채팅 등록(/chattingRoom/{chattingRoom_id}/chat)
- 조회
    - 단건 조회
        1. PK (/chattingRoom/{chattingRoom_id})
    - 다중 조회
        1. 채팅방에 속해 있는 채팅 전체 조회(/chattingRoom/{chattingRoom_id}/chats)
        2. 채팅방에 속해 있는 채팅 날짜로 조회(/chattingRoom/{chattingRoom_id}/chats?send_date={작성 일자})
- 제거 (/chattingRoom/{chattingRoom_id})

### 채팅(ChatController)

- 조회
    1. PK(/chat/{chat_id})
- 제거(/chat/{cid})

### 시간표(TimeTableController)

- 조회
    - 단건 조회
        1. PK(/timeTable/{timeTable_id})
    - 시간표에 등록된 수업 조회
        1. 시간표 상의 모든 수업 조회(/timeTable/{timeTable_id}/courses)

- 제거 
  1. 시간표 제거((/timeTable/{timeTable_id})
  2. 시간표에서 수업 제거(/timeTable/{timeTable_id}/course/{course_id})

- 수정
    1. 시간표에 수업 추가(/timeTable/{timeTable_id}/course)

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
    - 기존에는 와일드 카드를 이용한 BaseResponse 객체를 만들어 반환을 진행했다. 이 과정에서 HttpStatus가 클라이언트에게 반환되지 않는다는 문제를 발견하여
      ResponseEntity를 통해서 HttpStatus를 명시해줬다.

#### ***- 수정전***

~~~java

@GetMapping("/school/{school_id}")
public BaseResponse<ReadSchoolResponse> readSchool(@PathVariable("school_id") Long schoolId) {
    try {
        School school = schoolService.findSchoolById(schoolId);
        ReadSchoolResponse value = ReadSchoolResponse.from(school);

        return new BaseResponse<>(HttpStatus.OK, null, value, 1);
    } catch (AppException e) {
        return BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
    }
}
~~~

![수정전](https://github.com/riceCakeSsamanKo/spring-everytime-19th/assets/121627245/ef19026b-38da-4fab-b0df-b2bf5bb85a8f)
</br></br>

#### ***- 수정후***

~~~java

@GetMapping("/school/{school_id}")
public ResponseEntity<BaseResponse<ReadSchoolResponse>> readSchool(@PathVariable("school_id") Long schoolId) {
    try {
        School school = schoolService.findSchoolById(schoolId);
        ReadSchoolResponse value = ReadSchoolResponse.from(school);

        return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, null, value, 1));
    } catch (AppException e) {
        BaseResponse response =
                new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
        return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
    }
}
~~~

![수정후](https://github.com/riceCakeSsamanKo/spring-everytime-19th/assets/121627245/47045cb9-ebe0-4071-a614-77f1a5b09520)
<br></br>

2) **Controller 리팩토링**</br>
    - 코드 리뷰를 통해 받은 피드백 중에서 Service단과 Controller 단의 분리가 부족하다는 리뷰가 있었다. 이번에 리팩토링을 하면서 로직들은 서비스 단에 모두 몰아놓도록 다시 구현했다.
      추가적으로 나중에는 Service에서 바로 DTO를 반환하도록 구현하는 것도 고민해봐야겠다.</br>
      이렇게 구현하면 예외 발생에 따른 dto 생성도 모두 서비스 내부에서 처리하고 컨트롤러는 정말 메서드 호출만 하면 되어서 더욱 분리가 확실해질 것 같다. </br>
    - rest api도 재설계하였다. 피드백을 보니 기존의 url에서 단건/다중 조회시에 구분이 명확하지 않다는 피드백이 있었다. 예를들어, 기존에 PK로 학교를 조회하는 url은
      /schools/{school_id}이었고, 모든 학교를 조회하는 api는 /schools이었다.
      처음 생각하기에는 여러개의 학교들 중에서 하나의 학교를 school_id를 통해서 조회하는 것이니 /schools/{school_id}로 url을 설계하면 되겠다라고 생각했었다. 그러다 피드백을 받고 다시
      생각을 해보니, 하나의 학교만을 조회하는 것이니 s를 빼는게 맞겠다는 생각이 들어
      /school/{school_id}로 다시 리팩토링을 진행했다. 유사한 방식으로 설계했었던 다른 url들도 이와 같은 방식으로 수정했다.<br></br>
3) **Service 리팩토링**</br>
    - lambda 메서드를 사용하여 코드를 좀 더 보기 좋도록 리팩토링 했다.<br></br>
4) **global exception handler 구현**</br>

~~~java

@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)  // @Valid에서 에러가 발생한 경우 여기에서 처리
    public ResponseEntity<BaseResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors()
                .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));

        return ResponseEntity.badRequest()
                .body(new BaseResponse<>(HttpStatus.BAD_REQUEST, "invalid request parameter", errors, 0));
    }


    // ConstraintViolationException 처리
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponse<Object>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation ->
                errors.put(violation.getPropertyPath().toString(), violation.getMessage()));

        for (ConstraintViolation set : ex.getConstraintViolations()) {
            System.out.println("set.getMessage() = " + set.getMessage());
            System.out.println("set.getInvalidValue() = " + set.getInvalidValue());
        }

        BaseResponse<Object> response = new BaseResponse<>(HttpStatus.BAD_REQUEST, "validation failure", errors, 0);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 기타 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Object>> handleException(Exception ex) {
        BaseResponse<Object> response = new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "server error", null, 0);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
~~~

전역 예외를 처리하기 위해서 ApiControllerAdvice라는 global exception handler를 구현했다.
위의 두개의 handler에서 처리하지 못한 예외는 맨 아래의 exception handler에서 Exception을 처리하도록 해 전체적으로 처리하도록 구현했다.
## JWT
JWT는 Json Web Token의 약자로 *{Header}.{Payload}.{Signature}* 세가지 부분으로 구성된다.
각 부분을 예시로 들면 다음과 같다.

### Header
~~~json
base64enc({
  "alg": "HS256",
  "typ": "JWT"
})
~~~

### Payload
~~~json
base64enc({
  "name": "철수",
  "age": "25",
  "username": "chulsu@naver.com",
  "role": "ROLE_USER"
})
~~~

### Signature
~~~json
HMACSHA256(
  base64enc(header) + "." +
  base64enc(payload),
  {secret key}
)
~~~
Header와 Payload는 데이터를 주고 받기 편하게 base64 포맷으로 인코딩된다.
유저의 정보는 Payload에 담기며, 이때 Header와 Payload는 별도의 암호화를 거치지 않기 때문에 민감한 정보는 담아선 안된다. 
Signature에는 header와 payload를 더한 값을 비밀키를 통해서 암호화한다. 이렇게 암호화된 값은 유저 Payload 데이터의 변조 여부를 확인하는 무결성 체크에 사용된다.

전통적인 유저 인증 방식은 client와 server 그리고 database로 구성이된다. client에서 server로 인증 요청을 보내면 server는 DB에서 데이터를 조회하여 검증을 하는 방식이다.
전통적인 방식에서 유저의 정보는 모두 DB에서만 저장한다.

JWT는 DB를 유저 개인에게 전가한 것과 유사하다. JWT를 통해 client는 본인의 정보를 가지며, 이를 읽을 수 있다. 여기서 중요한 점은 client는 본인의 정보를 읽을 수만 있고, 
스스로 수정을 할 수는 없다는 것이다. 수정을 하기 위해서는 서버를 거쳐야한다. 

## Spring Security

### 필터 구조

![image](https://github.com/riceCakeSsamanKo/spring-everytime-19th/assets/121627245/72225923-1496-484b-8398-d257b3f61907)

**- HttpSecurity**</br>
SeurityFilterChain을 등록하는 과정에서 사용.
spring security의 각종 설정은 HttpSecurity로 한다.

- 리소스(URL) 접근 권한 설정
- 인증 전체 흐름에 필요한 Login, Logout 페이지 인증완료 후 페이지 인증 실패시 이동페이지 설정
- 인증 로직을 커스텀하기 위한 필터 설정
- 기타 csrf, 강제 https 호출 등등 거의 모든 스프링시큐리티 설정
- 리소스(URL)의 권한 설정
- 특정 리소스의 접근 허용 또는 특정 권한을 가진 사용자만 접근을 가능하게 할 수 있음

**- filter 추가 메서드**

- HttpSecurity addFilterBefore(Filter filter, Class<? extends Filter> beforeFilter): beforeFilter 앞에 filter를 추가
- HttpSecurity addFilterAt(Filter filter, Class<? extends Filter> atFilter): atFilter 자리에 filter를 추가
- HttpSecurity addFilterAfter(Filter filter, Class<? extends Filter> afterFilter): afterFilter 뒤에 filter를 추가

### 회원가입

~~~java

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    // 회원가입 로직
    public User join(JoinUserRequest request) {
        // 중복 검사
        userRepository.findByUsername(request.getUsername())
                .ifPresent(f -> {
                    log.error("에러 내용: 유저 가입 실패 " +
                            "발생 원인: 이미 존재하는 아이디로 가입 시도");
                    throw new AppException(DATA_ALREADY_EXISTED, "이미 존재하는 아이디입니다");
                });
        userRepository.findByEmail(request.getEmail())
                .ifPresent(f -> {
                    log.error("에러 내용: 유저 가입 실패 " +
                            "발생 원인: 이미 존재하는 이메일로 가입 시도");
                    throw new AppException(DATA_ALREADY_EXISTED, "이미 사용중인 이메일입니다");
                });
        userRepository.findBySchoolIdAndStudentNo(request.getSchoolId(), request.getStudentNo())
                .ifPresent(f -> {
                    log.error("에러 내용: 유저 가입 실패 " +
                            "발생 원인: 이미 존재하는 학번으로 가입 시도");
                    throw new AppException(DATA_ALREADY_EXISTED, "이미 사용중인 학번입니다");
                });


        School school = schoolRepository.findById(request.getSchoolId()).orElseThrow(() -> {
            log.error("에러 내용: 유저 가입 실패 " +
                    "발생 원인: 존재하지 않는 School PK로 조회");
            return new AppException(DATA_ALREADY_EXISTED, "존재하지 않은 학교입니다");
        });

        User user = User.builder()
                .username(request.getUsername())
                .password(encoder.encode(request.getPassword()))
                .name(request.getName())
                .studentNo(request.getStudentNo())
                .email(request.getEmail())
                .role(request.getRole())
                .school(school)
                .build();
        userRepository.save(user);

        return user;
    }
}
~~~

회원가입을 위한 join 메서드를 userService에 구현하였다. 먼저 중복 검사 및 학교 정보를 검사해서 잘못된 경우 exception을 발생시키고, 정상적인 경우 유저 정보를 DB에 저장한다.</br>
이때 중요한 것이 유저의 비밀번호를 암호화 한 후 DB에 저장하는 것이다. 만일 암호화가 되지 않은 상태로 DB에 저장하면
혹시라도 DB가 털렸을 때 유저의 비밀번호가 모두 그대로 유출되기 때문에 반드시 암호화를 한 후 DB에 저장해야한다.</br>
암호화를 하기 위해서 BCryptPasswordEncoder를 먼저 스프링 빈으로 등록한 후 사용하였다. 이때 spring security 관련 빈들은 Configuration을 위한 SecurityConfig
클래스에 선언하여 관리하도록 하였다.

~~~java

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean  // passwordEncoder 빈 등록
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
    ...
}
~~~

</br>SecurityConfig에 @EnableWebSecurity 어노테이션을 적용하여 web security 관련 기능을 활성화 시킬 수 있다.

~~~java
/**
 * Add this annotation to an {@code @Configuration} class to have the Spring Security
 * configuration defined in any {@link WebSecurityConfigurer} or more likely by exposing a
 * {@link SecurityFilterChain} bean:
 *
 * <pre class="code">
 * &#064;Configuration
 * &#064;EnableWebSecurity
 * public class MyWebSecurityConfiguration {
 *
 * 	&#064;Bean
 * 	public WebSecurityCustomizer webSecurityCustomizer() {
 * 		return (web) -> web.ignoring()
 * 		// Spring Security should completely ignore URLs starting with /resources/
 * 				.requestMatchers(&quot;/resources/**&quot;);
 *    }
 *
 * 	&#064;Bean
 * 	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
 * 		http.authorizeHttpRequests().requestMatchers(&quot;/public/**&quot;).permitAll().anyRequest()
 * 				.hasRole(&quot;USER&quot;).and()
 * 				// Possibly more configuration ...
 * 				.formLogin() // enable form based log in
 * 				// set permitAll for all URLs associated with Form Login
 * 				.permitAll();
 * 		return http.build();
 *    }
 *
 * 	&#064;Bean
 * 	public UserDetailsService userDetailsService() {
 * 		UserDetails user = User.withDefaultPasswordEncoder()
 * 			.username(&quot;user&quot;)
 * 			.password(&quot;password&quot;)
 * 			.roles(&quot;USER&quot;)
 * 			.build();
 * 		UserDetails admin = User.withDefaultPasswordEncoder()
 * 			.username(&quot;admin&quot;)
 * 			.password(&quot;password&quot;)
 * 			.roles(&quot;ADMIN&quot;, &quot;USER&quot;)
 * 			.build();
 * 		return new InMemoryUserDetailsManager(user, admin);
 *    }
 *
 * 	// Possibly more bean methods ...
 * }
 * </pre>
 *
 * @see WebSecurityConfigurer
 * @author Rob Winch
 * @since 3.2
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({WebSecurityConfiguration.class, SpringWebMvcImportSelector.class, OAuth2ImportSelector.class,
        HttpSecurityConfiguration.class})
@EnableGlobalAuthentication
public @interface EnableWebSecurity {

    /**
     * Controls debugging support for Spring Security. Default is false.
     * @return if true, enables debug support with Spring Security
     */
    boolean debug() default false;

}
~~~

@EnableWebSecurity는 환경 설정에 많이 사용되므로 보통 @Configuration과 함께 사용되며 WebSecurityConfiguration.class,
SpringWebMvcImportSelector.class, OAuth2ImportSelector.class,
HttpSecurityConfiguration.class등을 import 해주어 security 관련 기능을 제공한다.

### 로그인(액세스 토큰 발급 및 검증 로직)

~~~java

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;

    @Bean  //AuthenticationManager Bean 등록
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean  // passwordEncoder 빈 등록
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean  // 필터 등록
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //csrf disable
        http.csrf(AbstractHttpConfigurer::disable);  // jwt는 session을 stateless하게 유지하므로 csrf에 대한 방어가 필요하지 않다

        //From 로그인 방식 disable -> UsernamePasswordAuthenticationFilter 커스텀 구현 필요
        http.formLogin(AbstractHttpConfigurer::disable);

        //http basic 인증 방식 disable
        http.httpBasic(AbstractHttpConfigurer::disable);

        //경로별 인가 작업
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**")
                .permitAll()// swagger 경로 접근 허용
                .requestMatchers("/login", "/", "/api/join").permitAll()  // root 경로 접근 허용
                .requestMatchers("/admin").hasRole("ADMIN")  // ADMIN만 접근 허용
                .anyRequest().authenticated() // 이외의 경로는 로그인한 사용자만 접근 허용
        );

        // Form로그인 disable로 인해 기존에 설정 되었던 UsernamePasswordAuthenticationFilter가 사용되지 않으므로
        // 새로이 생성한 커스텀 필터(LoginFilter)를 해당 필터 자리에 대신 해서 넣어줌
        // ~/login에 대한 post 요청은 여기에서 처리
        http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        //세션 stateless 설정
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
~~~

session 로그인 방식에서는 UsernamePasswordAuthenticationFilter에서 로그인 처리를 진행하지만, 이번에는 jwt 방식을 사용하기 위해서 form 로그인 방식을 disable 시켰기
때문에 더 이상 UsernamePasswordAuthenticationFilter는 동작하지 않는다.
그렇기에 jwt를 이용한 로그인 기능을 위해 별도의 LoginFilter를 선언한 후 addFilterAt() 메서드를 이용해 UsernamePasswordAuthenticationFilter 자리에 넣어주었다.

이제 로그인 기능은 LoginFilter가 담당하게 되었다. 따라서 **컨트롤러에서 별도로 로그인 api를 만들지 않고 필터를 통해서 로그인을 진행할 수 있게된다 (/login에 post요청으로 로그인 가능)**.
![image](https://github.com/riceCakeSsamanKo/spring-everytime-19th/assets/121627245/96ded288-45f0-4aa1-9735-0ad40219b7c2)

### 로그인 과정

![img](https://github.com/riceCakeSsamanKo/spring-everytime-19th/assets/121627245/08205881-a265-4634-b29e-adde08f5a874) </br>

~~~java
package com.ceos19.everytime.jwt;

import com.ceos19.everytime.dto.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // request로 부터 username, password 가져오기
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        // DB에서 user 정보를 가져와서 authToken에 대한 검증 진행
        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시 실행하는 메소드 (JWT 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        // 타입 캐스팅을 통해서 UserDetails를 상속받은 CustomUserDetails로 타입 변경
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        // 로그인 성공 알림 log
        log.info("authentication success\n - username: {}\n - time: {}", username, LocalDateTime.now());


        // 사용자의 Role 정보
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        // access token 만료 시간
        long expiredMs = 60 * 60 * 10L;
        String token = jwtUtil.createToken(username, role, expiredMs);

        // header에 토큰 담아서 반환. RFC 7235에서 정의 돼 있듯이, 접두사 Bearer를 붙여서 Authorization 헤더를 반환한다.
        response.addHeader("Authorization", "Bearer " + token);
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        String username = obtainUsername(request);
        // 로그인 실패 알림 log
        log.info("authentication fail\n - username: {}\n - time: {}", username, LocalDateTime.now());

        // header에 인증 실패 정보 담아서 반환
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
~~~

LoginFilter에서 로그인 검증을 진행한다.</br>
requestMatchers의 <strong>/login</strong> url로 username, password를 담은 request가 들어오면 이를 Authentication 객체로 만든다.
Authentication 객체는 LoginFilter의 <strong>Authentication attemptAuthentication(HttpServletRequest request,
HttpServletResponse response){}</strong>로 전해지고, 해당 메서드에서 메서드에서는 로그인 정보에 대한 검증을 진행한다.
AuthenticationManager는 UsernamePasswordAuthenticationToken에 대한 검증을 진행한 후 검증이 성공적이라면 Authentication 객체를 발급한다.</br>
인증이 성공적인 경우 발급된 객체는 LoginFilter의 <strong>void successfulAuthentication(HttpServletRequest request, HttpServletResponse
response, FilterChain chain, Authentication authentication){}</strong>로 들어가고 해당 메서드에서는 인증이 성공했을 경우의 로직을 정의해서 실행하면 된다. 보통
jwt 토큰을 발급한다.</br>
인증이 실패한 경우에는 <strong>void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
AuthenticationException failed) {}</strong>에서 실패한 경우에 대한 로직을 정의해서 실행한다.

### Jwt 검증

~~~java

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {  // JWT 검증 필터
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // request에서 Authorization Header를 찾음.
        String authorization = request.getHeader("Authorization");

        // 인증이 실패하는 경우
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("authorization header not present");

            // 연결된 다음 필터에 request, response를 넘겨줌
            filterChain.doFilter(request, response);

            // 메서드 종료
            return;
        }

        // 인증 시작
        System.out.println("authorization now");
        String token = authorization.substring("Bearer ".length());  // "Bearer " 제거

        // token이 만료된 경우
        if (jwtUtil.isExpired(token)) {
            System.out.println("token is expired");
            filterChain.doFilter(request, response);

            return;
        }

        // token에서 username과 role을 가져옴
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        User user = User.createTempUser(username, "tempPassword", role);

        // UserDetails에 유저 정보 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

    }
}
~~~

### token secret key 생성

jwt를 사용하기 위해서는 token encoding을 위한 secret key가 필요하다. 나는 openssl을 사용해서 랜덤 키를 생성하여 사용하였다.
랜덤키를 생성하는 방법은 여러가지가 있지만 shell에서 다음 명령어를 이용한다면 쉽게 생성할 수 있다. (128바이트 문자열 생성)

~~~shell
openssl rand -hex 64
~~~
# 6주차
1. 리프레시 토큰 구현 + Refresh Rotate
2. 로그아웃 기능 구현
3. 도커파일로 이미지 생성후 docker compose를 사용하여 컨테이너 빌드
4. 도커 네트워크(정리 못함)

## 로그아웃
로그아웃을 도입함으로써 Access token의 유효시간을 줄이는 효과를 가져와 토큰 탈취로 인한 해킹의 위험성을 줄일 수 있다. 또한 더 이상 로그인을 유지하지 않으므로 /logout으로 리퀘스트가 들어올 때 refresh token도 DB에서 삭제되도록 구현하였다.

~~~java
@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final CookieUtil cookieUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        //path and method verify
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/logout$")) {

            filterChain.doFilter(request, response);
            return;
        }
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {

            filterChain.doFilter(request, response);
            return;
        }

        //get refresh token
        //refresh null check
        String refresh = null;
        try {
            refresh = cookieUtil.getRefreshToken(request.getCookies());
        } catch (AppException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            // 이미 로그아웃된 상태
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {

            //response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // DB에 저장되어 있는지 확인
        try{
            refreshTokenService.checkRefreshTokenIsSavedByRefresh(refresh);
            // 로그아웃 진행
            // Refresh 토큰 DB에서 제거
            refreshTokenService.deleteRefreshToken(refresh);
        } catch (AppException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }


        // Refresh 토큰 Cookie 값 0
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
~~~

## 도커 명령어 정리
 *내가 생각하기에 필수적인 명령어들을 정리해보았다.*
- docker run [옵션] [이미지명]: local repository(없다면 docker hub)에서 이미지를 불러와 컨테이너를 실행
  - 옵션	설명	예시
~~~shell
    옵션       설명                                                예시 
    -i	      상호입출력                                            -it
    -t	      tty를 활성화해서 shell을 사용하도록 컨테이너를 설정	 
    -d	      detached mode. 컨테이너를 백그라운드에서 실행	 
    -p	      호스트와 컨테이너의 포트를 포트포워딩.                      -p8000:80
    -v	      볼륨과 컨테이너 디렉토리를 마운트                          -v volume
    --name    컨테이너 이름을 명시                                    --name my_container
    -rm	      프로세스 종료시 컨테이너 자동 제거	 
    -link     컨테이너 연결. ip가 아닌 컨테이너 이름 기반 통신 가능	 
    --network 브릿지 네트워크에 연결                                  --network my_network
~~~
- docker images: local repository에 저장된 이미지 목록을 보여준다.
- docker ps: 현재 실행 중인 컨테이너를 보여줌
- docker ps -a: 존재하는 모든 컨테이너를 보여줌
- docker exec -it [컨테이너 id] [CMD]: CMD를 실행하여 컨테이너 내부로 진입 
- docker logs [컨테이너 id]: log 확인
- docker rm [컨테이너 id]: 컨테이너 제거
- docker rmi [이미지 id]: 이미지 제거
- docker commit [컨테이너 ID] dockerhub계정명/이미지 이름: commit하여 컨테이너를 이미지로 만듦
- docker push [커밋한 이미지 이름] [docker hub 계정명]/[이미지 이름](:[tag]): 도커 허브에 내가 만든 이미지를 push함. tag는 생략가능
- docker inspect [이미지|컨테이너|볼륨|네트워크]: 정보 확인 가능

## 도커 볼륨
볼륨을 사용하여 컨테이너의 특정 디렉토리를 호스트의 특정 디렉토리와 마운트 할 수 있다. 이로인해 컨테이저가 삭제되거나 재 실행되어도 데이터를 잃어버리지 않고 유지할 수 있다.
- docker volume create [볼륨명]: 볼륨을 생성함. 이를 named volume이라고 함.
- docker volume rm [볼륨명]: 볼륨 제거

### 컨테이너에 볼륨 마운트 하기

#### Bind mount volume
- docker run -v [로컬 디렉토리 경로]:[마운트할 컨테이너 디렉토리 경로] [옵션] [이미지]

내가 지정한 로컬 디렉토리의 경로에 컨테이너의 폴더가 마운트된다. 이 경우 컨테이너가 제거 되어도 볼륨은 제거되지 않는다.

#### Anonymous Volume
- docker run -v [마운트할 컨테이너 디렉토리 경로] [옵션] [이미지]

Bind mount volume과는 달리 로컬 경로를 명시하지 않은 경우 익명 볼륨으로 지정된다. 익명 볼륨으로 지정된 경우 사용자는 컨테이너 데이터가 호스트의 어느 경로에 저장되는지 알 수 없고, 컨테이너가 삭제되는 경우 익명볼륨도 함께 제거된다.

#### Named Volume
- docker -v [볼륨명]:[마운트할 컨테이너 디렉토리 경로] [옵션] [이미지]

 미리 생성해둔 볼륨에 컨테이너를 마운트하는 경우 named volume을 사용한다. 이 경우도 bind mount volume과 마찬가지로 컨테이너가 제거되더라도 볼륨은 제거되지 않아 컨테이너와 상관없이 데이터를 유지할 수 있다.

## 도커 파일
Dockerfile은 기존 일일히 CLI 명령어를 통해서 도커를 이미지를 빌드시켜야 하던 것을 하나의 파일에 정의해두어 편리하게 실행토록 하는 기능이다. 

~~~dockerfile
# Dockerfile
FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
~~~
에브리타임 서버 이미지를 만들기 위해서 jar 파일을 빌드한 후 위의 도커 파일로 이미지를 생성하였다. 
또한 내가 만든 에브리타임 서버는 mysql 데이터 베이스와 연동이 필요하기 때문에, mysql 이미지도 docker file을 통해서 정의하였다.
~~~dockerfile
# Dockerfile
FROM mysql:8.0

COPY init.sql /docker-entrypoint-initdb.d

ENV MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD}
ENV MYSQL_DATABASE=ceos_everytime_db
ENV MYSQL_HOST=%

CMD ["--character-set-server=utf8mb4","--collation-server=utf8mb4_unicode_ci"]
~~~

두개의 이미지를 모두 dockerfile로 정의해두었기 때문에 docker build 명령어로 이미지를 빌드할 수 있다.

- docker build -t [생성할 이미지 이름:tag] [도커 파일 경로]: 도커 파일로 이미지 빌드

나의 경우 compose.yml을 사용하였기 때문에 별도로 docker build를 실행하진 않았다.

## Docker Compose
도커 컴포즈를 이용하여 동시에 여러 이미지를 run 하여 컨테이너로 실행할 수 있다. 
~~~yml
version: '3'
services: 
  db:  # db 컨테이너
    build:
      context: ./docker-test-db
      dockerfile: Dockerfile  # ./docker-test-db 폴더의 Dockerfile로 이미지 빌드
    ports:
      - 3306:3306  # 포트포워딩 3306:3306
    volumes:
      - ./docker-test-db/store:/var/lib/mysql  # volume 마운트로 db 컨테이너가 삭제되어도 로컬에 데이터가 존재하도록 설정
    networks:
      - network
  
  server: # server 컨테이너
    build:
      context: ./spring-everytime-19th
      dockerfile: Dockerfile   # ./spring-everytime-19th 폴더의 Dockerfile로 이미지 빌드
    restart: always  # 서버는 꺼지면 다시 재시작
    ports:
      - 8080:8080  # 호스트의 8080 포트와 컨테이너의 8080 포트 포워딩
    depends_on: # db 컨테이너가 실행된 이후에 실행됨
      - db
    environment:  # 환경변수 설정
      SPRING_DATASOURCE_URL: jdbc:mysql://db:3306/ceos_everytime_db?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=Asia/Seoul&characterEncoding=UTF-8
      SPRING_DATASOURCE_USERNAME: ${SPRING_DATASOURCE_USERNAME}
      SPRING_DATASOURCE_PASSWORD: ${SPRING_DATASOURCE_PASSWORD}
      JWT_TOKEN_SECRET: ${JWT_TOKEN_SECRET}
    networks:
      - network

networks:  # 도커 네트워크 생성. db와 server를 동일한 네트워크로 묶어 통신 가능케 함
  network:
~~~

compose 파일을 통해서 db 컨테이너가 먼저 build된 이후에 server 컨테이너가 실행되도록 설정하였다(depends_on)

모든 설정 파일 (dockerfile, compose.yml)들이 준비 되었으니 docker-compose 명령어로 이미지를 빌드 후 컨테이너를 실행한다.
- docker-compose up -d --build: 컴포즈 yml 파일로 이미지 빌드 및 컨테이너 실행

*컴포즈 파일을 통해서 컨테이너가 정상적으로 실행되는 모습*
![image](https://github.com/riceCakeSsamanKo/spring-everytime-19th/assets/121627245/794cea15-93da-485f-8507-2ffd78fff305)
포스트 맨을 통해서 서버가 제대로 동작하는지 확인해 보았다.

![image](https://github.com/riceCakeSsamanKo/spring-everytime-19th/assets/121627245/90e46431-042c-4d92-a43c-efcd461e8899)
localhost:8080/login 경로로 로그인 post 요청을 날렸더니 정상적으로 로그인이 되는 모습이다.

### 실습 과정에서 생겼던 문제
![3306 포트 문제](https://github.com/riceCakeSsamanKo/spring-everytime-19th/assets/121627245/ea847ce6-284f-4b03-bd54-5a90b302610c)
도커 컴포즈를 실행하는 과정에서 생겼던 문제로 컴포즈 빌드를 하는 중에 이미 3306 포트가 사용중이라는 문제가 발생했다. 

![mysql stop](https://github.com/riceCakeSsamanKo/spring-everytime-19th/assets/121627245/7b695344-f652-4c25-a4c4-caa56ceb788a)
이는 로컬 컴퓨터에서 mysql db를 돌리고 있던 와중에 컴포즈로 빌드되는 mysql 컨테이너도 3306 포트를 사용하기 때문에 충돌이 일어난 것인데, 로컬 머신의 mysql을 종료함으로써 문제를 해결했다.
# 8주차
docker-compose를 이용하여 EC2에 배포를 진행하였다.
스프링부트 이미지를 빌드하여 docker hub에 push를 진행하였다.

![인바운드](https://github.com/riceCakeSsamanKo/spring-everytime-19th/assets/121627245/2c98553a-9eee-4b9f-92a2-93ca77f2a828)
[amd64빌드.jpg](..%2F..%2F..%2FDesktop%2Fceos-8%EC%A3%BC%EC%B0%A8%EA%B3%BC%EC%A0%9C%2Famd64%EB%B9%8C%EB%93%9C.jpg)
ec2의 8080포트로 웹서버에 접속할 것이므로 8080포트의 인바운드 규칙을 지정해주었다.

![amd64빌드](https://github.com/riceCakeSsamanKo/spring-everytime-19th/assets/121627245/78179d72-186c-4284-bcbe-afeecb1edff8)
위의 이미지를 보면 --platform 옵션을 통해서 아키텍처를 amd64로 지정한 것을 확인할 수 있다. 이유를 설명해보자면 이미지를 빌드한 컴퓨터가 m1 맥북인데, ec2 인스턴스의 경우 ubuntu 운영체제를 사용한다. 

m1 같은 경우 arm64/v8 아키텍처를 ubuntu는 amd64 아키텍처를 사용하기에 플랫폼을 정해주지 않고 이미지를 사용하게 되면 openjdk를 빌드할 때 ,오류가 발생하게 된다.
![image](https://github.com/riceCakeSsamanKo/spring-everytime-19th/assets/121627245/75e1c754-514e-4123-8699-a0936f98bcaf)

이후 인스턴스에 ssh 연결을 통해서 컴포즈 파일을 전송하였다.

~~~shell
scp -i [keypair] [파일절대경로] ubuntu@[public ipv4]:[파일받을경로]
~~~

컴포즈 파일의 내용은 다음과 같다.
~~~yaml
version: '3'
services:
  db:
    image: mysql:latest
    container_name: everytime-db
    restart: always
    ports:
      - 3306:3306
    environment:
      - MYSQL_DATABASE=ceos_everytime_db
      - MYSQL_ROOT_PASSWORD=${DB_PASSWORD}
    volumes:
      - ./mysql/store:/var/lib/mysql
    networks:
      - ceos-everytime-network

  server:
    image: ricecakessamanko/ceos-everytime-springboot-amd64:latest
    container_name: everytime-server
    restart: always
    ports:
      - 8080:8080  
    depends_on:
      - db
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://db:3306/ceos_everytime_db?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=Asia/Seoul&characterEncoding=UTF-8
      SPRING_DATASOURCE_USERNAME: ${DB_USERNAME}
      SPRING_DATASOURCE_PASSWORD: ${DB_PASSWORD}
      JWT_TOKEN_SECRET: ${JWT_TOKEN_SECRET}
    networks:
      - ceos-everytime-network

networks:
  ceos-everytime-network:
    driver: bridge
~~~

파일을 전송한 이후 ssh 연결을 통해서 ec2 머신에 접속을 해서 docker-compose up -d로 이미지를 불러와 컨테이너를 실행했다.

실제로는 위에 적어둔 docker-compose.yaml 파일에 환경변수로 값을 불러오지 못했다. 이상하게 컴포즈 파일에 환경변수로 값을 등록하고 docker-compose up을 하게되면 값이 주입되지 않았었다. 이 점은 좀더 알아봐야 할 듯하다

![image](https://github.com/riceCakeSsamanKo/spring-everytime-19th/assets/121627245/092810a2-f234-42b1-af22-767a907fd964)
ec2에 8080 포트로 정상적으로 웹서버에 접근이 가능한 모습이다. 

