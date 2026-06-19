package com.campus.collab.service;

import com.campus.collab.dto.AdminStatsResponse;
import com.campus.collab.dto.RoleUpdateRequest;
import com.campus.collab.dto.UserResponse;
import com.campus.collab.exception.ResourceNotFoundException;
import com.campus.collab.model.Post;
import com.campus.collab.model.User;
import com.campus.collab.repository.CommentRepository;
import com.campus.collab.repository.LikeRepository;
import com.campus.collab.repository.PostRepository;
import com.campus.collab.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LikeRepository likeRepository;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::fromUser)
                .collect(Collectors.toList());
    }

    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Delete all likes created by the user
        likeRepository.deleteByUserId(id);

        // Delete all comments created by the user
        commentRepository.deleteByAuthorId(id);

        // Delete all posts created by the user
        postRepository.deleteByAuthorId(id);

        // Finally delete the user
        userRepository.delete(user);
    }

    public void deletePostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
        postRepository.delete(post);
    }

    public AdminStatsResponse getStats() {
        AdminStatsResponse stats = new AdminStatsResponse();
        stats.setTotalUsers(userRepository.count());
        stats.setTotalPosts(postRepository.count());
        stats.setTotalComments(commentRepository.count());
        stats.setTotalLikes(likeRepository.count());
        return stats;
    }

    public UserResponse updateUserRole(Long id, RoleUpdateRequest roleUpdateRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        User.Role newRole = User.Role.valueOf(roleUpdateRequest.getRole());
        user.setRole(newRole);

        User updatedUser = userRepository.save(user);
        return UserResponse.fromUser(updatedUser);
    }
}
