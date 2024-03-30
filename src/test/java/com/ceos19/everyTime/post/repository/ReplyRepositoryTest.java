package com.ceos19.everyTime.post.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.ceos19.everyTime.community.domain.Community;
import com.ceos19.everyTime.community.repository.CommunityRepository;
import com.ceos19.everyTime.member.domain.Member;
import com.ceos19.everyTime.member.repository.MemberRepository;
import com.ceos19.everyTime.post.domain.Post;
import com.ceos19.everyTime.post.domain.Reply;
import jakarta.persistence.EntityListeners;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReplyRepositoryTest {

    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    CommunityRepository communityRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    MemberRepository memberRepository;

    Member member1;
    Member member2;

    Member member3;
    Community community;

    Post post;

    Reply reply1;
    Reply reply2;
    Reply reply3;
    Reply reply4;

    @BeforeEach
    public void saveSetting(){
         member1 = Member.builder().name("일진우").nickName("dionisos198").loginId("dionisos19").password("ddd").build();
         member2 = Member.builder().name("이진우").nickName("dionisos1982").loginId("dionisos192").password("ddd").build();
         member3 = Member.builder().name("삼진우").nickName("dionisos1983").loginId("dionisos193").password("ddd").build();

        memberRepository.save(member1); memberRepository.save(member2); memberRepository.save(member3);

         community=Community.of(member1,"자유게시판");
        communityRepository.save(community);

         post = Post.builder().member(member1).title("이정도면 몇타치?").contents("아침에 일어나서 사과먹음")
            .isQuestion(false).isHideNickName(true).community(community).member(member1).build();

        postRepository.save(post);

         reply1= Reply.builder().member(member2).post(post).contents("거짓말치지마").parent(null).build();
         reply2 =  Reply.builder().member(member3).post(post).contents("대단한걸?").parent(null).build();
        reply3 =  Reply.builder().member(member3).post(post).contents("대단한걸?").parent(reply2).build();
         reply4 =  Reply.builder().member(member1).post(post).contents("진짜요?").parent(reply2).build();

        replyRepository.save(reply1);replyRepository.save(reply2); replyRepository.save(reply3); replyRepository.save(reply4);

        System.out.println(post.getId());
        System.out.println(member1.getId());
    }

    @Test
    void findParentReplyByPostId() {
        //given

        //when

    }

    @Test
    void findParentReplyByPostIdWithFetchMember() {
        //given


        //when
        List<Reply> parentReplyByPostIdWithFetchMember = replyRepository.findParentReplyByPostIdWithFetchMember(
            post.getId());

        //then
        Assertions.assertThat(parentReplyByPostIdWithFetchMember).size().isEqualTo(2);

    }

    @Test
    void findDefaultNickNameReplyByMemberIdAndPostIdLimitOne() {

        //given

        //when
        Optional<Reply> defaultNickNameReplyByMemberIdAndPostIdLimitOne = replyRepository.findDefaultNickNameReplyByMemberIdAndPostIdLimitOne(
            member2.getId(), post.getId());

        //then
        Assertions.assertThat(defaultNickNameReplyByMemberIdAndPostIdLimitOne).isNotNull();

    }
}