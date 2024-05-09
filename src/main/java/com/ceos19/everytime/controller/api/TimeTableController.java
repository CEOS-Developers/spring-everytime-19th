package com.ceos19.everytime.controller.api;

import com.ceos19.everytime.domain.TimeTable;
import com.ceos19.everytime.dto.BaseResponse;
import com.ceos19.everytime.dto.ReadCourseResponse;
import com.ceos19.everytime.dto.ReadTimeTableResponse;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.repository.TimeTableCourseRepository;
import com.ceos19.everytime.service.CourseService;
import com.ceos19.everytime.service.TimeTableService;
import com.ceos19.everytime.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/timeTable/{timeTable_id}")
@RequiredArgsConstructor
public class TimeTableController {
    private final TimeTableService timeTableService;
    private final CourseService courseService;
    private final TimeTableCourseRepository timeTableCourseRepository;

    @PatchMapping("/course")
    public ResponseEntity<BaseResponse> addCourse(@PathVariable("timeTable_id") Long timeTableId, @RequestParam("course_id") Long courseId) {
        try {
            Long id = timeTableService.addCourseToTimeTable(timeTableId, courseId);

            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK, null, id, 1));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    @GetMapping
    public ResponseEntity<BaseResponse> readTimeTable(@PathVariable("timeTable_id") Long timeTableId) {
        try {
            TimeTable timeTable = timeTableService.findTimeTableById(timeTableId);
            ReadTimeTableResponse value = ReadTimeTableResponse.from(timeTable);

            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK, null, value, 1));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    @GetMapping("/courses")
    public ResponseEntity<BaseResponse<List<ReadCourseResponse>>> readCourses(@PathVariable("timeTable_id") Long timeTableId) {
        try {
            List<ReadCourseResponse> value = new ArrayList<>();
            courseService.findCourseByTimeTableId(timeTableId).forEach(table ->
                    value.add(ReadCourseResponse.from(table))
            );

            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK, null, value, value.size()));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    @DeleteMapping
    public ResponseEntity<BaseResponse> deleteTimeTable(@PathVariable("timeTable_id") Long timeTableId) {
        try {
            timeTableService.removeTimeTable(timeTableId);

            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK, null, null, 0));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }
    @DeleteMapping("/course/{course_id}")
    public ResponseEntity<BaseResponse> deleteCourse(@PathVariable("timeTable_id") Long timeTableId, @PathVariable("course_id") Long courseId) {
        try {
            timeTableService.removeCourseFromTimeTable(timeTableId, courseId);

            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK, null, null, 0));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }
}
