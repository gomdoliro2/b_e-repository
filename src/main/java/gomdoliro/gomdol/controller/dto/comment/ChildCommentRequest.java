package gomdoliro.gomdol.controller.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChildCommentRequest {
    private Long commentId;
    private String content;
}
