package gomdoliro.gomdol.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    private LocalDate createdAt;


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
