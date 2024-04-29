package com.ceos19.everytime.controller.api;

import com.ceos19.everytime.dto.AddChattingRoomRequest;
import com.ceos19.everytime.dto.BaseResponse;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.service.ChattingRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chattingRoom")
@RequiredArgsConstructor
public class ChattingRoomController {
    private final ChattingRoomService chattingRoomService;

    @PostMapping
    public ResponseEntity<BaseResponse> addChattingRoom(@Valid @RequestBody AddChattingRoomRequest request) {
        try {
            Long id = chattingRoomService.addChattingRoom(request);

            return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK, null, id, 1));
        } catch (AppException e) {
            BaseResponse response =
                    new BaseResponse(e.getErrorCode().getHttpStatus(), e.getMessage(), null, 0);
            return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
        }
    }
}
