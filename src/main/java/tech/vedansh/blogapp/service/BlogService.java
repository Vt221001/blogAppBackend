package tech.vedansh.blogapp.service;

import org.springframework.web.multipart.MultipartFile;
import tech.vedansh.blogapp.dto.ApiResponse;
import tech.vedansh.blogapp.dto.BlogResponseDTO;
import tech.vedansh.blogapp.model.BlogModel;

import java.util.List;
import java.util.Map;

public interface BlogService {

    ApiResponse<BlogResponseDTO> createBlog(String email, String title, String content, String slug, String category, MultipartFile image);
    ApiResponse<BlogResponseDTO> updateBlogFields(Long id, String email, Map<String, String> updates, MultipartFile image);
    ApiResponse<String> deleteBlog(Long id, String email);
    ApiResponse<List<BlogResponseDTO>> getAllBlogs();
    ApiResponse<BlogResponseDTO> getBlog(Long id);
    ApiResponse<List<BlogResponseDTO>> getAllBlogsByUser(Long userId);

}
