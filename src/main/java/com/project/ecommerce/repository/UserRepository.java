package com.project.ecommerce.repository;

import com.project.ecommerce.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, String> {
    User findByLogin(String login);
}
