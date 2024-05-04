package com.ceos19.everytime.dto.like;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class LikeRequest {

    @NotNull(message = "사용자 아이디를 적어주세요.")
    private Long memberId;
}
