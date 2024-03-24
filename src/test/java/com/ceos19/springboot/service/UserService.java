package com.ceos19.springboot.service;

import com.ceos19.springboot.domain.User;
import com.ceos19.springboot.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
@Transactional
public class UserService {

    private static final Logger Logger = (java.util.logging.Logger) LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Autowired
    private UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByUsername(final String username) {
        Logger.info("find by username");
        return userRepository.findByUsername(username);
    }

    public User addUser(final User user) {
        Logger.info("user sign in");
        return userRepository.save(user);
    }

    public void removeUser(final User user) {
        Logger.info("user sign out");
        userRepository.delete(user);
    }

}
