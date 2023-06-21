package com.ust.onlineBookStore.service;

import com.ust.onlineBookStore.domain.Rating;

import java.util.List;
import java.util.Optional;

public interface RatingService {
    Optional<Rating> findByIsbnAndUsername(String isbn, String username);

    Rating rateTheBook(Rating rating);

    Rating updateTheRatings(Rating rating);

    List<Rating> findByIsbn(String isbn);

    double getAverageRating(String isbn);
}
