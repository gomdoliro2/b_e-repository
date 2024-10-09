package gomdoliro.gomdol.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateBoardRequest {
    private Long id;
    private String title;
    private String content;
}
