package com.ceos19.everytime.service;

import static org.junit.jupiter.api.Assertions.*;

import com.ceos19.everytime.exception.AppException;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
class SchoolServiceTest {
    @Autowired
    SchoolService schoolService;

    @Test
    @DisplayName("학교 조회 테스트")
    public void test() throws Exception {
        //given
        Assert.assertThrows(AppException.class, () -> schoolService.findSchoolById(123456L));
    }
}