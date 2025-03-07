package gomdoliro.gomdol.service;

import gomdoliro.gomdol.controller.dto.comment.ChildCommentRequest;
import gomdoliro.gomdol.controller.dto.comment.CommentRequest;
import gomdoliro.gomdol.controller.dto.comment.CommentResponse;
import gomdoliro.gomdol.controller.dto.comment.UpdateCommentRequest;
import gomdoliro.gomdol.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public CommentResponse save(CommentRequest request) {
        Board board = boardRepository.findById(request.getBoardId())
                .orElseThrow(() -> new NoSuchElementException("해당 게시글이 존재하지 않습니다."));
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

    public CommentResponse childSave(ChildCommentRequest request) {
        Comment pComment = commentRepository.findById(request.getCommentId()).orElseThrow(NoSuchElementException::new);
        if(request.getContent() == null || request.getContent().isEmpty()) {
            throw new IllegalArgumentException("답글이 작성되지 않았습니다.");
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        String authorName = memberRepository.findByEmail(email)
                .map(Member::getNickname)
                .orElseThrow(() -> new NoSuchElementException("로그인 된 회원을 찾을 수 없습니다."));

        Comment comment = new Comment(pComment,authorName,request.getContent());
        Comment saveComment = commentRepository.save(comment);
        return new CommentResponse(saveComment);

    }

    @Transactional
    public CommentResponse update(UpdateCommentRequest request) {
        Comment comment = commentRepository.findById(request.getComment_id())
                .orElseThrow(() -> new NoSuchElementException("찾으시는 댓글이 존재하지 않습니다."));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if(commentRepository.findById(request.getComment_id())
                .orElseThrow(() -> new NoSuchElementException("해당 댓글을 찾을 수 없습니다.")).getAuthorName()
                .equals(memberRepository.findByEmail(email)
                        .orElseThrow(() -> new NoSuchElementException("해당 유저를 찾을 수 없습니다.")).getNickname()))
        {
            comment.update(request.getContent());

            return new CommentResponse(comment);
        }
        else {
            throw new NoSuchElementException("본인이 쓴 댓글만 수정할 수 있습니다.");
        }
    }

    public void delete(Long commentId) {
        if(!commentRepository.existsById(commentId)) {
            throw new NoSuchElementException("해당 게시물을 찾을 수 없습니다.");
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if(commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("해당 댓글을 찾을 수 없습니다.")).getAuthorName()
                .equals(memberRepository.findByEmail(email)
                        .orElseThrow(() -> new NoSuchElementException("해당 유저를 찾을 수 없습니다.")).getNickname()))
        {
            commentRepository.deleteById(commentId);
        }
        else {
            throw new NoSuchElementException("본인이 쓴 댓글만 삭제할 수 있습니다.");
        }


    }

    public List<CommentResponse> getChildComment(Long parentId) {
        return commentRepository.findByParentId(parentId).stream()
                .map(CommentResponse::new)
                .toList();
    }
}