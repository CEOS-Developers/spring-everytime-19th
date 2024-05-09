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

![image](https://github.com/parking0/parking0/assets/67892502/4f51a82c-4c2d-494f-b946-e82d6f8b6838)


![image](https://github.com/parking0/parking0/assets/67892502/bc7ac1eb-920c-4907-aaec-40489526ab47)

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

![Screenshot 2024-03-31 at 6 54 07 PM](https://github.com/parking0/parking0/assets/67892502/88a4a69d-6c4f-4250-b578-9126ec4de71c)

회원이 3명 이상 가입했을 때, 회원 3명의 기본키가 올바르게 생성됐다.

![Screenshot 2024-03-31 at 6 54 15 PM](https://github.com/parking0/parking0/assets/67892502/2f94b299-4962-4cdd-ad5c-13f65ddd6cc1)

amy라는 회원만 가입된 상태에서, amy로 회원을 조회하면 올바르게 나온다. 하지만 sarah라는 이름으로 조회하면 null값이 뜬다.

![Screenshot 2024-03-31 at 6 54 22 PM](https://github.com/parking0/parking0/assets/67892502/891f3a20-fddc-4b56-ae85-2e9a199f5d38)

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

![Screenshot 2024-03-31 at 6 55 52 PM](https://github.com/parking0/parking0/assets/67892502/e4ca499c-083d-4f98-b3d9-e11bc2085aa7)


2. BoardService
- 게시판 생성과 삭제가 제대로 진행되는지 확인했다.

![Screenshot 2024-03-31 at 6 55 59 PM](https://github.com/parking0/parking0/assets/67892502/a73ca8cc-e0e6-4d6a-a79f-f6f7a72ea4ef)


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

![image](https://github.com/parking0/parking0/assets/67892502/e086c07b-bfd1-47d6-86d5-9bcac5d5f25a)

- lazy loading이기 때문에, post.getBoard()을 하면 하이버네이트 프록시객체로 감싸져 있다.

#### 변화된 PostRepository - @EntityGraph 적용⭕️
@EntityGraph를 적용했을 때, 위 테스트를 실행하면 어떤 결과가 나오는지 살펴보자.

![image](https://github.com/parking0/parking0/assets/67892502/8893b005-e419-4c91-891a-5256bc01892d)

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

* * *
# 3️⃣ Everytime - CRUD API

## Refactoring
1. `.orElseThrow` 적용
- 기존엔 Optional 객체의 유무를 판단하고 예외를 처리하기 위해 if문 사용하여, 코드의 가독성이 떨어졌음
- `.orElseThrow`를 이용하여, 객체가 없으면 바로 예외를 throw 하도록 만들어서, 코드가 간결해지고 가독성이 향상됨.
```java
// 기존 Optional 방법
Optional<Comment> comment = commentRepository.findById(commentId);
if(comment.isPresent()) {
    return comment.get();
}
return null;

// 변경된 방법
final Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));
```

2. DTO 사용
- 클라이언트로부터 요청을 받고 응답을 보낼 때, 불필요한 정보는 제외하고 보낼 수 있음.
- 파라미터를 줄일 수 있음.
- Entity와 계층 간 이동하는 데이터를 분리하여, 유지 보수가 편리해짐.

3. `@Transactional(readOnly = true)` 사용
- 조회용 메서드에 적용하여, 조회용으로 가져온 Entity의 예상치 못한 수정을 방지할 수 있음.
  - 영속성 컨테스트는 Entity 조회시 초기 상태에 대한 Snapshot을 저장하는데, `readOnly = true`는 읽기 전용이기 때문에 변경감지를 위한 동작을 진행하지 않음.
- DB에서 데이터를 읽기만 한다는 것을 명확하게 알 수 있기 떄문에, 코드 가독성이 향상됨.

## HTTP Method API
![controller](https://github.com/parking0/parking0/assets/67892502/01ffa22c-f13b-40e7-a82f-9e2bf63629d9)
1. BoardController - `/api/boards`
    - 게시판 생성
    - 게시판 찾기 
    - 대학교 별 게시판 리스트 찾기 
    - 게시판 업데이트 
    - 게시판 삭제
2. MessageController - `/api/messages`
    - 메시지 생성 
    - 메시지 찾기 
    - 메시지 삭제 
3. PostController - `/api/posts`
    - 게시물 생성 
    - 게시물 찾기 
    - 게시판 별 게시물 리스트 찾기 
    - 게시물 업데이트 
    - 게시물 삭제 
4. PostLikeController - `/api/postlike`
    - 게시물 좋아요 누르기
    - 게시물 좋아요 해지

## DTO & 정적 팩토리 메서드
### DTO란?
- `DTO(Data Transfer Object)`는 계층간 데이터 교환을 위해 사용하는 객체
- Domain은 DB와 직접적으로 관련되어 있는 객체이기 때문에, DTO를 이용하여 Entity의 정보를 숨길 수 있음.

### 정적 팩토리 메서드
- `정적 팩토리 메서드`란 객체 생성의 역할을 하는 클래스 메서드
- 직접적으로 생성자를 통해 객체를 생성하지 않고, 메서드를 통해 객체를 생성
- 생성자가 아닌 정적 팩토리 메서드로 객체를 생성하는 이유는?
  - 메서드 이름으로 객체의 생성 목적을 담아낼 수 있음
  - 호출할 때마다 새로운 객체를 생성할 필요 없음
  - 객체 생성을 캡슐화할 수 있음 
    - 정적 팩토리 메서드를 사용하지 않고 Entity에서 DTO로 변환하면, 외부에서 생성자의 내부 구현을 모두 드러내야 함.
  ```java
    Car carDto = CarDto.from(car); 
    ```
- 네이밍 컨벤션
  - `from` : 하나의 매개변수를 받아서 객체 생성
  - `of` : 여러 개의 매개 변수를 받아서 객체 생성
  - `instance` : 인스턴스를 생성
  - `create` : 새로운 인스턴스 생성
  - `get[타입]` : 다른 타입의 인스턴스 생성
  - `new[타입]` : 다른 타입의 인스턴스 생성

### 에브리타임 DTO 구조

![dto](https://github.com/parking0/parking0/assets/67892502/a68c2ec2-cc14-41df-842d-e63c2f979ddd)

몇 가지 DTO를 알아보자.

1. CreateResponse
```java
   public class CreateResponse {
    
        private Long id;
        
        public static CreateResponse from (Long id){
            return new CreateResponse(id);
        }
   }
   ```
- CRUD 중 `CREATE`에 해당하는 요청은 모두 CreateResponse를 반환하도록 했다.
- 클라이언트 요청으로부터 생성된 객체의 id를 반환한다.

2. CreateBoardRequest
```java
public class CreateBoardRequest {

    private String boardName;
    private String description;
    private Long boardManagerId;
    private Long universityId;
}
```
- 클라이언트가 게시판 `CREATE`를 요청할 때 사용하는 DTO다.
- 게시판을 생성할 때 필요한 속성만 받도록 구성했다.

3. MessageResponse
          
```java
public class MessageResponse {

    private Long id;
    private Member sender;
    private Member receiver;
    private String content;
    private ReadStatus readStatus;

    public static MessageResponse from(Message message){
        return new MessageResponse(
                message.getId(),
                message.getSender(),
                message.getReceiver(),
                message.getContent(),
                message.getReadStatus()
        );
    }
}
   ```
- 클라이언트가 메시지 정보를 `READ` 요청할 때 필요한 DTO다.
- Repository에서 가져온 메시지 Entity를 DTO로 변환하기 위하여, `from`이라는 정적 팩토리 메서드를 이용했다.

4. PostUpdateRequest
```java
public class PostUpdateRequest {

    private String title;
    private String content;
    private boolean isAnonymous;
}
```
- 클라이언트가 게시물 정보를 `UPDATE` 요청할 때 필요한 DTO다. 
- 게시물 정보를 바꿀 때 필요한 정보로만 구성했다.

5. DeleteRequest
```java
public class DeleteRequest {
    private Long memberId;
}
```
- CRUD 중 `DELETE`에 해당하는 요청은 모두 DeleteRequest를 사용하도록 했다.
- 삭제 권한이 있는지 확인하기 위하여 회원의 id로만 구성했다.
- ex) 게시판 삭제는 게시판 매니저만 할 수 있기 때문에, 게시판 매니저의 id와 memberId를 비교한다.


## Global Exception
### Global Exception Handler
- `전역 에러 핸들러`(Global Exception Handler)란?
  -  `@ControllerAdvice` / `@RestControllerAdvice`와 `@ExceptionHandler` 어노테이션을 기반으로 Controller 내에서 발생하는 에러에 대해서, 해당 핸들러에서 캐치하여 오류를 발생시키지 않고 응답 메시지로 클라이언트에게 전달해 주는 기능
- `@ControllerAdvice` / `@RestControllerAdvice`
  - @Controller / @RestController 선언한 지점에서 발생한 에러를 도중에 @ControllerAdivce / @RestControllerAdvice로 선언한 클래스 내에서 이를 캐치하여, Controller 내에서 발생한 에러를 처리할 수 있도록 하는 어노테이션
  - 차이 : `@RestControllerAdvice`는 `@RequestBody` 어노테이션이 포함되어 있기 때문에, 응답을 JSON으로 제공
- `@ExceptionHandler`
  - Controller에서 특정 에러가 발생했을 때, 해당 에러를 캐치하여 클라이언트로 오류를 반환하도록 처리하는 기능을 수행

### 에브리타임 Global Exception 
#### Exception 구조
![exception](https://github.com/parking0/parking0/assets/67892502/7f7da7a0-46bc-48c1-833e-94b495a7361c)
1. ErrorCode
- Enum 클래스로 사용할 에러들을 적었다.
- 필드 
  - `HttpStatus httpStatus` - Http 상태 코드
  - `String message` - 오류 메시지
- 예시
```java
  INVALID_PARAMETER(BAD_REQUEST, "Invalid parameter included"),
  DATA_ALREADY_EXISTED(CONFLICT, "Data already exist"),

  MEMBER_NOT_FOUND(NOT_FOUND, "Member doesn't exist"),
```

2. CustomException
- 필드
  - `ErrorCode errorCode`
- RuntimeException을 상속 받은 커스텀 exception class
- 서비스 로직에서 이 exception을 throw 하면 된다.

3. ErrorResponse
- 필드
    - `HttpStatus httpStatus` - Http 상태 코드
    - `String message` - 오류 메시지
- `GlobalExceptionHandler`에서 발견한 에러를 반환하는 DTO다.

4. GlobalExceptionHandler
- 메서드
  - `handleCustomException`
    - CustomException을 캐치하기 위한 메서드. 즉 ErrorCode 내 에러를 캐치한다.
  - `handleException`
    - CustomException 이외의 모든 에러들을 캐치하기 위한 메서드.
- `@RestControllerAdvice`를 이용하여, RestController에서 발생한 에러를 잡도록 했다.

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)    // ErrorCode 내의 에러
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(new ErrorResponse(ex.getErrorCode()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception ex) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ErrorResponse(INTERNAL_SERVER_ERROR, ex.getMessage()));
    }
}
```

5. SuccessCode
- ErrorCode와 반대로, 클라이언트가 요청한 CRUD가 성공했을 때 반환하는 코드다.
- 필드
    - `HttpStatus httpStatus` - Http 상태 코드
    - `String message` - 성공 메시지 

#### Exception 테스트
![Screenshot 2024-03-31 at 7 15 13 PM](https://github.com/parking0/parking0/assets/67892502/b65ad5b9-04ba-4fc7-b3fd-7f6bd8bf238f)
![Screenshot 2024-03-31 at 7 16 45 PM](https://github.com/parking0/parking0/assets/67892502/a313cbdc-1832-4d7e-95ae-6d5ad54882b6)

- 잘못된 게시판 id로 게시판을 조회했을 때, CustomException이 제대로 발생하는 것을 확인했다.



## Swagger
### Swagger란?
- Open Api Specification(OAS)를 위한 프레임워크
- API의 스펙을 명세,관리할 수 있는 문서
- 사용자가 API 사용 방법을 쉽게 알 수 있도록 해줌.

### 환경 설정
- Swagger를 사용하기 위해선 Config 파일을 만들어야 한다.
- SwaggerConfig
  - `openAPI` 메서드 : Swagger의 전체적인 설정 (JWT 사용x)
  - `apiInfo` 메서드 : API 정보를 담고 있다
- 3.x.x 버전의 Spring을 사용하므로, `Springdoc`을 사용한다. (이전 버전은 Springfox)
- 서버 실행 후 아래 주소로 들어가면 된다.
  - http://localhost:8080/swagger-ui/index.html

### Everytime Swagger

<img width="468" alt="swag-con1" src="https://github.com/parking0/parking0/assets/67892502/1624444f-7599-4b41-a5a3-9ba7410d8d06">
<img width="381" alt="swag-con2" src="https://github.com/parking0/parking0/assets/67892502/1b30a1c5-7c4c-4d23-a354-c37614b28376">

- 예시
  - 게시판이 하나도 생성되어 있지 않을 때, 게시판 GET 요청해보자
  
    <img width="399" alt="swag-ex1" src="https://github.com/parking0/parking0/assets/67892502/cfc0df67-c447-4e8b-be1f-3c74be483168">
    
    - ErrorResponse가 반환됐다.

* * *
# 4️⃣️ Everytime - Security

## 로그인 인증 방법

### 인증의 필요성
- HTTP 프로토콜의 특징
  - 비연결성 : 클라이언트가 서버에 요청하고, 서버가 응답을 보낸 후, 서버와 클라이언트의 연결을 끊는 방식
  - 무상태 : 연결이 끊기면, 이후 어떠한 상태 정보도 유지하지 않음.
  
    ⇒ 서버는 클라이언트를 기억하지 못함
  
    ⇒ 서버에게 클라이언트가 누구인지 계속 기억시켜줘야 함
  
    => 인증이 필요하다 !

### 인증 방식

#### 1. 세션과 쿠키
##### (1) Cookie
- Key/Value 쌍으로 이루어진 문자열 - 이름, 값, 만료일, 경로 정보
- 사용자 브라우저에 저장 - 브라우저마다 쿠키 지원 형태가 달라, 브라우저 간 공유 불가능
- 보안 취약 - 요청시 쿠키의 값을 그대로 보내기 때문

##### (2) Session
- 일정 시간 동안 같은 사용자(브라우저)로부터 들어오는 일련의 요구를 하나의 상태로 보고, 그 상태를 일정하게 유지시키는 기술
    - ex) 화면이 이동해도 로그인이 풀리지 않고 로그아웃되기 전까지 유지됨
- Key/Value 쌍으로 이루어짐
- 쿠키가 보안에 취약하기에 비밀번호 같은 민감한 인증 정보를 브라우저가 아닌 서버 측에 저장하고 관리
- 서버 메모리나 서버 로컬 파일 또는 데이터 베이스에 저장
- 쿠키에 session id를 저장
    - 쿠키가 노출되더라도 session id에 개인 정보가 없기 때문에, 1번째 쿠키 인증보다 안전
    - 하지만 해커가 세션 ID 자체를 탈취하고 위장하여 접근할 수 있다는 한계 있음 - 하이재킹 공격

#### 2. Access Token
##### Token
- 세션과 달리 클라이언트에 저장 ⇒ 서버 부담 감소
- 세션과 달리 DB 조회 필요 x
- stateless
- 데이터 길이가 길어, 인증 요청이 많을수록 네트워크 부하가 심해질 수 있음

##### JWT

- 인증에 필요한 정보들을 암호화시킨 JSON토큰
  - JSON 데이터를 URL로 이용할 수 있는 문자(Base64 URL-safe Encode)로 인코딩하여 직렬화한 것
  - 전자 서명도 있어 JSON의 변조를 체크할 수 있음
- 쿠키를 통해 클라이언트에 저장
  - '.'을 기준으로 Header, Payload, Signature로 나뉨
      - `Header` : JWT에서 사용할 타입과 해시 알고리즘의 종류
      - `Payload` : 서버에서 첨부된 사용자 권한 정보와 데이터
      - `Signature` : Payload를 Base 64 URL-safe Encode를 한 후, Header에 명시된 해시함수를 적용. 개인 키로 서명한 전자 서명이 담겨 있음

    ⇒ Header 와 Payload로 Signature를 생성하여, 데이터 위변조를 막을 수 O

- 쿠키/세션과 다르게 JWT는 토큰의 길이가 길어, 인증 요청이 많을수록 네트워크 부하가 심함
- Payload 자체는 암호화되지 않기 때문에 유저의 중요한 정보는 담을 수 X

#### 3. Access Token + Refresh Token
##### JWT의 문제점
- JWT는 한 번 발급되면 유효기간이 될 때까지는 계속 사용이 가능하고, 중간에 삭제 불가능

  ⇒  해커에 의해 정보가 털린다면 대처할 방법 X

  ⇒  해결책 -  Refresh Token을 추가적으로 발급

##### Refresh Token
- Access Token보다 긴 유효 기간을 가지고 Access Token이 만료됐을 때 새로 발급해주는 열쇠

#### 4. OAuth 2.0
- 별도의 회원가입 없이 외부 서비스에서도 인증을 가능하게 하고, 해당 서비스의 API를 이용하게 해주는 프로토콜
- 인터넷 사용자들이 비밀번호를 제공하지 않고 다른 웹사이트 상의 자신들의 정보에 대해 웹사이트나 애플리케이션의 접근 권한을 부여할 수 있는 공통적인 수단으로서 사용되는, 접근 위임을 위한 개방형 표준
- Authorization Server
    - 클라이언트가 자원 서버의 서비스를 이용할 수 있게 인증하고 토큰을 발생하는 서버(인증 서버) - ex) 페이스북, 구글

* * *
## 액세스 토큰 발급 및 검증 로직 구현

### JWT Authentication
#### 1. 전체적인 인증 방법

<img width="883" alt="jwtAuthenticaiton" src="https://github.com/parking0/parking0/assets/67892502/fadcae24-3e45-442b-990e-4d0a52159bc2">

1. `Http` 요청이 들어온다.
2. 해당 요청이 `AuthenticationFilter`에 걸리면, 요청의 payload로부터 ID/PW를 추출하는데, 이를 `User Credentials`라고 한다. 이를 기반으로 `Authentication`의 구현체의 일종인 `UsernamePasswordAuthenticationToken` 객체를 생성한다.
3. `AuthenticationManager`는 각 필터들의 인증 절차를 정의한다. 이는 Interface이기 때문에 그 구현체인 `ProviderManager`가 실질적인 일을 한다.
4. `ProviderManager`의 관리 대상이 `AuthenticationProvider`다. 실질적인 인증 과정의 구현은 `AuthenticationProvider`이 한다. (UsernamepasswordProvider 등 각 인증 종류별로 provider가 있다.)
5. `AuthenticationProvider`는 인증을 수행하기 위해 ID/PW 등의 자격 증명 정보를 DB 같은 지정된 저장소로부터 받아온다. 이 때 `UserDetailsService`를 통해 `UserDetails` 객체 형태로 받아온다.
6. `UserDetailsService`는 DB 내에 문자열 형태로 저장되어 있는 username, password 등의 정보를 `UserDetails` 형태의 객체로 변환한다.
7. `UserDetails` 객체를 받은 `AuthenticationProvider`가 이를 사용자의 입력 정보와 비교한다.
8. 인증이 성공적으로 되었다면, `Authentication` 객체를 `SecurityContextHolder` 내의 `SecurityContext`에 저장한다. 이후 `AuthenticationSuccessHandler`가 실행된다.
9. 인증에 실패했다면, `AuthenticationException`이 던져지고 `AuthenticationFailureHandler`가 실행된다.

#### 개념

<img width="626" alt="SecurityContext" src="https://github.com/parking0/parking0/assets/67892502/636b419a-0a49-4ae7-be57-6a381674578d">

##### 1. Authentication
- 역할
  - `AuthenticationManager`에 credentials을 제공한다.
  - 현재 인증된 사용자임을 나타낸다.
- 요소
  - `Principal` : 사용자를 나타낸다. 사용자 ID라고 보면 된다. 대부분의 경우 `UserDetails`의 객체이다.
  - `Credentials` : 비밀번호와 같은 인증용 키이다. 대부분의 경우, 인증 후에는 유출을 막기 위해 사라진다.
  - `Authorities`: 역할이나 권한이다. (e.g. 관리자, 무료 사용자, 유료 사용자).

##### 2. GrantedAuthority
- 사용자에게 부여된 높은 수준의 권한
- `Authentication.getAuthorities()`를 통해 확인이 가능하며, Collection형의 객체를 반환

##### 3. ProviderManager
- `AuthenticationManager`의 가장 보편적인 구현체
- `AuthenticationProvider` 체인에 일을 맡긴다
- `ProviderManager`에 의해 `AuthenticationProvider`는 주어진 정보로 인증을 시도.

##### 4. AuthenticationProvider
- 특정 타입의 인증 방식을 지원하는 Provider

##### 5. UserDetailsService
- ID/PW/그 외 인증에 필요한 다른 속성들을 얻기 위해, `DaoAuthenticationProvider`에 의해 사용된다.
  - `DaoAuthenticationProvider` : `AuthenticationProvider`의 구현체 중 하나. UserDetailsService와 PasswordEncoder를 활용하여 ID/PW를 인증
- 커스텀 UserDetailsService을 빈으로 등록하여, 커스텀 인증을 정의할 수도 있다

##### 6. SecurityContext
- `Authentication` 객체가 저장되는 보관소
- SecurityContextHolder 전략에 따라 SecurityContext의 저장 방식이 다름
  - 하지만 일반적으로는 ThreadLocal(쓰레드마다 갖는 고유한 저장공간)에 저장
  - 코드 어디서나 Authentication을 꺼내서 사용 가능
- 인증이 완료되면 HttpSession에 저장되어, 어플리케이션 전반에 걸쳐 전역적인 참조가 가능

##### 7. SecurityContextHolder
- `SecurityContext`를 저장하는 객체
- 일반적으로 SecurityContext 저장을 위한 ThreadLocal 를 갖고 있는 객체
- SecurityContext 객체의 저장 방식(전략)을 지정


### 구현한 내용
#### 1. CustomUserDetails
- `UserDetails`를 상속받아 사용한다.
- 회원정보를 나타내는 도메인 `Member`를 가진다.

#### 2. CustomUserDetailsService
- `UserDetailsService`를 상속받아 사용한다.
```java
 @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return new CustomUserDetails(member);
    }
```
- `CustomUserDetails`를 반환하도록 `loadUserByUsername`를 만들었다.

#### 3. TokenDto
- `grantType` : JWT 대한 인증 타입. Bearer 사용
- `accessToken`
- `accessTokenExpiresIn` : accessToken 만료일

#### 4. TokenProvider
- 1) `generateToken`
   - 유저 정보로 AccessToken을 생성하고, TokenDto를 반환하는 메서드
- 2) `getAuthentication`
  - JWT 토큰을 복호화하여 토큰에 들어 있는 정보를 꺼내는 메서드
  - 주어진 Access Token에서 username을 꺼내서, customUserDetailsService를 이용하여 customUserDetails를 구한다. 
  - 이후 Authentication를 상속 받은 UsernamePasswordAuthenticationToken을 만들어 반환한다. 
- 3) `validateToken`
    - 토근이 유효한지 검증하는 메서드

#### 5. JwtAuthenticationFilter
- `OncePerRequestFilter`를 상속 받음
  - 하나의 HTTP 요청에 대해 한번만 실행하는 Filter
- 1) `doFilterInternal`
    - JWT 토큰의 인증 정보를 현재 쓰레드의 `SecurityContext`에 저장
    - Request Header에서 토큰을 꺼낸 뒤, 토큰 유효성을 검사하여, 정상 토큰이면 Authentication을 가져와서 SecurityContext에 저장

* * *
## 회원가입 및 로그인 API
### 1. 회원가입 API

```java
@PostMapping("/signup")
public ResponseEntity<MemberDto> signUp(@RequestBody SignUpRequest signUpRequest) {

        return ResponseEntity.status(INSERT_SUCCESS.getHttpStatus())
                .body(memberService.signUp(signUpRequest));
    }
```
- `SignUpRequest`: username과 password를 담은 dto
- `MemberService`에서 `passwordEncoder`를 이용하여 password를 암호화 함.

### 2. 로그인 API

```java
@GetMapping("/login")
public ResponseEntity<TokenDto> login(@RequestBody SignInRequest logInRequest) {

    return ResponseEntity.status(SELECT_SUCCESS.getHttpStatus())
        .body(memberService.login(logInRequest.getUsername(), logInRequest.getPassword()));
}
```
- `SignInRequest`: username과 password를 담은 dto

```java
public TokenDto login (String loginId, String password) {
    
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginId, password);
    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

    TokenDto tokenDto = tokenProvider.generateToken(authentication);
    return tokenDto;
    }
```
- 아래와 같은 순서로 진행된다.
- 1) username과 password를 기반으로 `Authentication` 객체 생성
- 2) authenticate() 메서드를 통해 요청된 Member에 대한 실제 검증 진행
- 3) 인증 정보를 기반으로 JWT 토큰 생성

### 3. 회원가입 API 테스트

<img width="640" alt="signupTest" src="https://github.com/parking0/parking0/assets/67892502/5c86322b-5294-4cdf-85da-dfdf7409f064">

### 4. 로그인 API 테스트

<img width="636" alt="loginTest" src="https://github.com/parking0/parking0/assets/67892502/ddd90365-b2ca-48a5-b978-bd8fe9b951f3">


## 토큰이 필요한 API

### 로그인 중인 사용자 가져오는 방법
#### 1. SecurityContextHolder
```java
Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
UserDetails userDetails = (UserDetails)principal;
String username = principal.getUsername();
String password = principal.getPassword();
```
- 위와 같은 방법으로 `SecurityContextHolder`에 접근하여 현재 로그인 중인 객체를 가져올 수도 있다.
- 하지만 로그인중인 사용자가 필요할 때마다 위 코드를 작성하는 건 비효율적이다.

#### 2. @AuthenticationPrincipal
- Spring Security 3.2부터 `@AuthenticationPrincipal`을 통해 로그인 객체를 가져올 수 있다.
- 1번 방법과 달리, 반복되는 코드가 줄어들고 custom 로그인 객체를 가져올 수 있기 때문에 편리하다.


### MessageController

```java
@PostMapping
public ResponseEntity<CreateResponse> createMessage (
    @AuthenticationPrincipal CustomUserDetails userDetails,
    @RequestBody@Valid final CreateMessageRequest createMessageRequest){

        final Member member = userDetails.getMember();
        final Long messageId = messageService.createMessage(createMessageRequest, member.getId());

        return ResponseEntity.status(INSERT_SUCCESS.getHttpStatus())
                .body(CreateResponse.from(messageId));
    }
```
- 기존 방법
  - 클라이언트로부터 메시지 발신 요청 받을 때,`CreateMessageRequest`에 발신자의 id를 포함해서 받았음.

- 변경된 방법
  - `@AuthenticationPrincipal`을 통해 `CustomUserDetails`을 받아서, 현재 로그인 중인 사용자를 알아낼 수 있음 
      - `userDetails.getMember()`
  - `CreateMessageRequest`에는 수신자의 id와 메시지 내용만 넣도록 변경했다.


### 메시지 전송 테스트

<img width="317" alt="MessageTest" src="https://github.com/parking0/parking0/assets/67892502/416ad667-7399-4231-8653-9ca01cc00721">

<img width="614" alt="headerMessage" src="https://github.com/parking0/parking0/assets/67892502/8425f59d-fddc-40a5-a206-8c52c58c1af9">

- 메시지를 보낼 때 header에 발신자의 `토큰`을 포함하면, 정상적으로 작동한다.

<img width="611" alt="messageError" src="https://github.com/parking0/parking0/assets/67892502/39f11044-5363-458b-a37a-0d5fbac31d79">

- 만약 토큰을 포함하지 않으면, 403 forbidden이 발생한다.





