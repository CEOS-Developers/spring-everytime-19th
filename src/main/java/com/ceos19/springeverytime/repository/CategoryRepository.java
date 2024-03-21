package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.Category;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CategoryRepository extends JpaRepository<Category, Long> {}