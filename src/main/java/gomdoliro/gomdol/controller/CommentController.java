package gomdoliro.gomdol.controller;

import gomdoliro.gomdol.controller.dto.comment.ChildCommentRequest;
import gomdoliro.gomdol.controller.dto.comment.CommentRequest;
import gomdoliro.gomdol.controller.dto.comment.CommentResponse;
import gomdoliro.gomdol.controller.dto.comment.UpdateCommentRequest;
import gomdoliro.gomdol.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/board/{boardId}/comments")
    public CommentResponse saveComment(@PathVariable Long boardId, @RequestBody CommentRequest commentRequest) {
        commentRequest.setBoardId(boardId);
        return commentService.save(commentRequest);
    }

    @PostMapping("/comments/{commentId}")
    public CommentResponse saveChildComment(@PathVariable Long commentId, @RequestBody ChildCommentRequest commentRequest) {
        commentRequest.setCommentId(commentId);
        return commentService.childSave(commentRequest);
    }

    @GetMapping("/comments/{parentId}")
    public List<CommentResponse> getComment(@PathVariable Long parentId) {
        return commentService.getChildComment(parentId);
    }

    @PutMapping("/comments/update")
    public CommentResponse updateComment(@RequestBody UpdateCommentRequest request) {
        return commentService.update(request);
    }

    @DeleteMapping("/comments/delete/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.delete(commentId);
    }
}
