package com.ceos19.everyTime.community.repository;

import com.ceos19.everyTime.community.domain.Community;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommunityRepository extends JpaRepository<Community,Long> {

}
