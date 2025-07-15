package com.example.User.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "COMMENT-SERVICE", url = "${comment.service.url}")
public interface CommentClient {

    @GetMapping("/{commentId}")
    ResponseEntity<Comment> getComment(@PathVariable Long commentId);
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Comment>> getCommentsByUserId(@PathVariable Long userId);
}
