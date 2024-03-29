package com.ceos19.everytime.controller.api;

import com.ceos19.everytime.domain.Board;
import com.ceos19.everytime.domain.Course;
import com.ceos19.everytime.domain.School;
import com.ceos19.everytime.dto.*;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.service.BoardService;
import com.ceos19.everytime.service.CourseService;
import com.ceos19.everytime.service.SchoolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/schools")
@RequiredArgsConstructor
public class SchoolController {
    private final SchoolService schoolService;
    private final BoardService boardService;
    private final CourseService courseService;

    @PostMapping
    public BaseResponse addSchool(@Valid @RequestBody AddSchoolRequest request) {
        School school = new School(request.getName());
        try {
            Long id = schoolService.addSchool(school);
            return new BaseResponse(HttpStatus.OK, null, id, 1);
        } catch (AppException e) {
            return new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
        }
    }

    @GetMapping("/{sid}")
    public BaseResponse<ReadSchoolResponse> readSchool(@PathVariable("sid") Long schoolId) {
        try {
            School school = schoolService.findSchoolById(schoolId);
            ReadSchoolResponse readSchoolResponse = ReadSchoolResponse.from(school);
            return new BaseResponse<>(HttpStatus.OK, null, readSchoolResponse, 1);
        } catch (AppException e) {
            return new BaseResponse<>(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
        }
    }

    @GetMapping
    public BaseResponse<List<ReadSchoolResponse>> findSchools(@RequestParam(value = "name", required = false) String name) {
        try {
            List<ReadSchoolResponse> value = new ArrayList<>();
            if (name == null) {
                // 전체 조회
                List<School> schools = schoolService.findSchools();
                schools.forEach(school -> value.add(ReadSchoolResponse.from(school)));
            } else {
                // 이름으로 단건 조회
                School school = schoolService.findSchoolByName(name);
                ReadSchoolResponse readSchoolResponse = ReadSchoolResponse.from(school);
                value.add(readSchoolResponse);
            }

            return new BaseResponse<>(HttpStatus.OK, null, value, value.size());


        } catch (AppException e) {
            return new BaseResponse<>(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
        }
    }

    @PatchMapping("/{sid}")
    public BaseResponse modifySchool(@PathVariable("sid") Long schoolId, @Valid @RequestBody ModifySchoolRequest request) {
        try {
            School school = schoolService.findSchoolById(schoolId);
            school.updateName(request.getName());
            return new BaseResponse(HttpStatus.OK, null, null, 0);
        } catch (AppException e) {
            return new BaseResponse<>(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
        }
    }

    @PostMapping("/{sid}/boards")
    public BaseResponse addBoard(@PathVariable("sid") Long schoolId, @Valid @RequestBody AddBoardRequest request) {
        try {
            School school = schoolService.findSchoolById(schoolId);
            Board board = new Board(request.getName(), school);
            Long id = boardService.addBoard(board);

            return new BaseResponse<>(HttpStatus.OK, null, id, 1);
        } catch (AppException e) {
            return new BaseResponse<>(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
        }
    }

    @GetMapping("/{sid}/boards")
    public BaseResponse<List<ReadBoardResponse>> readBoard(@PathVariable("sid") Long schoolId, @RequestParam(value = "name", required = false) String name) {
        try {
            List<ReadBoardResponse> value = new ArrayList<>();
            if (name == null) {
                // 전체 조회
                List<Board> boards = boardService.findBoardBySchoolId(schoolId);
                boards.forEach(board -> value.add(ReadBoardResponse.from(board)));
            } else {
                // 이름으로 단건 조회
                Board board = boardService.findBoardBySchoolIdAndName(schoolId, name);
                value.add(ReadBoardResponse.from(board));
            }

            return new BaseResponse<>(HttpStatus.OK, null, value, value.size());
        } catch (AppException e) {
            return new BaseResponse<>(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
        }
    }

    @PostMapping("/{sid}/courses")
    public BaseResponse addCourse(@PathVariable("sid") Long schoolId, @Valid @RequestBody AddCourseRequest request) {
        try {
            School school = schoolService.findSchoolById(schoolId);

            Course course = Course.builder()
                    .name(request.getName())
                    .courseNumber(request.getCourseNumber())
                    .room(request.getRoom())
                    .openingGrade(request.getOpeningGrade())
                    .professorName(request.getProfessorName())
                    .credit(request.getCredit())
                    .school(school)
                    .build();
            Long id = courseService.addCourse(course);

            return new BaseResponse(HttpStatus.OK, null, id, 1);
        } catch (AppException e) {
            return new BaseResponse<>(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
        }
    }

    @GetMapping("/{sid}/courses")
    public BaseResponse<List<ReadCourseResponse>> readCourse(@PathVariable("sid") Long schoolId,
                                                             @RequestParam(value = "name",required = false) String name,
                                                             @RequestParam(value = "professorName",required = false) String professorName) {
        try {
            List<ReadCourseResponse> value = new ArrayList<>();

            if (name == null && professorName == null) {  // 전체 조회
                List<Course> courses =
                        courseService.findCourseBySchoolId(schoolId);

                courses.forEach(course -> value.add(ReadCourseResponse.from(course)));
            } else if (name == null && professorName != null) {  // 교수명으로 조회
                List<Course> courses =
                        courseService.findCourseByProfessorName(schoolId, professorName);

                courses.forEach(course -> value.add(ReadCourseResponse.from(course)));
            } else if (name != null && professorName == null) { // 수업명으로 조회
                List<Course> courses =
                        courseService.findCourseByName(schoolId, name);

                courses.forEach(course -> value.add(ReadCourseResponse.from(course)));
            } else {  // 수업명 + 교수명으로 조회
                List<Course> courses =
                        courseService.findCourseByNameAndProfessorName(schoolId, name, professorName);

                courses.forEach(course -> value.add(ReadCourseResponse.from(course)));
            }

            return new BaseResponse<>(HttpStatus.OK, null, value, value.size());
        } catch (AppException e) {
            return new BaseResponse<>(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
        }

    }
}
