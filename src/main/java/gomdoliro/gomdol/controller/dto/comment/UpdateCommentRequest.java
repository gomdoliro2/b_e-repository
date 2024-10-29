package gomdoliro.gomdol.controller.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateCommentRequest {
    private Long comment_id;
    private String content;
}
