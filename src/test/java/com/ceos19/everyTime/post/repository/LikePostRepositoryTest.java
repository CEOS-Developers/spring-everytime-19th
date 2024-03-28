package com.ceos19.everyTime.post.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.ceos19.everyTime.community.domain.Community;
import com.ceos19.everyTime.community.repository.CommunityRepository;
import com.ceos19.everyTime.member.domain.Member;
import com.ceos19.everyTime.member.repository.MemberRepository;
import com.ceos19.everyTime.post.domain.LikePost;
import com.ceos19.everyTime.post.domain.Post;
import jakarta.persistence.EntityListeners;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@EntityListeners(AuditingEntityListener.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LikePostRepositoryTest {

    @Autowired
    LikePostRepository likePostRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CommunityRepository communityRepository;

    @Autowired
    PostRepository postRepository;

    @Test
    void findByMemberIdAndPostId() {
        //given
        Member member1 =Member.builder().name("이진우").nickName("dionisos198").password("harurka198").loginId("ddd").build();
        Member member2 =Member.builder().name("이진우2").nickName("dionisos1982").password("harurka1982").loginId("ddd2").build();

        memberRepository.save(member1);
        memberRepository.save(member2);

        Community community = Community.builder().name("자유게시판").member(member1).build();

        communityRepository.save(community);

        Post savedPost1 = Post.builder().member(member1).title("제목1").contents("내용1").community(community).isHideNickName(true).isQuestion(true).build();
        Post savedPost2 = Post.builder().member(member1).title("제목2").contents("내용2").community(community).isHideNickName(true).isQuestion(true).build();
        Post savedPost3 = Post.builder().member(member1).title("제목3").contents("내용3").community(community).isHideNickName(true).isQuestion(true).build();

        postRepository.save(savedPost1);
        postRepository.save(savedPost2);
        postRepository.save(savedPost3);

        LikePost likePost=LikePost.builder().member(member2).post(savedPost1).build();


        //when
        likePostRepository.save(likePost);
        LikePost findLikePost = likePostRepository.findByMemberIdAndPostId(member2.getId(),savedPost1.getId()).orElse(null);
        LikePost findLikePost2 = likePostRepository.findByMemberIdAndPostId(member1.getId(),savedPost1.getId()).orElse(null);


        //then
        Assertions.assertThat(findLikePost).isNotNull();
        Assertions.assertThat(findLikePost2).isNull();

    }
}