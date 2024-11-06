package gomdoliro.gomdol.controller;

import gomdoliro.gomdol.controller.dto.*;
import gomdoliro.gomdol.domain.Board;
import gomdoliro.gomdol.domain.BoardRepository;
import gomdoliro.gomdol.service.BoardService;
import gomdoliro.gomdol.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final OpenAiService openAiService;

    @PostMapping("/save")
    public SaveBoardResponse save(@RequestBody SaveBoardRequest request) {
        return boardService.save(request);
    }

    @GetMapping
    public List<SaveBoardResponse> getAll() {
        return boardService.getAll();
    }

    @GetMapping("/get/{id}")
    public BoardAndCommentResponse get(@PathVariable Long id) {
        return boardService.get(id);
    }

    @PutMapping("/update")
    public SaveBoardResponse update(@RequestBody UpdateBoardRequest request) {
        return boardService.update(request);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        boardService.delete(id);
        return ResponseEntity.ok("삭제가 완료되었습니다.");
    }

    @GetMapping("/summarize/{boardId}")
    public SummaryResponse summarize(@PathVariable Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NoSuchElementException("해당 게시글을 찾을 수 없습니다."));
        return openAiService.getSummary(board.getContent(),board.getTitle());
    }

}
