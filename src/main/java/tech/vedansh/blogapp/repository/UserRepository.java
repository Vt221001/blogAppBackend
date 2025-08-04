package tech.vedansh.blogapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.vedansh.blogapp.model.UserModel;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel,Long> {

    Optional<UserModel> findByEmail(String username);
}
