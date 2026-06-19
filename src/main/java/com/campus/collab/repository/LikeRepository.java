package com.campus.collab.repository;

import com.campus.collab.model.Like;
import com.campus.collab.model.Post;
import com.campus.collab.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(User user, Post post);
    long countByPost(Post post);
    boolean existsByUserAndPost(User user, Post post);
    void deleteByUserId(Long userId);
}
