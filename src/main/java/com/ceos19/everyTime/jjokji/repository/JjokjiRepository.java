package com.ceos19.everyTime.jjokji.repository;

import com.ceos19.everyTime.jjokji.domain.Jjokji;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JjokjiRepository extends JpaRepository<Jjokji, Long> {
    @Query("select j from Jjokji j  where j.jjokjiRoom.id =:jjokjiRoomId order by j.createdAt desc limit 1")
    Optional<Jjokji> findLatestJjokjiByJjokjiRoomId(@Param("jjokjiRoomId") Long jjokjiRoomId);

    List<Jjokji> findJjokjisByJjokjiRoom_Id(Long jjokjiRoomId);




}
