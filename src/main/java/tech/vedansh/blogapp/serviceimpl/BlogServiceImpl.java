package tech.vedansh.blogapp.serviceimpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tech.vedansh.blogapp.dto.ApiResponse;
import tech.vedansh.blogapp.dto.BlogResponseDTO;
import tech.vedansh.blogapp.enums.Role;
import tech.vedansh.blogapp.model.BlogModel;
import tech.vedansh.blogapp.model.UserModel;
import tech.vedansh.blogapp.repository.BlogRepository;
import tech.vedansh.blogapp.repository.UserRepository;
import tech.vedansh.blogapp.service.BlogService;
import tech.vedansh.blogapp.util.UploadUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepo;
    private final UserRepository userRepo;
    private final UploadUtil uploadUtil;



    @Override
    public ApiResponse<List<BlogResponseDTO>> getAllBlogs() {
        List<BlogResponseDTO> dtoList = blogRepo.findAll().stream().map(this::toDTO).toList();
        return new ApiResponse<>(true, "All Blogs Fetched", dtoList);
    }


    @Override
    public ApiResponse<BlogResponseDTO> getBlog(Long id) {
        BlogModel blog = blogRepo.findById(id).orElseThrow(() -> new RuntimeException("Blog Not Found"));
        return new ApiResponse<>(true, "Blog Fetched", toDTO(blog));
    }


    @Override
    public ApiResponse<BlogResponseDTO> createBlog(String email, String title, String content, String slug, String category, MultipartFile image) {
        UserModel user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        String relativePath = uploadUtil.saveFile(image);
        String imageUrl = "http://localhost:8080" + relativePath;

        BlogModel blog = BlogModel.builder()
                .title(title)
                .content(content)
                .slug(slug)
                .category(category)
                .author(user)
                .imageUrl(imageUrl)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        return new ApiResponse<>(true, "Blog created", toDTO(blogRepo.save(blog)));
    }



    @Override
    public ApiResponse<BlogResponseDTO> updateBlogFields(Long id, String email, Map<String, String> updates, MultipartFile image) {
        BlogModel blog = blogRepo.findById(id).orElseThrow(() -> new RuntimeException("Blog not found"));

        UserModel user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        boolean isOwner = blog.getAuthor().getEmail().equals(email);
        boolean isAdmin = user.getRole() == Role.ROLE_ADMIN;

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("You are not allowed to modify this blog");
        }

        if (updates.containsKey("title")) {
            blog.setTitle(updates.get("title"));
        }

        if (updates.containsKey("content")) {
            blog.setContent(updates.get("content"));
        }

        if (updates.containsKey("slug")) {
            blog.setSlug(updates.get("slug"));
        }

        if (updates.containsKey("category")) {
            blog.setCategory(updates.get("category"));
        }

        blog.setUpdatedAt(new Date());

        if (image != null && !image.isEmpty()) {
            String relativePath = uploadUtil.saveFile(image);
            blog.setImageUrl("http://localhost:8080" + relativePath);
        }

        return new ApiResponse<>(true, "Blog updated", toDTO(blogRepo.save(blog)));
    }


    @Override
    public ApiResponse<String> deleteBlog(Long id, String email) {
        BlogModel blog = blogRepo.findById(id).orElseThrow(()->new RuntimeException("Blog Not Found"));

        UserModel user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        boolean isOwner = blog.getAuthor().getEmail().equals(email);
        boolean isAdmin = user.getRole() == Role.ROLE_ADMIN;

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("You are not allowed to delete this blog");
        }
        blogRepo.delete(blog);

        return new ApiResponse<>(true, "Blog deleted successfully", null);


    }


    @Override
    @Transactional
    public ApiResponse<List<BlogResponseDTO>> getAllBlogsByUser(Long userId) {
        UserModel user = userRepo.findById(userId).orElseThrow(()->new RuntimeException("User not found"));


        List<BlogModel> blogs = blogRepo.findByAuthor(user);
        List<BlogResponseDTO> blogDTOs = blogs.stream()
                .map(this::toDTO)
                .toList();

        return new ApiResponse<>(true,"Blogs by user fetched",blogDTOs);

    }


    private BlogResponseDTO toDTO(BlogModel blog) {
        BlogResponseDTO dto = new BlogResponseDTO();
        dto.setId(blog.getId());
        dto.setTitle(blog.getTitle());
        dto.setContent(blog.getContent());
        dto.setSlug(blog.getSlug());
        dto.setCategory(blog.getCategory());
        dto.setImageUrl(blog.getImageUrl());
        dto.setCreatedAt(blog.getCreatedAt());
        dto.setUpdatedAt(blog.getUpdatedAt());
        dto.setAuthorName(blog.getAuthor().getName());
        return dto;
    }



}
