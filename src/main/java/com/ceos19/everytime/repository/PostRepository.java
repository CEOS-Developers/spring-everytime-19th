package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Transactional
public interface PostRepository extends JpaRepository<Post, Long> {
//    @EntityGraph(attributePaths = {"author", "attachments"})
    Optional<Post> findById(Long postId);

    @EntityGraph(attributePaths = {"author", "attachments"})
    List<Post> findByBoardIdAndTitle(Long boardId, String title);  // 특정 게시판의 제목으로 검색

    @EntityGraph(attributePaths = {"author", "attachments"})
    List<Post> findByAuthorId(Long userId);

    @EntityGraph(attributePaths = {"author", "attachments"})
    List<Post> findByBoardId(Long boardId);

//    @Query("delete from Post p where p.user.id = :userId")
    @EntityGraph(attributePaths = {"author", "attachments"})
    void deleteAllByAuthorId(Long userId);
}
