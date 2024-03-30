package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.AboutPost.Post;
import com.ceos19.everytime.domain.AboutUser.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface PostRepository extends JpaRepository<Post,Long> {

}
