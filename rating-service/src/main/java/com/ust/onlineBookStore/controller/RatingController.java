package com.ust.onlineBookStore.controller;

import com.ust.onlineBookStore.domain.Rating;
import com.ust.onlineBookStore.dto.RatingPostDto;
import com.ust.onlineBookStore.dto.RatingResponseDto;
import com.ust.onlineBookStore.dto.ToListDto;
import com.ust.onlineBookStore.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rating")
@CrossOrigin("*")
public class RatingController {

    @Autowired
    RatingService ratingService;

    @PostMapping
    public ResponseEntity<RatingResponseDto> rateTheBook(@RequestBody RatingPostDto ratingPostDto){
        final var rating = postTOEntity(ratingPostDto);
        final var result = ratingService.findByIsbnAndUsername(rating.getIsbn(),rating.getUsername());
        if (result.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(entityToPost(ratingService.rateTheBook(rating)));
    }

    @GetMapping
    public ResponseEntity<RatingResponseDto> getTheRating(@RequestParam String isbn, String username){
        final var result = ratingService.findByIsbnAndUsername(isbn,username);
        if (result.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(entityToPost(result.get()));
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/reviews")
    public ResponseEntity<ToListDto> getTheRatingandReviews(@RequestParam String isbn){
        final var result = ratingService.findByIsbn(isbn);
        if (!result.isEmpty()){
            List<RatingResponseDto> ratingResponseDtos = result.stream().map(this::entityToPost).toList();
            return ResponseEntity.status(HttpStatus.OK).body(new ToListDto(ratingResponseDtos));
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/update")
    public ResponseEntity<RatingResponseDto> updateTheRating( @RequestBody RatingPostDto ratingPostDto){
        final var rating = postTOEntity(ratingPostDto);
        final var result = ratingService.findByIsbnAndUsername(rating.getIsbn(),rating.getUsername());
        if (result.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(entityToPost(ratingService.updateTheRatings(postTOEntity(ratingPostDto))));
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public Rating postTOEntity(RatingPostDto ratingPostDto) {
        return new Rating(
                0,
                ratingPostDto.username(),
                ratingPostDto.isbn(),
                ratingPostDto.rating(),
                ratingPostDto.review()
                );
    }

    public RatingResponseDto entityToPost(Rating rating){
        return new RatingResponseDto(
                rating.getUsername(),
                rating.getIsbn(),
                rating.getRating(),
                rating.getReview()
        );
    }

}
