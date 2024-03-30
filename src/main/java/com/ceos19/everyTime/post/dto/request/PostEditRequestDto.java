package com.ceos19.everyTime.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@Setter
public class PostEditRequestDto {

    @NotEmpty
    @Schema(description = "글의 제목", example = "고민있어요")
    private String title;

    @NotEmpty
    @Schema(description = "글의 내용", example = "사실없어요")
    private String contents;

    @Schema(description = "질문글 여부", example = "false")
    boolean question;

    @Schema(description = "익명 여부", example = "true")
    boolean hideNickName;

    @Schema(description = "이미지 데이터")
    List<MultipartFile> multipartFileList = new ArrayList<>();

    @Builder
    public PostEditRequestDto(String title,String contents,boolean question,boolean hideNickName,List<MultipartFile> multipartFileList){
        this.title = title;
        this.contents = contents;
        this.question = question;
        this.hideNickName = hideNickName;
        this.multipartFileList = multipartFileList;
    }
}
