package tech.vedansh.blogapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name= "blogs")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlogModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private UserModel author;


    private String slug;

    @Column(nullable = false)
    private String imageUrl;

    private Date createdAt;
    private Date updatedAt;

    @Column(nullable = false)
    private String category;



}
