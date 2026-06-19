package com.campus.collab.service;

import com.campus.collab.dto.PostRequest;
import com.campus.collab.dto.PostResponse;
import com.campus.collab.exception.ResourceNotFoundException;
import com.campus.collab.exception.UnauthorizedException;
import com.campus.collab.model.Post;
import com.campus.collab.model.User;
import com.campus.collab.repository.PostRepository;
import com.campus.collab.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public PostResponse createPost(PostRequest postRequest, Authentication authentication) {
        String username = authentication.getName();
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setAuthor(author);

        Post savedPost = postRepository.save(post);
        return mapToPostResponse(savedPost);
    }

    public Page<PostResponse> getAllPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAllByOrderByCreatedAtDesc(pageable);
        return posts.map(this::mapToPostResponse);
    }

    public List<PostResponse> searchPosts(String keyword) {
        List<Post> posts = postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrderByCreatedAtDesc(keyword, keyword);
        return posts.stream()
                .map(this::mapToPostResponse)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getMyPosts(Authentication authentication) {
        String username = authentication.getName();
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return postRepository.findByAuthorIdOrderByCreatedAtDesc(author.getId()).stream()
                .map(this::mapToPostResponse)
                .collect(Collectors.toList());
    }

    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        return mapToPostResponse(post);
    }

    public PostResponse updatePost(Long id, PostRequest postRequest, Authentication authentication) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        String username = authentication.getName();
        if (!post.getAuthor().getUsername().equals(username)) {
            throw new UnauthorizedException("You are not authorized to update this post");
        }

        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());

        Post updatedPost = postRepository.save(post);
        return mapToPostResponse(updatedPost);
    }

    public void deletePost(Long id, Authentication authentication) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        String username = authentication.getName();
        if (!post.getAuthor().getUsername().equals(username)) {
            throw new UnauthorizedException("You are not authorized to delete this post");
        }

        postRepository.delete(post);
    }

    private PostResponse mapToPostResponse(Post post) {
        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());
        response.setAuthorId(post.getAuthor().getId());
        response.setAuthorUsername(post.getAuthor().getUsername());
        return response;
    }
}
