package com.example.User.external;


import com.example.User.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    private List<Post> posts = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();
    
    public UserDTO(User user,List<Post> post,List<Comment> comment)
    {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.createdAt = user.getCreatedAt();
        this.posts = post;
        this.comments = comment;
    }
}
