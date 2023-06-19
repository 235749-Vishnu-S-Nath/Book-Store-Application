package com.ust.onlineBookStore.controller;

import com.ust.onlineBookStore.domain.Book;
import com.ust.onlineBookStore.dto.BookDto;
import com.ust.onlineBookStore.dto.PostRequestDto;
import com.ust.onlineBookStore.dto.ToListDto;
import com.ust.onlineBookStore.dto.UpdateDto;
import com.ust.onlineBookStore.service.AdminBookService;
import com.ust.onlineBookStore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/books")
@CrossOrigin("*")
public class AdminBookController {
    @Autowired
    private AdminBookService adminBookService;

    @Autowired
    private BookService bookService;

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDto> getByIsbn(@PathVariable String isbn){
        final var book = bookService.findByIsbn(isbn);
        if(book.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(EntityToDto(book.get()));

    }

    @GetMapping
    public ResponseEntity<ToListDto> getAllBook(){
        final var books = bookService.findAll();
        if(books.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        final var bookDtoList = books.stream().map(this::EntityToDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(new ToListDto(bookDtoList));
    }

    @PostMapping("/add")
    public ResponseEntity<BookDto> addBook(@RequestBody PostRequestDto postRequestDto){
        final var book = PostToEntity(postRequestDto);
        final var result = bookService.findByIsbn(book.getIsbn());
        if(result.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityToDto(adminBookService.save(book)));
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<BookDto> updateBook(@PathVariable String isbn){
        final var book = bookService.findByIsbn(isbn);
        if(book.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        adminBookService.delete(book.get().getBookId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{isbn}")
    public ResponseEntity<BookDto> updateBook(@PathVariable("isbn") String isbn, @RequestBody UpdateDto updateDto) {
        var book = bookService.findByIsbn(isbn);
        if(book.isPresent()){
            if(!updateDto.seriesName().trim().isEmpty()){
                book.get().setSeriesName(updateDto.seriesName());
            }
            if (!updateDto.author().trim().isEmpty()){
                book.get().setAuthor(updateDto.author());
            }
            if(!updateDto.summary().trim().isEmpty()){
                book.get().setSummary(updateDto.summary());
            }
            if(updateDto.minAge()!=0){
                book.get().setMinAge(updateDto.minAge());
            }
            if(updateDto.maxAge()!=0){
                book.get().setMaxAge(updateDto.maxAge());
            }
            adminBookService.update(book.get());
            return ResponseEntity.ok(EntityToDto(book.get()));
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    public BookDto EntityToDto(Book book){
        return new BookDto(
                book.getIsbn(),
                book.getTitle(),
                book.getSeriesName(),
                book.getAuthor(),
                book.getLexile(),
                book.getPageCount(),
                book.getMinAge(),
                book.getMaxAge(),
                book.getCategories(),
                book.getSummary(),
                book.getCoverArtUrl(),
                book.getCopyright(),
                book.getLanguage(),
                book.getRating()
        );
    }

    public Book PostToEntity(PostRequestDto postRequestDto) {
        return new Book(
                0,
                postRequestDto.isbn(),
                postRequestDto.title(),
                postRequestDto.seriesName(),
                postRequestDto.author(),
                postRequestDto.lexile(),
                postRequestDto.pageCount(),
                postRequestDto.minAge(),
                postRequestDto.maxAge(),
                postRequestDto.categories(),
                postRequestDto.summary(),
                postRequestDto.coverArtUrl(),
                postRequestDto.authorFirstName(),
                postRequestDto.authorLastName(),
                postRequestDto.copyright(),
                postRequestDto.publishedWorkId(),
                postRequestDto.binding(),
                postRequestDto.language(),
                postRequestDto.rating()
        );
    }
}