# 2주차 과제


## 에브리타임 

- 편리한 시간표 제작, 대학교 커뮤니티, 대학 관련 정보 공유를 목적으로 제작된 온라인 서비스


## 에브리타임 일부 기능 모델링 

### 전체적인 구조 

![img.png](img.png)

- 전체적인 구조를 위와 같이 잡았습니다.
1. 쪽지 기능을 위한 **_쪽지, 쪽지방(room) 엔티티_**
2. 게시판 생성을 위한 **_커뮤니티 엔티티_**
3. 게시글 생성 수정 삭제 조회 를 위한 **_게시글, 게시글 이미지 엔티티_**
4. 게시글에 첨가되는 부가 기능을 위한 **_게시글 좋아요, 댓글, 댓글 좋아요 엔티티_**
5. 회원가입, 다른 위 기능 등과의 상호작용 등을 위한 **_멤버 엔티티_**

---

- CRUD 학습 목적 특성으로 아래와 같은 기능은 임의로 제외했습니다 .ㅎ 
1. 대학교 관련 엔티티
2. 강의 관련 엔티티 
3. 시간표 관련 엔티티 등등 


### 세부 구조 

---

#### 게시판(커뮤니티) + 게시글 



<img src="img_5.png" width="900" height="500">



- 멤버는 여러 커뮤니티를 생성 할 수 있습니다(ex: 자유게시판 등) 따라서 멤버와 커뮤니티를 1대 N 관계로 생각하고 커뮤니티 테이블에는 커뮤니티 생성자 아이디를 외래키 값으로 잡아주었습니다.
- 한 커뮤니티에는 여러 게시글이 존재합니다. 또한 한 게시글은 여러 커뮤니티에 속할 수 없기 때문에 커뮤니티와 게시글을 1대 N 관계로 생각하였습니다. 따라서 커뮤니티 아이디 값을 게시글 테이블에 외래키로 추가하였습니다.
- 멤버는 여러 게시글을 작성할 수 있고, 게시글은 여러 멤버를 작성자로 가질 수 없습니다. 따라서 멤버와 게시글을 1대 N 관계로 생각 -> 게시글 테이블에 멤버의 아이디를 외래키 값으로 잡아 주었습니다.


---

#### 멤버 + 게시글 + 게시글 이미지


![img_7.png](img_7.png)

- 한 멤버는 여러 게시글을 작성할 수 있고 한 게시글은 여러 멤버에 의해 작성될 수 없으므로 멤버와 게시글을 1대 N 관계라고 생각했기에 게시글에 멤버 아이디를 외래키 값으로 두었습니다.
- 또한 게시글에 질문글 여부를 나타내는 자바 입장의 boolean 을 표현할 수 있는 tinyint 를 추가해서 이 값이 설정되어 있으면 지우지 못하도록 합니다. 
- 한 게시글에는 여러 이미지를 가질 수 있고 한 이미지는 여러 게시글에 의해 소유될 수 없으므로 1대 N 관계라고 생각했기에 게시글 이미지에 게시글 아이디 값을 외래키로 추가하였습니다.
- 게시글에 작성자 이름의 필드를 두어 "익명" 으로 표시할지 닉네임으로 표시할지 결정합니다. 

---

#### 게시글 부가 기능 

![img_8.png](img_8.png)

- 한 멤버는 여러 게시글에 좋아요 를 누를 수 있고 한 게시글은 여러 멤버에 의해 좋아요가 눌러질 수 있습니다. 이에 따라 N:M 다대다 관계라고 생각을 하였기 때문에 그 중간테이블인 게시글 좋아요 테이블에 멤버아이디와 게시글 아이디 값을 외래키로 잡아주었습니다. 
- 게시글 좋아요 테이블을 통해 좋아요 수를 알 수 있지만 이 방법보다 더 성능이 좋은 좋아요 수 필드를 게시글에 잡아 다로 관리합니다. 
- 한 게시글에는 여러 댓글이 달릴 수 있고 한 댓글은 여러 게시글에 의해 사용될 수 없으므로 게시글과 댓글을 1대 N 관계라고 생각 하였기 때문에 댓글 테이블에 게시글 아이디 외래키를 두었습니다.
- 한 댓글에는 여러 대댓글이 달릴 수 있고 한 대댓글은 여러 댓글에 의해 사용될 수 없으므로 댓글과 대댓글을 1대 N 관계라고 생각 하였기 때문에 댓글 테이블에 부모 답글 아이디라는 외래키 값을 두어 따로 관리합니다.
- 한 멤버는 여러 댓글,대댓글을 작성할 수 있고 댓글, 대댓글은 여러 멤버에 의해 작성될 수 없으므로 멤버와 댓글,대댓글을 1대 N 관계라고 생각하였기 때문에 멤버와 댓글 테이블에 멤버 아이디라는 외래키 값을 두었습니다.
- 한 멤버는 여러 댓글에 좋아요 를 누를 수 있고 한 댓글은 여러 멤버에 의해 좋아요가 눌러질 수 있으므로 다대다 관계라고 생각을 하여 댓글 좋아요를 중간테이블로 설정하여 멤버아이디와 답글 아이디를 외래키 값으로 설정하였습니다. 


---

#### 쪽지 기능

![img_4.png](img_4.png)

가장 헷갈리고 확신 없는 부분입니다. 


- 처음에는 멤버와 쪽지를 1대다 관계로 설정하고, 쪽지에 sender 와 receiver를 받는 것으로 해야 되겠다고 생각했으나, 이렇게 하면 한 상대방과의 채팅 기록을 조회할 떄 where (sender_id=:myId and receiver_id=:yourId) or (sender_id=:yourId and receiver_id=:myId) order by createdAt desc 이런 식으로 조회하는 것보다 방자체를 따로 관리하는게 채팅 기록조회 관점에서 더 효율적일 것이라고 생각했습니다.  
- 한 멤버는 여러 쪽지방을 생성할 수 있습니다. 이로써 1:N 관계는 확정이라고 생각했습니다. 처음 한 쪽지방은 여러 멤버 (2명) 가 들어올 수 있으므로 처음에는 다대다 관계를 생각했으나 그럼 ERD 테이블 상으로는 1대1 쪽지 인지 단체 톡방 같은 느낌인지 확신할 수 없을 것 같았습니다.
따라서 쪽지방에 first_sender_id 와 first_receiver_id 라는 외래키 필드를 둔 이후 각각의 필드의 입장에서 생각했습니다. 한 채팅방에서 first_sender 와 first_receiver 는 각각 한 명 뿐이기 때문에 멤버와 쪽지방을 1대 N 관계로 생각하였습니다. 단 1:N 관계를 두번 
- 한 쪽지방에는 여러 쪽지가 오갈 수 있고 한 쪽지는 여러 쪽지방에서 사용될 수 없으므로 쪽지방 과 쪽지가 1대 N 관계라고 생각하였습니다. 따라서 쪽지에는 쪽지방 아이디라는 외래키를 추가했습니다.
- 한 멤버는 여러개의 쪽지를 보낼 수 있고 한 쪽지는 여러 멤버에 의해 보내질 수 없으므로 멤버와 쪽지가 각각 1대 N 관계 라고 생각하였습니다. 따라서 쪽지에는 멤버 아이디라는 외래키 필드를 추가하였습니다. 


---


## JPA 관련 옵션 과제 

# 프로젝트 세팅

먼저 주의점의 관찰을 위해 프로젝트를 세팅한다.

### Member(다대일의 다쪽 연관관계)

```java
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickName;

    @Builder
    public Member(String name,String loginId,String password,String nickName,Team team){
        this.name=name;
        this.loginId=loginId;
        this.password=password;
        this.nickName=nickName;
        this.team=team;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;
}
```

### Team(다대일의 일쪽 연관관계)

```java
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Team {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Member> members=new ArrayList<>();

    @OneToMany(mappedBy = "team",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<TeamImage> teamImages=new ArrayList<>();

    @Builder
    public Team(String name){
        this.name=name;
    }

}
```


### TeamImage(다대일의 다쪽 연관관계)

Team 과 TeamImage 의 관계를 세팅한다.

```java
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TeamImage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accessUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder
    public TeamImage(String accessUrl,Team team){
        this.accessUrl=accessUrl;
        this.team=team;
    }

}
```

---


# 주의점 1: distinct를 사용해야 하는 경우


## Hibernate Version

실험 환경 기준은 하이버네이트 버전 ```5.6.14``` 버전이다.

![](https://velog.velcdn.com/images/dionisos198/post/068cf2ec-f344-4c0f-83af-ca6116e2293c/image.png)


## 사용 배경

```@ManyToOne``` 에서 다(Member) 쪽에서 일(Team)쪽을 fetch Join 을 사용하여 일(Team) 의 필드 또한 함께 영속화 할 수 있다는 것을 알게된 A는

이제는 일(Team) 을 조회할 때 그 와 연관된 다(Member) 의 필드를 함께 조회하고 싶어 일(Team) 에서 다(Member)에 Fetch Join 을 적용했다.

아래와 같이 말이다.

```java

public interface TeamRepository extends JpaRepository<Team,Long> {
    @Query("select t from Team t join fetch t.members")
    List<Team> findTeamWithFetchMembers();
}
```


## 테스트 코드 결과 예상 및 조회


A는 처음에 위 코드를 짤때 원하는 결과는 각각의 팀을 한번씩 조회하고 그와 연관된 Member 또한 같이 조회하는 결과를 원했다.

즉 아래와 같이 테스트 코드를 통해 결과를 확인하면

```java
@Test
    @DisplayName("팀과 Member 의 1대다 Fetch Join 테스트 Pageable")
    public void SecondTest() {
        Team teamA=Team.builder().name("teamA").build();
        Team teamB=Team.builder().name("teamB").build();


        Member member1=Member.builder().loginId("afdf").password("adfdf").nickName("일진우").name("이진우1").team(teamA).build();
        Member member2=Member.builder().loginId("bfdf").password("bdfdf").nickName("이진우").name("이진우2").team(teamA).build();
        Member member3=Member.builder().loginId("cfdf").password("cdfdf").nickName("삼진우").name("이진우3").team(teamA).build();
        Member member4=Member.builder().loginId("dfdf").password("ddfdf").nickName("사진우").name("이진우4").team(teamB).build();
        Member member5=Member.builder().loginId("efdf").password("edfdf").nickName("오진우").name("이진우5").team(teamB).build();

        teamA.getMembers().add(member1);
        teamA.getMembers().add(member2);
        teamA.getMembers().add(member3);
        teamB.getMembers().add(member4);
        teamB.getMembers().add(member5);

        teamRepository.save(teamA);
        teamRepository.save(teamB);


        em.clear();

        List<Team> teams=teamRepository.findTeamWithFetchMembersWithPageable(PageRequest.of(0,2));

        for(Team t: teams){
            System.out.println("팀의 이름: "+t.getName());
            System.out.println(t.getMembers().size());
        }

    }
```

즉 예상된 출력결과는 팀의 이름 A: 와 팀의 Member 에 대한 각각의 이름 + 팀의 이름 :B 와 팀 B의 Member 에 대한 각각의 이름

이렇게만 나오는 것을 예상했지만 출력결과는 그렇지 않다.

```java
팀의 이름: teamA
멤버의 이름:이진우1
멤버의 이름:이진우2
멤버의 이름:이진우3
팀의 이름: teamA
멤버의 이름:이진우1
멤버의 이름:이진우2
멤버의 이름:이진우3
팀의 이름: teamA
멤버의 이름:이진우1
멤버의 이름:이진우2
멤버의 이름:이진우3
팀의 이름: teamB
멤버의 이름:이진우4
멤버의 이름:이진우5
팀의 이름: teamB
멤버의 이름:이진우4
멤버의 이름:이진우5
```

그 이유는 쿼리 내용을 확인하면 짐작이 가능하다.

```java
select
        team0_.id as id1_1_0_,
        members1_.member_id as member_i1_0_1_,
        team0_.name as name2_1_0_,
        members1_.created_at as created_2_0_1_,
        members1_.updated_at as updated_3_0_1_,
        members1_.login_id as login_id4_0_1_,
        members1_.name as name5_0_1_,
        members1_.nick_name as nick_nam6_0_1_,
        members1_.password as password7_0_1_,
        members1_.team_id as team_id8_0_1_,
        members1_.team_id as team_id8_0_0__,
        members1_.member_id as member_i1_0_0__ 
    from
        team team0_ 
    inner join
        member members1_ 
            on team0_.id=members1_.team_id
```

team의 아이디와 member의 team_id가 같은데에 join이 일어난다.

그럼 위 같은 쿼리의 결과로 나타나는 테이블의 형태는
팀A 와 멤버 1에 대한 레코드 +
팀A 와 멤버 2에 대한 레코드 +
팀A 와 멤버 3에 대한 레코드 +
팀B 와 멤버 4에 대한 레코드 +
팀 B와 멤버 5에 대한 레코드


이런 식으로 나타날 것이다.
따라서 위와 같이 문제의 출력결과가 발생한다.
이를 방지하려면 ```distinct``` 키워드를 통해 방지할 수 있다.
이는 객체의 중복을 방지하게 한다.

## 수정

 ```java
 public interface TeamRepository extends JpaRepository<Team,Long> {
    @Query("select distinct t from Team t join fetch t.members")
    List<Team> findTeamWithFetchMembers();
}
```

## 결과 확인

```java
팀의 이름: teamA
멤버의 이름:이진우1
멤버의 이름:이진우2
멤버의 이름:이진우3
팀의 이름: teamB
멤버의 이름:이진우4
멤버의 이름:이진우5
```

위와 같이 Team 이 중복되어 조회되지 않는 것을 볼 수 있다.

## 스프링 부트 3이상에서는...

HiberName 버젼의 상승과 함께 초기처럼 코드를 작성해도 자동으로 중복은 제거해서 아래와 같은 결과가 나타난다.

```java
팀의 이름: teamA
멤버의 이름:이진우1
멤버의 이름:이진우2
멤버의 이름:이진우3
팀의 이름: teamB
멤버의 이름:이진우4
멤버의 이름:이진우5
```

---

# 주의점 2: 일대다 Fetch Join 에서 페이징 처리

## 사용 배경

A는 주의점 1 과 같은 결과를 학습했으므로

이제 페이징 처리를 일대다 페치조인에 적용하여 제한된 양을 조회하고 싶다고 한다.

따라서 아래와 같이 코드를 작성하였다.

```java
public interface TeamRepository extends JpaRepository<Team,Long> {

    @Query("select t from Team t join fetch t.members")
    List<Team> findTeamWithFetchMembersWithPageable(Pageable pageable);

}

```


## 테스트 코드 및 에러 확인

따라서 아래와 같이 테스트를 진행하여 결과를 확인한다.


```java
@Test
    @DisplayName("팀과 Member 의 1대다 Fetch Join 테스트 Pageable")
    public void SecondTest() {
        Team teamA=Team.builder().name("teamA").build();
        Team teamB=Team.builder().name("teamB").build();


        Member member1=Member.builder().loginId("afdf").password("adfdf").nickName("일진우").name("이진우1").team(teamA).build();
        Member member2=Member.builder().loginId("bfdf").password("bdfdf").nickName("이진우").name("이진우2").team(teamA).build();
        Member member3=Member.builder().loginId("cfdf").password("cdfdf").nickName("삼진우").name("이진우3").team(teamA).build();
        Member member4=Member.builder().loginId("dfdf").password("ddfdf").nickName("사진우").name("이진우4").team(teamB).build();
        Member member5=Member.builder().loginId("efdf").password("edfdf").nickName("오진우").name("이진우5").team(teamB).build();

        teamA.getMembers().add(member1);
        teamA.getMembers().add(member2);
        teamA.getMembers().add(member3);
        teamB.getMembers().add(member4);
        teamB.getMembers().add(member5);

        teamRepository.save(teamA);
        teamRepository.save(teamB);


        em.clear();

        List<Team> teams=teamRepository.findTeamWithFetchMembersWithPageable(PageRequest.of(0,2));

        for(Team t: teams){
            System.out.println("팀의 이름: "+t.getName());
            for(int i=0;i<t.getMembers().size();i++){
                System.out.println("멤버의 이름:"+t.getMembers().get(i).getName());
            }
        }

    }

```




결과를 확인하던 중 아래와 같은 에러가 발생한다.


```
2024-03-16T15:10:47.651+09:00  WARN 14408 --- [    Test worker] org.hibernate.orm.query                  : HHH90003004: firstResult/maxResults specified with collection fetch; applying in memory
```


이는 메모리에서 페이징 처리를 진행한다는 의미로
기존처럼 DB에서 ```limit``` 로 제한된 갯수만큼을 가져오는 것이 아니라 메모리에서 그 작업을 진행하기 때문에 그런 부분에서 위험하다.

실제로 일대다 페치조인+ 페이징 처리에서 발생하는 쿼리를 조회하면

```java
select
        t1_0.id,
        m1_0.team_id,
        m1_0.member_id,
        m1_0.login_id,
        m1_0.name,
        m1_0.nick_name,
        m1_0.password,
        t1_0.name 
    from
        team t1_0 
    join
        member m1_0 
            on t1_0.id=m1_0.team_id
```

위와 같이 ```limit``` 이 보이지 않는다는 것을 알 수 있다.

반면 다대일 페치조인 + 페이징 처리를 진행하면

```java
List<Member> members=memberRepository.findMemberWithTeamFetch(PageRequest.of(0,2));
```

```java
select
        m1_0.member_id,
        m1_0.login_id,
        m1_0.name,
        m1_0.nick_name,
        m1_0.password,
        t1_0.id,
        t1_0.name 
    from
        member m1_0 
    join
        team t1_0 
            on t1_0.id=m1_0.team_id 
    limit
        ?,?

```

위와 같이 ```limit``` 이 존재한다.


---


# 주의점 3: DTO + Fetch JOIN

## 사용 배경


A는 ```Fetch Join``` 을 사용해서 Member 의 데이터의 일부와 Team 의 데이터 일부를 함께 DTO로 반환한다.

```java
@Data
@NoArgsConstructor
public class MemberResponseDto {
    private String memberNickName;
    private String teamName;

    @Builder
    public MemberResponseDto(String memberNickName,String teamName){
        this.memberNickName=memberNickName;
        this.teamName=teamName;
    }
}

```

또한 위 DTO 를 설계하기 위해

```java
 @Query("select m from Member m join fetch m.team t")
    List<Member> findMemberWithTeamFetch(Pageable pageable);
```

위 메서드를 사용하고 있는데 기본적으로 Member 의 내용 전부를 조회하다 보니 아래와 같이

```
select
        m1_0.member_id,
        m1_0.login_id,
        m1_0.name,
        m1_0.nick_name,
        m1_0.password,
        t1_0.id,
        t1_0.name 
    from
        member m1_0 
    join
        team t1_0 
            on t1_0.id=m1_0.team_id 
    limit
        ?,?
``` 

실제 사용하지 않는 ```password```, ```id``` 등 필요없는 부분을 함께 조회하고 있다.

위 과정에서 성능이 걱정되었던 A 는 이를 DTO로 조회하여 해결하려 한다.

## 코드 작성

따라서 아래와 같이 코드를 작성한다.

```java
@Query("select new com.ceos19.everyTime.member.domain.dto.MemberResponseDto(m.nickName,t.name) from Member m join fetch m.team t")
    List<MemberResponseDto> findMemberDTOWithTeamNameWithFetchJoin();
```



## 테스트 코드 및 에러 확인

위에 대해 테스트를 작성하기 위해 아래와 같은 코드를 작성한다.

```java
@Test
    @DisplayName("Member 입장에서 테스트")
    public void MemberTest() {
        Team teamA=Team.builder().name("teamA").build();
        Team teamB=Team.builder().name("teamB").build();


        Member member1=Member.builder().loginId("afdf").password("adfdf").nickName("일진우").name("이진우1").team(teamA).build();
        Member member2=Member.builder().loginId("bfdf").password("bdfdf").nickName("이진우").name("이진우2").team(teamA).build();
        Member member3=Member.builder().loginId("cfdf").password("cdfdf").nickName("삼진우").name("이진우3").team(teamA).build();
        Member member4=Member.builder().loginId("dfdf").password("ddfdf").nickName("사진우").name("이진우4").team(teamB).build();
        Member member5=Member.builder().loginId("efdf").password("edfdf").nickName("오진우").name("이진우5").team(teamB).build();

        teamA.getMembers().add(member1);
        teamA.getMembers().add(member2);
        teamA.getMembers().add(member3);
        teamB.getMembers().add(member4);
        teamB.getMembers().add(member5);

        teamRepository.save(teamA);
        teamRepository.save(teamB);


        em.clear();

        List<MemberResponseDto> memberResponseDtos=memberRepository.findMemberDTOWithTeamNameWithFetchJoin();

        for(MemberResponseDto memberResponseDto : memberResponseDtos){
            System.out.println("팀의 이름: "+memberResponseDto.getTeamName());
            System.out.println("멤버의 이름: "+memberResponseDto.getMemberNickName());
        }

    }
```

하지만 아래와 같은 에러가 발생한다.

```
Caused by: org.hibernate.query.SemanticException: 
query specified join fetching, but the owner of the fetched 
association was not present in the select list [SqmSingularJoin
(com.ceos19.everyTime.member.domain.Member(m).team(t) : team)]
```

페치 연관관계가 select list에 없다고 한다.


## 해결방법

```fetch join``` 을 없애고 ```join```으로만 사용한다.

```
 select
        m1_0.nick_name,
        t1_0.name 
    from
        member m1_0 
    join
        team t1_0 
            on t1_0.id=m1_0.team_id
```

그럼 위와 같은 쿼리로 최적화를 진행할 수 있다.

---

## 주의점 4 : 1쪽에서 여러 연관관계를 fetch join 할 때


## 사용 배경

A는 Team 에서 Team 의 이미지와 Team 의 Member 에 대한 내용 을 함께 영속화해서 가져오고 싶다.


## 코드 작성

따라서 아래와 같은 코드를 작성한다.

```
@Query("select t from Team t join fetch t.members join fetch t.teamImages")
    List<Team> findTeamWithFetchMembersAndImages();
```


## 테스트 코드 + 에러 확인

```
   @Test
    @DisplayName("팀과 Member 의 1대다 Fetch Join 테스트 Pageablefadfa")
    public void ThirdTest() {
        Team teamA=Team.builder().name("teamA").build();
        Team teamB=Team.builder().name("teamB").build();


        Member member1=Member.builder().loginId("afdf").password("adfdf").nickName("일진우").name("이진우1").team(teamA).build();
        Member member2=Member.builder().loginId("bfdf").password("bdfdf").nickName("이진우").name("이진우2").team(teamA).build();
        Member member3=Member.builder().loginId("cfdf").password("cdfdf").nickName("삼진우").name("이진우3").team(teamA).build();
        Member member4=Member.builder().loginId("dfdf").password("ddfdf").nickName("사진우").name("이진우4").team(teamB).build();
        Member member5=Member.builder().loginId("efdf").password("edfdf").nickName("오진우").name("이진우5").team(teamB).build();

        TeamImage teamImage=TeamImage.builder().team(teamA).accessUrl("www").build();
        TeamImage teamImage1=TeamImage.builder().team(teamA).accessUrl("www").build();

        teamA.getMembers().add(member1);
        teamA.getMembers().add(member2);
        teamA.getMembers().add(member3);
        teamB.getMembers().add(member4);
        teamB.getMembers().add(member5);

        teamA.getTeamImages().add(teamImage);
        teamA.getTeamImages().add(teamImage1);

        teamRepository.save(teamA);
        teamRepository.save(teamB);


        em.clear();

        List<Team> teams=teamRepository.findTeamWithFetchMembersAndImages();

        for(Team t: teams){
            System.out.println("팀의 이름: "+t.getName());
            System.out.println(t.getMembers().size());
        }

    }
 ```

위와 같은 테스트 코드를 작성하고 돌리면

그럼 아래와 같은

 ```
 Caused by: java.lang.IllegalArgumentException: org.hibernate.loader.MultipleBagFetchException: cannot simultaneously fetch multiple bags: [com.ceos19.everyTime.member.domain.Team.members, com.ceos19.everyTime.member.domain.Team.teamImages]
	at org.hibernate.internal.ExceptionConverterImpl.convert(ExceptionConverterImpl.java:141)
	at org.hibernate.query.spi.AbstractSelectionQuery.list(AbstractSelectionQuery.java:378)
	at org.hibernate.query.Query.getResultList(Query.java:119)
```

여러 객체에 대해 페치조인을 사용하지 말라는 오류를 발생시킨다. 

 