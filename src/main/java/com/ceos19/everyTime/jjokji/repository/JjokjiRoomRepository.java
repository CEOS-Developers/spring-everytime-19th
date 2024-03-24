package com.ceos19.everyTime.jjokji.repository;

import com.ceos19.everyTime.jjokji.domain.JjokjiRoom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JjokjiRoomRepository extends JpaRepository<JjokjiRoom,Long> {

    @Query("select distinct jr from JjokjiRoom jr where  jr.firstReceiver.id=:memberId or jr.firstSender.id=:memberId order by jr.createdAt desc")
    List<JjokjiRoom> findJjokjiRoomByMemberId(@Param("memberId")Long memberId);

    @Query("select jr from JjokjiRoom jr where (jr.firstSender.id=:firstId and jr.firstReceiver.id =:secondId) or (jr.firstSender.id=:secondId and jr.firstReceiver.id=:firstId)")
    Optional<JjokjiRoom> findByJjokjiRoomByParticipant(@Param("firstId")Long firstId,@Param("secondId") Long secondId);

}
