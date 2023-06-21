package com.ust.onlineBookStore.dto;

public record RatingResponseDto (
        String username,
        String isbn,
        double rating,
        String review) {
}
