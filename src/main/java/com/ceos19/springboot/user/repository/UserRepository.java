package com.ceos19.springboot.user.repository;

import com.ceos19.springboot.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
