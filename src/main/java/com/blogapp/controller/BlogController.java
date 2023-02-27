package com.blogapp.controller;

import com.blogapp.dto.PostDto;
import com.blogapp.services.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
@Controller
public class BlogController {
    private PostService postService;

    public BlogController (PostService postService){
        this.postService = postService;
    }
    @GetMapping("/")
    public String viewBlogPosts (Model model){
        List<PostDto> postsResponse = postService.findAllPosts();
        model.addAttribute("postsResponse", postsResponse);
        return "blog/view_posts";
    }
    @GetMapping("/post/{postUrl}")
    public String showPost(@PathVariable ("postUrl") String postUrl, Model model){
        PostDto post = postService.findPostByUrl(postUrl);
        model.addAttribute("post", post);
        return "blog/blog_posts";
    }
}
