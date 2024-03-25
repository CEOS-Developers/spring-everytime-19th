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
