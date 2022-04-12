package bv.nba.challenge.controllers;

import bv.nba.challenge.audit.ActionAudit;
import bv.nba.challenge.dto.CommentRequest;
import bv.nba.challenge.dto.MatchComment;
import bv.nba.challenge.dto.ModifyCommentRequest;
import bv.nba.challenge.enums.ActionEnum;
import bv.nba.challenge.services.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * Create a new comment for a game.
     */
    @ActionAudit(action = ActionEnum.CREATE)
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public MatchComment createComment(@RequestBody CommentRequest request) {

        return commentService.createComment(request);

    }

    /**
     * Create a new comment for a game.
     */
    @ActionAudit(action = ActionEnum.MODIFY)
    @PatchMapping("/{commentId}")
    public ResponseEntity<String> modifyComment(@RequestBody ModifyCommentRequest request,
                                                @PathVariable Long commentId
    ) {

        commentService.modifyComment(request, commentId);
        return ResponseEntity.ok("Comment updated");

    }

    /**
     * delete comment using commentId.
     */
    @ActionAudit(action = ActionEnum.DELETE)
    @DeleteMapping("/delete/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(@PathVariable Long commentId) {

        commentService.deleteComment(commentId);

    }




}
