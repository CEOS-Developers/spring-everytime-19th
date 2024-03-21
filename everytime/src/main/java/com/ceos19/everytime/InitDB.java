package com.ceos19.everytime;


import com.ceos19.everytime.domain.*;
import com.ceos19.everytime.repository.PostRepository;
import com.ceos19.everytime.repository.SchoolRepository;
import com.ceos19.everytime.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct  // 초기화 콜백
    public void init() {
//        initService.schoolInit();
//        initService.userAndPostInit();
//        initService.commentAndReplyInit();
//        initService.heartInit();
//        initService.messageInit();
//        initService.messageTest();
//        initService.removePost();
//        initService.removeUser();
//        initService.removeComment();
//        initService.removeReply();
//        initService.removeHeart();
    }


    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        private final PostRepository postRepository;
        private final UserRepository userRepository;
        private final SchoolRepository schoolRepository;

        public void schoolInit() {
            School school = new School("school", "asdf");
            schoolRepository.save(school);
        }

        public void userAndPostInit() {
            School school = schoolRepository.findByName("school").get();
            User user1 = new User("testUser1", "10000", "10000@asdf.com", "asdf1", school);
            User user2 = new User("testUser2", "10001", "10001@asdf.com", "asdf2", school);
            user1.createPost("title", "content...");
            userRepository.save(user1);
            userRepository.save(user2);
        }

        public void commentAndReplyInit() {
            Post post = postRepository.findByTitle("title").get();
            User user1 = userRepository.findByName("user1").get();
            User user2 = userRepository.findByName("user2").get();

            post.addCommentFromUser(user1, "comment..");
            Comment comment = post.getComments().get(0);
            comment.addReplyFromUser(user2, "reply1..");
            comment.addReplyFromUser(user1, "reply2..");
            comment.addReplyFromUser(user2, "reply3..");

            post.addCommentFromUser(user1, "comment2..");
            Comment comment2 = post.getComments().get(1);
            comment2.addReplyFromUser(user1, "reply4..");
            comment2.addReplyFromUser(user2, "reply5..");
            comment2.addReplyFromUser(user1, "reply6..");
        }

        public void heartInit() {
            Post post = postRepository.findByTitle("title").get();
            User user1 = userRepository.findByName("user1").get();
            User user2 = userRepository.findByName("user2").get();

            post.addHeartFromUser(user1);
            post.addHeartFromUser(user2);
        }

        public void messageInit() {
            User sender = userRepository.findByName("user1").get();
            User receiver = userRepository.findByName("user2").get();

            Message message = new Message("i love you", "구라임 ㅋ");
            message.setSenderAndReceiver(sender, receiver);
            em.flush();

        }

        public void messageTest() {
            User sender = userRepository.findByName("user1").get();
            User receiver = userRepository.findByName("user2").get();

            List<Message> sendMessages = sender.getSendMessages();
            List<Message> receiveMessages = receiver.getReceiveMessages();

            for (Message sendMessage : sendMessages) {
                System.out.println("sendMessage = " + sendMessage);
            }
            for (Message receiveMessage : receiveMessages) {
                System.out.println("receiveMessage = " + receiveMessage);
            }
        }

        public void removePost() {
            postRepository.deleteAll();
        }

        public void removeUser() {
            userRepository.deleteAll();
        }

        public void removeComment() {
            Post post = postRepository.findByTitle("title").get();
            Comment comment = post.getComments().stream()
                    .filter(c -> c.getContent().equals("comment.."))
                    .findFirst()
                    .orElse(null);

            post.removeCommentFromUserAndPost(comment);
        }

        public void removeReply() {
            Post post = postRepository.findByTitle("title").get();
            Comment comment = post.getComments().stream()
                    .filter(c -> c.getContent().equals("comment.."))
                    .findFirst()
                    .orElse(null);

            Reply reply = comment.getReplies().stream()
                    .filter(r -> r.getContent().equals("reply1.."))
                    .findFirst()
                    .orElse(null);

            comment.removeReplyFromUserAndComment(reply);
        }

        public void removeHeart() {
            Post post = postRepository.findByTitle("title").get();
            User user1 = userRepository.findByName("user1").get();

            post.removeHeartFromUserAndPost(user1);
        }
    }
}
