package tech.vedansh.blogapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.vedansh.blogapp.model.BlogModel;
import tech.vedansh.blogapp.model.UserModel;

import java.util.List;

public interface BlogRepository extends JpaRepository<BlogModel,Long> {

    List<BlogModel> findByAuthor(UserModel user);
}
