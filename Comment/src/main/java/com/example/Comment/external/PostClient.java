package com.example.Comment.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "POST-SERVICE", url = "${post.service.url}")
public interface PostClient {


    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPost(@PathVariable Long postId);
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Post>> getPostsByUserId(@PathVariable Long userId);

}
