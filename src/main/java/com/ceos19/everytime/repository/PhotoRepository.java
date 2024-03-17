package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
