package com.ust.onlineBookStore.dto;

public record RatingResponseDto(
        String username,
        String isbn,
        int rating,
        String review) {
}
