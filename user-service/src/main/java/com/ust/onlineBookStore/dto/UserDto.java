package com.ust.onlineBookStore.dto;

import com.ust.onlineBookStore.domain.Role;

public record UserDto(long userId, String username, String email, String token, Role role) {
}
