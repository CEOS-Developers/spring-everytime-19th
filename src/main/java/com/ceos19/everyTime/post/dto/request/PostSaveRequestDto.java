package com.ceos19.everyTime.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@Setter
@Schema(description = "Post 를 저장하기 위한 DTO")
public class PostSaveRequestDto {


    @NotBlank(message = "글의 제목은 빌 수 없습니다")
    @Schema(description = "글의 제목", example = "고민있어요")
    @Size(min = 5,message = "글의 제목은 5 이상이어야 합니다. ")
    private String title;

    @NotBlank(message = "글의 내용은 빌 수 없습니다")
    @Schema(description = "글의 내용",example = "사실 없어요")
    @Size(min = 5,message = "글의 내용은 5 이상이어야 합니다. ")
    private String contents;

    @NotNull(message = "질문글 여부는 빌 수 없습니다")
    @Schema(description = "질문글 여부",example = "true")
    boolean question;

    @NotNull(message = "익명 여부는 빌 수 없습니다")
    @Schema(description = "익명 여부", example = "true")
    boolean hideNickName;

    @Schema(description = "이미지 데이터")
    List<MultipartFile> multipartFileList = new ArrayList<>();

    @Builder
    public PostSaveRequestDto(String title,String contents,boolean question,boolean hideNickName,List<MultipartFile> multipartFileList){
        this.title = title;
        this.contents = contents;
        this.question = question;
        this.hideNickName = hideNickName;
        this.multipartFileList = multipartFileList;
    }
}
