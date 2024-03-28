package com.ceos19.springeverytime.domain.post.repository;

import com.ceos19.springeverytime.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {
    /**
     * 왜인지 모르겠는데, userId 를 Post로 인식해서 JPQL을 사용했습니다..
     * */
    @Query("""
    select exists(select 1 from Post p where p.author.userId = :userId and p.postId = :id)
    """)
    boolean existsByUserIdAndId(final Long userId, final Long id);
}
