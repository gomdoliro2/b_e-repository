package gomdoliro.gomdol.service;

import gomdoliro.gomdol.controller.dto.comment.CommentRequest;
import gomdoliro.gomdol.controller.dto.comment.CommentResponse;
import gomdoliro.gomdol.controller.dto.comment.UpdateCommentRequest;
import gomdoliro.gomdol.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public CommentResponse save(CommentRequest request) {
        Board board = boardRepository.findById(request.getBoardId()).orElseThrow(NoSuchElementException::new);
        if(request.getContent() == null || request.getContent().isEmpty()) {
            throw new IllegalArgumentException("답글이 작성되지 않았습니다.");
        }

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        String authorName = memberRepository.findByEmail(email)
                .map(Member::getNickname)
                .orElseThrow(() -> new NoSuchElementException("로그인 된 회원을 찾을 수 없습니다."));

        Comment comment = Comment.builder()
                .board(board)
                .authorName(authorName)
                .content(request.getContent())
                .build();
        Comment saveComment = commentRepository.save(comment);
        return new CommentResponse(saveComment);
    }

    @Transactional
    public CommentResponse update(UpdateCommentRequest request) {
        Comment comment = commentRepository.findById(request.getComment_id())
                .orElseThrow(() -> new NoSuchElementException("찾으시는 댓글이 존재하지 않습니다."));
        comment.update(request.getContent());

        return new CommentResponse(comment);
    }

    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}