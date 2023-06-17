package com.ust.onlineBookStore.controller;

import com.ust.onlineBookStore.domain.ReadList;
import com.ust.onlineBookStore.dto.BookDto;
import com.ust.onlineBookStore.dto.ReadListDto;
import com.ust.onlineBookStore.service.ApiClient;
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
    ApiClient apiClient;

    @PostMapping("/addtofav")
    public ResponseEntity<ReadListDto> addToFav(@RequestBody ReadListDto fav){
        final var favourites = PostToEntity(fav);
        final var result = readListService.findByIsbnAndUserId(favourites.getIsbn(),favourites.getUserId());
        if(result.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        readListService.addFavourites(favourites);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/getfavourites")
    public ResponseEntity<List<BookDto>>  getFavourates(@RequestParam long id){
        final var result = readListService.findByUserId(id);
        if(result.isPresent()){
            List<String> isbns = result.stream()
                    .map(ReadList::getIsbn)
                    .toList();
            log.warn(isbns.toString());
            return ResponseEntity.status(HttpStatus.OK).body(apiClient.getAllByIsbn(isbns).getBody());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/removefav")
    public ResponseEntity<ReadListDto> removeFavourites(@RequestParam String isbn, @RequestParam long id){
        final var result = readListService.findByIsbnAndUserId(isbn,id);
        if(result.isPresent()) {
            readListService.deleteFavourites(result.get().getReadListId());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public ReadList PostToEntity(ReadListDto readListDto) {
        return new ReadList(
                0,
                readListDto.userId(),
                readListDto.isbn(),
                LocalDateTime.now(ZoneId.of("Asia/Kolkata"))
        );
    }

}
