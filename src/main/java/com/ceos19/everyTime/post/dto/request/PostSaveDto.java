package com.ceos19.everyTime.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class PostSaveDto {


    @NotEmpty
    private String title;

    @NotEmpty
    private String contents;

    boolean question;
    boolean hideNickName;

    List<MultipartFile> multipartFileList = new ArrayList<>();

    @Builder
    public PostSaveDto(String title,String contents,boolean question,boolean hideNickName,List<MultipartFile> multipartFileList){
        this.title = title;
        this.contents = contents;
        this.question = question;
        this.hideNickName = hideNickName;
        this.multipartFileList = multipartFileList;
    }
}
