package gomdoliro.gomdol.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SaveBoardRequest {
    private String title;
    private String content;
}
