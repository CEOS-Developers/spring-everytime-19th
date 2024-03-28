package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.Board;
import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.domain.Post;
import com.ceos19.everytime.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {

    public static final int MAX_TITLE_LENGTH = 100;
    public static final int MAX_CONTENT_LENGTH = 2000;

    private final PostRepository postRepository;

    public Post publish(String title, String content, Member author, Board board, boolean isAnonymous){
        if(!validatePost(title,content)){
            log.info("[Service][publish] FAIL");
            return null;
        }

        Post post = new Post(title, content, author, board, isAnonymous);
        postRepository.save(post);

        log.info("[Service][publish] SUCCESS    title:{}, author:{}", title, author.getUsername());

        return post;
    }

    public void delete(Long postId){
        Optional<Post> post = postRepository.findById(postId);
        if(post.isEmpty()){
            log.info("[Service][deletePost] FAIL");
        }
        else{
            postRepository.delete(post.get());
            log.info("[Service][deletePost] SUCCESS");
        }

    }

    public Post updateTitle(Long postId, String newTitle){
        Optional<Post> post = postRepository.findById(postId);
        if(post.isEmpty() || !validateTitle(newTitle)){
            log.info("[Service][updateTitle] FAIL");
            return null;
        }

        post.get().changeTitle(newTitle);
        log.info("[Service][updateTitle] SUCCESS");
        return post.get();
    }

    public Post updateContent(Long postId, String newContent){
        Optional<Post> post = postRepository.findById(postId);
        if(post.isEmpty() || !validateContent(newContent)){
            log.info("[Service][updateContent] FAIL");
            return null;
        }

        post.get().changeContent(newContent);
        log.info("[Service][updateContent] SUCCESS");
        return post.get();
    }


    private boolean validateTitle(String title){
        if(title.isEmpty() || title.length()> MAX_TITLE_LENGTH)
            return false;
        return true;
    }

    private boolean validateContent(String content){
        if(content.isEmpty() || content.length()> MAX_CONTENT_LENGTH)
            return false;
        return true;
    }

    private boolean validatePost(String title, String content){
        if(!validateTitle(title) || !validateContent(content))
            return false;
        return true;
    }



}
