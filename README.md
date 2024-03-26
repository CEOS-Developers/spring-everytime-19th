# spring-everytime-19th
# 1️⃣ Everytime - DB Modeling
* * *

## 에브리타임 DB 모델링

### 에브리타임 서비스의 주요 특징
- 사용자는 게시물과 댓글을 익명으로 작성할 수 있다.
- 게시글, 댓글, 대댓글에 '좋아요'를 누를 수 있다.
- 1대1 쪽지를 주고 받을 수 있다.
- 게시글에 사진을 여러 장 첨부할 수 있다.


### 모델링 결과

![Screenshot 2024-03-17 at 10 22 42 PM](https://github.com/parking0/TeenTalk_Server/assets/67892502/2fb7b3c3-2a1d-416f-aeb8-c3859a8bb1d1)

![Screenshot 2024-03-17 at 10 29 37 PM](https://github.com/parking0/TeenTalk_Server/assets/67892502/e9bd44a0-5ea0-419f-a9f6-28f4ee7c998d)

#### 1. Comment
- id : Comment의 기본키다. `@GeneratedValue`라는 기본 키를 자동으로 생성해주는 어노테이션을 사용했다.
  전략은 `GenerationType.IDENTITY`를 사용하여, 기본 키 생성을 데이터베이스에 위임했다. 따라서 id 값을 null로 하면 DB가 알아서 AUTO_INCREMENT해준다.
- postId : 해당 댓글이 달린 게시글의 아이디다. `@ManyToOne(fetch = FetchType.LAZY)`를 이용하여 조인했다.
```java
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parentComment;          //댓글

    @OneToMany(mappedBy = "parentComment", orphanRemoval = true)        //대댓글
    private List<Comment> childrenComment = new ArrayList<>();
```
- parentComment : 만약 대댓글이 아닌 댓글일 경우, 값이 null이 된다.
- childrenComment : 대댓글은 여러 개일 수 있다. 따라서 List로 저장한다.
- author : 댓글 작성자다. `@ManyToOne`을 이용하여 Member와 조인했다.
- content : 댓글의 내용이다. 길이는 최대 1000이고, null일 수 없다.
- likes : 댓글의 좋아요 개수다.
- isAnonymous : 에브리타임은 익명으로 댓글을 작성할 수 있기 떄문에, boolean으로 익명 여부도 확인한다.
- isDeleted : 댓글이 삭제될 수 있기 때문에 boolean으로 확인한다.
- createdAt : 댓글이 작성된 시간이다.
- updatedAt : 댓글이 수정된 시간이다.


#### 2. CommentLike
- id : CommentLike의 기본키다.
- commentId : 해당 댓글을 의미한다. `@ManyToOne`을 이용하여 Comment와 조인했다.
- user : 좋아요를 누른 회원이다. `@ManyToOne`으로 Member와 조인했다.


#### 3. Member
- id : Member의 기본키다. 
- loginId : 사용자가 로그인할 때 사용하는 아이디다. 길이는 최대 20이고, null값으로 둘 수 없도록 했다.
- userPw : 사용자가 로그인할 때 사용하는 비밀번호다. 길이는 최대 30이고, null값이 될 수 없다.
- userName : 사용자가 에브리타임에서 사용하는 닉네임이다. 중복되는 닉네임은 있을 수 없고, 길이는 10, null값은 불가능하도록 했다.
- email : 사용자의 이메일도 입력받도록 했다. 이 또한 null이 될 수 없다.


#### 4. Message
- id : Message의 기본키다. 
- sender : 메시지 발신자다. `@ManyToOne`으로 Member와 조인했다.
- receiver : 메시지 수신자다. `@ManyToOne`으로 Member와 조인했다.
- content : 메시지의 내용이다. 길이는 최대 2000이고 null일 수 없다.
- isRead : 수신 상태를 확인하기 위해 boolean값으로 했다. 기본값은 false다.
- createdAt : 쪽지가 보내진 시간이다.


#### 5. Photo
- id : Photo의 기본키다. 
- postId : 사진이 첨부된 게시글의 아이디다. `@ManyToOne`으로 Post와 조인했다.
- photoURL : 사진 경로를 나타내기 위해 String으로 지정했다.


#### 6. Post
- id : Post의 기본키다. 
- title : 게시글의 제목이다. 길이는 최대 100이고 null일 수 없다.
- content : 게시글의 내용이다. 길이는 최대 2000이고 Null일 수 없다.
- createdAt : 게시글이 작성된 시간이다.
- updatedAt : 게시글이 수정된 시간이다.
- author : 게시글의 작성자다. `@ManyToOne`으로 Member와 조인했다.
- isAnonymous : 게시글은 익명으로 작성할 수 있기 때문에 boolean값으로 지정했다.
- likes : 게시글이 받은 좋아요 개수다. 초기값은 0이다.


#### 7. PostLike
- id : PostLike의 기본키다. 
- postId : `@ManyToOne`으로 Post와 조인했다.
- user : 좋아요를 누른 회원이다. `@ManyToOne`으로 Member와 조인했다.


### Repository 테스트
![Screenshot 2024-03-17 at 11 30 24 PM](https://github.com/parking0/TeenTalk_Server/assets/67892502/e76ebdde-8998-4a60-952a-ab10ff4fe54c)

회원이 3명 이상 가입했을 때, 회원 3명의 기본키가 올바르게 생성됐다.

![Screenshot 2024-03-17 at 11 30 32 PM](https://github.com/parking0/TeenTalk_Server/assets/67892502/1f4c18f4-b700-4e9a-9b53-cdab0255cbe7)

amy라는 회원만 가입된 상태에서, amy로 회원을 조회하면 올바르게 나온다. 하지만 sarah라는 이름으로 조회하면 null값이 뜬다.

![Screenshot 2024-03-17 at 11 32 28 PM](https://github.com/parking0/TeenTalk_Server/assets/67892502/9f1f78ea-b493-4076-8ee7-e669702980d7)

게시글을 작성하고 기본키로 조회했을 때, DB에 게시글이 올바르게 저장된 것을 확인할 수 있다.

* * *
# 2️⃣ Everytime - Service Layer
## Refactoring
1. `BaseTimeEntity` 생성
  - 대다수의 domain에 들어가는 공통적인 컬럼인 생성 시간과 업데이트 시간을 추상클래스 BaseTimeEntity을 이용하여 구현했다.
2. 추가된 도메인
  - University, Board 
3. ReadStatus 적용
  - 메시지 읽음 여부를 확인하기 위해서 enum인 ReadStatus를 생성했다.
  - 값은 READ, NOT_READ가 있다.


## Service 기능
1. BoardService
   - 게시판 생성
   - 게시판 삭제
   - 게시판 이름 변경
   - 게시판 소개 변경
2. CommentLikeService
   - 댓글 좋아요 누르기
   - 댓글 좋아요 해지
3. CommentService
   - 댓글 달기
   - 댓글 삭제
   - 댓글 내용 변경
4. MemberService
   - 회원 가입
   - 로그인
   - 회원 이름 변경
   - 회원 탈퇴
5. MessageService
   - 쪽지 생성
   - 쪽지 읽음 상태 변화
6. PostLikeService
   - 게시글 좋아요 누르기
   - 게시글 좋아요 해지
7. PostService
   - 게시글 생성
   - 게시글 삭제
   - 게시글 제목 변경
   - 게시글 내용 변경
8. UniversityService
   - 대학교 생성
   - 대학교 삭제
   - 대학교 이름 변경

## Service Test
1. MemberService
- 회원가입, 중복 이름 불가, 이름 변경, 멤버 삭제가 제대로 진행되는지 확인했다.
 
![Screenshot 2024-03-26 at 12 49 04 PM](https://github.com/parking0/TeenTalkServer_Test/assets/67892502/97b66005-9ea9-4ba8-bbd4-45876044576a)


2. BoardService
- 게시판 생성과 삭제가 제대로 진행되는지 확인했다.
  
![Screenshot 2024-03-26 at 12 50 53 PM](https://github.com/parking0/TeenTalkServer_Test/assets/67892502/65b5bee5-e1b7-4bd1-9686-eb9084e3b3ef)


## N+1 문제
### N+1 문제란?
- 특정 객체를 대상으로 수행한 쿼리가 해당 객체가 가지고 있는 연관관계 또한 조회하면서 N번의 추가적인 쿼리가 발생하는 문제
- 해결법
    - JPA : JPQL 에서 지원하는 fetch join 을 사용
    - 스프링데이터JPA : @EntityGraph 로 fetch join 사용

### N+1 Test - @EntityGraph
- Post와 Board는 다대일 관계다.
- post를 2개 만들어, postRepository를 통해 만들어진 post 전체를 찾는 테스트를 만들었다.
- N+1 Test 여러 해결 방법 중 `@EntityGraph`를 이용하여 확인하고자 한다.
  - 연관관계가 있는 엔티티를 조회할 때 지연 로딩으로 설정되어 있으면, 연관관계에서 종속된 엔티티는 쿼리 실행 시 select 되지 않고 proxy 객체를 만들어 엔티티가 적용시킨다.
  - 이후 해당 프록시 객체를 호출할 때마다 그때그때 select 쿼리가 실행된다.
  - => fetch 조인을 사용하여 여러 번의 쿼리를 한 번에 해결할 수 있다.

```java
    @DisplayName("N+1 테스트")
    @Test
    void nPlus1() throws Exception {
        //given
        Post post1 = new Post("안녕하세요", "신입생입니다...", member1, board1, false);
        Post post2 = new Post("어떤 과목", "추천해주세요", member1, board2, false);
        postRepository.save(post1);
        postRepository.save(post2);

        em.flush();
        em.clear();

        //when
        List<Post> posts = postRepository.findAll();        //    @EntityGraph(attributePaths = {"board"})

        //then
        for(Post post : posts){
            System.out.println("post = " + post.getTitle());
            System.out.println("post.getBoard().getClass() = " + post.getBoard().getClass());
            System.out.println("post.getBoard().getBoardName() = " + post.getBoard().getBoardName());
        }
    }
```

#### 기존 PostRepository - @EntityGraph 적용❌
@EntityGraph를 적용하지 않았을 때, 위 테스트를 실행하면 어떤 결과가 나오는지 살펴보자.
![Screenshot 2024-03-26 at 1 48 28 PM](https://github.com/parking0/TeenTalkServer_Test/assets/67892502/45861a1f-ec1c-4acf-9a36-f29608c73711)

- lazy loading이기 때문에, post.getBoard()을 하면 하이버네이트 프록시객체로 감싸져 있다.

#### 변화된 PostRepository - @EntityGraph 적용⭕️
@EntityGraph를 적용했을 때, 위 테스트를 실행하면 어떤 결과가 나오는지 살펴보자.
![Screenshot 2024-03-26 at 1 47 55 PM](https://github.com/parking0/TeenTalkServer_Test/assets/67892502/e531c519-e240-4868-ba9a-d1569776e120)

- post와 board 테이블을 left join하여 한 번에 모든 데이터를 가져오는 것을 확인할 수 있다.


## 🖍️ 수정해야 하는 부분
1. 검증을 진행하는 시기
  - service 단계에서 진행해야 하는지, domain 단계에서 진행해야 하는지
2. 에러 처리
  - 현재는 에러 처리를 하지 않고, log로 성공/실패 여부를 확인하고 있다.
3. DTO 적용
  - service layer에서 바로 entity에 접근하지 않고, DTO를 따로 만들어 데이터에 접근할 수 있도록 한다.
4. Mockito 적용
  - 지금까지 `SpringBootTest`를 이용하여 테스트 코드를 작성했다. 단위 테스트에 알맞는 `Mockito`를 적용해봐야 한다.
5. 데이터 삭제시
  - ex) 회원 탈퇴시, 회원이 남긴 게시글이나 댓글을 어떻게 처리할지
  - 에브리타임에서는 사용자명이 (알 수 없음) 으로 표시됨.





