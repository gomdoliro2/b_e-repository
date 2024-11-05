package gomdoliro.gomdol.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SummaryResponse {
    private String content;

    @Builder
    public SummaryResponse(String content) {
        this.content = content;
    }
}
