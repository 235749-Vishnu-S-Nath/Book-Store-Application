package com.ust.onlineBookStore.dto;

public record RatingPostDto(
        long ratingId,
        String username,
        String isbn,
        int rating,
        String review) {
}
