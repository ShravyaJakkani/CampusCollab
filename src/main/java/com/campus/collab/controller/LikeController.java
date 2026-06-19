package com.campus.collab.controller;

import com.campus.collab.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/api/posts/{postId}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long postId, Authentication authentication) {
        likeService.likePost(postId, authentication);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/posts/{postId}/like")
    public ResponseEntity<Void> unlikePost(@PathVariable Long postId, Authentication authentication) {
        likeService.unlikePost(postId, authentication);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/posts/{postId}/likes/count")
    public ResponseEntity<Map<String, Long>> getLikeCount(@PathVariable Long postId) {
        long count = likeService.getLikeCount(postId);
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/posts/{postId}/likes/me")
    public ResponseEntity<Map<String, Boolean>> hasUserLiked(@PathVariable Long postId, Authentication authentication) {
        boolean hasLiked = likeService.hasUserLiked(postId, authentication);
        Map<String, Boolean> response = new HashMap<>();
        response.put("liked", hasLiked);
        return ResponseEntity.ok(response);
    }
}
