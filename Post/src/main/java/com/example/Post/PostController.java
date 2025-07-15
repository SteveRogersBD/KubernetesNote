package com.example.Post;


import com.example.Post.external.*;
import com.example.Post.messaging.EmailRequest;
import com.example.Post.messaging.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private CommentClient commentClient;
    @Autowired
    private MessageProducer messageProducer;
    @Autowired
    private UserClient userClient;
    // Creates a new post in the database
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {

        Post savedPost = postRepo.save(post);
        User user = userClient.getUserById(savedPost.getUserId()).getBody();
        messageProducer.sendMessage(new EmailRequest(user.getEmail(), "New post created",savedPost.getTitle()));
        return ResponseEntity.ok(savedPost);
    }

    // Retrieves all posts from the database
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        return ResponseEntity.ok(postRepo.findAll());
    }

    // Retrieves a specific post by its ID
    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) {
        Post post = postRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        List<Comment>comments = commentClient.getCommentsByPostId(id).getBody();
        PostDTO postDTO =  new PostDTO(post,comments);
        return ResponseEntity.ok(postDTO);
    }

    // Updates an existing post's information
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post post) {
        Post existingPost = postRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());
        return ResponseEntity.ok(postRepo.save(existingPost));
    }

    // Deletes a post from the database
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // Retrieves all posts for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Post>> getPostsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(postRepo.findByUserId(userId));
    }


}
