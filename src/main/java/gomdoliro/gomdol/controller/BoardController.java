package gomdoliro.gomdol.controller;

import gomdoliro.gomdol.controller.dto.BoardAndCommentResponse;
import gomdoliro.gomdol.controller.dto.SaveBoardRequest;
import gomdoliro.gomdol.controller.dto.SaveBoardResponse;
import gomdoliro.gomdol.controller.dto.UpdateBoardRequest;
import gomdoliro.gomdol.domain.Board;
import gomdoliro.gomdol.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

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

}
