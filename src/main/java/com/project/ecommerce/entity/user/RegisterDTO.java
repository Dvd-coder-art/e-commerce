package com.project.ecommerce.entity.user;

public record RegisterDTO(String login, String password, UserRole role) {
}
