package bv.nba.challenge.repositories;

import bv.nba.challenge.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByGameId(Integer id);

    Optional<Comment> findByCommentId(Long id);

}
