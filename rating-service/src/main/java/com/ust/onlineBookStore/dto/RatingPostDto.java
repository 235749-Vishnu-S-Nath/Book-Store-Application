package com.ust.onlineBookStore.dto;

public record RatingPostDto(
        long ratingId,
        String username,
        String isbn,
        double rating,
        String review) {
}
