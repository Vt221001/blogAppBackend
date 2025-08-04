package tech.vedansh.blogapp.controller;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.vedansh.blogapp.dto.ApiResponse;
import tech.vedansh.blogapp.dto.BlogResponseDTO;
import tech.vedansh.blogapp.model.BlogModel;
import tech.vedansh.blogapp.service.BlogService;
import tech.vedansh.blogapp.serviceimpl.BlogServiceImpl;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class BlogController {

     private final BlogServiceImpl blogService;

     @GetMapping("/blogs")
     public ResponseEntity<ApiResponse<List<BlogResponseDTO>>> getAllBlogs(){
         return ResponseEntity.ok(blogService.getAllBlogs());
     }

     @GetMapping("/blog/{id}")
     public ResponseEntity<ApiResponse<BlogResponseDTO>> getBlog(@PathVariable Long id){
         return ResponseEntity.ok(blogService.getBlog(id));
     }

     @PostMapping("blog/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<BlogResponseDTO>>  createBlog(
             @RequestParam("title") String title,
             @RequestParam("content") String content,
             @RequestParam("slug") String slug,
             @RequestParam("category") String category,
             @RequestParam("image")MultipartFile image,
             Principal principal
     )
     {
        return ResponseEntity.ok(blogService.createBlog(principal.getName(),title,content,slug,category,image));
     }

    @PatchMapping("/updateBlog/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<BlogResponseDTO>> updateBlogFields(
            @PathVariable Long id,
            @RequestParam Map<String, String> updates,
            @RequestParam(value = "image", required = false) MultipartFile image,
            Principal principal
    ) {
        return ResponseEntity.ok(blogService.updateBlogFields(id, principal.getName(), updates, image));
    }


    @DeleteMapping("deleteBlog/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<String>> deleteBlog(@PathVariable Long id, Principal principal) {
        blogService.deleteBlog(id, principal.getName());
        return ResponseEntity.ok(new ApiResponse<>(true, "Blog deleted successfully", null));
    }


    @GetMapping("blogByUser/{userId}")
    public ResponseEntity<ApiResponse<List<BlogResponseDTO>>> getAllBlogsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok( blogService.getAllBlogsByUser(userId));
    }


}
