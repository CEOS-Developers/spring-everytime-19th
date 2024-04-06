package com.ceos19.springeverytime.room.presentation;

import com.ceos19.springeverytime.room.dto.RoomDto;
import com.ceos19.springeverytime.room.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Room Controller", description = "대화방 컨트롤러")
@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @GetMapping("/{roomId}")
    @Operation(summary = "단건 대화방 조회", description = "특정한 대화방을 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "대화방 조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 대화방 조회")
    })
    public ResponseEntity<RoomDto> roomDetail(@PathVariable Long roomId){
        return ResponseEntity.ok(roomService.getRoomById(roomId));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "전체 대화방 조회", description = "유저가 포함된 모든 대화방을 조회합니다")
    public ResponseEntity<List<RoomDto>> roomList(@PathVariable Long userId){
        List<RoomDto> result = roomService.getAllRooms(userId).stream()
                .map(RoomDto::of)
                .toList();
        return ResponseEntity.ok(result);
    }

    @PostMapping("")
    @Operation(summary = "대화방 생성", description = "새로운 대화방 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "대화방 생성 성공"),
            @ApiResponse(responseCode = "409", description = "이미 같은 유저와 대화방이 존재함")
    })
    public ResponseEntity<Void> roomAdd(@RequestBody final RoomDto request){
        roomService.createRoom(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{roomId}")
    @Operation(summary = "대화방 삭제", description = "존재하는 대화방 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "대화방 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 대화방")
    })
    public ResponseEntity<Void> roomDelete(@PathVariable Long roomId){
        roomService.deleteRoomById(roomId);
        return ResponseEntity.ok().build();
    }
}
