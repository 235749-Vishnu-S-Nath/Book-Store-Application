package com.ust.onlineBookStore.controller;

import com.ust.onlineBookStore.domain.Rating;
import com.ust.onlineBookStore.dto.RatingPostDto;
import com.ust.onlineBookStore.dto.RatingResponseDto;
import com.ust.onlineBookStore.dto.ToListDto;
import com.ust.onlineBookStore.exception.BookAlreadyRatedException;
import com.ust.onlineBookStore.exception.RatingNotFoundException;
import com.ust.onlineBookStore.service.ApiClientBook;
import com.ust.onlineBookStore.service.RatingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rating")
@CrossOrigin("*")
public class RatingController {

    private Logger logger = LoggerFactory.getLogger(RatingController.class);
    @Autowired
    RatingService ratingService;

    @Autowired
    ApiClientBook apiClientBook;

    @PostMapping
    public ResponseEntity<RatingResponseDto> rateTheBook(@RequestBody RatingPostDto ratingPostDto){
        logger.info("rateTheBook: Rating the book with isbn {}", ratingPostDto.isbn());
        final var rating = postTOEntity(ratingPostDto);
        final var result = ratingService.findByIsbnAndUsername(rating.getIsbn(),rating.getUsername());
        if (result.isPresent()){
            logger.info("rateTheBook: Book with isbn{} has been already rated", ratingPostDto.isbn());
            throw new BookAlreadyRatedException(
                    String.format("Book with isbn %s is already rated", ratingPostDto.isbn())
            );
        }
        final var response = ratingService.rateTheBook(rating);
        double avrgRting = ratingService.getAverageRating(ratingPostDto.isbn());
        logger.warn("averge rating : "+String.valueOf(avrgRting));

        if(avrgRting >=0){
            apiClientBook.updateRating(ratingPostDto.isbn(),avrgRting);
        }
        else {
            apiClientBook.updateRating(ratingPostDto.isbn(), ratingPostDto.rating());
        }
        return ResponseEntity.status(HttpStatus.OK).body(entityToPost(response));
    }

    @GetMapping
    public ResponseEntity<RatingResponseDto> getTheRating(@RequestParam String isbn, String username){
        logger.info("getTheRating: get  the rating of book with isbn {} and username {}", isbn,username);
        final var result = ratingService.findByIsbnAndUsername(isbn,username);
        if (result.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(entityToPost(result.get()));
        }
        logger.info("getTheRating: get  the rating of book with isbn {} and username {} is not found", isbn,username);
        throw new RatingNotFoundException(
                String.format("User %s not rated the book with isbn %s ", username,isbn)
        );
    }

    @GetMapping("/reviews")
    public ResponseEntity<ToListDto> getTheRatingandReviews(@RequestParam("isbn") String isbn){
        logger.info("getTheRatingandReviews: get  the rating and reviews of book with isbn {}", isbn);
        final var result = ratingService.findByIsbn(isbn);
        if (!result.isEmpty()){
            List<RatingResponseDto> ratingResponseDtos = result.stream().map(this::entityToPost).toList();
            return ResponseEntity.status(HttpStatus.OK).body(new ToListDto(ratingResponseDtos));
        }
        logger.info("getTheRatingandReviews: No rating and reviews available for book with isbn {}", isbn);
        throw new RatingNotFoundException(
                String.format("No ratings and reviews available for book with isbn %s",isbn));
    }

    @PutMapping("/update")
    public ResponseEntity<RatingResponseDto> updateTheRating( @RequestBody RatingPostDto ratingPostDto){
        logger.info("updateTheRating: update  the rating and review of book with isbn {}", ratingPostDto.isbn());
        final var rating = postTOEntity(ratingPostDto);
        var result = ratingService.findByIsbnAndUsername(rating.getIsbn(),rating.getUsername());
        if (result.isPresent()){
            if(ratingPostDto.rating()!=0){
                result.get().setRating(ratingPostDto.rating());
            }
            if (!ratingPostDto.review().trim().isEmpty()){
                result.get().setReview(ratingPostDto.review());
            }
            double avrgRting = ratingService.getAverageRating(ratingPostDto.isbn());
            logger.warn("averge rating : "+String.valueOf(avrgRting));

            if(avrgRting >=0){
                apiClientBook.updateRating(ratingPostDto.isbn(),(avrgRting+ratingPostDto.rating())/2);
            }
            else {
                apiClientBook.updateRating(ratingPostDto.isbn(), ratingPostDto.rating());
            }
            return ResponseEntity.status(HttpStatus.OK).body(entityToPost(ratingService.updateTheRatings((result.get()))));
        }
        logger.info("updateTheRating: update  the rating and review of book with isbn {} not found", ratingPostDto.isbn());
        throw new RatingNotFoundException(
                String.format("No ratings and reviews available for book with isbn %s",ratingPostDto.isbn()));
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
