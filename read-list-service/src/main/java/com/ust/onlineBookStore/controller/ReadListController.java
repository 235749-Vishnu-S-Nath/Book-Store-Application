package com.ust.onlineBookStore.controller;

import com.ust.onlineBookStore.domain.ReadList;
import com.ust.onlineBookStore.dto.BookDto;
import com.ust.onlineBookStore.dto.BookUserDto;
import com.ust.onlineBookStore.dto.ReadListDto;
import com.ust.onlineBookStore.dto.ToListDto;
import com.ust.onlineBookStore.service.ApiClientBook;
import com.ust.onlineBookStore.service.ApiClientRating;
import com.ust.onlineBookStore.service.ReadListService;
import lombok.extern.slf4j.Slf4j;
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

    @Autowired
    ReadListService readListService;

    @Autowired
    ApiClientBook apiClient;

    @Autowired
    ApiClientRating apiClientRating;

    @PostMapping("/addtofav")
    public ResponseEntity<ReadListDto> addToFav(@RequestBody ReadListDto fav){
        final var favourites = PostToEntity(fav);
        final var result = readListService.findByIsbnAndUsername(favourites.getIsbn(),favourites.getUsername());
        if(result.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        readListService.addFavourites(favourites);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/getfavourites")
    public ResponseEntity<ToListDto>  getFavourates(@RequestParam String username){
        final var result = readListService.findByUsername(username);
        if(result.isPresent()){
            List<String> isbns = result.stream()
                    .map(ReadList::getIsbn)
                    .toList();
            log.warn(isbns.toString());//need to delete
            return ResponseEntity.status(HttpStatus.OK).body(new ToListDto(apiClient.getAllByIsbn(isbns).getBody().bookDtoList()));
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/viewfavbook")
    public ResponseEntity<BookUserDto>  viewFavourateBook(@RequestParam String username, @RequestParam String isbn){
        final var result = apiClientRating.getTheRating(isbn, username);

//        if (!result.getBody().isbn().isEmpty()){
//            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(bookToRatingDto(apiClient.getByIsbn(isbn).getBody(),null,0));
//        }
        if(result.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.OK).body(bookToRatingDto(apiClient.getByIsbn(isbn).getBody(), result.getBody().review(), result.getBody().rating()));
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(bookToRatingDto(apiClient.getByIsbn(isbn).getBody(),null,0));

    }




    @DeleteMapping("/removefav")
    public ResponseEntity<ReadListDto> removeFavourites(@RequestParam String isbn, @RequestParam String username){
        final var result = readListService.findByIsbnAndUsername(isbn,username);
        if(result.isPresent()) {
            readListService.deleteFavourites(result.get().getReadListId());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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
