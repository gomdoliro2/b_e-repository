package gomdoliro.gomdol.service;

import gomdoliro.gomdol.controller.dto.BoardAndCommentResponse;
import gomdoliro.gomdol.controller.dto.SaveBoardRequest;
import gomdoliro.gomdol.controller.dto.SaveBoardResponse;
import gomdoliro.gomdol.controller.dto.UpdateBoardRequest;
import gomdoliro.gomdol.controller.dto.comment.CommentResponse;
import gomdoliro.gomdol.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    public SaveBoardResponse save(SaveBoardRequest request) {
        if (request.getTitle() == null || request.getTitle().isEmpty()) {
            throw new IllegalArgumentException("제목이 비어있습니다.");
        }

        if (request.getContent() == null || request.getContent().isEmpty()) {
            throw new IllegalArgumentException("내용이 비어있습니다.");
        }

        //현재 로그인한 사용자의 이메일 호출
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        //레포지터리에서 닉네임 가져오기
        String authorNickname = memberRepository.findByEmail(email)
                .map(Member::getNickname)
                .orElseThrow(() -> new NoSuchElementException("로그인한 사용자를 찾을 수 없습니다."));

        Board board = Board.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .authorNickname(authorNickname)
                .build();
        Board save = boardRepository.save(board);

        return new SaveBoardResponse(save);
    }

    public List<SaveBoardResponse> getAll() {
        return boardRepository.findAll().stream()
                .map(SaveBoardResponse::new)
                .toList();
    }

    public BoardAndCommentResponse get(Long id) {
        Board board =  boardRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 게시글을 찾을 수 없습니다."));
        List<CommentResponse> comments = commentRepository.findByBoardId(id).stream()
                .map(CommentResponse::new)
                .toList();

        return new BoardAndCommentResponse(board, comments);
    }

    @Transactional
    public SaveBoardResponse update(UpdateBoardRequest request) {
        if (request.getTitle() == null || request.getTitle().isEmpty()) {
            throw new IllegalArgumentException("제목이 비어있습니다.");
        }

        if (request.getContent() == null || request.getContent().isEmpty()) {
            throw new IllegalArgumentException("내용이 비어있습니다.");
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if(boardRepository.findById(request.getId())
                .orElseThrow(() -> new NoSuchElementException("해당 게시물을 찾을 수 없습니다.")).getAuthorNickname().equals(memberRepository.findByEmail(email)
                        .orElseThrow(() -> new NoSuchElementException("해당 유저를 찾을 수 없습니다.")).getNickname())
        )
        {
            Board board = boardRepository.findById(request.getId())
                    .orElseThrow(() -> new NoSuchElementException("해당 게시글을 찾을 수 없습니다."));
            board.update(request.getTitle(), request.getContent());

            return new SaveBoardResponse(board);
        }
        else {
            throw new IllegalArgumentException("본인이 쓴 글만 수정할 수 있습니다.");
        }
    }

    public void delete(Long id) {
        if(!boardRepository.existsById(id)) {
            throw new NoSuchElementException("해당 게시물을 찾을 수 없습니다.");
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if(boardRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 게시물을 찾을 수 없습니다.")).getAuthorNickname().equals(memberRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("해당 유저를 찾을 수 없습니다.")).getNickname())) {
            boardRepository.deleteById(id);
        }
        else {
            throw new IllegalArgumentException("본인이 쓴 글만 삭제할 수 있습니다.");
        }

    }
}
