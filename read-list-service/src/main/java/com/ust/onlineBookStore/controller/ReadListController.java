package com.ust.onlineBookStore.controller;

import com.ust.onlineBookStore.domain.ReadList;
import com.ust.onlineBookStore.dto.BookDto;
import com.ust.onlineBookStore.dto.BookUserDto;
import com.ust.onlineBookStore.dto.ReadListDto;
import com.ust.onlineBookStore.dto.ToListDto;
import com.ust.onlineBookStore.exception.BookAlreadyAddedException;
import com.ust.onlineBookStore.exception.BookNotFoundException;
import com.ust.onlineBookStore.service.ApiClientBook;
import com.ust.onlineBookStore.service.ApiClientRating;
import com.ust.onlineBookStore.service.ReadListService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping("/api/v1/readlist")
@CrossOrigin("*")
@Slf4j
public class ReadListController {
    private Logger logger = LoggerFactory.getLogger(ReadListController.class);

    @Autowired
    ReadListService readListService;

    @Autowired
    ApiClientBook apiClient;

    @Autowired
    ApiClientRating apiClientRating;

    @PostMapping("/addtofav")
    public ResponseEntity<ReadListDto> addToFav(@RequestBody ReadListDto fav){
        logger.info("addToFav: user {} adding the book {} to favourates", fav.username(),fav.isbn());
        final var favourites = PostToEntity(fav);
        final var result = readListService.findByIsbnAndUsername(favourites.getIsbn(),favourites.getUsername());
        if(result.isPresent()) {
            logger.info("addToFav: user {} already added the book {} to favourates", fav.username(),fav.isbn());
            throw new BookAlreadyAddedException(
                    String.format("Book with isbn %s is already added to favourites", fav.isbn()));
        }
        readListService.addFavourites(favourites);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/getfavourites")
    public ResponseEntity<ToListDto>  getFavourates(@RequestParam String username){
        logger.info("getFavourates: fetching favourite books using username{}", username);
        final var result = readListService.findByUsername(username);
        if(!result.isEmpty()){
            List<String> isbns = result.stream()
                    .map(ReadList::getIsbn)
                    .toList();
            log.info(isbns.toString());//need to delete
            log.warn(apiClient.getAllByIsbn(isbns).getBody().bookDtoList().toString());
            return ResponseEntity.status(HttpStatus.OK).body(new ToListDto(apiClient.getAllByIsbn(isbns).getBody().bookDtoList()));
        }
        logger.info("getFavourates: fetching favourite books using username{} not found", username);
        throw new BookNotFoundException(
                String.format("User with username %s is has no favourites", username));
    }

    @GetMapping("/viewfavbook")
    public ResponseEntity<BookUserDto>  viewFavourateBook(@RequestParam String username, @RequestParam String isbn){
        final var result = apiClientRating.getTheRating(isbn, username);
        logger.info("viewFavourateBook: fetching favourite book using username{} and isbn{}", username,isbn);
        if(result.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.OK).body(bookToRatingDto(apiClient.getByIsbn(isbn).getBody(), result.getBody().review(), result.getBody().rating()));
        }
        logger.info("viewFavourateBook: fetching favourite book using username{} and isbn{} not found", username,isbn);
        throw new BookNotFoundException(
                String.format("No book found in favourites with isbn{} and username{}",isbn, username));
    }




    @DeleteMapping("/removefav")
    public ResponseEntity<ReadListDto> removeFavourites(@RequestParam String isbn, @RequestParam String username){
        logger.info("removeFavourites: removing book from favourites using isbn{} and username{}",isbn,username);
        final var result = readListService.findByIsbnAndUsername(isbn,username);
        if(result.isPresent()) {
            readListService.deleteFavourites(result.get().getReadListId());
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        logger.info("removeFavourites: removing book from favourites using isbn{} and username{} not found",isbn,username);
        throw new BookNotFoundException(
                String.format("No book found in favourites with isbn{} and username{}",isbn, username));
    }

    public ReadList PostToEntity(ReadListDto readListDto) {
        return new ReadList(
                0,
                readListDto.username(),
                readListDto.isbn(),
                LocalDateTime.now(ZoneId.of("Asia/Kolkata"))
        );
    }


    public BookUserDto bookToRatingDto(BookDto bookDto,String review,int rating){
        return new BookUserDto(
                bookDto.isbn(),
                bookDto.title(),
                bookDto.seriesName(),
                bookDto.author(),
                bookDto.lexile(),
                bookDto.pageCount(),
                bookDto.minAge(),
                bookDto.maxAge(),
                bookDto.categories(),
                bookDto.summary(),
                bookDto.coverArtUrl(),
                bookDto.copyright(),
                bookDto.language(),
                rating,
                review
        );
    }

}
