package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    @Autowired
    final EntityManager em;

    public void save(User user) {
        em.persist(user);
    }

    public User findOne(String userId) {
        return em.find(User.class, userId);
    }
}
