package com.ceos19.everytime.controller.api;

import com.ceos19.everytime.domain.School;
import com.ceos19.everytime.dto.AddSchoolRequest;
import com.ceos19.everytime.dto.BaseResponse;
import com.ceos19.everytime.dto.FindSchoolResponse;
import com.ceos19.everytime.dto.ModifySchooolRequest;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.service.SchoolService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/schools")
@RequiredArgsConstructor
public class SchoolController {
    private final SchoolService schoolService;

    @PostMapping
    public BaseResponse addSchool(@RequestBody AddSchoolRequest request) {
        School school = new School(request.getName());
        try {
            Long id = schoolService.addSchool(school);
            return new BaseResponse(HttpStatus.OK, null, id, 1);
        } catch (AppException e) {
            return new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
        }
    }

    @GetMapping("/{sid}")
    public BaseResponse<FindSchoolResponse> findSchool(@PathVariable("sid") Long schoolId) {
        try {
            School school = schoolService.findSchoolById(schoolId);
            FindSchoolResponse findSchoolResponse = FindSchoolResponse.from(school);
            return new BaseResponse<>(HttpStatus.OK, null, findSchoolResponse, 1);
        } catch (AppException e) {
            return new BaseResponse<>(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
        }
    }

    @GetMapping
    public BaseResponse<List<FindSchoolResponse>> findSchools(@RequestParam(value = "name",required = false) String name) {
        try {
            List<FindSchoolResponse> value = new ArrayList<>();
            if (name==null) {
                // 전체 조회
                List<School> schools = schoolService.findSchools();
                schools.forEach(school -> value.add(FindSchoolResponse.from(school)));
                return new BaseResponse<>(HttpStatus.OK, null, value, value.size());
            }

            // 이름으로 단건 조회
            School school = schoolService.findSchoolByName(name);
            FindSchoolResponse findSchoolResponse = FindSchoolResponse.from(school);
            value.add(findSchoolResponse);
            return new BaseResponse<>(HttpStatus.OK, null, value, 1);

        } catch (AppException e) {
            return new BaseResponse<>(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
        }
    }

    @PatchMapping("/{sid}")
    public BaseResponse modifySchool(@PathVariable("sid") Long schoolId, @RequestBody ModifySchooolRequest request) {
        try {
            School school = schoolService.findSchoolById(schoolId);
            school.updateName(request.getName());
            return new BaseResponse(HttpStatus.OK, null, null, 0);
        } catch (AppException e) {
            return new BaseResponse<>(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
        }
    }
}
