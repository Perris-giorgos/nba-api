package bv.nba.challenge.services;

import bv.nba.challenge.dto.CommentRequest;
import bv.nba.challenge.dto.MatchComment;
import bv.nba.challenge.dto.ModifyCommentRequest;

import java.util.List;

public interface CommentService {

    List<MatchComment> getGameComments(Integer id);

    MatchComment createComment(CommentRequest request);

    void modifyComment(ModifyCommentRequest request, Long commentId);

    void deleteComment(Long commentId);

}
