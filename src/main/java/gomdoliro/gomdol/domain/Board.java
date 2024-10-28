package gomdoliro.gomdol.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String authorNickname;


    @Builder
    public Board(String title, String content, String authorNickname) {
        this.title = title;
        this.content = content;
        this.authorNickname = authorNickname;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
