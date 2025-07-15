package com.example.Post.external;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "COMMENT-SERVICE", url = "${comment.service.url}")
public interface CommentClient {
    @GetMapping("/post/{postId}")
    ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable Long postId);
}
