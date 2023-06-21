package com.ust.onlineBookStore.service;

import com.ust.onlineBookStore.domain.Rating;
import com.ust.onlineBookStore.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService{

    @Autowired
    RatingRepository ratingRepository;

    @Override
    public Optional<Rating> findByIsbnAndUsername(String isbn, String username) {
        return ratingRepository.findByIsbnAndUsername(isbn,username);
    }

    @Override
    public Rating rateTheBook(Rating rating) {
        return ratingRepository.save(rating);
    }

    @Override
    public Rating updateTheRatings(Rating rating) {
        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> findByIsbn(String isbn) {
        return ratingRepository.findAllByIsbn(isbn);
    }

    @Override
    public double getAverageRating(String isbn) {
        return ratingRepository.findAverageRatingByIsbn(isbn);
    }

}
