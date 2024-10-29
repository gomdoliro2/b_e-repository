package gomdoliro.gomdol.controller.dto.comment;

import gomdoliro.gomdol.domain.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CommentResponse {
    private Long id;
    private String authorName;
    private String content;
    private LocalDate createdAt;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.authorName = comment.getAuthorName();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
    }
}
