package com.ceos19.springeverytime.user.service;

import com.ceos19.springeverytime.user.domain.User;
import com.ceos19.springeverytime.user.dto.request.UserRequestDto;
import com.ceos19.springeverytime.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void createUser(UserRequestDto userRequestDto) {
        if (userRepository.existsUserByLoginId(userRequestDto.getId())) {
            throw new IllegalStateException("User already exists");
        }

        User user = userRequestDto.toEntity();
        userRepository.save(user);
    }

    public User readUser(Long userId) {
        return userRepository.findUserById(userId)
                .orElseThrow(IllegalStateException::new);
    }

    public List<User> readAllUsers(){
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    public void updateUser(UserRequestDto userRequestDto, Long userId) throws Exception{
        User user = userRepository.findUserById(userId).orElseThrow(NotFoundException::new);
        user.update(userRequestDto.getPassword());
    }
}
