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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SchoolController {
    private final SchoolService schoolService;
    private final BoardService boardService;
    private final CourseService courseService;

    /**
     * 학교 등록
     *
     * @param request
     * @return
     */
    @PostMapping("/school")
    public ResponseEntity<BaseResponse> addSchool(@Valid @RequestBody AddSchoolRequest request) {
        try {
            Long id = schoolService.addSchool(request.getName());

            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK, null, id, 1));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    /**
     * PK로 학교 조회
     *
     * @param schoolId
     * @return
     */
    @GetMapping("/school/{school_id}")
    public ResponseEntity<BaseResponse<ReadSchoolResponse>> readSchool(@PathVariable("school_id") Long schoolId) {
        try {
            School school = schoolService.findSchoolById(schoolId);
            ReadSchoolResponse value = ReadSchoolResponse.from(school);

            return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, null, value, 1));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    /**
     * 이름으로 학교 조회
     *
     * @param name
     * @return
     */
    @GetMapping("/school")
    public ResponseEntity<BaseResponse<ReadSchoolResponse>> readSchoolResponseBaseResponse(@RequestParam(value = "name") String name) {
        try {
            School school = schoolService.findSchoolByName(name);
            ReadSchoolResponse value = ReadSchoolResponse.from(school);

            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK, null, value, 1));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    /**
     * 모든 학교 조회
     *
     * @return
     */
    @GetMapping("/schools")// 전체 조회
    public ResponseEntity<BaseResponse<List<ReadSchoolResponse>>> readSchool() {
        try {
            List<ReadSchoolResponse> value = new ArrayList<>();
            List<School> schools = schoolService.findSchools();
            schools.forEach(school -> value.add(ReadSchoolResponse.from(school)));

            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK, null, value, value.size()));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    /**
     * 학교명 수정
     *
     * @param schoolId
     * @param request
     * @return
     */
    @PatchMapping("/school/{school_id}")
    public ResponseEntity<BaseResponse> modifySchool(@PathVariable("school_id") Long schoolId, @Valid @RequestBody ModifySchoolRequest request) {
        try {
            schoolService.modifySchool(schoolId, request.getName());

            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK, null, null, 0));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    /**
     * 학교에 게시판 등록
     *
     * @param schoolId
     * @param request
     * @return
     */
    @PostMapping("/school/{school_id}/board")
    public ResponseEntity<BaseResponse> addBoard(@PathVariable("school_id") Long schoolId, @Valid @RequestBody AddBoardRequest request) {
        try {
            Board board = boardService.addBoard(request.getName(), schoolId);

            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK, null, board.getId(), 1));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    /**
     * 학교에 있는 모든 게시판 조회
     *
     * @param schoolId
     * @return
     */
    @GetMapping("/school/{school_id}/boards")
    public ResponseEntity<BaseResponse<List<ReadBoardResponse>>> readBoards(@PathVariable("school_id") Long schoolId) {
        List<ReadBoardResponse> value = new ArrayList<>();
        try {
            List<Board> boards = boardService.findBoardBySchoolId(schoolId);
            boards.forEach(board -> value.add(ReadBoardResponse.from(board)));
            return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, null, value, value.size()));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    /**
     * 이름으로 교내게시판 조회
     *
     * @param schoolId
     * @param name
     * @return
     */
    @GetMapping("/school/{school_id}/board")
    public ResponseEntity<BaseResponse<ReadBoardResponse>> readBoard(@PathVariable("school_id") Long schoolId,
                                                                     @RequestParam(value = "name") String name) {
        try {
            // 이름으로 게시판 단건 조회
            Board board = boardService.findBoardBySchoolIdAndName(schoolId, name);
            ReadBoardResponse value = ReadBoardResponse.from(board);

            return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, null, value, 1));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    /**
     * 학교에 수업 등록
     *
     * @param schoolId
     * @param request
     * @return
     */
    @PostMapping("/school/{school_id}/course")
    public ResponseEntity<BaseResponse> addCourse(@PathVariable("school_id") Long schoolId,
                                  @Valid @RequestBody AddCourseRequest request) {
        try {
            School school = schoolService.findSchoolById(schoolId);
            Long courseId = courseService.addCourse(request, schoolId);

            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK, null, courseId, 1));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }

    /**
     * 수업명, 교수명으로 교내 수업 조회
     *
     * @param schoolId
     * @param name
     * @param professorName
     * @return
     */
    @GetMapping("/school/{school_id}/courses")
    public ResponseEntity<BaseResponse<List<ReadCourseResponse>>> readCourse(@PathVariable("school_id") Long schoolId,
                                                             @RequestParam(value = "name", required = false) String name,
                                                             @RequestParam(value = "professorName", required = false) String professorName) {
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

            return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, null, value, value.size()));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }
}
