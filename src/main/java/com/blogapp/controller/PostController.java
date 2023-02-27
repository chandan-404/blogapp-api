package com.blogapp.controller;

import com.blogapp.dto.PostDto;
import com.blogapp.services.PostService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.List;
@Controller
public class PostController {
    private PostService postService;

    private PostController(PostService postService){
        this.postService = postService;
    }

    // http://localhost:8080/admin/posts
    @GetMapping("/admin/posts")
    public String posts(Model model){
        List<PostDto> posts = postService.findAllPosts();
        model.addAttribute("posts", posts);
        return "/admin/posts";
    }

    // http://localhost:8080/admin/posts/newpost
    @GetMapping("/admin/posts/newpost")
    public String newPostForm(Model model){
        PostDto postDto = new PostDto();
        model.addAttribute("post", postDto);
        return "admin/create_post";
    }
    @PostMapping("/admin/posts")
    public String createPost(@Valid @ModelAttribute("post") PostDto postDto, BindingResult result, Model model){
        if (result.hasErrors()){
            model.addAttribute("posts", postDto);
            return "admin/create_post";
        }

        postDto.setUrl(getUrl(postDto.getTitle()));
        postService.createPost(postDto);
        return "redirect:/admin/posts";
    }

    private static String getUrl(String postTitle) {
        String title = postTitle.trim().toLowerCase();
        String url = title.replaceAll("\\s+", "-");
        return url;
    }
    @GetMapping("/admin/posts/{postId}/edit")
    public String editPostForm(@PathVariable("postId") Long postId, Model model){
        PostDto postDto = postService.findPostById(postId);
        model.addAttribute("post", postDto);
        return "admin/edit_post";
    }
    @PostMapping("/admin/posts/{postId}")
    public String updatePost(@PathVariable("postId") @Valid @ModelAttribute Long postId, Model model, PostDto post, BindingResult result){
        if (result.hasErrors()) {
            model.addAttribute("post", post);
            return "admin/edit_post";
        }

        post.setId(postId);
        postService.updatePost(post);
        return "redirect:/admin/posts";
    }
    @GetMapping("/admin/posts/{postId}/delete")
    public String deletePost(@PathVariable ("postId") Long postId){
        postService.deletePost(postId);
        return "redirect:/admin/posts";
    }
    @GetMapping("/admin/posts/{postUrl}/view")
    public String viewPost(@PathVariable ("postUrl") String postUrl, Model model){
        PostDto postDto = postService.findPostByUrl(postUrl);
        model.addAttribute("post", postDto);
        return "admin/view_post";
    }

    @GetMapping("/admin/posts/search")
    public String searchPosts(@RequestParam(value = "query") String query, Model model){
        List<PostDto> posts = postService.searchPosts(query);
        model.addAttribute("posts", posts);
        return "admin/posts";
    }
}
