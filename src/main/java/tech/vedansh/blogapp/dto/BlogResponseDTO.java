package tech.vedansh.blogapp.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BlogResponseDTO {
    private Long id;
    private String title;
    private String content;
    private String slug;
    private String category;
    private String imageUrl;
    private Date createdAt;
    private Date updatedAt;
    private String authorName;

}
