package gomdoliro.gomdol.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    private String authorName;

    @Column(columnDefinition = "TEXT")
    private String content;

    @CreatedDate
    private LocalDate createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent",  cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Comment> children = new ArrayList<>();

    @Builder
    public Comment(Board board, String authorName, String content) {
        this.board = board;
        this.authorName = authorName;
        this.content = content;
    }

    public Comment(Comment parent, String authorName, String content) {
        this.parent = parent;
        this.authorName = authorName;
        this.content = content;
    }

    public void update(String content) {
        this.content = content;
    }
}
