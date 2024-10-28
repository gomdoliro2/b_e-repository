package gomdoliro.gomdol.controller.dto;

import gomdoliro.gomdol.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class SaveBoardResponse {
    private Long id;
    private String title;
    private String content;
    private String authorNickname;
    private LocalDate createdAt;

    public SaveBoardResponse(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.authorNickname = board.getAuthorNickname();
        this.createdAt = board.getCreatedAt();
    }
}
