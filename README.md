# spring-everytime-19th
CEOS 19th BE study - everytime clone coding

## 에브리타임 서비스 설명 & ERD 의도 설명
![img.png](readme_img/img_whole.png)
에브리타임은 대학생 익명 커뮤니티 사이트이다.

### 회원
먼저 서비스를 이용할 회원 엔티티를 정의하였다.   

<img src="readme_img/img.png" width="400"/>


에브리타임 마이페이지를 참고하여 회원 ID, PW, 닉네임, 이름, 이메일, 가입일과 같은 기본 유저 정보에 학과, 학번, 재학생 인증 여부 정보를 추가적으로 입력받도록 설계하였다.

### 게시판 / 게시글
<img src="readme_img/img2.png" width="600"/>
다음은 유저가 작성하는 게시글과 게시글이 올라가는 게시판에 대한 ERD이다.   

#### 게시판 - 유저
게시판은 관리하는 유저가 한명만 존재할 수 있으므로 유저와 1:1 관계이다.   
관리자는 변경될 수 있기 때문에 one and only one 대신 one and one 으로 설정하였다.   

#### 게시글 - 유저
게시글은 한명의 유저가 여러 게시글을 작성할 수 있지만, 하나의 게시글에는 한명의 작성자만 존재한다.
따라서 유저와 게시글의 관계는 1:N 관계이다.

에브리타임은 익명 커뮤니티이므로, 작성자의 익명 설정 여부를 저장하는 필드도 추가하였다.   
에브리타임에는 질문글 옵션이 있으나, 해당 옵션은 클론하지 않았다.

#### 게시판 - 게시글
하나의 게시판에는 게시글이 없을 수도 있고, 여러개 있을 수도 있다.   
하나의 게시글은 오직 하나의 게시판에만 속하므로 1:N 관계이다.

#### 게시글 - 첨부 사진
<img src="readme_img/img3.png" width="600"/>
게시글을 작성할 때는 사진을 첨부할 수 있다.   
이때 하나의 게시글에는 사진이 없을 수도 있고, 여러개 있을 수도 있는 1:N 관계이다.   
사진을 직접 저장하는 것은 데이터베이스의 용량을 많이 차지하므로 서버에 있는 사진의 경로를 대신 저장하도록 하였다.   

#### 게시글 - 좋아요
<img src="readme_img/img4.png" width="600"/>
게시글 하나에는 여러개의 좋아요가 달릴 수 있지만, 하나의 좋아요는 하나의 게시글에 대한 좋아요를 나타낸다.   
따라서 게시글과 좋아요의 관계는 1:N 관계이다.

#### 유저 - 좋아요
한명의 유저는 하나의 게시글에 좋아요를 한번만 누를 수 있다.     
이를 파악하려면 좋아요를 누른 유저의 정보도 같이 저장해야 한다.     
한명의 유저는 여러 게시글에 좋아요를 누를 수 있지만, 좋아요 하나는 한명의 유저가 생성한 좋아요이므로 1:N 관계이다.   

### 댓글
<img src="readme_img/img5.png" width="600"/>
댓글 역시 익명으로 작성이 가능하므로, 댓글의 익명성 여부를 같이 저장하도록 설계하였다.

#### 게시글 - 댓글
게시글 하나에는 댓글이 없거나 여러개 달릴 수 있다.   
하나의 댓글은 하나의 게시글에 대한 댓글이다.   
따라서 게시글과 댓글 사이의 관계는 1:N 관계이다.

#### 유저 - 댓글
한명의 유저는 여러개의 댓글을 작성할 수 있다.   
하나의 댓글은 한명의 유저가 작성하였다.   
따라서 유저와 댓글 사이의 관계는 1:N 관계이다.

#### 댓글 - 대댓글
하나의 댓글에는 대댓글이 없거나 여러개의 대댓글이 달릴 수 있다.
하나의 대댓글은 대상으로 하는 댓글이 하나이다.   
따라서 댓글과 대댓글 사이의 관계는 1:N 이다.   

게시글에 작성한 댓글은 대상으로 하는 댓글이 없으므로 부모댓글 ID는 NULL 값을 허용하였다.

#### 댓글 - 좋아요
<img src="readme_img/img6.png" width="600"/>
하나의 댓글에는 어러개의 좋아요가 달릴 수 있다.   
하나의 좋아요는 하나의 댓글에 대한 좋아요이다.   
따라서 댓글과 좋아요 사이의 관계는 1:N 이다.

#### 유저 - 좋아요
한명의 유저는 여러 댓글에 대해 좋아요를 누를 수 있다.   
좋아요 하나는 한명의 유저에 의해서 생성된다.   
따라서 유저와 좋아요 사이의 관계는 1:N 이다.

#### 좋아요 모델링 특이사항
좋아요는 댓글 좋아요와 게시글 좋아요가 좋아요의 대상만 다르고 나머지 기능이 동일하다.    
<img src="readme_img/img8.png" width="400"/>   
따라서 Like 라는 추상클래스를 상속받아서 PostLike, CommentLike 를 구현하도록 하였다.   
<img src="readme_img/img9.png" width="400"/>   
실제 DB에는 likes 라는 테이블 하나만 존재하고, 이 테이블에 댓글 좋아요와 게시글 좋아요 데이터가 모두 저장된다. 

### 쪽지
<img src="readme_img/img7.png" width="600"/>
쪽지는 유저와 유저 사이 1:1로 진행되며, 쪽지를 주고받는 공간을 '쪽지함'으로 부른다.

#### 유저 - 쪽지함
에브리타임의 쪽지는 항상 1:1로 진행되는 점을 고려하여 설계하였다.   
쪽지함에는 해당 쪽지함에서 어떤 유저와 쪽지를 주고 받는지 참여한 2명의 유저를 기록하도록 하였다.   
이때 회원1은 처음으로 쪽지를 보내는 유저, 회원2는 처음으로 쪽지를 받는 유저로 정의하였고, 각각 1:1 관계로 설정하였다.

#### 쪽지함 - 쪽지
하나의 쪽지함에서는 여러개의 쪽지를 주고받을 수 있다.   
하나의 쪽지는 반드시 하나의 쪽지함에 속해있다.   
따라서 쪽지함과 쪽지의 관계는 1:N 관계이다.

#### 유저 - 쪽지
한명의 유저는 여러개의 쪽지를 보낼 수 있지만, 하나의 쪽지는 한명의 유저가 보낸 것이므로, 1:N 관계이다.

## 3주차 - JPA 심화
### 필요한 비즈니스 로직 리스트
#### 유저
- [x] 유저 회원 가입
- [x] 유저 회원 탈퇴
- [x] 유저 대학교 인증
- [ ] ~~유저 로그인~~ (TODO)
- [ ] ~~유저 로그아웃~~ (TODO)
#### 게시판
- [x] 게시판 생성
- [x] 게시판 관리자 변경
- [x] 게시판 삭제 (소속 게시글, 댓글 모두 삭제)
#### 게시글
- [x] 게시글 조회
- [x] 게시글 작성 (사진 여러장 첨부)
- [x] 게시글 삭제
- [x] 게시글 수정
- [x] 게시글 좋아요 누르기
- [x] 게시글 좋아요 취소
#### 댓글
- [x] 게시글 댓글 작성
- [x] 게시글 댓글 삭제
- [x] 대댓글 작성
- [x] 대댓글 삭제
- [x] 댓글 좋아요 누르기
- [x] 댓글 좋아요 취소
#### 쪽지
- [x] 최초 쪽지 전송 (쪽지방 생성 + 쪽지 전송)
- [x] 쪽지 전송
- [x] 쪽지방 삭제 (전체 쪽지 삭제)
- [x] 쪽지함 (전체 쪽지방) 조회
- [x] 특정 유저와의 쪽지함 조회
#### 이미지
- [x] 이미지 업로드 (서버에 저장)
- [x] 이미지 DB에서 삭제

### 레포지토리 테스트
@SpringBootTest 사용    
- **ChatRoom 삭제 테스트 (N+1 테스트)**    
  이번 스터디를 통해 공부한 fetch를 연관관계 매핑에 적용해보았다.
  ![image](https://github.com/kckc0608/kckc0608/assets/64959010/63448be0-c9ab-4220-8d95-5a00cb1b5795)   
  채팅 메세지를 전송할 때, 채팅 메세지 객체를 직접 생성하면서 채팅방을 직접 매핑하는 것보다, 채팅방 객체에서 채팅 전송 메서드를 호출하는 것이 더 객체지향적인 설계라고 생각하여 `CascaseType.ALL`로 설정하였다.
  
  ```java
    @Test
    @DisplayName("채팅방 삭제시 채팅 데이터 삭제 테스트")
    void 채팅방_삭제시_채팅_데이터_삭제_테스트() {
        // given
        ChatRoom chatRoom = chatRoomRepository.save(new ChatRoom(user1, user2));
        chatRoom.send(user1, "hello");
        chatRoom.send(user2, "hello!");
    
        System.out.println("--------------------");
        System.out.println("create Chat Message Data");
        System.out.println("--------------------");

        em.flush();
        em.clear();
    
        // when
        System.out.println("--------------------");
        System.out.println("Delete Chat Message Data");
        System.out.println("--------------------");
        chatRoomRepository.delete(chatRoom);
        
        // then
        Assertions.assertThat(chatRoomRepository.findChatRoomByUser1AndUser2(user1, user2).isEmpty()).isTrue();
        Assertions.assertThat(chatRoomRepository.findChatRoomByUser1AndUser2(user2, user1).isEmpty()).isTrue();
        Assertions.assertThat(chatMessageRepository.findAllByChatRoom(chatRoom).isEmpty()).isTrue();
    }
  ```
  N+1을 테스트하기 위해 위와 같은 테스트 코드를 작성하였다.   
  영속성 전이를 설정해 둔 ChatRoom 객체를 삭제하면, 해당 ChatRoom과 연관관계가 있는 모든 ChatMessage에 대해 아래와 같은 쿼리가 함께 나간다.
  ```mysql
  Hibernate:
      delete
      from
          chat_message
      where
          message_id=?
  Hibernate:
      delete
      from
          chat_message
      where
          message_id=?
  Hibernate:
      delete
      from
          chat_room
      where
          room_id=?
  ```
  chat_message 들이 먼저 삭제된 이후, chat_room 을 삭제하는 것을 알 수 있었다.

- **새로 공부한 부분**   
  ![image](https://github.com/kckc0608/kckc0608/assets/64959010/931c4d3f-1717-426b-b9c8-af078e4068c8)   
  에브리타임에서 대댓글이 달린 댓글(이하 '부모 댓글')을 삭제해도, 대댓글(이하 '자식 댓글')은 모두 남아있는 것을 볼 수 있다.   
  이 부분을 구현하는 방법을 공부하고, 적용하는 과정에서 문제를 만났다.

  <br/>
  처음에는 부모 댓글만 삭제하면 자식 댓글들의 FK값이 Null이 아니더라도 더 이상 존재하지 않는 부모 댓글 PK를 가리키므로 그냥 부모 댓글만 지우면 될 거라고 생각했다.<br/>
  하지만 이렇게 구현하면 DB의 Constraint 제약 조건에 따라 에러가 발생하였다.<br><br>    
  
  그래서 FK값이 일단 Null이 되어야 하니 부모댓글을 지우면 자동으로 자식 댓글들의 FK를 NULL로 설정하도록 하는 방법을 찾고 싶었다.     
  하지만 같은 고민을 했던 사람의 질문에 대해 김영한님이 남겨주신 답변은 for 문을 돌면서 연관된 자식 댓글들의 FK를 수동으로 Null로 설정하는 것이 맞다고 한다.      
  
  [관련 글 링크](https://www.inflearn.com/questions/39769/%EB%B6%80%EB%AA%A8-%EC%9E%90%EC%8B%9D%EA%B4%80%EA%B3%84%EC%97%90%EC%84%9C-%EB%B6%80%EB%AA%A8-%EC%82%AD%EC%A0%9C%EC%8B%9C-set-null%EB%B0%A9%EB%B2%95%EC%97%90-%EB%8C%80%ED%95%B4%EA%B6%81%EA%B8%88%ED%95%A9%EB%8B%88%EB%8B%A4)   
  
  그래서 해당 내용을 아래와 같이 구현하였다.
  ```java
      @Test
      @DisplayName("부모 댓글 삭제 테스트")
      void 부모댓글_삭제_테스트() {
          // given
          Comment parent = post1.addComment(user2, "댓글1", true);
          parent.addReply(user1, "댓글2", true);
          parent.addReply(user1, "댓글3", true);
          em.flush();
  
          // when
          for (int i = 0; i < 2; i++) {
              parent.getChildComments().get(i).setParentComment(null);
          }
          em.flush();
    
          System.out.println("--------------------------");
          commentRepository.delete(commentRepository.findById(parent.getCommentId()).get());
          em.flush();
          em.clear();
          System.out.println("--------------------------");
  
          // then
          Assertions.assertThat(commentRepository.findAllByPost(post1).size()).isEqualTo(2);
      }
  ```
  그러나 이렇게 구현했을 때 Delete 쿼리가 나가지 않는 문제가 있었다.   
  ![image](https://github.com/kckc0608/kckc0608/assets/64959010/1f261789-bbd4-4f4e-9261-1ebf3e263bb9)    
  이에 대해 찾아본 결과, 위와 같이 자식 댓글을 조회해서 1차 캐시에 남아있는 상태에서 그에 대한 부모 댓글을 delete하면 이미 조회했던 자식 댓글들에는 해당 부모 댓글 정보가 남아있지만 실제 DB에서는 남아있지 않는 동기화 문제가 발생하므로 delete 쿼리를 실행시키지 않는다고 한다.   
  그래서 이를 해결하기 위해 1차 캐시를 깔끔하게 비운 뒤, 다시 조회하도록 `em.clear()` 를 해주었다.
  
  ```java
  // when
  for (int i = 0; i < 2; i++) {
  parent.getChildComments().get(i).setParentComment(null);
  }
  em.flush();
  em.clear();
  // 연관관계가 있는 child 가 1차 캐시에 있는 상태에서는 parent를 지워도 쿼리가 안나감.
  // https://velog.io/@jkijki12/JPA-Entity%EA%B0%80-delete%EA%B0%80-%EC%95%88%EB%90%9C%EB%8B%A4 참고
  
  System.out.println("--------------------------");
  commentRepository.delete(commentRepository.findById(parent.getCommentId()).get());
  em.flush();
  em.clear();
  System.out.println("--------------------------");
  ```
  실행결과는 아래와 같다.
  ```mysql
  --------------------------
  Hibernate:
    select
        c1_0.comment_id,
        c1_0.user_id,
        c1_0.content,
        c1_0.create_date,
        c1_0.is_anonymous,
        c1_0.modify_date,
        c1_0.parent_comment_id,
        c1_0.post_id
    from
        comment c1_0
    where
        c1_0.comment_id=?
  Hibernate:
    delete
    from
        comment
    where
        comment_id=?
  --------------------------
  ```
### 서비스 단위 테스트
서비스 단위 테스트는 Mockito를 이용해 레포지토리를 mocking 하여 진행하였다.   
과제 PR 리뷰를 통해 `@SpringBootTest` 는 단위테스트를 위한 어노테이션인지 통합테스트를 위한 어노테이션인지 공부를 다시 하게 되었다.   
그리고 2주차 과제에서 Repository 테스트를 `@SpringBootTest` 를 통해 진행한 것은 단위 테스트가 아니라 통합테스트였다는 것을 새로 공부하게 되었다.      
그래서 이번 과제에서는 단위 테스트 / 통합 테스트를 모두 작성해야 하는 만큼 확실하게 분리해서 테스트를 해보고자 했다.   

#### @Mock, @InjectMock
![image](https://github.com/kckc0608/kckc0608/assets/64959010/7ef925c4-ba31-4d5b-bca4-b8857a75bf7a)    
서비스 계층 단위테스트를 진행할 때는 Repository 계층과 연결을 끊고 서비스 계층의 로직만을 테스트 해야한다.   
따라서 레포지토리를 `@Mock` 어노테이션을 사용해 Mocking하고 service는 이 Mock을 대신 주입받도록 하였다.

```java
void 게시판_생성_테스트() {
    // given
    Category category = EntityGenerator.generateCategory(user1);
    given(categoryRepository.save(any(Category.class))).willReturn(category);

    // when
    Category newCategory = categoryService.create(category);

    // then
    assertThat(newCategory).isEqualTo(category);
}
```
Mocking한 레포지토리의 동작을 정상적으로 작동하는 것처럼 보이게 하기 위해 `given()` 을 사용하여 return 값을 정해주었다.   
이렇게 repository 의 메서드 방식을 정해주면, service 계층의 코드를 실행할 때, mocking한 레포지토리의 코드가 대신 실행된다.

```java
    @Test
    @DisplayName("14일 이후 게시판 삭제 테스트")
    void 게시판_생성_후_14일_이후_삭제_테스트() {
        // given
        Category category = EntityGenerator.generateCategory(user1);
        category.setDateForTest(LocalDateTime.of(2024, 3, 1, 0, 0));

        // when
        categoryService.delete(category);

        // then
        verify(categoryRepository, times(1)).delete(category);
    }
```
게시판 삭제와 같은 로직을 테스트할 때는 특정 데이터를 return 하는 로직이 없으므로 `given()` 함수를 사용할 일이 없다.   
이런 경우에는 `verify()` 를 사용하여 함수 호출 여부 및 횟수를 검증하여 테스트할 수 있다.

## 4주차
### API 명세 [ **/api/v1** ]
<table style="text-align: center">
  <tr>
    <th>Domain</th>
    <th>Method</th>
    <th>Base URL</th>
    <th>URL</th>
    <th>Description</th>
  </tr>
  <tr>
    <td rowspan="4">Category</td>
    <td>GET</td>
    <td rowspan="4"><code>/category</code></td>
    <td><code>/{category_id}/page/{page_number}</code></td>
    <td>게시판 게시글 조회(페이징)</td>
  </tr>
  <tr>
    <td>POST</td>
    <td><code>/</code></td>
    <td>게시판 생성</td>
  </tr>
  <tr>
    <td>PUT</td>
    <td><code>/{category_id}/description</code></td>
    <td>게시판 설명 수정</td>
  </tr>
  <tr>
    <td>DELETE</td>
    <td><code>/{category_id}</code></td>
    <td>게시판 삭제</td>
  </tr>

  <tr>
    <td rowspan="5">Post</td>
    <td>POST</td>
    <td rowspan="5"><code>/post</code></td>
    <td><code>/</code></td>
    <td>글 작성</td>
  </tr>
  <tr>
    <td>GET</td>
    <td><code>/{post_id}</code></td>
    <td>글 조회</td>
  </tr>
  <tr>
    <td>PUT</td>
    <td><code>/{post_id}</code></td>
    <td>글 수정</td>
  </tr>
  <tr>
    <td>DELETE</td>
    <td><code>/{post_id}</code></td>
    <td>글 삭제</td>
  </tr>
  <tr>
    <td>POST</td>
    <td><code>/{post_id}/like</code></td>
    <td>게시글 좋아요 업데이트(생성/삭제)</td>
  </tr>

  <tr>
    <td rowspan="4">Comment</td>
    <td>POST</td>
    <td rowspan="4"><code>/comments</code></td>
    <td><code>/</code></td>
    <td>댓글 생성</td>
  </tr>
  <tr>
    <td>POST</td>
    <td><code>/{comment_id}</code></td>
    <td>대댓글 생성</td>
  </tr>
  <tr>
    <td>DELETE</td>
    <td><code>/{comment_id}</code></td>
    <td>댓글 삭제</td>
  </tr>
  <tr>
    <td>POST</td>
    <td><code>/{comment_id}/like</code></td>
    <td>댓글 좋아요 업데이트 (생성/삭제)</td>
  </tr>

  <tr>
    <td rowspan="4">ChatRoom</td>
    <td>GET</td>
    <td rowspan="4"><code>/chat/room</code></td>
    <td><code>/list</code></td>
    <td>쪽지함 내 쪽지방 조회</td>
  </tr>
  <tr>
    <td>GET</td>
    <td><code>/{room_id}</code></td>
    <td>쪽지방 단건 조회</td>
  </tr>
  <tr>
    <td>DELETE</td>
    <td><code>/{room_id}</code></td>
    <td>쪽지방 삭제</td>
  </tr>
  <tr>
    <td>POST</td>
    <td><code>/</code></td>
    <td>쪽지방 생성</td>
  </tr>

  <tr>
    <td rowspan="1">ChatMessage</td>
    <td>POST</td>
    <td rowspan="1"><code>/chat/rooms/{room_id}/message</code></td>
    <td><code>/</code></td>
    <td>기존 채팅방에 채팅 전송</td>
  </tr>

  <tr>
    <td rowspan="2">Image</td>
    <td>POST</td>
    <td rowspan="2"><code>/image</code></td>
    <td><code>/</code></td>
    <td>이미지 업로드</td>
  </tr>
</table>

### Controller Test를 위한 Swagger 연동 확인
1. 의존성을 추가한다. spring-doc, spring-fox 가 있는데, spring-fox는 4년전 이후로 업데이트가 없어 spring-doc를 사용하였다. 
![image](https://github.com/kckc0608/kckc0608/assets/64959010/211f9511-0b7d-416e-9e14-605a19311416)

2. 앱을 실행한 후 아래 주소로 접속하면 된다.    
    http://localhost:8080/swagger-ui/index.html/

3. 아래는 접속한 이미지이다.
  ![image](https://github.com/kckc0608/kckc0608/assets/64959010/13e4680b-acdf-4117-b3f8-4f07281f1079)

### 3주차에서 리펙토링한 부분

- `Optional` 객체의 존재 여부를 체크하여 에러를 발생시킬 때 `orElseThrow`를 사용하도록 변경하였다.
- DTO 파일이 많아져 관리의 용이성을 위해 디렉토리 구조를 계층형에서 도메인형으로 변경하였다.   
  <img src="https://github.com/kckc0608/kckc0608/assets/64959010/6a717016-915d-4f4e-a4f5-bd731f0a2bad" style="width: 50%"/>
- 테스트 결과 두 객체를 비교할 때 usingRecursiveComparison 사용 
- 좋아요를 생성/삭제할 때, post, comment 객체에서 like 메서드를 호출하는 방식대신 likeService.updatePostLike(), updateCommentLike() 메서드로 처리하도록 수정하였다.    
  이때 좋아요를 생성/삭제하는 메서드를 분리하지 않고, 업데이트 메서드 하나로 없으면 생성, 있으면 삭제하는 방식으로 구현하였다.
  ```java
    @Transactional
    public void updatePostLike(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_POST_ID));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_USER_ID));

        likeRepository.findPostLikeByPostIdAndUserId(postId, userId).ifPresentOrElse(
                likeRepository::delete,
                () -> {
                    final PostLike postLike = new PostLike(user, post);
                    likeRepository.save(postLike);
                }
        );
    }
  ```
- Pageable을 사용하여 페이지 네이션을 적용하였다.

### 4주차 과제를 하면서 새로 공부한 점
1. DTO 클래스에 필드가 1개만 있을 때, json과 object가 변환되지 못하는 문제가 발생했다.   
   ![image](https://github.com/kckc0608/kckc0608/assets/64959010/e9e29b5a-feb0-43cc-803f-515afd9e198a)
   위 그림과 같이 필드가 1개만 있을 때, 생성자를 분명히 넣었음에도 요청을 전송하면 상태코드 406 에러가 발생했다.
   스프링에서는 아래 에러가 발생했다.
   ```java
   (although at least one Creator exists): cannot deserialize from Object value (no delegate- or property-based Creator)
   ```
   구글에서 검색한 해결책은 대부분 기본생성자를 만들라는 것이었는데,다른 DTO는 기본 생성자가 없어도 문제가 없었던 점에서 기본 생성자가 근본적인 해결책같지는 않았다.   
   다행히 [나와 같은 고민을 한 사람](https://kong-dev.tistory.com/236?category=1072302)이 있었다.   
   json-object 변환은 jackson 라이브러리를 활용하는데, 클래스의 필드가 1개 일 때는 jackson 라이브러리가 넘어온 데이터를 임의 문자열로 판단해야 할 지 필드가 1개인 객체로 봐야할 지 모호해서 판단을 못한다고 한다.
  
   - 문자열 1개인 경우에는 해당 문자열을 적절히 커스텀된 방식으로 파싱할 수 있고 이 때는 `delegating` 방식을 사용한다. (임의 문자열로 판단)   
   - 객체인 경우에는 `properties`방식을 적용하여 json 형식 객체로 인식하여 자바 객체로 만든다. 원래는 기본적으로 이 방식을 사용한다.
   
   따라서 json 형식 객체로 인식시키기 위해 `properties` 방식을 적용하라고 명시적으로 알려주면 문제를 해결 할 수 있다.   
   이는 생성자 위에 `@JsonCreator` 어노테이션을 달아주면 된다.   
   ![image](https://github.com/kckc0608/kckc0608/assets/64959010/f9bd9a32-bfcd-487b-8907-4fb522ec75f0)

2. GlobalExceptionHandler를 이용하여 커스텀으로 생성한 에러를 `@ExceptionHandler(BadRequestException.class)` 어노테이션으로 잡을 수 있었다.   
   다만 매개변수에 넘기는 RequestBody의 `@Valid` 어노테이션을 통한 Validation 체크시 발생한 에러는 `MethodArgumentNotValid`가 발생하는데, 이 에러는 `ResponseEntityExceptionHandler` 클래스를 상속한 뒤 `handleMethodArgumentNotValid` 메서드를 오버라이딩하여 처리할 수 있다.   
   ```java
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {

        String errMsg = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(INVALID_REQUEST.getCode(), errMsg));
    }
   ```

<hr>

# 5주차
## 로그인 인증 방식
클라이언트가 서버에게 요청을 보낼 때, 자신이 요청을 실제로 보내려는 사용자가 맞다는 것을 인증하는 것   
일반적으로 아이디와 패스워드를 이용해 인증을 하게 되는데, 매번 요청을 보낼 때마다 로그인을 해서 사용자를 인증하는 것은 번거롭다.   
따라서 한번 인증을 한 이후에는 내가 이전에 인증했던 사용자가 맞다는 것을 서버에게 계속 알려주어야 한다. (http는 stateless 이기 때문이다.)   
이때 이를 서버에게 알려주는 방식에는 크게 아래와 같이 4가지가 있다.

### 쿠키
로그인에 성공하면 쿠키 자체에 사용자의 정보를 담아두고, 클라이언트가 요청을 보낼 때마다 쿠키 정보로부터 사용자를 식별하는 방법   
클라이언트가 자신의 인증을 책임지기 때문에, 클라이언트가 해킹당할 경우 자신의 정보를 모두 탈취당할 수 있다.   
일반적으로 클라이언트보다 서버를 해킹하는 것이 더 어려우므로 서버에 인증 책임을 넘기는 세션을 이용한다.

### 쿠키와 세션
로그인에 성공하면, 서버는 사용자 정보를 담은 '세션'을 세션 저장소에 저장한다.   
그리고 클라이언트에게는 해당 세션 데이터를 가리키는 '세션 ID' 를 Set-Cookie 헤더에 담아 보내준다.    
사용자는 세션 ID를 쿠키로 설정한 뒤, 이후 요청을 보낼 때는 쿠키에 세션 ID를 담아 같이 보낸다.   
서버는 요청을 받았을 때, 쿠키에 담긴 세션 ID로부터 해당하는 사용자 정보를 가져오는 방식으로 사용자를 인증한다.

#### 장점
- 쿠키에 담긴 세션ID는 그 자체로 유의미한 값을 갖고 있지 않으므로, http 요청이 노출되더라도 안전하다.   
- 각 사용자는 고유의 세션ID를 발급받으므로, 서버 입장에서도 사용자를 바로 식별할 수 있어서 서버 자원에 접근하기 용이하다.   
- 중복 로그인을 방지하고자 할 때 구현이 용이하다.   
  중복 로그인 여부를 확인하려면 서버에 사용자의 로그인 정보(상태)가 남아있을 수 밖에 없는데, 세션을 사용하면 이미 사용자의 정보를 서버에 저장하고 있기 때문이다.

#### 단점
- 쿠키를 탈취당했을 때, 그 자체의 값에는 의미가 없지만, 그 값을 이용해 서버에게 마치 자신이 그 쿠키의 원래 주인인 것처럼 속여서 요청을 보낼 수 있다. (이를 `세션 하이재킹 공격`이라고 한다.)   
  따라서 이 문제를 방지하기 위해, HTTP 요청 자체를 암호화하는 HTTPS 방식을 사용하고, 세션에 유효기간을 설정해둔다.
- 서버 입장에서는 세션을 저장할 별도의 저장 공간이 필요하므로, 서버의 부하가 높아진다.

### JWT
Json Web Token의 약자로, 사용자를 인증하는데 필요한 정보를 암호화시킨 토큰(Access Token)을 만들고, 이 토큰을 클라이언트에게 쿠키로 설정하게 한다.   
사용자는 요청을 보낼 때마다 암호화된 토큰을 서버로 같이 보내고, 서버는 자신이 가진 비밀키로 토큰을 디코딩하여 암호화된 내용을 해독해 사용자를 식별한다.   

토큰을 만들 때는 아래 3가지가 들어간다.
1. Header : 토큰을 암호화 할 때 어떤 암호화 알고리즘을 사용하는지, 토큰 타입 
2. Payload : 실제로 서버에 보낼 사용자 인증 데이터, 일반적으로 유저 ID값, 토큰 유효기간이 들어간다.
3. Verify Signature : 1, 2를 각각 Base64 방식으로 인코딩(암호화x)한 뒤, 여기에 SECRET KEY를 더해 서명한다.  

헤더와 페이로드는 인코딩만 될 뿐 암호화되지 않아 누구나 디코딩할 수 있으므로 비밀번호와 같은 민감한 정보는 담지 않는 것이 좋다.   
하지만 verify signature는 secret key를 모르면 복호화할 수 없다.   
따라서 시크릿 키를 모르는 이상, JWT를 조작하는 것도 할 수 없다.

JWT를 통한 요청 인증 과정은 아래와 같다.
1. 사용자가 로그인을 하면 서버는 JWT 토큰을 발급하여 응답한다.
2. 클라이언트가 JWT와 함께 요청을 보내면 서버는 JWT의 verify signature를 자신의 비밀키로 복호화하여 유효기간과 조작 여부를 확인한다.   
3. 토큰에 대한 검증이 끝나면 Payload를 디코딩하여 사용자 ID에 해당하는 데이터를 가져와 응답한다.

JWT는 세션과 함께 가장 많이 쓰이는 방식이다.   
세션과의 차이점은 사용자를 인증하는 정보를 서버가 아닌 토큰 안에 넣는다는 점이다.   
서버는 세션을 저장할 별도 DB를 가질 필요가 없지만, 토큰을 복호화할 로직을 가져야 한다.

#### 장점
- 세션/쿠키는 별도의 저장소를 관리해야 하지만, JWT를 저장소를 관리하지 않아도 되므로 간단하다.   
이는 특히 stateless 서버를 만드는데 있어서 큰 장점을 갖기에 서버 확장, 유지보수에 용이하다.   
- 토큰 기반의 다른 인증 시스템(구글 로그인, 페이스북 로그인)에 접근하기 쉬워 확장성이 용이하다.

#### 단점
- 한번 발급한 JWT에 대해서는 서버의 손을 떠났으므로 돌이킬 수 없다. 세션 방식이라면, 쿠키가 악의적으로 이용될 경우, 서버에서 세션을 지워버리면 문제에 대처할 수 있지만,
  JWT는 한번 발급한 토큰의 유효기간이 지날 때까지 계속 사용이 가능하므로, 악의적인 사용자가 지속적으로 정보를 탈취할 수 있다.

    → 따라서 이를 해결하기 위해 access token을 짧게 하고, 비교적 유효기간이 긴 refresh token을 만들어, 이 토큰을 사용하면 새로운 access token 을 만들도록 하는 방식을 사용하기도 한다.   
      하지만 refresh token도 결국 탈취당할 위험이 없는 것은 아니다. 그래서 이를 막으려고 redis에 refresh toekn을 저장해서, 탈취당하거나 로그아웃된 상태의 refresh toekn을 redis에서 제거하는 방법을 사용하기도 한다.   
      이렇게 구현하면 결국 서버가 refresh 토큰의 state를 저장해야 하는데, 이는 stateless 서버를 만드는 jwt의 장점을 퇴색시킨다.   
      이에 대해 JWT가 자주 사용되는 이유가 stateless가 아니라 모바일 앱 환경 특성상 jwt를 사용하는 것이 유리하다는 점, 장기간 로그인 상태를 유지하고자 할 때 서버에게 가해지는 부하가 세션보다 적다는 점을 JWT의 주 사용 이유로 보고
      stateless는 부수적인 효과로 받아들이면 될 것 같다는 의견도 있다. (https://substantial-park-a17.notion.site/14-JWT-3721466022d24a2fad0e7272e5b15c76)

- 페이로드에 담을 수 있는 정보가 제한적이다. 페이로드는 암호화되지 않기 때문이다.
  반면 세션에서는 유저의 모든 정보를 서버가 갖고 있으므로, 민감한 정보를 안전하게 저장할 수 있다.

- JWT의 길이는 세션 ID에 비해 길다. 따라서 인증이 필요한 요청이 많을수록, 서버의 자원낭비가 발생한다.
  
### OAuth
OAuth는 외부 서비스의 인증 및 권한 부여까지 관리하는 범용 `프로토콜`이다.   
현재는 OAuth 2.0을 많이 사용하는데, OAuth 1.0보다 사용성, 보안이 강화된 버전의 프로토콜이다.   

OAuth 2.0의 인증 방식은 크게 4가지로 나뉜다.
1. Authorization Code Grant
2. Implicit Grant
3. Resource Owner Password Credentials Grant
4. Client Credentials Grant

이 중 첫번째 방식 Authorization Code Grant 방식의 과정을 정리하면 아래와 같다.
1. 사용자(Resource Owner)가 우리 서버(Client, 제 3의 인증서버 입장에서 클라이언트)에게 인증 요청을 보낸다.
2. 우리 서버(Client)는 Authorization Request를 통해, 사용자(Resource Ownder)에게 구글/페이스북 로그인 링크를 보낸다.
3. 사용자는 해당 request를 통해 인증을 완료하고, 인증 완료 신호로 `Authorization Grant`를 url에 실어 우리 서버로 보낸다.
4. 우리 서버는 `Authorization Grant'를 실제 제3 인증서버(Authorization Server) 에게 보낸다.
5. Authorization Server는 `권한 증서`를 확인하고, 권한이 맞다면 access token, refresh token, 유저의 프로필 정보 등을 발급한다.
6. client는 발급받은 access token을 사용자에게 넘기거나, DB에 저장한다.
7. 사용자가 제3 서버의 resource를 원하는 경우, client가 대신 access token으로 `Authorization Server`에 요청을 보낸다.
8. 제 3서버는 access token을 확인하고 client에게 리소스를 반환하면, 클라이언트가 사용자에게 리소스를 반환한다.

## 엑세스토큰 발급 및 검증 로직
### 스프링 시큐리티를 이용하여 엑세스토큰을 발급하는 과정
1.  Spring Security 필터 체인에서 `login` 경로를 허용한다.
    ```java
    http.authorizeHttpRequests((auth) -> auth
        .requestMatchers("/swagger-ui/**", "/login").permitAll()
        .requestMatchers(HttpMethod.POST, "/user").permitAll()
        .anyRequest().authenticated()
    );
    ```
2.  `login` 경로로 들어온 요청은 `LoginFilter` 를 거친다.
    ```java
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = obtainUsername(request);
        String password = obtainPassword(request);
    
        UsernamePasswordAuthenticationToken authToken
                = new UsernamePasswordAuthenticationToken(username, password, null);
    
        return authenticationManager.authenticate(authToken);
    }
    ```
    이때 요청으로 들어온 ID/PW는 AuthenticationToken으로 만든 뒤, `AuthenticationManager`에게 검증을 요청한다. 
3. `AuthenticationManager`는 SecurityConfig.java 파일에 빈으로 등록되어 있다.
    ```java
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    ```
    `AuthenticationManager`는 `AuthenticationToken`을 받은 뒤, 등록된 `AuthenticationProvider`에게 이 토큰을 활용해 인증을 요구한다.
4. `AuthenticationProvider`는 `UserDetailsService`를 이용해 User 테이블에 저장된 유저 데이터를 `UserDetails` 객체로 전달 받고, 이 객체와 `AuthenticationToken` 정보가 일치하는지 확인한다.
    ```java
    public class CustomUserDetailsService implements UserDetailsService {
    
        private final UserRepository userRepository;
    
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            User user = userRepository.findByLoginId(username).orElseThrow(
                    () -> new BadRequestException(ExceptionCode.NOT_FOUND_LOGIN_ID)
            );
            return new CustomUserDetails(user);
        }
    }
   ```
   인증에 성공하면 권한 등의 사용자 정보가 담긴 `Authentication` 객체를 반환한다.
5. 인증에 성공했을 때 호출되는 메서드를 오버라이딩하여 JWT 토큰을 발급한다.
    ```java
   @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();
        String username = customUserDetails.getUsername();

        String token = jwtUtil.createJwt(username, 60*60*10L);

        response.addHeader("Authorization", "Bearer " + token);
    }
    
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(401);
    }
   ```
6. 반환한 `Authentication` 객체는 세션 영역에 있는 `SecurityContext`에 저장하여 로그인 상태를 저장하고, 로그인을 유지한다. 

### JWT 토큰을 인증하는 과정
1. 사용자의 요청을 검증할 `JWTFilter` 를 등록한다.
    ```java
    http.addFilterAt(
            new JWTFilter(jwtUtil),
            LoginFilter.class
    );
   ```
2. 커스텀 `JWTFilter` 를 구현한다.
    ```java
    @RequiredArgsConstructor
    public class JWTFilter extends OncePerRequestFilter {
    
        private final JWTUtil jwtUtil;
    
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            String authorization = request.getHeader("Authorization");
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                System.out.println("token null");
                filterChain.doFilter(request, response);
                return;
            }
    
            String token = authorization.split(" ")[1];
            if (jwtUtil.isExpired(token)) {
                System.out.println("token is expired");
                filterChain.doFilter(request, response);
                return;
            }
    
            String loginId = jwtUtil.getLoginId(token);
    
            User user = new User(
                    loginId,
                    "",
                    "",
                    "",
                    "",
                    "",
                    ""
            );
    
            CustomUserDetails customUserDetails = new CustomUserDetails(user);
            Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
    
            filterChain.doFilter(request, response);
        }
    }
    ```
   요청 헤더에서 토큰 값을 가져와 디코딩 한 뒤, 그 정보로 유저 객체를 만들어 `CustomUserDetails` 객체로 변환한다.   
    이 객체를 기반으로 `Authentication`을 만들어 `SecurityContext`에 등록해둔다.
3. `SecurityContext`에 `Authentication`이 등록된 이후에는, 해당 권한을 요구하는 API를 사용할 수 있게 된다.

## 회원가입 & 로그인 API 구현 및 테스트
### 회원가입 API 명세서
<table style="text-align: center">
  <tr>
    <th>Domain</th>
    <th>Method</th>
    <th>Base URL</th>
    <th>URL</th>
    <th>Description</th>
  </tr>
  <tr>
    <td rowspan="2">Auth</td>
    <td>POST</td>
    <td rowspan="2"><code></code></td>
    <td><code>/login</code></td>
    <td>로그인</td>
  </tr>
  <tr>
    <td>PUT</td>
    <td><code>/logout</code></td>
    <td>로그아웃</td>
  </tr>
  <tr>
    <td rowspan="4">User</td>
    <td>GET</td>
    <td rowspan="4"><code>/user</code></td>
    <td><code>/{user_id}</code></td>
    <td>유저 마이페이지 조회</td>
  </tr>
  <tr>
    <td>POST</td>
    <td><code>/</code></td>
    <td>유저 생성</td>
  </tr>
  <tr>
    <td>PUT</td>
    <td><code>/{user_id}</code></td>
    <td>유저 정보 수정</td>
  </tr>
  <tr>
    <td>DELETE</td>
    <td><code>/{user_id}</code></td>
    <td>유저 삭제</td>
  </tr>
</table>

![image](https://github.com/kckc0608/kckc0608/assets/64959010/0f1bd918-a53e-4c0c-a6e3-119f0300a08a)   
다음과 같이 /user로 POST 요청을 보냈을 때   
![image](https://github.com/kckc0608/kckc0608/assets/64959010/49df2e2e-463a-4624-8459-8615a688fd38)   
요청에 대해 성공적으로 유저를 등록한다.   

![image](https://github.com/kckc0608/kckc0608/assets/64959010/ef2a9ada-d9a5-4026-a9c9-ccbdb71f91e5)
등록한 유저 계정 정보로 로그인을 시도하기 위해 /login 경로로 post 요청을 보내는 경우      

![image](https://github.com/kckc0608/kckc0608/assets/64959010/b0ce58ff-3c56-44d1-bfa2-f591f0e519fa)
그림과 같이 Authorization 헤더를 통해 JWT토큰을 받는다.

## 토큰이 필요한 API 1개 이상 구현 및 테스트
로그인, 회원가입 이외에는 모든 API에 대해 인증을 요구하기 때문에
![image](https://github.com/kckc0608/kckc0608/assets/64959010/f926fea5-3e86-4dc5-ae14-fee25e7c2b38)   
그림과 같이 토큰 없이 요청을 보내는 경우   
![image](https://github.com/kckc0608/kckc0608/assets/64959010/01b88269-b467-48a2-8dc6-c7d43ae416db)   
403 에러를 받으며 접근이 제한된다.

![image](https://github.com/kckc0608/kckc0608/assets/64959010/9a1afa70-5f40-43a3-ac85-44862349490e)   
그림과 같이 토큰을 담아서 요청을 보내면   
![image](https://github.com/kckc0608/kckc0608/assets/64959010/2cbdcd6f-e786-4ab8-94a8-0955894e20f3)   
인증에 성공하여 서버로부터 적절한 응답을 받게 된다.

<hr>

# 6주차
## 어플리케이션 배포
어플리케이션을 배포하는 것은 나만 사용하는 내 컴퓨터(로컬 환경)가 아니라, 인터넷 환경에 연결된 공공 컴퓨터(서버)에서 어플리케이션을 24시간 실행시켜 누구나 언제든 어플리케이션에 요청을 보내고, 응답을 받을 수 있도록 하는 것을 말한다.   

서버에서 자바 어플리케이션을 실행하려면 당연히 서버 컴퓨터에 자바를 설치해야 한다.
그런데 이 과정은 서버 컴퓨터가 1대일 때는 어느 정도 시행착오를 하면서 할 수 있지만, 똑같은 개발 환경을 여러 컴퓨터에 설정해야 한다면, 게다가 각각의 컴퓨터마다 사용하는 OS가 다르다면 그에 맞춰서 일일히 환경을 준비하는 것은 매우 번거로울 것이다.   

도커는 이 문제를 컨테이너 기술을 이용해 해결해준다.   
서로 다른 하드웨어 구조를 가진 컴퓨터 위에 `도커` 라는 JVM 같은 중간 매개 플랫폼을 두고 이 `도커`라는 공통의 플랫폼 위에 하나의 개발 환경을 `컨테이너`로 만들어 실행함으로써 서로 다른 하드웨어가 모두 같은 환경을 공유할 수 있다.



## 도커 설치
[공식문서](https://docs.docker.com/engine/install/ubuntu/)를 참고하여 우분투 서버에 도커를 설치한다.
1. docker `apt` repository 설정
    ```shell
    # Add Docker's official GPG key:
    sudo apt-get update
    sudo apt-get install ca-certificates curl
    sudo install -m 0755 -d /etc/apt/keyrings
    sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
    sudo chmod a+r /etc/apt/keyrings/docker.asc
    
    # Add the repository to Apt sources:
    echo \
      "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
      $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
      sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
    sudo apt-get update
    ```
2. 최신 버전 도커 설치
    ```shell
    sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
    ```
3. 도커 설치 확인
    ```shell
    sudo docker run hello-world
    ```
   실행 결과
    ```shell
    Unable to find image 'hello-world:latest' locally
    latest: Pulling from library/hello-world
    c1ec31eb5944: Pull complete
    Digest: sha256:a26bff933ddc26d5cdf7faa98b4ae1e3ec20c4985e6f87ac0973052224d24302
    Status: Downloaded newer image for hello-world:latest
    
    Hello from Docker!
    This message shows that your installation appears to be working correctly.
    
    To generate this message, Docker took the following steps:
    1. The Docker client contacted the Docker daemon.
    2. The Docker daemon pulled the "hello-world" image from the Docker Hub.
       (amd64)
    3. The Docker daemon created a new container from that image which runs the
       executable that produces the output you are currently reading.
    4. The Docker daemon streamed that output to the Docker client, which sent it
       to your terminal.
    
    To try something more ambitious, you can run an Ubuntu container with:
    $ docker run -it ubuntu bash
    
    Share images, automate workflows, and more with a free Docker ID:
    https://hub.docker.com/
    
    For more examples and ideas, visit:
    https://docs.docker.com/get-started/
    ```

## 도커 위에서 스프링 어플리케이션 실행하기
1. 도커의 컨테이너에서 스프링 어플리케이션을 실행하려면, 스프링 어플리케이션이 실행될 컨테이너 환경과 컨테이너에서 실행할 명령어를 `dockerfile`에 기술해야 한다.
    ```dockerfile
    FROM openjdk:17
    ARG JAR_FILE=/build/libs/*.jar
    COPY ${JAR_FILE} app.jar
    ENTRYPOINT ["java","-jar", "/app.jar"]
    ```
   
2. 어플리케이션을 빌드한다. 빌드된 jar 파일까지 포함하여 하나의 이미지로 만들기 위함이다.
    ```shell
    .\gradlew build
    ```
   `build/libs` 경로에 가면 아래와 같이 `.jar` 파일이 2개 생성되었음을 알 수 있다.
    ```shell
    Mode                 LastWriteTime         Length Name
    ----                 -------------         ------ ----
    -a----      2024-05-11   오후 9:35          93943 spring-everytime-0.0.1-SNAPSHOT-plain.jar
    -a----      2024-05-11   오후 9:35       57863436 spring-everytime-0.0.1-SNAPSHOT.jar
    ```
   뒤에 `-plain`이 붙은 파일은 아카이빙 파일이다. ([공식문서](https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/htmlsingle/#packaging-executable.and-plain-archives))   
   `dockerfile`에서 jar 파일을 구분하는 것이 불편하기에 이를 생성하고 싶지 않다면, `build.gradle`에 아래와 같은 코드를 추가한다.   
    ```groovy
    tasks.named('jar') {
      enabled = false
    }
    ```
   
    기존 빌드 파일을 지우고 다시 빌드하면 아래와 같이 하나의 `jar`파일만 나오는 것을 볼 수 있다.   
    ```shell
    Mode                 LastWriteTime         Length Name
    ----                 -------------         ------ ----
    -a----      2024-05-11  오후 10:33       57863436 spring-everytime-0.0.1-SNAPSHOT.jar
    ```
   
3. `dockerfile`과 위에서 빌드한 파일을 이용해 도커 이미지 만들기
    도커 엔진이 실행되는 상태에서 아래 명령어를 실행한다.   
    로컬 컴퓨터에 도커를 설치한 뒤, 이미지를 빌드하고, 서버에서는 빌드한 이미지를 가져와 자바가 설치된 컨테이너에서 바로 실행한다.   
    (따라서 서버 컴퓨터에는 자바를 별도로 설치할 필요가 없다!)
    ```shell
    docker image build -t spring:v1 .
    ```
    현재 위치(`.`)에서 `dockerfile`을 찾아 `spring`이라는 이름과 `v1`이라는 태그로 이미지를 만든다.
    ```shell
    [+] Building 10.2s (7/7) FINISHED
     => [internal] load build definition from Dockerfile                                                                                                                                                                                             0.0s
     => => transferring dockerfile: 150B                                                                                                                                                                                                             0.0s 
     => [internal] load .dockerignore                                                                                                                                                                                                                0.0s 
     => => transferring context: 2B                                                                                                                                                                                                                  0.0s 
     => [internal] load metadata for docker.io/library/openjdk:17                                                                                                                                                                                    2.5s 
     => [internal] load build context                                                                                                                                                                                                                0.3s
     => => transferring context: 57.88MB                                                                                                                                                                                                             0.3s 
     => [1/2] FROM docker.io/library/openjdk:17@sha256:528707081fdb9562eb819128a9f85ae7fe000e2fbaeaf9f87662e7b3f38cb7d8                                                                                                                              7.1s 
     => => resolve docker.io/library/openjdk:17@sha256:528707081fdb9562eb819128a9f85ae7fe000e2fbaeaf9f87662e7b3f38cb7d8                                                                                                                              0.0s 
     => => sha256:528707081fdb9562eb819128a9f85ae7fe000e2fbaeaf9f87662e7b3f38cb7d8 1.04kB / 1.04kB                                                                                                                                                   0.0s 
     => => sha256:98f0304b3a3b7c12ce641177a99d1f3be56f532473a528fda38d53d519cafb13 954B / 954B                                                                                                                                                       0.0s 
     => => sha256:5e28ba2b4cdb3a7c3bd0ee2e635a5f6481682b77eabf8b51a17ea8bfe1c05697 4.45kB / 4.45kB                                                                                                                                                   0.0s 
     => => sha256:38a980f2cc8accf69c23deae6743d42a87eb34a54f02396f3fcfd7c2d06e2c5b 42.11MB / 42.11MB                                                                                                                                                 1.9s 
     => => sha256:de849f1cfbe60b1c06a1db83a3129ab0ea397c4852b98e3e4300b12ee57ba111 13.53MB / 13.53MB                                                                                                                                                 1.0s
     => => sha256:a7203ca35e75e068651c9907d659adc721dba823441b78639fde66fc988f042f 187.53MB / 187.53MB                                                                                                                                               5.8s 
     => => extracting sha256:38a980f2cc8accf69c23deae6743d42a87eb34a54f02396f3fcfd7c2d06e2c5b                                                                                                                                                        0.7s 
     => => extracting sha256:de849f1cfbe60b1c06a1db83a3129ab0ea397c4852b98e3e4300b12ee57ba111                                                                                                                                                        0.2s 
     => => extracting sha256:a7203ca35e75e068651c9907d659adc721dba823441b78639fde66fc988f042f                                                                                                                                                        1.2s 
     => [2/2] COPY /build/libs/*.jar app.jar                                                                                                                                                                                                         0.4s 
     => exporting to image                                                                                                                                                                                                                           0.2s 
     => => exporting layers                                                                                                                                                                                                                          0.2s 
     => => writing image sha256:6d62b0cee38d5cacbb4ee62cae7138cf0ff9a185bbe269a023557005e61c1c43                                                                                                                                                     0.0s 
     => => naming to docker.io/library/spring:v1
    ```
    생성된 이미지는 아래 명령어로 확인할 수 있다.
    ```shell
    docker images
    ```
    ```shell
    REPOSITORY       TAG                   IMAGE ID       CREATED          SIZE
    spring           v1                    6d62b0cee38d   11 minutes ago   529MB
    ```
4. 이미지를 도커 허브에 push하기   
    도커 이미지는 깃허브와 유사한 `도커 허브`라는 곳에 업로드 수 있다.
    우선 업로드하기 전에 도커 허브에 로그인한다.
    ```shell
    docker login
    ```
    ```shell
    Login with your Docker ID to push and pull images from Docker Hub. If you don't have a Docker ID, head over to https://hub.docker.com to create one.
    Username: kckc0608@naver.com
    Password:
    Login Succeeded
    
    Logging in with your password grants your terminal complete access to your account.
    For better security, log in with a limited-privilege personal access token. Learn more at https://docs.docker.com/go/access-tokens/
    ```
    도커 이미지를 허브에 올릴 때는 깃허브와 비슷하게 `push`를 사용한다.
    ```shell
    docker push spring:v1
    ```
    push할 이미지읭 이름과 태그를 명시한다. 태그는 명시하지 않으면 기본값으로 `latest`를 사용한다. 
    ```shell
    The push refers to repository [docker.io/library/spring]
    79e34dc2859b: Preparing                                                                                                                                                                                                                               
    dc9fa3d8b576: Preparing                                                                                                                                                                                                                               
    27ee19dc88f2: Preparing                                                                                                                                                                                                                               
    c8dd97366670: Preparing                                                                                                                                                                                                                               
    denied: requested access to the resource is denied
    ```
    `denied: requestsd access to the resource is denied` 에러는 현재 로그인한 사용자와 이미지에 명시된 사용자가 일치하지 않아서 발생하는 오류이다.   
    이미지에 사용자를 명시하려면 `로그인 사용자명`/`이미지 이름` 형식으로 이미지 이름을 만들면 된다.
    ```shell
    PS D:\Hongik\CEOS\spring-everytime-19th> docker images
    REPOSITORY        TAG                   IMAGE ID       CREATED          SIZE
    kckc0608/spring   v1                    6d62b0cee38d   27 minutes ago   529MB
    spring            v1                    6d62b0cee38d   27 minutes ago   529MB
    ```
    다시 이미지를 만들어주었다. 이제 다시 만든 이미지를 push 한다.
    ```shell
    PS D:\Hongik\CEOS\spring-everytime-19th> docker push kckc0608/spring:v1
    The push refers to repository [docker.io/kckc0608/spring]
    79e34dc2859b: Pushed
    dc9fa3d8b576: Pushed
    27ee19dc88f2: Pushed
    c8dd97366670: Pushed
    v1: digest: sha256:bcaffbf056d4cd03f6610dc9f7e9cf3afd3c7f17a58106d31bbcf71bbc8b5f5e size: 1166
    ```
5. 서버의 도커 엔진에서 도커에 로그인한다.
    ```shell
    ubuntu@everdu-sub:~$ docker login
    Log in with your Docker ID or email address to push and pull images from Docker Hub. If you don't have a Docker ID, head over to https://hub.docker.com/ to create one.
    You can log in with your password or a Personal Access Token (PAT). Using a limited-scope PAT grants better security and is required for organizations using SSO. Learn more at https://docs.docker.com/go/access-tokens/
    
    Username: kckc0608@naver.com
    Password:
    WARNING! Your password will be stored unencrypted in /home/ubuntu/.docker/config.json.
    Configure a credential helper to remove this warning. See
    https://docs.docker.com/engine/reference/commandline/login/#credentials-store
    
    Login Succeeded
    ```
6. 도커 이미지를 다운 받는다. 깃허브와 유사하게 `pull` 명령어를 사용한다.
    ```shell
    ubuntu@everdu-sub:~$ sudo docker pull kckc0608/spring:v1
    v1: Pulling from kckc0608/spring
    38a980f2cc8a: Pull complete
    de849f1cfbe6: Pull complete
    a7203ca35e75: Pull complete
    8356870fb9b5: Pull complete
    Digest: sha256:bcaffbf056d4cd03f6610dc9f7e9cf3afd3c7f17a58106d31bbcf71bbc8b5f5e
    Status: Downloaded newer image for kckc0608/spring:v1
    docker.io/kckc0608/spring:v1
    ```
7. 다운받은 이미지로 컨테이너를 실행한다.
    ```shell
    sudo docker run -p 8080:8080 kckc0608/spring:v1
    ```
    이때 서버 포트와 컨테이너가 사용할 포트를 설정해준다.
    ```shell
      .   ____          _            __ _ _
     /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
    ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
     \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
      '  |____| .__|_| |_|_| |_\__, | / / / /
     =========|_|==============|___/=/_/_/_/
     :: Spring Boot ::                (v3.1.9)
    
    2024-05-11T14:20:00.881Z  INFO 1 --- [           main] c.c.s.SpringEverytimeApplication         : Starting SpringEverytimeApplication v0.0.1-SNAPSHOT using Java 17.0.2 with PID 1 (/app.jar started by root in /)
    
    ```
    스프링 어플리케이션이 실행된다.
8. Postman 테스트   
   ![image](https://github.com/kckc0608/kckc0608/assets/64959010/6faf481d-4a6f-4a20-b08b-eafa06e1d124)   
    login 요청은 허용을 해뒀으므로 별도 reqeust body 없이 요청을 보내보았다.   
   ![image](https://github.com/kckc0608/kckc0608/assets/64959010/e9c8c3b7-e973-4c9d-84ac-ae07dd2c515c)   
    401 에러가 발생하였으며, 이는 의도한 에러코드가 맞다.

### docker-compose 사용하기
`dockerfile`은 하나의 컨테이너를 위한 파일이다.   
만약 여러 컨테이너를 띄우고 이들 사이에 데이터를 주고 받아야 한다면 `docker-compose`를 사용하여 하나의 파일로 여러 컨테이너의 환경(이미지)을 정의할 수 있다.   

#### docker-compose의 장점
1. 간단한 컨테이너 제어 : yaml 파일 하나로 여러 컨테이너 어플리케이션을 제어할 수 있다.
2. 효율적인 협업 : `docker-compose`파일은 공유하기 쉽고, 개발팀-운영팀 사이에 협업을 더 간단하게 해준다.
3. 빠른 개발 : `docker-compose`는 컨테이너를 만들 때 캐시를 활용한다. 따라서 변경사항이 없는 컨테이너는 기존 컨테이너를 그대로 재사용하기 때문에 빠르게 실행할 수 있다.
4. 이식성 : `compose`파일에 변수를 쓸 수 있기 때문에, 각각의 환경과 사용자에 맞는 변수를 사용하여 다른 환경에서도 쉽게 compose를 이식하여 사용할 수 있다.
5. 광범위한 커뮤니티 지원

### docker-compose.yml 작성
```yaml
services:
  web:
    container_name: spring
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

  db:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: mysql
      MYSQL_DATABASE: spring_everytime
    volumes:
      - dbdata:/var/lib/mysql
    ports:
      - 3307:3306
    restart: always

volumes:
  dbdata:
  app:
```
yaml 파일을 이용해 `docker-compose up` 명령어 실행
```shell
docker-compose -f docker-compose.yml up --build
```
실행결과
```shell
[+] Building 0.7s (7/7) FINISHED
 => [internal] load .dockerignore                                                                                                              0.0s
 => => transferring context: 2B                                                                                                                0.0s 
 => [internal] load build definition from Dockerfile                                                                                           0.0s 
 => => transferring dockerfile: 150B                                                                                                           0.0s 
 => [internal] load metadata for docker.io/library/openjdk:17                                                                                  0.7s 
 => [internal] load build context                                                                                                              0.0s
 => => transferring context: 125B                                                                                                              0.0s 
 => [1/2] FROM docker.io/library/openjdk:17@sha256:528707081fdb9562eb819128a9f85ae7fe000e2fbaeaf9f87662e7b3f38cb7d8                            0.0s 
 => CACHED [2/2] COPY /build/libs/*.jar app.jar                                                                                                0.0s 
 => exporting to image                                                                                                                         0.0s 
 => => exporting layers                                                                                                                        0.0s 
 => => writing image sha256:da0ae0ad28cca7159a32989ef473e32e740b5e4d9c97ab808a4a354f6b9d5e78                                                   0.0s 
 => => naming to docker.io/library/spring-everytime-19th-web                                                                                   0.0s 
[+] Running 2/2
 ✔ Container spring-everytime-19th-db-1  Recreated                                                                                             0.1s 
 ✔ Container spring                      Recreated                                                                                             0.1s 
Attaching to spring, spring-everytime-19th-db-1
spring-everytime-19th-db-1  | 2024-05-12 01:30:35+00:00 [Note] [Entrypoint]: Entrypoint script for MySQL Server 8.0.37-1.el9 started.
spring-everytime-19th-db-1  | 2024-05-12 01:30:35+00:00 [Note] [Entrypoint]: Switching to dedicated user 'mysql'
spring-everytime-19th-db-1  | 2024-05-12 01:30:35+00:00 [Note] [Entrypoint]: Entrypoint script for MySQL Server 8.0.37-1.el9 started.
spring-everytime-19th-db-1  | 2024-05-12 01:30:35+00:00 [Note] [Entrypoint]: Initializing database files
spring-everytime-19th-db-1  | 2024-05-12T01:30:35.330023Z 0 [Warning] [MY-011068] [Server] The syntax '--skip-host-cache' is deprecated and will be removed in a future release. Please use SET GLOBAL host_cache_size=0 instead.
spring-everytime-19th-db-1  | 2024-05-12T01:30:35.330084Z 0 [System] [MY-013169] [Server] /usr/sbin/mysqld (mysqld 8.0.37) initializing of server in progress as process 83
spring-everytime-19th-db-1  | 2024-05-12T01:30:35.367326Z 1 [System] [MY-013576] [InnoDB] InnoDB initialization has started.
spring-everytime-19th-db-1  | 2024-05-12T01:30:35.595921Z 1 [System] [MY-013577] [InnoDB] InnoDB initialization has ended.
spring                      | 
spring                      |   .   ____          _            __ _ _
spring                      |  /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
spring                      | ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
spring                      |  \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
spring                      |   '  |____| .__|_| |_|_| |_\__, | / / / /
spring                      |  =========|_|==============|___/=/_/_/_/
spring                      |  :: Spring Boot ::                (v3.1.9)

```

`docker ps` 명령어 실행 결과
```shell
CONTAINER ID   IMAGE                       COMMAND                   CREATED         STATUS          PORTS                               NAMES
4c960f1161fe   spring-everytime-19th-web   "java -jar /app.jar"      8 minutes ago   Up 8 minutes    0.0.0.0:8080->8080/tcp              spring
47e90aafa7cd   mysql:8.0                   "docker-entrypoint.s…"   8 minutes ago   Up 8 minutes    33060/tcp, 0.0.0.0:3307->3306/tcp   spring-everytime-19th-db-1
```

### postman 테스트
![image](https://github.com/kckc0608/kckc0608/assets/64959010/9744898e-a1b8-4e24-8b22-dab2a434e0fa)   
`http://localhost:8080/login` 으로 POST요청을 보냈을 때   
![image](https://github.com/kckc0608/kckc0608/assets/64959010/0411b246-6c02-4549-b6b9-1d9565106489)   
다음과 같이 401로 응답하는 것을 볼 수 있다.

## 추가 API 개발
회원 탈퇴 API를 추가로 개발하였다.   
```java
@DeleteMapping
public ResponseEntity<Void> deleteUser() {
    userService.delete();
    return ResponseEntity.noContent().build();
}
```
현재 로그인한 사용자 정보는 `SecurityContextHolder`에서 가져오면 되는데, `ContextHolder`에는 어떤 서비스 레이어에서도 접근할 수 있기 때문에 컨트롤러에서 접근한 뒤, 서비스로 데이터를 굳이 넘길 필요가 없다고 생각해서 컨트롤러는 간소하게 작성하였다.

```java
public void delete() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
    String loginId = userDetails.getUsername();
    User user = userRepository.findByLoginId(loginId).orElseThrow(
            () -> new BadRequestException(ExceptionCode.NOT_FOUND_LOGIN_ID)
    );
    userRepository.delete(user);
}
```
`UserService`에서는 ContextHolder에서 현재 요청을 보낸 사용자의 auth 정보를 통해 사용자의 `loginId`를 얻고, 이를 이용해 사용자를 삭제한다.

회원 탈퇴 기능을 postman에서 테스트 해보았다.
![image](https://github.com/kckc0608/kckc0608/assets/64959010/5984700f-48a7-42c9-b78a-d765f6026a4a)   
다음과 같은 정보로 회원가입을 진행한다. (User 데이터 생성)   
![image](https://github.com/kckc0608/kckc0608/assets/64959010/0200e4f5-1342-45f0-9d06-fd5ed9349cbb)    
![image](https://github.com/kckc0608/kckc0608/assets/64959010/931381b0-f2ad-4ddd-a64d-4974cb443bf9)   
로그인에 성공해서 access 토큰을 받았다.

![image](https://github.com/kckc0608/kckc0608/assets/64959010/2ca1c420-eee7-46a9-822d-59cad83d40d4)   
access token을 이용해 회원 탈퇴 API 요청을 보낸다.   

![image](https://github.com/kckc0608/kckc0608/assets/64959010/e8a729ed-9bd2-4d4c-ada0-3c8761e9f9c4)   
다시 같은 정보로 로그인을 시도하면 로그인에 실패한다.   

### 회원 탈퇴 단위 테스트
회원 탈퇴에 대해서 단위 테스트도 작성하였다.   
```groovy
testImplementation 'org.springframework.security:spring-security-test'
```
Spring Security 테스트를 위해 의존성을 추가한다.   
```java
@BeforeEach
void setup() {
    CustomUserDetails userDetails = new CustomUserDetails(EntityGenerator.generateUser("test"));
    Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(auth);
}
```
단위 테스트를 진행할 때는 JWTFilter를 거치지 않아 `SecurityContext`에 아무런 auth가 존재하지 않는다.   
따라서 테스트를 진행하기 전에 미리 Context에 Auth Token을 넣어준다.

```java
@Test
@DisplayName("유저 회원 탈퇴 테스트")
void 회원탈퇴_테스트() {
    // given
    User user = EntityGenerator.generateUser("test");
    when(userRepository.findByLoginId(anyString())).thenReturn(Optional.of(user));

    // when
    userService.delete();

    // then
    verify(userRepository, times(1)).delete(user);
}
```
테스트 코드는 다른 메소드를 테스트할 때와 동일하게 작성한다.

### API를 작성하면서 느꼈던 점
회원 탈퇴를 하면 그 회원이 갖고 있는 토큰으로는 더 이상 인증이 필요한 api에 접근할 수 없어야 한다.   

처음에는 `Security Context Holder` 라는 개념을 보면서 나도 모르게 사용자의 인증 정보를 계속 `Security Context`에 저장하고 있다고 생각해서 `Security Context`만 비워주면 로그아웃이 된다고 생각했었다.   

```java
SecurityContextHolder.clearContext();
```
그래서 `clearContext()` 만 해주면 알아서 사용자의 인증 정보가 사라지니 로그아웃이 될 거라고 생각했었다.   
그런데 postman에서 테스트해보니 회원 탈퇴한 이후에도 여전히 기존에 발급받은 토큰으로 다른 api에 접근할 수 있다는 것을 확인했다.   

이유는 사실 금방 이해할 수 있었다.   
jwt는 사용자의 인증 정보를 서버가 저장하지 않는 stateless 특성을 갖는 방식이고, 사용자가 jwt를 매 요청마다 보내면, 서버가 토큰을 매번 인증해서 인증이 완료된 요청에 대해서 임시로 `Security Context` 에 인증 정보를 저장하는 방식이었던 것이다.   
그리고 이렇게 구현하는 이유는 스프링 어플리케이션에서 인증 정보(상태)를 전역적으로 접근하기 위해서라고 이해했다. (redux가 해주는 상태 관리와 유사하다고 이해했다.)   

그래서 로그아웃을 어떻게 구현할 수 있을지 찾아봤는데, access token만 이용하는 방식으로는 로그아웃을 구현할 수 없다.   
한번 발급한 토큰은 서버가 더 이상 제어할 수 없기 때문이다. 따라서 로그아웃한 access token을 일종의 black list에 등록해두어 로그아웃 처리를 시키거나, 클라이언트가 직접 토큰을 삭제하도록 구현해야 한다.   
access token을 black list에 등록해둔다면, 그때부터는 jwt의 상태를 서버가 관리하는 것과 같다고 생각해서 이렇게 하는 것이 옳은지 궁금했는데, 이에 대한 고민이 지난 주 정리한 JWT에 정리되어 있다.   

그래서 어차피 서버가 토큰의 상태를 관리한다면, 보안상으로도 더 유리한 refresh token을 사용해서 로그아웃을 구현한다.    
지난 주에 정리할 때는 단순히 보안 문제로만 refresh token을 사용한다고 생각했었는데, 로그아웃, 로그인 유지와 같이 서버가 로그인 정보에 대한 상태를 관리해야 할 때도 사용할 수 있음을 새로 깨닫게 되었다.   

# 7주차
## 도커 이미지 배포
도커 이미지 배포는 프리티어 기간 제한으로 인해 AWS대신 평생 무료 프리티어를 제공하는 Oracle Cloud를 이용하여 배포하였다.      
인스턴스 생성 과정은 AWS와 비슷하기 때문에 리드미에는 정리하지 않았다.   



## 배포 환경에 대한 테스트