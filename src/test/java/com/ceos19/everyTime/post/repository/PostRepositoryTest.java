package com.ceos19.everyTime.post.repository;



import com.ceos19.everyTime.community.domain.Community;
import com.ceos19.everyTime.community.repository.CommunityRepository;
import com.ceos19.everyTime.member.domain.Member;
import com.ceos19.everyTime.member.repository.MemberRepository;
import com.ceos19.everyTime.post.domain.Post;
import jakarta.persistence.EntityListeners;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @Test
    @DisplayName("게시글 단건 저장")
    public void savePost(){
        //given
        Post post =createTestPost(1);

        //when
        Post savedPost = postRepository.save(post);

        //then
        Assertions.assertThat(post).isSameAs(savedPost);
        Assertions.assertThat(post.getContents()).isEqualTo("좋아!1");
        Assertions.assertThat(post.getTitle()).isEqualTo("에붕이");

    }
    @Test
    @DisplayName("게시글 단건 조회")
    public void getPost(){
        //given
        Post savedPost= postRepository.save(createTestPost(2));


        //when
        Post findPost=postRepository.findById(savedPost.getId()).get();

        //then
        Assertions.assertThat(savedPost).isSameAs(findPost);
        Assertions.assertThat(findPost.getContents()).isEqualTo("좋아!2");

    }
    @Test
    @DisplayName("게시글 목록 조회")
    public void getPostList(){
        //given
        postRepository.save(createTestPost(1));
        postRepository.save(createTestPost(2));
        postRepository.save(createTestPost(3));

        //when
        List<Post> postList=postRepository.findAll();

        //then
        Assertions.assertThat(postList.size()).isEqualTo(3);

        //해당 객체가 출력 되는지 확인하기
        postList.stream().forEach(p->{
            System.out.println("내용"+p.getContents()+"제목"+p.getTitle());
        });

    }





    private Member createTestMember(){
        return Member.builder().loginId("hi").nickName("jinu").password("5151").name("jinwoo").build();
    }

    private Community createTestCommunity(Member member){
          return Community.builder().member(member).name("자유게시판").build();
    }

    private Post createTestPost(int i){
        Member member=createTestMember();
        memberRepository.save(member);

        Community community = createTestCommunity(member);
        communityRepository.save(community);


        return   Post.builder()
            .member(member)
            .community(community)
            .isHideNickName(true)
            .isQuestion(false)
            .title("에붕이")
            .contents("좋아!"+i)
            .build();
    }


    //게시글 목록 조회 테스트를 수행하였을 때

   /* Hibernate:
    insert
        into
    member
        (login_id,name,nick_name,password)
    values
        (?,?,?,?)
    Hibernate:
    insert
        into
    community
        (created_at,member_id,name,updated_at)
    values
        (?,?,?,?)
    Hibernate:
    insert
        into
    post
        (community_id,contents,created_at,is_question,like_count,member_id,reply_count,title,updated_at,writer)
    values
        (?,?,?,?,?,?,?,?,?,?)
    Hibernate:
    insert
        into
    member
        (login_id,name,nick_name,password)
    values
        (?,?,?,?)
    Hibernate:
    insert
        into
    community
        (created_at,member_id,name,updated_at)
    values
        (?,?,?,?)
    Hibernate:
    insert
        into
    post
        (community_id,contents,created_at,is_question,like_count,member_id,reply_count,title,updated_at,writer)
    values
        (?,?,?,?,?,?,?,?,?,?)
    Hibernate:
    insert
        into
    member
        (login_id,name,nick_name,password)
    values
        (?,?,?,?)
    Hibernate:
    insert
        into
    community
        (created_at,member_id,name,updated_at)
    values
        (?,?,?,?)
    Hibernate:
    insert
        into
    post
        (community_id,contents,created_at,is_question,like_count,member_id,reply_count,title,updated_at,writer)
    values
        (?,?,?,?,?,?,?,?,?,?)
    Hibernate:
    select
    p1_0.post_id,
    p1_0.community_id,
    p1_0.contents,
    p1_0.created_at,
    p1_0.is_question,
    p1_0.like_count,
    p1_0.member_id,
    p1_0.reply_count,
    p1_0.title,
    p1_0.updated_at,
    p1_0.writer
        from
    post p1_0
    내용좋아!1제목에붕이
    내용좋아!2제목에붕이
    내용좋아!3제목에붕이

    과 같은 쿼리가 발생한다.
    */


}