package com.ceos19.springboot.timetable.repository;

import com.ceos19.springboot.timetable.entity.TimeTable;
import com.ceos19.springboot.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {

}
