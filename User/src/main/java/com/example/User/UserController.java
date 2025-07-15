package com.example.User;

import com.example.User.external.*;
import com.example.User.messaging.EmailRequest;
import com.example.User.messaging.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PostClient postClient;
    @Autowired
    private CommentClient commentClient;
    @Autowired
    private MessageProducer messageProducer;


    // Creates a new user in the database
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        EmailRequest emailRequest = new EmailRequest(user.getEmail(),"Registration successful!!!",
                "Thanks for registering to our");
        messageProducer.sendMessage(emailRequest);
        return ResponseEntity.ok(userRepo.save(user));
    }

    // Retrieves all users from the database
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {

        return ResponseEntity.ok(userRepo.findAll());
    }

    // Retrieves a specific user by their ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Post>posts = postClient.getPostsByUserId(id).getBody();
        List<Comment>comments = commentClient.getCommentsByUserId(id).getBody();
        UserDTO userDTO = new UserDTO(user,posts,comments);
        return ResponseEntity.ok(userDTO);
    }

    // Updates an existing user's information
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User existingUser = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        return ResponseEntity.ok(userRepo.save(existingUser));
    }

    // Deletes a user from the database
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }


}
