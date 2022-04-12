package bv.nba.challenge.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import bv.nba.challenge.dto.CommentRequest;
import bv.nba.challenge.dto.MatchComment;
import bv.nba.challenge.dto.ModifyCommentRequest;
import bv.nba.challenge.entities.Comment;
import bv.nba.challenge.exceptions.CommentNotFound;
import bv.nba.challenge.exceptions.TransactionException;
import bv.nba.challenge.repositories.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @InjectMocks
    CommentServiceImpl service;
    @Mock
    CommentRepository repository;

    List<Comment> commentList = new ArrayList<>();
    CommentRequest request = new CommentRequest();
    Comment comment = new Comment();

    @BeforeEach
    void setUp() {
        Comment oldComment = new Comment();
        oldComment.setCommentId(1L);
        oldComment.setGameId(1);
        oldComment.setTimestampCreated(LocalDateTime.MIN);
        oldComment.setCommentMessage("Message sample");

        comment.setCommentId(2L);
        comment.setGameId(1);
        comment.setTimestampCreated(LocalDateTime.now());
        comment.setCommentMessage("Message sample 2");

        commentList.addAll(Arrays.asList(oldComment, comment));

        request.setGameId(2);
        request.setCommentMessage("Comment");
    }

    @Test
    void getGameComments() {

        when(repository.findByGameId(any())).thenReturn(commentList);

        List<MatchComment> matchComments = service.getGameComments(1);


        verify(repository).findByGameId(any());
        assertFalse(matchComments.isEmpty());
        assertEquals(2, matchComments.size());
        assertEquals(LocalDateTime.MIN, matchComments.get(1).getTimestampCreated());

    }

    @Test
    void getGameCommentsNotExist() {

        when(repository.findByGameId(any())).thenReturn(new ArrayList<>());

        List<MatchComment> matchComments = service.getGameComments(1);


        verify(repository).findByGameId(any());
        assertTrue(matchComments.isEmpty());

    }


    @Test
    void createComment() {

        when(repository.save(any())).thenReturn(comment);
        MatchComment commentResponse = service.createComment(request);


        verify(repository).save(any());
        assertEquals(comment.getCommentMessage(), commentResponse.getComment());
        assertNotNull(commentResponse.getId());
    }

    @Test
    void createCommentFailed() {

        when(repository.save(any())).thenThrow(RuntimeException.class);
        assertThrows(TransactionException.class, () -> service.createComment(request));

    }

    @Test
    void modifyCommentNotExists() {
        ModifyCommentRequest changeReq = new ModifyCommentRequest();

        assertThrows(CommentNotFound.class, () -> service.modifyComment(changeReq, 2L));

    }

    @Test
    void modifyCommentFailed() {
        ModifyCommentRequest changeReq = new ModifyCommentRequest();
        when(repository.findByCommentId(any())).thenReturn(java.util.Optional.ofNullable(comment));
        when(repository.save(any())).thenThrow(RuntimeException.class);

        assertThrows(TransactionException.class, () -> service.modifyComment(changeReq, 2L));

    }


    @Test
    void modifyComment() {
        ModifyCommentRequest changeReq = new ModifyCommentRequest();
        when(repository.findByCommentId(any())).thenReturn(java.util.Optional.ofNullable(comment));
        service.modifyComment(changeReq, 2L);

        verify(repository).save(any());

    }

    @Test
    void deleteComment() {

        when(repository.findByCommentId(any())).thenReturn(java.util.Optional.ofNullable(comment));
        service.deleteComment(2L);

        verify(repository).delete(any());

    }


    @Test
    void deleteCommentNotFound() {

        when(repository.findByCommentId(any())).thenReturn(Optional.empty());
        assertThrows(CommentNotFound.class, () -> service.deleteComment(2L));



    }
}