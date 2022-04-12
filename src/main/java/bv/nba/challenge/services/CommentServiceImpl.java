package bv.nba.challenge.services;

import bv.nba.challenge.dto.CommentRequest;
import bv.nba.challenge.dto.MatchComment;
import bv.nba.challenge.dto.ModifyCommentRequest;
import bv.nba.challenge.entities.Comment;
import bv.nba.challenge.exceptions.CommentNotFound;
import bv.nba.challenge.exceptions.CustomBaseException;
import bv.nba.challenge.exceptions.TransactionException;
import bv.nba.challenge.repositories.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }


    @Override
    public List<MatchComment> getGameComments(Integer id) {

        var comments = commentRepository.findByGameId(id);

        return comments.stream()
                       .map(comment ->
                           MatchComment.builder()
                                       .timestampCreated(comment.getTimestampCreated())
                                       .comment(comment.getCommentMessage())
                                       .id(comment.getCommentId())
                                       .build())
                       .sorted(Comparator.comparing(MatchComment::getTimestampCreated).reversed())
                       .collect(Collectors.toList());

    }

    /**
     * creates a new comment for a specific game.
     */
    @Override
    @Transactional
    public MatchComment createComment(CommentRequest request) {

        Comment commentAdded = new Comment();
        commentAdded.setCommentMessage(request.getCommentMessage());
        commentAdded.setTimestampCreated(LocalDateTime.now());
        commentAdded.setGameId(request.getGameId());
        Comment savedComment;
        try {
            savedComment = commentRepository.save(commentAdded);
        } catch (RuntimeException exception) {
            log.error("New comment could not be saved due error during saving for game id: " + request.getGameId());
            throw new TransactionException(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return createMatchComment(savedComment);

    }

    /**
     * modifies a specific comment.
     * @param commentId comment to be updated
     */
    @Override
    @Transactional
    public void modifyComment(ModifyCommentRequest request, Long commentId) {

        var comment =
            commentRepository.findByCommentId(commentId)
                             .orElseThrow(() -> new CommentNotFound("Comment not found", HttpStatus.NOT_FOUND));

        comment.setCommentMessage(request.getModifiedComment());
        try {
            commentRepository.save(comment);
        } catch (RuntimeException exception) {
            throw new TransactionException("Could not modify comment", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * modifies a specific comment.
     * @param commentId comment to be updated
     */
    @Override
    @Transactional
    public void deleteComment(Long commentId) {

        var comment = commentRepository.findByCommentId(commentId)
                                       .orElseThrow(() ->
                                           new CommentNotFound("Comment not found", HttpStatus.NOT_FOUND)
                                       );

        commentRepository.delete(comment);

    }

    private MatchComment createMatchComment(Comment savedComment) {

        return MatchComment.builder()
                           .comment(savedComment.getCommentMessage())
                           .timestampCreated(savedComment.getTimestampCreated())
                           .id(savedComment.getCommentId())
                           .build();

    }

}
