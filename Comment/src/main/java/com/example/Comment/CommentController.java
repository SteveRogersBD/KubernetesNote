package com.example.Comment;


import com.example.Comment.external.Post;
import com.example.Comment.external.PostClient;
import com.example.Comment.external.User;
import com.example.Comment.external.UserClient;
import com.example.Comment.messaging.EmailRequest;
import com.example.Comment.messaging.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private PostClient postClient;
    @Autowired
    private UserClient userClient;
    @Autowired
    private MessageProducer messageProducer;
    // Creates a new comment in the database
    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        Post post = postClient.getPost(comment.getPostId()).getBody();
        User user = userClient.getUserById(post.getUserId()).getBody();
        Comment savedComment = commentRepo.save(comment);
        //User commentUser = userClient.getUserById(savedComment.getUserId()).getBody();
        messageProducer.sendMessage(new EmailRequest(user.getEmail(),"New comment!!!",
                "Someone has commented on your post."));
        return ResponseEntity.ok(savedComment);
    }

    // Retrieves all comments from the database
    @GetMapping
    public ResponseEntity<List<Comment>> getAllComments() {
        return ResponseEntity.ok(commentRepo.findAll());
    }

    // Retrieves a specific comment by its ID
    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(commentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found")));
    }

    // Updates an existing comment's information
    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @RequestBody Comment comment) {
        Comment existingComment = commentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        existingComment.setContent(comment.getContent());
        return ResponseEntity.ok(commentRepo.save(existingComment));
    }

    // Deletes a comment from the database
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // Retrieves all comments for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Comment>> getCommentsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(commentRepo.findByUserId(userId));
    }
    // Retrieves all comments for a specific post
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(commentRepo.findByPostId(postId));
    }


}
