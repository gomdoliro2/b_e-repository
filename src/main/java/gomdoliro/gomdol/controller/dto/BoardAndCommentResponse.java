package gomdoliro.gomdol.controller.dto;

import gomdoliro.gomdol.controller.dto.comment.CommentResponse;
import gomdoliro.gomdol.domain.Board;
import gomdoliro.gomdol.domain.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class BoardAndCommentResponse {
    private Long id;
    private String title;
    private String content;
    private String authorNickname;
    private LocalDate createdAt;
    private List<CommentResponse> comments;

    public BoardAndCommentResponse(Board board, List<CommentResponse> comments) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.authorNickname = board.getAuthorNickname();
        this.createdAt = board.getCreatedAt();
        this.comments = comments;
    }
}
