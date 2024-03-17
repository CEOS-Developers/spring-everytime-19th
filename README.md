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
  
  
