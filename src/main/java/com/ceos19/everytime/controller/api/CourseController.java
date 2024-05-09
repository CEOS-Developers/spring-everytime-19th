package com.ceos19.everytime.controller.api;

import com.ceos19.everytime.domain.Course;
import com.ceos19.everytime.dto.BaseResponse;
import com.ceos19.everytime.dto.ReadCourseResponse;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/course")
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/{course_id}")
    public ResponseEntity<BaseResponse<ReadCourseResponse>> readCourse(@PathVariable("course_id") Long courseId) {
        try {
            Course course = courseService.findCourseById(courseId);
            ReadCourseResponse value = ReadCourseResponse.from(course);
            return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, null, value, 1));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    @DeleteMapping("/{course_id}")
    public ResponseEntity<BaseResponse> deleteCourse(@PathVariable("course_id") Long courseId) {
        try{
            courseService.removeCourseById(courseId);
            return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, null, null, 0));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }
}
