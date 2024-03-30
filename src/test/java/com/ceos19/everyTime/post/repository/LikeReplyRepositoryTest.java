package com.ceos19.everyTime.post.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.ceos19.everyTime.community.domain.Community;
import com.ceos19.everyTime.community.repository.CommunityRepository;
import com.ceos19.everyTime.member.domain.Member;
import com.ceos19.everyTime.member.repository.MemberRepository;
import com.ceos19.everyTime.post.domain.LikeReply;
import com.ceos19.everyTime.post.domain.Post;
import com.ceos19.everyTime.post.domain.Reply;
import jakarta.persistence.EntityListeners;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LikeReplyRepositoryTest {

    @Autowired
    LikeReplyRepository likeReplyRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CommunityRepository communityRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    ReplyRepository replyRepository;

    @Test
    void findByMemberIdAndReplyId() {

        //given
        Member member1 =Member.builder().name("이진우").nickName("dionisos198").password("harurka198").loginId("ddd").build();
        Member member2 =Member.builder().name("이진우2").nickName("dionisos1982").password("harurka1982").loginId("ddd2").build();

        memberRepository.save(member1);
        memberRepository.save(member2);

        Community community = Community.of(member1,"자유게시판");

        communityRepository.save(community);

        Post savedPost1 = Post.builder().member(member1).title("제목1").contents("내용1").community(community).isHideNickName(true).isQuestion(true).build();

        postRepository.save(savedPost1);

        Reply reply = Reply.builder().writer("지누").member(member1).parent(null).contents("바부").post(savedPost1).isHideNickName(true).build();

        replyRepository.save(reply);

        LikeReply likeReply = LikeReply.builder().reply(reply).member(member2).build();
        likeReplyRepository.save(likeReply);


        //when
        LikeReply findLikeReply = likeReplyRepository.findByMemberIdAndReplyId(member2.getId(),reply.getId()).orElse(null);
        LikeReply findLikeReply2 = likeReplyRepository.findByMemberIdAndReplyId(member1.getId(),reply.getId()).orElse(null);





        //then
        Assertions.assertThat(findLikeReply).isNotNull();
        Assertions.assertThat(findLikeReply2).isNull();


    }
}