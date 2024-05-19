# CEOS 19th 2nd Assignment - Everytime DB Modeling
## Everytime DB Modeling
![ceos-19th-everytime](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/851566d6-1bd4-4876-96d5-7f4a247fc3cb)

### User
![스크린샷 2024-03-17 211210](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/26a3ae14-37cf-4e46-977b-5a0b0bb6052b)

- 로그인 기능: 로그인할 때 필요한 아이디, 비밀번호 정보 조회를 위해 저장
- 회원정보 저장 기능: 소속 학교, 학과, 학번, 실명, 닉네임, 이메일 등 기본 정보 저장
- 학교 정보 저장: 각 유저는 어느 학교에 속하는지, 각 학교에는 몇 명의 학생이 속해있는지에 대한 정보는 School과 User 엔터티에 저장 (User가 school_id 외래키를 통해 참조)

### Post, Image, Scrap, Board, School
![스크린샷 2024-03-17 204523](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/8004db90-c90a-42a5-baca-32c25763de9c)

- 게시글 조회: 제목, 내용으로 검색 가능하게 title, contents 저장
- 메타데이터 조회: 각 게시글 좋아요 개수(좋아요 추가/삭제 서비스), 신고 접수 여부(신고 접수/취소 서비스), 댓글 개수(댓글 추가/삭제 서비스), 스크랩된 횟수 정보 저장
- 게시글에 사진과 함께 글 작성: Image 엔터티에서 post_id라는 외래키를 통해 해당 게시글에 원하는 사진 첨부
- 게시판 별 게시글 조회: Post 엔터티에서 board_id라는 외래키를 통해 어떤 게시판에 속하는지 명시
- 게시글 스크랩: post_id와 user_id라는 외래키를 통해 각각 Post와 User를 참조하여 스크랩 정보 저장

### Comment, SubComment
![스크린샷 2024-03-17 204703](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/5482885d-2c32-4c98-a2d2-1e8ef1ce741e)

- 게시글에 댓글 및 대댓글 기능: 각각 post_id와 comment_id 외래키를 통해 어느 게시판에 속한 어느 댓글인지, 어느 댓글에 속한 어느  대댓글인지 저장
- 댓글/대댓글 좋아요 추가/삭제 기능: like_num이라는 속성을 통해 좋아요 수 반영
- 댓글/대댓글 삭제 기능: is_deleted라는 속성을 통해 삭제 여부 저장

### Message, MessageBox
![스크린샷 2024-03-17 204714](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/b74ea48f-ed1f-46a6-9bc5-e6c9c10cdd96)

- 1:1 쪽지 기능: 누가(request_user_id) 누구에게(response_user_id) 어떤 내용(contents)의 쪽지를 전송했는지 저장
- 쪽지함의 쪽지 개수 저장: message_num이라는 속성을 통해 쪽지 개수 반영

### Timetable, AddedCourse, Course
![스크린샷 2024-03-17 204645](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/1cdf5c40-127f-4042-9f3d-7a22032a3832)

- 시간표 생성 기능: 한 사용자당 여러 개 시간표 생성 가능(OneToMany), 시간표에 들어가는 여러 강의 정보는 AddedCourse를 통해, 존재하는 모든 강의 정보는 Course에 저장
- 강의 기본정보 제공: 학점, 교수님, 강의실, 시작/종료 시간 등 모든 정보 Course에 저장되어 있음
 
### Friend
![스크린샷 2024-03-17 204605](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/0622ba0a-e4a6-4fbb-b3c1-220c226e5d65)

- 친구 추가 기능: 친구 요청 보낸 사람(request_user_id)과 받은 사람(response_user_id)의 정보는 외래키를 통해 User 엔터티의 user_id를 참조하여 저장
- 친구 요청 수락/거절 기능: 친구 요청 거절하는 기능을 is_accepted라는 속성을 통해 구현

---
## Repository 단위 테스트
school_id라는 외래키를 포함하는 'User' entity로 repository 단위 테스트를 진행  
0. '@BeforeEach'를 통해 필요한 환경 세팅
- **@BeforeEach란?**   @BeforeEach는 각 테스트 메소드가 실행되기 전에 먼저 실행되어야 하는 메소드를 지정하는 데 사용됨. 테스트 환경을 초기화하거나 테스트에 필요한 데이터를 준비하는 데 주로 사용됨.
```java
@BeforeEach
void setup() { //School 객체 미리 생성
    testSchool = new School("Sogang", Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now()), 5000L);
    testSchool = entityManager.persistFlushFind(testSchool);
}
```
User에서 School을 참조하기 위해 school_id라는 외래키를 가지므로, school이라는 객체를 미리 생성하여 User 생성자에 넣기 위해 '@BeforeEach'를 활용

1. Given/When/Then에 따라 테스트 작성
```java
// Given
User testUser1 = createUser("Chloe", "Business");
User testUser2 = createUser("Soobin", "Computer Science Engineering");
User testUser3 = createUser("Jennifer", "Philosophy");

userRepository.save(testUser1);
userRepository.save(testUser2);
userRepository.save(testUser3);
```
- test를 위한 User 객체를 3개 생성
- save()를 쓰는 이유
  - **영속성이란?** 영속성을 갖지 않으면 데이터는 메모리에서만 존재하게 되고 프로그램이 종료되면 해당 데이터는 모두 사라지게 된다. 그래서 우리는 데이터를 파일이나 DB에 영구 저장함으로써 데이터에 영속성을 부여한다. 
  - **영속성 컨텍스트는?** entity를 영구 저장하는 환경이라는 뜻. 엔티티 매니저는 영속성 컨텍스트에 엔티티를 보관하고, 관리한다.(개발자 입장에서 엔티티 매니저는 엔티티를 저장하는 가상의 데이터베이스) ![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/91009785-bb8d-4f1e-af13-5a821ccc9e4a)
  - **repository.save()를 통해 객체를 저장해야 하는 이유는?** JPA의 save() 메서드는 비영속적인 객체를 persist하여서 영속 상태로 만들어준다. 그 후 save() 메서드에 붙어있는 @Transactional 어노테이션을 통해서 flush(DB에 반영)됨.

2. 테스트에서 객체를 3개 이상 넣은 이후에 해당 객체가 출력되는지 확인하기
```java
// When
User testedUser1 = userRepository.findByUsername(testUser1.getUsername());
User testedUser2 = userRepository.findByUsername(testUser2.getUsername());
User testedUser3 = userRepository.findByUsername(testUser3.getUsername());
```
- 1번에서 생성해놓은 testUser 객체에 getter를 통해 username을 얻은 다음, 이 username을 findByUsername의 인자로 전달하여 그 반환값을 새로운 User 객체인 testedUser로 받는다.

3. 테스트를 수행할 때 발생하는 JPA 쿼리를 조회해보기
```java
// Then
assertNotNull(testedUser1);//해당 객체가 null이 아닌지 확인 -> null이 아니라면 정상 작동된 것
assertNotEquals(testUser1.getUsername(), testedUser1.getUsername(), "It is not expected result."); //기대되는 객체와 테스트로 나온 실제 객체의 값이 같은지 확인

assertNotNull(testedUser2);//해당 객체가 null이 아닌지 확인 -> null이 아니라면 정상 작동된 것
assertNotEquals(testUser2.getUsername(), testedUser2.getUsername(), "It is not expected result."); //기대되는 객체와 테스트로 나온 실제 객체의 값이 같은지 확인

assertNotNull(testedUser3);//해당 객체가 null이 아닌지 확인 -> null이 아니라면 정상 작동된 것
assertNotEquals(testUser3.getUsername(), testedUser3.getUsername(), "It is not expected result."); //기대되는 객체와 테스트로 나온 실제 객체의 값이 같은지 확인
```
- asserNotNull(): 파라미터로 전달받은 객체가 null이 아닌지 확인
- assertNotEquals(): 1st 인자는 기대되는 값, 2nd 인자는 실제 값, 마지막은 같지 않을 경우 출력되는 메시지
  - cf) **삽질 기록...** 처음에 assertEquals() 써놓고 왜 test가 자꾸 실패하지...라는 엄청난 삽질을 했었다... 앞으로는 같은 실수를 반복하지 말아야겠다는 다짐을 하며 기록...
- JPA 쿼리 결과(application.yml 파일에서 show-sql: true라고 작성했기 때문에 테스트 실행 시 콘솔에서 직접 SQL을 확인할 수 있다)
0. '@BeforeEach'를 통해 school 객체 생성
```java
Hibernate: 
    insert 
    into
        school
        (created_at, school_name, student_num, updated_at) 
    values
        (?, ?, ?, ?)
```
새로 생성한 school 객체가 School에 잘 저장된 걸 확인할 수 있다.

1. 본격적인 test 시작 -> 생성자를 통해 user 객체 생성 
```java
Hibernate: 
    select
        s1_0.school_id,
        s1_0.created_at,
        s1_0.school_name,
        s1_0.student_num,
        s1_0.updated_at 
    from
        school s1_0 
    where
        s1_0.school_id=?
```
앞서 만든 school 객체가 school_id를 외래키로 삼아 user와 잘 결합되는 걸 확인할 수 있다.
```java
Hibernate: 
    insert 
    into
        user
        (department, email, is_active, join_at, latest_login_at, login_at, login_id, login_password, nickname, school_id, student_id, updated_at, username) 
    values
        (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
```
생성자를 통해 생성된 user 객체가 User에 잘 insert된 걸 확인할 수 있다. 아까 객체를 3개 만들었으므로 해당 쿼리문도 3번 반복되지만 여기서는 생략하여 작성한다.

2. findByUsername 실행
```java
Hibernate: 
    select
        u1_0.user_id,
        u1_0.department,
        u1_0.email,
        u1_0.is_active,
        u1_0.join_at,
        u1_0.latest_login_at,
        u1_0.login_at,
        u1_0.login_id,
        u1_0.login_password,
        u1_0.nickname,
        u1_0.school_id,
        u1_0.student_id,
        u1_0.updated_at,
        u1_0.username 
    from
        user u1_0 
    where
        u1_0.username=?
```
findByUsername의 파라미터로 전달받은 username과 같은 user가 있는지 SELECT문을 통해 조사하는 걸 확인할 수 있다.

---
## [옵션] JPA 관련 문제 해결
### 어떻게 data jpa는 interface만으로도 함수가 구현이 되는가?
1. Repository 인터페이스 정의
- 개발자는 JPA의 Repository 인터페이스를 확장하는 자신만의 리포지토리 인터페이스를 정의 -> 이 인터페이스에는 엔티티를 저장, 삭제하는 것과 같은 기능을 위한 메소드 선언이 포함됨.

2. 쿼리 메소드 규칙
- JPA는 메소드 이름을 분석하여 해당 메소드의 목적에 맞는 SQL 또는 JPQL 쿼리를 생성 -> 개발자가 복잡한 쿼리를 직접 작성하지 않아도 되게 해주는 편리성 제공
- ex) findByUserId이라는 메소드 이름은 "UserId" 필드를 기준으로 엔티티를 찾는 쿼리를 생성하도록 지시

3. 프록시 객체 생성
- JPA는 ApplicationContext가 초기화될 때 개발자가 정의한 Repository 인터페이스에 대한 프록시 객체를 생성
- 이 프록시 객체는 인터페이스의 메소드 호출을 가로채어 적절한 JPA 쿼리를 실행하는 역할을 합니다.

4. 쿼리 실행
- 개발자가 리포지토리 인터페이스의 메소드를 호출 -> 해당 호출은 프록시 객체가 가로챔
- 프록시 객체는 메소드 이름, 파라미터 등을 분석하여 동적으로 쿼리를 생성하고 이를 실행한 뒤 결과를 반환

### data jpa를 찾다보면 SimpleJpaRepository에서  entity manager를 생성자 주입을 통해서 주입 받는다. 근데 싱글톤 객체는 한번만 할당을 받는데, 한번 연결될 때 마다 생성이 되는 entity manager를 생성자 주입을 통해서 받는 것은 수상하지 않는가? 어떻게 되는 것일까? 
- EntityManagerFactory는 애플리케이션에서 단 하나만 존재하는 싱글톤 객체인 반면, EntityManager는 보통 한 개의 데이터베이스 트랜잭션 또는 요청을 처리하는 데 사용되며 사용 후에는 닫힘   -> 따라서 EntityManager는 각 트랜잭션마다 새로운 인스턴스가 생성되어야 함.
- SimpleJpaRepository에 EntityManager를 생성자 주입하는 것은 실제로는 EntityManager의 프록시에 대한 주입
- 이 프록시는 애플리케이션의 생명 주기 동안 재사용될 수 있으며, 실제 작업이 필요할 때마다 적절한 EntityManager 인스턴스를 제공

### fetch join 할 때 distinct를 안하면 생길 수 있는 문제
- fetch join: 연관관계의 entity를 함께 조회할 때 사용됨
- 이는 '중복 문제'를 발생시킬 수 있음
- ex) 하나의 'Post'에 여러 개의 'Comment'가 연관관계에 있을 때: fetch join으로 함께 조회하면, SQL문의 결과는 Post 데이터가 중복된 채로 포함되어 나올 수 있음. 각 Comment에 대응되는 Post 정보도 함께 조회되기 때문!

### fetch join 을 할 때 생기는 에러가 생기는 3가지 에러 메시지의 원인과 해결 방안
1. `HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!`
- 원인: 컬렉션(fetch)과 페이징(pagination)이 함께 사용될 때(컬렉션 데이터를 메모리 상에서 페이징으로 가져오려고 할때) 발생하는 에러. 
- 해결법: 컬렉션을 fetch join하는 대신 @BatchSize또는 Hibernate의 setFetchSize()를 사용하여 N+1 문제를 최소화하면서 페이징 처리를 수행하면 해결 가능
  
2. `query specified join fetching, but the owner of the fetched association was not present in the select list`
- 원인: 연관된 entity를 select하려고 하지만 해당 연관 entity의 소유자가 select list에 없어서 발생
- 해결법: SELECT문에 연관 entity의 소유자를 포함시키키

3. `org.hibernate.loader.MultipleBagFetchException: cannot simultaneously fetch multiple bags`
- 원인: 2개 이상의 OneToMany 자식 테이블에 Fetch Join을 선언했을때
- 해결법: hibernate.default_batch_fetch_size 옵션 추가하기  
 
---

# CEOS 19th 3rd Assignment - Everytime Service Layer
## Refactoring
### About DB
![ceos-19th-everytime-240324ver](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/8db7333e-2811-4fa0-8998-6f8d6a9a9484)
1. 대댓글 작성 기능: SubComment라는 별도의 entity를 지우고 Comment entity에서 자기참조하는 형식으로 변경
2. AddedCourse: Timetable과 Course를 바로 연결하면 `@ManyToMany` 관계로 매핑해야 하기 때문에 중간에 AddedCourse라는 entity를 두어 `@ManyToMany` 매핑을 피하고자 함

### About Code
1. 자바 컨벤션(카멜🐪케이스)과 DB 컨벤션(스네이크🐍케이스)을 따르기 위해 아래와 같이 수정  
```java
@Column(name="school_name", nullable = false)
private String schoolName;
```
JPA에서는 기본적으로 엔티티 클래스의 필드 이름을 자동으로 데이터베이스의 컬럼 이름으로 사용하기 때문에,  
위와 같이 카멜 케이스와 스네이크 케이스가 다른 경우 @Column 어노테이션의 name 속성으로 컬럼명을 지정해주었다.  

2. 기본값 초기화 설정  
0이나 false와 같이 기본값으로 초기화해놓는 게 좋은 column은 아래와 같이 수정  
```java
@Column(name="like_num")
@Builder.Default
private Long likeNum=0L;
```
`@Builder` 어노테이션과 같이 사용했기 때문에 위와 같이 `@Builder.Default`도 함께 썼다.  
`@Builder` 어노테이션만 사용할 경우 필드에 직접 할당된 기본값들은 무시되기 때문에, `@Builder` 어노테이션 사용 시 해당 필드에 대한 기본값을 명시적으로 설정하려면 `@Builder.Default`를 써야 한다.  

3. BaseEntity 사용  
`@MappedSuperclass` 어노테이션을 이용해 BaseEntity로 모든 entity에 반복적으로 입력했던 `createdAt`과 `updatedAt`을 한 번에 구현하였다.  
```java
@Getter
@MappedSuperclass //테이블과 매핑되지 않고 자식 클래스에 엔티티의 매핑 정보를 상속하기 위해 사용됨
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @CreatedDate
    private Timestamp createdAt;

    @LastModifiedDate
    private Timestamp updatedAt;
}
```

4. 양방향 매핑 수정  
2주차 과제에서 `Could not determine recommended JdbcType for ~`라는 에러가 떴었다.  
조사해보니 entity 연관관계 매핑 과정에서 문제가 발생했을 확률이 높다고 해서 양방향 매핑을 다 넣어본 거였는데, 알고 보니 다른 entity에서 @JoinColumn만 정의하고 연관관계를 명시 안해놔서 생긴 문제였다...😅  
그래서 3주차에서 refactoring할 때 필요한 부분에만 양방향 매핑이 들어가도록 수정하였다.  

5. `@Setter` 삭제  
`@Setter`를 사용하면 의도를 파악하기 힘들기 때문에 메서드 이름만으로 의도를 파악할 수 있도록 구성해야 한다.  
```java
user.setEmail("hwangdo@exam.com");
```
위와 같은 기본적인 Setter의 경우 새로 생긴 User의 이메일을 설정해 주는 것인지, 기존 User의 이메일을 업데이트 해주는 것인지 이 코드만으로는 이해하기 어렵다.  
이렇게 의도가 불분명한 메서드는 프로젝트 규모가 커질수록 의도치 않은 실수가 발생할 확률이 높아진다. 이는 객체의 일관성을 보장해주지 않는다.  
```java
public void updateUserEmail(String email){
	this.email = email;
}
```
위와 같이, 사용하는 사람 입장에서 정확한 의도를 파악할수 있도록 메서드 이름을 정의하는것이 좋다.  

6. Optional의 사용  
아래의 코드와 같이 return형을 `User`에서 `Optional<User>`로 수정하였다.  
```java
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
```
Optional로 감싸면 메소드가 결과값을 반환하지 않을 경우 Optional.empty()를 반환한다.  
이는 null 체크를 강제로 하므로 NullPointerException(실제 값이 아닌 null을 가지고 있는 객체/변수를 호출할 때 발생하는 예외)을 방지할 수 있기 때문에 Optional을 사용하는 것이 좋다.  

7. `GenerationType.AUTO`에서 `GenerationType.IDENTITY`로 변경  
모든 entity의 pk인 id를 `@GeneratedValue(strategy=GenerationType.IDENTITY)`로 수정하였다.  
사실 처음에 조사를 했을 때는 AUTO가 DB 종류에 상관없이 가장 적합한 방식을 선택하도록 한다고 해서 베스트인줄 알고 처음에 다 AUTO로 했었다.  
하지만 나중에 더 찾아보니 IDENTITY가 구현이 더 간단하고 DB에 맡기는 방식이라 더 편리하다고 하여 3주차 Refactoring에서는 모두 IDENTITY로 수정하였다.  
---

## Service Layer Test
![스크린샷 2024-03-24 224956](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/50957e8b-06b9-4a94-b779-90c284c7e521)
- 구현한 Service 중 '게시글 저장', '게시글 좋아요 추가', '게시글 좋아요 삭제' 기능에 대해 테스트를 진행하였다.
- 위의 사진에서도 확인가능하듯이, 모두 테스트를 통과하였다.

## N+1 Test
- UserRepository  
```java
@Query("select distinct u from User u join fetch u.school")
List<User> findUserFetchJoin();

@EntityGraph(attributePaths = {"school"})
@Query("select u from User u")
List<User> findUserEntityGraph();
```
N+1 문제를 테스트 하기 위해 위와 같이 1) JPQL fetch join 방법과 2) EntityGraph 방법을 모두 작성하였다.  
그리고 테스트 결과는 다음과 같다.  
1. `fetch = fetchtype.LAZY`만 쓴 경우  
```java
    select
        u1_0.user_id,
        u1_0.created_at,
        u1_0.department,
        u1_0.email,
        u1_0.is_active,
        u1_0.login_at,
        u1_0.login_id,
        u1_0.login_password,
        u1_0.nickname,
        u1_0.school_id,
        u1_0.student_id,
        u1_0.updated_at,
        u1_0.username 
    from
        user u1_0
Hibernate: 
    select
        u1_0.user_id,
        u1_0.created_at,
        u1_0.department,
        u1_0.email,
        u1_0.is_active,
        u1_0.login_at,
        u1_0.login_id,
        u1_0.login_password,
        u1_0.nickname,
        u1_0.school_id,
        u1_0.student_id,
        u1_0.updated_at,
        u1_0.username 
    from
        user u1_0
user = user1
2024-03-24T22:53:06.498+09:00 DEBUG 15220 --- [    Test worker] org.hibernate.SQL                        : 
    select
        s1_0.school_id,
        s1_0.created_at,
        s1_0.school_name,
        s1_0.student_num,
        s1_0.updated_at 
    from
        school s1_0 
    where
        s1_0.school_id=?
Hibernate: 
    select
        s1_0.school_id,
        s1_0.created_at,
        s1_0.school_name,
        s1_0.student_num,
        s1_0.updated_at 
    from
        school s1_0 
    where
        s1_0.school_id=?
user.getSchool().getSchoolName() = Sogang
user = user2
user.getSchool().getSchoolName() = Sogang
user = user3
2024-03-24T22:53:06.503+09:00 DEBUG 15220 --- [    Test worker] org.hibernate.SQL                        : 
    select
        s1_0.school_id,
        s1_0.created_at,
        s1_0.school_name,
        s1_0.student_num,
        s1_0.updated_at 
    from
        school s1_0 
    where
        s1_0.school_id=?
Hibernate: 
    select
        s1_0.school_id,
        s1_0.created_at,
        s1_0.school_name,
        s1_0.student_num,
        s1_0.updated_at 
    from
        school s1_0 
    where
        s1_0.school_id=?
user.getSchool().getSchoolName() = Psick
user = user4
user.getSchool().getSchoolName() = Psick
```
각 사용자의 학교 이름을 출력하기 위해 user.getSchool().getSchoolName()을 호출할 때마다, Hibernate는 DB에서 해당 사용자의 School 엔티티를 가져오기 위한 추가 쿼리를 실행한다.  
이 테스트에서는 4명의 사용자가 있으므로, 최초의 User 조회 쿼리 이후 총 4번의 추가 쿼리가 발생하고 있다.  
user 를 조회할 때 School 쿼리가 안 나갈 뿐 어차피 school 들을 조회할 때 N개의 쿼리가 추가적으로 나가게 되기 때문에 결국은 지연로딩이 N+1문제를 완벽히 해결한다고 보기에는 어렵다. 

2. JPQL을 사용한 경우  
```java
    select
        distinct u1_0.user_id,
        u1_0.created_at,
        u1_0.department,
        u1_0.email,
        u1_0.is_active,
        u1_0.login_at,
        u1_0.login_id,
        u1_0.login_password,
        u1_0.nickname,
        s1_0.school_id,
        s1_0.created_at,
        s1_0.school_name,
        s1_0.student_num,
        s1_0.updated_at,
        u1_0.student_id,
        u1_0.updated_at,
        u1_0.username 
    from
        user u1_0 
    join
        school s1_0 
            on s1_0.school_id=u1_0.school_id
Hibernate: 
    select
        distinct u1_0.user_id,
        u1_0.created_at,
        u1_0.department,
        u1_0.email,
        u1_0.is_active,
        u1_0.login_at,
        u1_0.login_id,
        u1_0.login_password,
        u1_0.nickname,
        s1_0.school_id,
        s1_0.created_at,
        s1_0.school_name,
        s1_0.student_num,
        s1_0.updated_at,
        u1_0.student_id,
        u1_0.updated_at,
        u1_0.username 
    from
        user u1_0 
    join
        school s1_0 
            on s1_0.school_id=u1_0.school_id
user = user1
user.getSchool().getSchoolName() = Sogang
user = user2
user.getSchool().getSchoolName() = Sogang
user = user3
user.getSchool().getSchoolName() = Psick
user = user4
user.getSchool().getSchoolName() = Psick
```
(inner) join을 사용하여 User와 School 테이블을 join하고 있기 때문에, 처음 데이터를 가져올 때 관련된 모든 데이터를 한 번의 쿼리로 가져온다.  
이로 인해, 각 User의 School 정보에 접근할 때 별도의 쿼리가 발생하지 않고 있다.  
이와 같은 join을 통해 N+1문제를 해결할 수 있다.  

3. `@EntityGraph`를 사용한 경우  
```java
    select
        u1_0.user_id,
        u1_0.created_at,
        u1_0.department,
        u1_0.email,
        u1_0.is_active,
        u1_0.login_at,
        u1_0.login_id,
        u1_0.login_password,
        u1_0.nickname,
        s1_0.school_id,
        s1_0.created_at,
        s1_0.school_name,
        s1_0.student_num,
        s1_0.updated_at,
        u1_0.student_id,
        u1_0.updated_at,
        u1_0.username 
    from
        user u1_0 
    left join
        school s1_0 
            on s1_0.school_id=u1_0.school_id
Hibernate: 
    select
        u1_0.user_id,
        u1_0.created_at,
        u1_0.department,
        u1_0.email,
        u1_0.is_active,
        u1_0.login_at,
        u1_0.login_id,
        u1_0.login_password,
        u1_0.nickname,
        s1_0.school_id,
        s1_0.created_at,
        s1_0.school_name,
        s1_0.student_num,
        s1_0.updated_at,
        u1_0.student_id,
        u1_0.updated_at,
        u1_0.username 
    from
        user u1_0 
    left join
        school s1_0 
            on s1_0.school_id=u1_0.school_id
user = user1
user.getSchool().getSchoolName() = Sogang
user = user2
user.getSchool().getSchoolName() = Sogang
user = user3
user.getSchool().getSchoolName() = Psick
user = user4
user.getSchool().getSchoolName() = Psick
```
LEFT JOIN을 통해 초기 쿼리 실행 시 User 정보와 함께 관련된 School 정보도 함께 가져온다. 따라서, 추가적인 쿼리 없이도 필요한 모든 데이터에 접근할 수 있다.  
이와 같은 join을 통해 N+1문제를 해결할 수 있다.  

---
# CEOS 19th 4th Assignment - Everytime Controller Layer
## Refactoring

1. `@Column`에서 스네이크 케이스 명명한 것 삭제  
entity의 column 작성 단계에서 각 column의 이름을 카멜 케이스로 작성된 자바의 변수를 자동으로 스네이크 케이스로 변경해주기 때문에 일일이 스네이크 케이스로 명명한 것을 삭제
```java
@Column(nullable = false)
private String courseCode;
```
위와 같이 `nullable = false`와 같은 조건이 있는 경우에만 `@Column`을 작성하고, 특별한 조건이 없다면 `@Column`없이 변수만 작성  

2. 연산자 앞뒤로 공백 한 칸씩 추가  
```java
//강의 요일
@Enumerated(EnumType.STRING)
@Column(nullable = false)
private DayOfWeek classDay;
```
위와 같이 코드의 가독성을 위해 '='과 같은 연산자의 앞뒤로 한 칸씩 공백을 생성함  

3. `@XToOne`은 즉시로딩이 default이므로 지연로딩으로 변경, `@XToMany`는 지연로딩이 default이므로 굳이 작성X  
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "timetable_id")
private Timetable timetable;
```
  
4. import문에서 와일드카드 삭제  
IntelliJ에서는 같은 라이브러리의 import가 5개 이상이 되면 자동적으로 와일드 카드 import로 바꾸어준다는 사실을 이번 주차 피드백을 통해 알 수 있었다.
import에서 와일드 카드는 (런타임 속도에는 미치는 영향이 없지만) 컴파일 속도는 느려진다는 측면에서 google java 컨벤션에서는 지양해야 하는 요소로 지정하고 있다는 사실도 새롭게 알 수 있었다.  
아래 참고 자료를 통해 IntelliJ에서 5개 이상되면 자동으로 와일드카드로 변경하는 기능을 100개 이상으로 하한선을 높임으로써 해제하였다.  
[참고-와일드카드 해제] https://velog.io/@hooni_/%EC%99%80%EC%9D%BC%EB%93%9C-%EC%B9%B4%EB%93%9C-import  
[참고-와일드카드을 지양해야 하는 이유] https://tharakamd-12.medium.com/is-it-bad-to-use-wildcard-imports-in-java-1b46a863b2be  

6. `@AllArgsConstructor` 삭제  
`@AllArgsConstructor`는 모든 필드를 인자로 입력받는 생성자를 만들어준다는 편리함은 있지만, 개발 중간에 개발자가 같은 타입인 필드들의 순서를 변경하는 경우 치명적일 수 있다는 치명적인 단점이 있다. 이 단점 때문에 해당 어노테이션이 지양되어야 한다는 것을 이번 주차 피드백을 통해 알 수 있었다.  
예를 들어, 어떤 객체가 2개의 String 타입을 필드로 가지고 있고, 이에 대한 생성자를 외부에서 사용하고 있다는 상황이 있다고 가정하자. 해당 객체의 필드 순서가 개발자의 실수로 중간에 변경되어도 컴파일 에러가 발생하지 않고, 추후 인지하지 못한 치명적인 버그가 발생할 수 있는 여지가 있기 때문에 위험하다.  
[참고] https://zrr.kr/iwD2  

- `@AllArgsConstructor` 사용 지양에 따른 리팩토링  
앞서 서술한 이유로 `@AllArgsConstructor`를 제거하면 `@Builder`와 `@NoArgsConstructor`를 사용해야 하는데 이 둘을 클래스 정의 이전에 같이 사용하면 모든 필드를 인자로 갖는 생성자가 없기 때문에 build시 오류가 발생!  
--> 해결 방법: 원하는 필드를 설정할 생성자를 만들되 private으로 막고 생성자 레벨에 `@Builder`를 사용하고, 클래스 레벨에 `@NoArgsConstructor`를 사용
이 해결 방법을 적용하면 아래와 같은 코드가 된다.
```java
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AddedCourse extends BaseTimeEntity {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addedCourseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_id")
    private Timetable timetable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @Builder
    public AddedCourse(Long addedCourseId, Timetable timetable, Course course) {
        this.addedCourseId = addedCourseId;
        this.timetable = timetable;
        this.course = course;
    }
}
```

- `@Builder.Default`삭제
위와 같이 `@Builder`의 위치를 클래스 내부로 이동시키면서 초기화 단계에서 필요했던 `@Builder.Default`를 삭제할 수 있었다.
[참고] https://seungyong.tistory.com/39  

6. `@Repository` 삭제  
이번주차 과제에서 JpaRepository를 사용할 때 `@Repository`를 명시적으로 붙이지 않아도 된다는 피드백을 받고 그 이유가 궁금해져서 조사해봤다.
JpaRepository 인터페이스가 스프링 데이터 JPA의 일부이기 때문에 JpaRepository를 사용할 때 @Repository를 명시적으로 붙이지 않아도 된다. 스프링 데이터 JPA는 @Repository 없이도 레포지토리 인터페이스를 스프링 Bean으로 자동 등록하고, 스프링 데이터 JPA의 예외 변환 기능을 내장하고 있다.  
스프링 데이터 JPA는 JpaRepository, CrudRepository 등의 인터페이스를 상속받은 사용자 정의 인터페이스를 자동으로 구현한다. 이 과정에서 스프링 데이터 인프라가 자동으로 구현 클래스를 생성하고 스프링 컨테이너에 Bean으로 등록하기 때문에, 개발자가 직접 @Repository를 붙일 필요가 없는 것이다.
[참고] https://bombo96.tistory.com/67

위와 같은 이유로 JpaRepository를 사용할 때는 `@Repository`를 굳이 붙이지 않아도 된다. 따라서, 코드의 가독성을 위해 모든 JpaRepository를 상속하여 구현한 Repository에서 `@Repository`를 삭제해주었다.  

7. DTO 코드 리팩토링
- record 사용으로 간결화
	- record를 사용하면 필드별 getter가 자동으로 생성되고, 모든 필드를 인자로 하는 public 생성자도 자동으로 생성되기 때문에 불필요한 코드를 제거할 수 있다. (이외에도 equals, toString 등을 자동으로 생성)
 	- 멤버 변수가 자동으로 private final로 선언된다.

- 정적 팩토리 메서드 사용
	- 기본 생성자는 제공하지 않으므로 필요한 경우 직접 작성해줘야 한다.
 	- 이번 주차 과제의 미션인 '정적 팩토리 메서드'를 사용하여 기본 생성자를 작성해주었다.
  	- 정적 팩토리 메서드의 장점과 명명 규칙은 다음 링크를 참고하였다. -> [참고] https://zrr.kr/oLbN

위의 사항을 모두 종합하여 작성한 `PostResponseDTO`는 아래와 같다.
```java
@Builder
public record PostResponseDTO (String title, String contents){


    public static PostResponseDTO from(Post post){
        return PostResponseDTO.builder()
                .title(post.getTitle())
                .contents(post.getContents())
                .build();
    }

}
```
[참고] https://s7won.tistory.com/2  

8. 'BDDMockito' 라이브러리의 'given' 사용  
원래는 Service layer test 코드를 작성했을 때 given 단계에서 'Mockito'의 'when'을 사용했었다.
안그래도 왜 given 단계에서 왜 when을 사용하는지에 대해 이해가 어려웠는데, 피드백을 통해 'BDDMockito' 라이브러리를 사용하면 given 단계에서 'given'을 사용할 수 있다는 것을 새로 알 수 있었다.
덕분에 'BDDMockito'에 대해서도 더 조사해볼 수 있었다. 참고한 자료의 링크는 다음과 같다.  
[참고] https://velog.io/@lxxjn0/Mockito%EC%99%80-BDDMockito%EB%8A%94-%EB%AD%90%EA%B0%80-%EB%8B%A4%EB%A5%BC%EA%B9%8C
```java
@Test
void savePostTest() {
        // given
        given(postRepository.save(any(Post.class))).willReturn(post);

        //when
        Post testedResult = postService.savePost(postDTO);

        //Then
        assertThat(testedResult).isEqualTo(post);
}
```
덕분에 위와 같이 좀 더 가독성이 좋은 코드를 완성할 수 있었다.  

9. `if-else`를 `.orElseThrow()`로 변경  
`if-else`문을 `.orElseThrow()`로 변경하면 훨씬 더 간결하고 가독성 좋은 코드가 된다는 것을 이번 피드백을 기회로 확실히 이해하고 넘어갈 수 있었다.  
원래 코드는 아래와 같았다.
```java
@Transactional
    public Comment deleteSubcomment(long commentId) {
        //commentId로 게시글 조회 -> 존재하는지 Optional을 통해 확인
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();

            comment.deleteSubcomment(comment.getCommentId());
            comment = commentRepository.save(comment); // 변경 사항 저장
            return comment;
        } else {
            // 댓글을 찾을 수 없는 경우, 적절한 예외 처리 또는 로직을 구현
            throw new RuntimeException("Not Found: " + commentId);
        }
```
하지만 피드백을 기반으로 고친 후에는 아래와 같이 간결해졌다.
```java
@Transactional
    public Comment deleteSubcomment(long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new IllegalArgumentException("Not Found: "+commentId));
        comment.deleteSubcomment(comment.getCommentId());
        comment = commentRepository.save(comment); // 변경 사항 저장
        return comment;
    }
```
  
## Controller layer
아래와 같이 4개의 CRUD API 구현을 완료하였다. 
```java
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/posts")
public class PostController {

    private final PostService postService;

    //1. CREATE: 새로운 게시글 생성을 요청하는 API 만들기
    @PostMapping
    public ResponseEntity<Post> addPost(@Validated @RequestBody PostDTO postDTO) {
        Post savedPostDTO = postService.savePost(postDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPostDTO);
    }

    //2. READ: 모든 게시글을 조회하는 API 만들기
    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> findAllPosts() {
        List<PostResponseDTO> posts = postService.findAll().stream().map(PostResponseDTO::from).toList();
        return ResponseEntity.ok().body(posts);
    }

    //3. READ: 특정 게시글만 조회하는 API 만들기
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDTO> findPost(@PathVariable long postId){
        Post post = postService.getPostById(postId);
        return ResponseEntity.ok().body(new PostResponseDTO(post.getTitle(), post.getContents()));
    }

    //4. DELETE: 특정 게시글을 삭제하는 API
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

}
```

## 전역 예외 처리 (Global Exception)
아직 모든 에러를 다 처리할 수 있을 만큼 완벽하지는 않지만, 주요 에러들만 전역 예외 처리로 해결할 수 있게 아래와 같이 구현해보았다.
```java
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * '@Validated'으로 binding error 발생시 발생
     * 주로 @RequestBody, @RequestPart 어노테이션에서 발생
     * MethodArgumentNotValidException: request body의 데이터가 유효하지 않을 때 발생하는 에러
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException: ", e); // 로그 기록 추가
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.REQUEST_BODY_MISSING_ERROR);
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }

    /**
     * JPA를 통해 DB 조작시 발생
     * ConstraintViolationException : 제약 조건 위배되었을 때 발생
     * DataIntegrityViolationException : 데이터의 삽입/수정이 무결성 제약 조건을 위반할 때 발생
     */
    @ExceptionHandler(value = { ConstraintViolationException.class, DataIntegrityViolationException.class})
    protected ResponseEntity<ErrorResponse> handleDataException(Exception e) {
        log.error("DataException: ", e); // 로그 기록 추가
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.DUPLICATE_RESOURCE);
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }


    /**
     * HttpRequestMethodNotSupportedException: 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException: ", e); // 로그 기록 추가
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.METHOD_NOT_ALLOWED);
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }

    /**
     * CustomException: Business Logic 수행 중 발생시킬 커스텀 에러
     * 여기서 CustomException은 'PostNotFoundException'으로 게시글이 발견되지 않을 때 발생하는 에러를 처리한다
     */
    @ExceptionHandler(value = { PostNotFoundException.class })
    protected ResponseEntity<ErrorResponse> handleCustomException(PostNotFoundException e) {
        log.error("PostNotFoundException: ", e); // 로그 기록 추가
        ErrorResponse response = new ErrorResponse(ErrorCode.NOT_FOUND_ERROR); // CustomException에 ErrorCode Enum 반환
        return ResponseEntity.status(response.getStatus()).body(response);
    }



    /**
     * 위에 해당하는 예외에 해당하지 않을 때 모든 예외를 처리하는 메소드
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unhandled exception:", e); // 예외 정보를 로그로 기록
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }

}
```
[참고] https://velog.io/@u-nij/Spring-%EC%A0%84%EC%97%AD-%EC%98%88%EC%99%B8-%EC%B2%98%EB%A6%AC-RestControllerAdivce-%EC%A0%81%EC%9A%A9  
[참고] https://blog.naver.com/hj_kim97/222838956315  


## Swagger 연동
처음에 swagger 2.x.x 버전을 시도했다가 spring boot와의 버전 불일치로 인해 에러가 발생하면서 swagger와의 연동에 실패하였다.  
조사해보니 **Spring Boot의 버전이 3.X.X 이상으로 버전 업되면서 기존 SpringFox가 호환되지 않아 SwaggerConfig 설정이 꼬이는 문제** 때문이었다.  
'https://twojun-space.tistory.com/201' 이 참고자료를 통해 해당 에러를 해결할 수 있었다.  
![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/50897394-6fd2-4f25-b0d1-67668927c5b1)

---
# CEOS 19th 5th Assignment - Spring Security & Login
## JWT 인증(Authentication) 방법
### JWT를 이용한 인증 방식(액세스토큰, 리프레쉬 토큰)
- 액세스 토큰과 리프레쉬 토큰 생성
1. 클라이언트가 서버에게 인증을 요청
2. 서버는 클라이언트로부터 전달받은 정보를 바탕으로 인증 정보가 유효한지 확인한 뒤, 액세스 토큰과 리프레쉬 토큰을 만들어서 클라이언트에게 전달
```java
//Authentication 객체의 권한 정보를 이용하여 액세스 토큰을 생성
    public String createAccessToken(Authentication authentication) {

        //authorities 설정
        String authorities =
                authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(","));

        //토큰 만료 시간 설정
        long now = (new Date()).getTime(); //현재 시각 구하기
        Date validDate = new Date(now + this.tokenValidityInMilliseconds); //현재 시각 + 토큰 유효 기간

        return Jwts.builder()
                .setSubject(authentication.getName()) //토큰의 용도를 명시
                .claim(AUTHORITY_KEY, authorities)
                .signWith(SignatureAlgorithm.HS512, key) //sign시 사용할 알고리즘과 key값
                .setExpiration(validDate) //토큰 만료시간 설정
                .compact(); //토큰을 생성
    }
```
3. 클라이언트는 서버로부터 전달받은 액세스 토큰과 리프레쉬 토큰을 저장
4. 서버에서 생성했던 리프레쉬 토큰은 DB에도 저장해두었다가 계속 씀

- 이후 API 요청과 응답 과정
[액세스 토큰이 만료가 안된 경우]
1. 클라이언트가 인증을 필요로 하는 API를 호출할 때마다 클라이언트는 갖고 있던 액세스 토큰과 함께 서버에게 API를 요청
2. 서버에서 액세스 토큰이 유효한지 검사 -> 만료되지 않았다면 응답
```java
// JWT 토큰 유효성 검증 메서드
    public boolean validateAccessToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key) //비밀값으로 복호화
                    .build().parseClaimsJws(token);
            return true;
        }
        //복호화 과정 시도했다가 에러가 나면 아래와 같이 경우에 따라 유효하지 않은 토큰으로 처리
        catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {log.info("잘못된 JWT 서명입니다.");}
        catch (ExpiredJwtException e) { log.info("만료된 JWT 토큰입니다."); }
        catch (UnsupportedJwtException e) { log.info("지원되지 않는 JWT 토큰입니다."); }
        catch (IllegalArgumentException e) { log.info("잘못된 JWT 토큰입니다."); }
        return false;
    }
```

[액세스 토큰이 만료된 경우]
1. 시간이 지나고 액세스 토큰이 만료된 이후 클라이언트에서 서버에 API 요청
2. 서버에서 액세스 토큰이 유효한지 검사 -> 만료되었다면 토큰 만료로 인한 에러 응답을 전달
3. 클라이언트에서는 이 응답을 받고 저장해둔 리프레쉬 토큰과 함께 새로운 액세스 토큰을 발급하는 요청을 서버에 전송
4. 서버에서는 전달받은 리프레쉬 토큰의 유효성을 검사하고, DB에서 리프레쉬 토큰을 조회한 후 저장해두었던 리프레쉬 토큰과 같은지 확인
```java
//Authentication 객체의 권한 정보를 이용하여 액세스 토큰을 생성
    public String createAccessToken(Authentication authentication) {

        //authorities 설정
        String authorities =
                authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(","));

        //토큰 만료 시간 설정
        long now = (new Date()).getTime(); //현재 시각 구하기
        Date validDate = new Date(now + this.tokenValidityInMilliseconds); //현재 시각 + 토큰 유효 기간

        return Jwts.builder()
                .setSubject(authentication.getName()) //토큰의 용도를 명시
                .claim(AUTHORITY_KEY, authorities)
                .signWith(SignatureAlgorithm.HS512, key) //sign시 사용할 알고리즘과 key값
                .setExpiration(validDate) //토큰 만료시간 설정
                .compact(); //토큰을 생성
    }
```

5. 유효한 리프레쉬 토큰이라면 -> 서버에서는 새로운 액세스 토큰을 생성한 뒤 클라이언트에게 응답
```java
    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if(!tokenProvider.validateAccessToken(refreshToken)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_ERROR);
        }

        // DB에 저장되어 있던 refresh token과 일치하는지 확인
        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userService.findById(userId);

        Authentication authentication = tokenProvider.getAuthentication(refreshToken);
        return tokenProvider.createAccessToken(authentication);
    }
```

### 쿠키
- 정의: 사용자가 어떠한 웹 사이트를 방문했을 때 웹사이트가 사용하는 서버에서 유저의 로컬 환경에 저장하는 작은 데이터.
- 용도: 이전에 해당 웹사이트를 방문한 적이 있는지 확인, 이전의 로그인 기록을 통해 로그인 정보도 유지 가능
- 구성: '키'와 '값'으로 구성되어 있으며 '만료 기간', '도메인' 등의 정보도 함유하고 있음.
- 쿠키가 추가되는 과정: 브라우저에서 "GET /members"와 같이 요청 -> 서버에서 "Set-Cookie: member_id=1"과 같이 쿠키 설정 -> 브러우저에 해당 쿠키 저장 -> 나중에 해당 서버에 방문 시 저장해두었던 쿠키 정보 조회 가능
  
### OAuth
- 정의: 네이버나 카카오와 같은 제3의 서비스에 계정 관리를 맡기는 방식
- 과정: 인증 서버에서 발급 받은 토큰을 사용해서 리소스 서버에 리소스 오너의 정보를 요청하고 응답받아 사욛
- 리소스 오너 정보 취득 방법
  	1. 권한 부여 코드 승인 타입: 가장 잘 알려진 인증 방법으로, 클라이언트가 리소스에 접근할 때 권한에 접근할 수 있는 코드와 리소스 오너에 대한 액세스 토큰을 발급받는 방식
  	2. 암시적 승인 타입: 서버가 없는 자바스크립트 웹 클라이언트에서 주로 사용하는 방식. 클라이언트의 요청을 받으면 리소스 오너의 인증 과정 이외에는 권한 코드 교환 등의 별다른 인증과정을 거치지 않고 액세스 토큰을 제공받는 방식
  	3. 리소스 소유자 암호 자격증명 승인 타입: 클라이언트의 패스워드를 사용해서 액세스 토큰에 대한 사용자의 자격 증명을 교환하는 방식
  	4. 클라이언트 자격증명 승인 타입: 클라이언트가 컨텍스트 외부에서 액세스 토큰을 얻어서 특정 리소스에 접근을 요청할 때 사용하는 방식

---
# CEOS 19th 6th Assignment - Docker
## 도커 컨테이너와 이미지
### 도커를 사용하는 이유
- 기존의 클라우드 환경 이주 방법  
	- Iaas(서비스로서의 인프라): 어플리케이션의 각 컴포넌트가 모두 가상 머신에서 독립적으로 동작. 특정 클라우드에 종속되지는 않기 때문에 이주 과정을 쉽지만, 가상 머신의 성능을 완전히 활용하지 못하며 운영비가 비싸다는 단점이 있다. 
   	- PaaS(서비스로서의 플랫폼): 어플리케이션의 각 컴포넌트를 하나씩 클라우드의 Managed Service로 옮기는 복잡한 과정이 필요. 운영비가 저렴하고 관리가 쉽다는 장점도 있다.
- 도커가 각광받는 이유: 위에서 언급된 단점들이 없는 선택지를 제공. 어플리케이션의 각 컴포넌트를 컨테이너로 이주한 다음 동작되기 때문에 이들 컴포넌트는 가상 머신처럼 독립적이지만 경량이며 Paas의 Managed Service만큼 효율적이다. 컨테이너로 이주된 각 컴포넌트들은 Azure Kubernetes Service나 Amazon Elastic Container Service 혹은 직접 구축한 docker cluster에서 전체 어플리케이션을 수행할 수 있는데, 이렇게 도커화된 어플리케이션은 이식성이 뛰어나다는 장점도 있다. 

### 도커의 기본적인 사용법
- 도커 엔진 동작 확인  
![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/76492184-d311-451f-8c2c-e8dace601e0d)
  
-도커 컴포즈 확인  
![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/49647da0-2b51-4a9a-82bf-a8ae4f25465f)

- (예제) 도커 이미지 다운받고 컨테이너로 어플리케이션을 실행  
![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/c8a1bf82-4893-4090-9baa-29b3c3569eb2)
`container run diamol/ch02-hello-diamol`이라는 명령어를 입력하면 위의 사진과 같이 결과가 나온다.  
```console
latest: Pulling from diamol/ch02-hello-diamol
31603596830f: Pull complete
93931504196e: Pull complete
d7b1f3678981: Pull complete
Digest: sha256:c4f45e04025d10d14d7a96df2242753b925e5c175c3bea9112f93bf9c55d4474
Status: Downloaded newer image for diamol/ch02-hello-diamol:latest
```
이 컴퓨터에는 현재 diamol/ch02-hello-diamol 이미지가 없으므로 내려받는다는 의미이다.  
```console
---------------------
Hello from Chapter 2!
---------------------
My name is:
a48e29387b83
---------------------
Im running on:
Linux 5.15.146.1-microsoft-standard-WSL2 x86_64
---------------------
My address is:
inet addr:172.17.0.2 Bcast:172.17.255.255 Mask:255.255.0.0 inet6 addr: fe80::42:acff:fe11:2/64 Scope:Link
---------------------
```
이미지를 이용해 컨테이너를 실행하여, 그 결과를 출력한 부분에 해당하는 로그이다. 컴퓨터의 이름, 운영체제 종류, 네트워크 주소를 출력하고 있다.  
이처럼 아주 간단한 어플리케이션이지만 이 과정에서 도커를 사용하는 플로우를 확인할 수 있다.  
1. 빌드: 어플리케이션을 컨테이너에서 수행할 수 있도록 패키징하기 
2. 공유: 타인이 패키지를 사용할 수 있도록 공유
3. 실행: 해당 패키지를 내려받은 사람이 컨테이너를 통해 어플리케이션을 실행

- 같은 예제를 같은 명령어로 다시 실행해보면?  
![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/cf2c272a-9f77-48bd-8511-889eeab71782)
조금 전 이미지를 이미 내려받았기 때문에 이미지를 내려받는 과정에 대한 부분이 사라지고, 바로 컨테이너를 실행하는 메시지가 출력된다.  
같은 컴퓨터를 사용하므로 컨테이너가 출력하는 내용 중 운영체제 종류는 같고 컴퓨터 이름과 네트워크 주소에 대한 내용은 달라진 상태이다.

- 컨테이너란?  
![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/2d191a62-7279-4ee7-b6e0-c788218c06ee)  
도커 컨테이너는 어플리케이션과 어플리케이션을 실행할 컴퓨터(IP 주소, 컴퓨터 이름, 디스크 드라이브)가 들어있다고 생각하면 된다.  
IP 주소, 호스트명, 디스크 드라이브, 파일 시스템까지 이들은 모두 도커가 만들어낸 가상 리소스이다. 이들이 엮여서 어플리케이션이 동작하는 방식이다.  
도커는 양립이 불가능해 보이는 장점을 모두 갖는다.  
1. 밀집: 컴퓨터에 CPU와 메모리가 허용하는 한 되도록 많은 수의 어플리케이션을 실행하도록 하는 것
2. 격리: 서로 다른 어플리케이션은 독립된 환경에서 실행되어야 하는 것

가상머신도 '격리'된 환경에서 어플리케이션을 작동하게는 할 수 있지만, 컨테이너와 달리 호스트 컴퓨터의 운영체제를 공유하지 않고 각자 별도의 운영체제를 필요로 하기 때문에 '밀집'이라는 목표는 달성하지 못한다는 단점이 있다.  

- 컨테이너를 원격 컴퓨터처럼 이용  
![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/a6a9fa5a-e1fd-450c-a557-e07c3adc1955)
docker container run --interactive --tty diamol/base`라는 명령어를 입력하면 위의 사진과 같이 사용할 수 있다.  
1. `--interactive`: 컨테이너에 접속
2. `--tty`: 터미널 세션을 통해 컨테이너를 조작

즉, 위의 명령어를 입력하면 diamol/base라는 이미지로부터 대화식 컨테이너를 실행하게 되는 것이다.  
사진에서도 확인할 수 있듯이, hostname과 date를 입력했더니 해당 정보를 바로 출력한다.  

- 실행 중인 컨테이너 정보 확인  
![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/4ddd3439-e26a-47be-be7e-85dbea1b07bc)
새로운 터미널 세션을 열고 `docker container ls`를 입력했더니 위의 사진과 같이 현재 실행 중인 컨테이너 정보를 확인할 수 있다.  
이때 container ID가 좀 전에 컨테이너 내부에서 hostname으로 확인한 호스트명과 동일한 것도 확인할 수 있다.  

- 컨테이너를 사용해 웹 사이트 호스팅  
![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/11c0a5fb-79f1-4b22-9adf-7641ccbadf64)  
위의 사진과 같이 `docker container run --detach --publish 8088:80 diamol/ch02-hello-diamol-web`라는 명령어를 입력하면 컨테이너는 종료되지 않은 채로 백그라운드에서 계속 동작하게 된다.  
정말 종료되지 않은 채로 동작하고 있는지 확인하려면 아래 사진과 같이 `docker container ls`를 입력해보면 된다.  
![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/fae4ef99-0e3f-4590-a7e0-aa11d424c813)  
이 컨테이너를 만드는 데 사용된 diamol/ch02-hello-diamol-web이라는 이미지는 apache 서버와 간단한 HTML 페이지를 담고 있다.  
이 이미지로 컨테이너를 실행하면 실제 웹 서버를 통해 웹 페이지가 작동한다. 이처럼 컨테이너가 백그라운드에서 동작하면서 네트워크를 주시(listen)하게 하려면 다음과 같은 플래그를 추가하면 된다.  
1. `--detach`: 컨테이너를 백그라운드에서 실행하며 컨테이너 ID를 출력
2. `--publish`: 컨테이너의 포트를 호스트 컴퓨터에 공개

![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/e93d35ef-38ab-4db1-bec9-690125b5a6bb)  
위의 명령어를 정리하자면 다음과 같다.  
도커는 호스트 컴퓨터의 8088포트로 들어오는 네트워크 트래픽을 주시하다가 필요한 트래픽을 컨테이너의 80 포트로 전달한다.  
`--publish`라는 플래그 덕분에 호스트 컴퓨터의 물리 네트워크 주소가 컨테이너의 가상 네트워크 주소에 접근할 수 있는 것이다. 왜냐하면 이 가상 주소는 도커 내부에만 존재하는 주소이기 때문이다. 하지만, `--publish`라는 플래그를 통해 컨테이너의 포트가 공개되었으므로 컨테이너로 트래픽을 전달할 수는 있는 것이다.  

![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/d42d7bb7-1cda-443e-9a4a-8151c204b8e5)  
호스트 컴퓨터에서 http://localhost:8088 페이지에 접근하면 위의 사진과 같이 컨테이너로부터 응답을 받을 수 있는 것을 확인할 수 있다.  

- 도커가 컨테이너를 실행하는 원리  
![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/270ed89a-d606-4974-8ad0-6155fec648e4)  
1. 도커 명령행 인터페이스(Docker CLI)는 도커 API의 클라이언트로서, 우리가 docker 명령어를 사용하면 실제로 도커 API를 호출하는 역할을 한다.
2. 도커 API를 통해 도커 엔진의 기능에 접근할 수 있다.
3. 도커 엔진은 백그라운드로 항시 동작하면서 컨테이너와 이미지를 관리한다.

### 도커 이미지 빌드하고 컨테이너 실행하기
- Dockerfile 작성하기
```Dockerfile
FROM openjdk:17
ARG JAR_FILE=/build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar", "/app.jar"]
```
Dockerfile은 어플리케이션을 패키징하여 이미지를 만들기 위한 스크립트이다.  
각 줄의 instruction은 다음을 의미한다.
1. `FROM`: 모든 이미지는 다른 이미지로부터(FROM) 출발한다. 해당 이미지는 `openjdk:17`를 시작 지점으로 설정하였다.
2. `ARG`: 도커 빌드 중에 사용되는 변수를 정의한다. 여기서는 `JAR_FILE`이라는 변수가 JAR 파일의 경로를 지정하는 역할을 한다.
3. `COPY`: 도커 빌드가 실행 중인 디렉토리에 포함된 모든 파일과 서브 디렉토리를 현재 이미지 내 작업 디렉토리로 복사한다. 이 명령은 빌드된 JAR 파일을 도커 이미지의 `/app.jar` 경로로 복사한다는 뜻이다.
4. `ENTRYPOINT`: 컨테이너가 실행될 때 실행되는 명령을 정의한다. 여기서는 Java 실행 명령을 통해 `/app.jar` 경로에 있는 JAR 파일을 실행하도록 지정한다.

- 컨테이너 이미지 빌드하기  
![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/c9655c69-779c-4b8b-8652-bc5f76d0fc73)  
`docker image build -t`라는 명령어를 통해 앞서 작성한 Dockerfile 스크립트로 이미지를 빌드할 수 있다.  
플래그 `-t`는 `--tag`의 약어로 이미지의 이름을 지정해주는 역할을 한다.  
위의 사진에서는 Dockerfile의 경로로 가서 `everytime0512`라는 이름의 이미지를 빌드하라는 뜻이다.  

![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/847f3b7a-a88a-4547-8562-ecb6d251759f)  
`docker image ls 'everytime0512'`라는 명령어를 통해, `everytime0512`이미지가 성공적으로 빌드된 것을 확인할 수 있었다.  

- 빌드된 이미지로 컨테이너 실행하기  
![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/4567d46a-43be-405e-8f08-c926831aba9f)  
`docker container run -p 8080:8080 everytime0512`라는 명령어를 통해 앞서 빌드한 이미지를 컨테이너로 실행하였다.  

근데... `Caused by: com.mysql.cj.exceptions.CJCommunicationsException: Communications link failure`라는 오류가 뜨면서 8080 포트에 연결이 안된다...  
뭐가 문제인지 계속 뒤져봤지만... 결국 해결 fail... 계속 고민해보겠습니다...ㅠㅠ  

### 도커 컴포즈로 분산 어플리케이션 실행하기
- 도커 컴포즈 파일이란?
  어플리케이션의 '원하는 상태', 즉 모든 컴포넌트가 실행 중일 때 어떤 상태여야 하는지를 기술하는 파일이다. 또한, `docker container run` 명령으로 컨테이너를 실행할 때 지정하는 모든 옵션을 한데 모아 놓은 단순한 형식의 파일이기도 하다. 
- 도커 컴포즈 파일 실행 과정: 도커 컴포즈 파일 작성 -> 도커 컴포즈 도구를 사용해 어플리케이션 실행 -> 도커 컴포즈가 컨테이너, 네트워크, 볼륨 등 모든 도커 객체를 만들도록 도터 API에 요청
- 도커 컴포즈 파일 구조
```console
version: "3"

services:
  db:
    image: mysql:5.7 #windows
    environment:
      MYSQL_ROOT_PASSWORD: mysql
      MYSQL_DATABASE: mysql
    volumes:
      - dbdata:/var/lib/mysql
    ports:
      - 3306:3306
    restart: always

  web:
    container_name: web
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - db
    environment:
      mysql_host: db
    restart: always
    volumes:
      - app:/app

volumes:
  dbdata:
  app:
```

1. `version`: 이 파일에서 사용된 도커 컴포즈 파일 형식의 버전을 말한다.
2. `services`: 어플리케이션을 구성하는 모든 컴포넌트를 열거하는 부분이다. 도커 컴포즈에서는 실제 컨테이너 대신 서비스 개념을 단위로 삼는다. 하나의 서비스를 같은 이미지로 여러 컨테이너에서 실행할 수 있기 때문이다.

- 도커 컴포즈 파일이 필요한 이유  
도커 컴포즈 파일을 통해 우리는 도커 이미지를 여러 개 띄워서 서로 네트워크도 만들어주고 컨테이너의 밖의 호스트와 연결하는 방식, 파일 시스템 공유(volumes) 방식 등을 조정할 수 있다.  
웹 백엔드, 프론트엔드, 데이터베이스를 모두 갖고 있는 어플리케이션을 패키징하려면 각 컴포넌트에 하나씩 3개의 Dockerfile 스크립트가 필요하다.  
이처럼 분산된 컴포넌트를 실행하는 데 이상적인 환경을 제공하기 위해서 도커 컴포즈 파일이 필요한 것이다. 

## API 추가 구현
### 로그인 api
- UserController
```java
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> userSignUp(AddUserRequest request) {
        userService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginDTO loginDTO) {

        LoginResponseDTO loginResponseDTO = userService.loginUser(loginDTO.userId(), loginDTO.password());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
}
```

- UserService
```java
   public LoginResponseDTO loginUser(Long userId, String password) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_ERROR));

       //패스워드 비교하여 일치여부 판단
        if(!bCryptPasswordEncoder.matches(password, user.getLoginPassword())) throw new RuntimeException();

        //LoginResponseDTO 객체 생성
        LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
                .token(tokenProvider.createToken(String.valueOf(user.getUserId()))).build();

        return loginResponseDTO;
    }
```

### 댓글 조회, 작성 api
- CommentController
```java
@RequestMapping("/post")
@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    //1. comment 작성
    @PostMapping("/{postId}/comment")
    public ResponseEntity<?> postComment(@Valid @PathVariable long postId, @RequestBody CommentDTO commentDTO) {

        Comment comment  = commentService.addNewComment(commentDTO, postId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //2.comment 리스트 조회
    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentDTO>> getComments(@Valid @PathVariable long postId) {
        List<CommentDTO> commentList = commentService.getComments(postId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
```

- CommentService
```java
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    public Comment getCommentById(long commentId) {
        return commentRepository.findByCommentId(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_ERROR));
    }


    public Comment addNewComment(CommentDTO commentDTO, long postId) {

        //게시글 조회하여 존재 여부 확인
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_ERROR));

        //댓글 엔티티 생성
        Comment comment = Comment.builder().contents(commentDTO.contents()).build();

        //댓글 엔티티를 DB에 저장
        Comment savedComment = commentRepository.save(comment);

        return savedComment;
    }

    public List<CommentDTO> getComments(Long postId) {
        return commentRepository.findByPost_postId(postId)
                .stream()
                .map(CommentDTO::fromComment)
                .collect(Collectors.toList());
    }

}
```
---
# CEOS 19th 7th Assignment - AWS
## 지난주 과제 트러블슈팅
지난주 과제에서 mysql db connection error 문제가 있었는데 아래와 같은 과정을 통해 해결할 수 있었다.  
1/ docker에 mysql을 설치해주고 container을 만들어준다.
```console
docker pull mysql
```
위의 명령어를 입력하여 mysql을 설치해준다.  
그 다음, 아래의 명령어를 입력하면 mysql이 도커에서 작동하게 된다.
```console
docker run --name mysql-container -e MYSQL_ROOT_PASSWORD=<password> -d -p 3308:3306 mysql:latest
```
![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/87f39f81-fc27-4721-9917-21c29b89f75f)

2/ application.yml 파일에서 DB url 중 `localhost` 부분을 `host.docker.internal`로 수정해준다.
```yml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://host.docker.internal:3306/everytime
```

위의 과정을 거친 후 도커에서 차례로 명령어를 입력하면 아래 사진과 같이 작동한다.  
![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/71ec6569-27cf-4388-a36e-554c23aa3a7b)
위와 같이 빌드해준 뒤, 아래와 같이 실행(run)을 하면 
![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/7a071f35-56aa-4485-9185-11252990d4d4)  
(중간 로그 생략)  
![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/9341a207-0f35-49c6-a5f8-930071a6a887)  
아래 사진과 같이 localhost:8080/swagger-ui.html로 접속했을 때 정상적으로 접속이 이루어진다.
![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/8de3f36f-e853-4b4a-af8f-90f44c850acd)


이번에는 도커에서 별도의 mysql 로드없이 docker-compose.yml 파일 하나만으로 어플리케이션을 실행시켜보았다.  
![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/e6a190fd-94d5-4584-8660-788ac10205cc)
그러기 위해 위의 사진과 같이 실행중이던 mysql 컨테이너를 중단시켰다.  

그 다음에 `docker-compose -f docker-compose.yml up --build` 명령어를 실행시켜 docker-compose.yml을 통해 어플리케이션을 실행시켜보았다.  
![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/0bae3af7-2186-48cf-b19c-dd138df53d5f)
(중간 로그 생략)  
![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/3919a596-ac98-4ed2-9140-a6a999dffa44)
위와 같이 성공적으로 빌드된 것을 확인할 수 있고, `localhost:8080/swagger-ui.html`로 접속하면 아래 사진과 같이 정상적으로 접속이 가능하다는 것도 확인할 수 있다.  
![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/068c625b-55cf-4fdc-a64c-74a00a385906)

## EC2 생성
![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/ed16814a-6b9a-4ace-9c7e-0c852eb9147c)  
위의 사진과 같이 EC2 생성을 완료하였다.  

## RDS 생성성
![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/35ef1733-acf6-4d23-bef5-b114d5cd73c6)  
EC2에 이어 RDS 생성도 완료하였다.  

## Github Actions
```yml
name: CI

## develop 브랜치에 push가 되면 실행됩니다
on:
  push:
    branches: [ "develop" ]

permissions:
  contents: read

jobs:
  build:
    runs-on: ubuntu-latest
    steps:

      - name: checkout
        uses: actions/checkout@v3

      ## JDK 17을 사용하므로 17로 수정
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'

      ## gradle build
      - name: Build with Gradle
        run: ./gradlew bootJar


      ## 웹 이미지 빌드 및 도커허브에 push
      - name: web docker build and push
        run: |
          docker login -u ${{ secrets.DOCKER_USERNAME }} -p ${{ secrets.DOCKER_PASSWORD }}
          docker build -t my-repo/my-web-image .
          docker push my-repo/my-web-image
          docker build -f dockerfile-nginx -t my-repo/my-nginx-image .
          docker push my-repo/my-nginx-image

      - name: executing remote ssh commands using password
        uses: appleboy/ssh-action@master
        with:
          host: ${{ secrets.HOST }}
          username: ubuntu
          key: ${{ secrets.KEY }}
          script: |
          
          ## 여러분이 원하는 경로로 이동합니다.
            cd /home/ubuntu/
            
          ## .env 파일을 생성합니다.
            sudo touch .env
            echo "${{ secrets.ENV_VARS }}" | sudo tee .env > /dev/null
          
          ## docker-compose.yaml 파일을 생성합니다.
            sudo touch docker-compose.yaml
            echo "${{ vars.DOCKER_COMPOSE }}" | sudo tee docker-compose.yaml > /dev/null
            
          ## docker-compose를 실행합니다.
            sudo chmod 666 /var/run/docker.sock
            sudo docker rm -f $(docker ps -qa)
            sudo docker pull my-repo/my-web-image
            sudo docker pull my-repo/my-nginx-image
            docker-compose -f docker-compose.yaml --env-file ./.env up -d
            docker image prune -f
```
위와 같이 `gradle.yml` 파일도 작성 완료하였다.  

Github에 secrets와 variables도 아래 사진과 같이 등록하였다.  
![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/806e386c-7451-4ce8-9fd3-bc9c29d2f792)  
`DOCKER_COMPOSE`는 `variables`에 docker-compose.yaml의 내용으로 저장하였다.  

## Route 53
EIP를 할당받은 뒤, '가비아'에서 도메인 주소를 구매하여 앞서 만든 EC2에 연결하였다.  
![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/9bc0d874-1d0f-4a30-bf54-0711c227d7f7)

그런 다음 도메인을 입력하여 연결을 확인하였다.  
![스크린샷 2024-05-19 201727](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/8e3127de-eacf-4d54-9698-7cbb92db357f)  

## ELB
![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/dbcd4f3a-8c3f-4ef1-a3be-5ac8c5b3ed74)  
위의 사진과 같이 Target Group을 생성하였다.  

그 다음 ALB도 생성하였다.  
![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/a393732a-cbcf-4f70-ae9d-43b9cc8f2e1b)

![스크린샷 2024-05-19 184457](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/1aa5da95-3094-424f-9b06-822a779f26d6)
도메인을 ALB에 연결한 모습이다.  

![스크린샷 2024-05-19 183829](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/7dc1adf8-b307-44cb-99d2-a980faccad63)  
ACM 인증기관에서 인증서를 발급받았다.  

![스크린샷 2024-05-19 183953](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/c3729079-5b32-4752-acae-84c73b908a44)  
리스너 추가 및 규칙 편집을 완료하였다.  

## Docker image 배포
![image](https://github.com/chlolive/CEOS-19th-spring-everytime/assets/101798714/da53df13-681f-467c-9031-d5d9beddfdc6)
