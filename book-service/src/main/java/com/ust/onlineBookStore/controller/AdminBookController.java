package com.ust.onlineBookStore.controller;

import com.ust.onlineBookStore.domain.Book;
import com.ust.onlineBookStore.dto.BookDto;
import com.ust.onlineBookStore.dto.PostRequestDto;
import com.ust.onlineBookStore.dto.ToListDto;
import com.ust.onlineBookStore.dto.UpdateDto;
import com.ust.onlineBookStore.exception.BookAlreadyExistsException;
import com.ust.onlineBookStore.exception.BookNotFoundException;
import com.ust.onlineBookStore.exception.NoBooksFoundException;
import com.ust.onlineBookStore.service.AdminBookService;
import com.ust.onlineBookStore.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/books/admin")
@CrossOrigin("*")
public class AdminBookController {
    @Autowired
    private AdminBookService adminBookService;

    private Logger logger = LoggerFactory.getLogger(AdminBookController.class);

    @Autowired
    private BookService bookService;

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDto> getByIsbn(@PathVariable("isbn") String isbn) {
        logger.info("getBook: Fetching book with isbn {}", isbn);
        var book = bookService.findByIsbn(isbn);
        if (book.isEmpty()) {
            logger.error("getBook: Fetching book with isbn {} not found", isbn);
            throw new BookNotFoundException(
                    String.format("Book with isbn %s not found", isbn)
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(EntityToDto(book.get()));
    }

    @GetMapping
    public ResponseEntity<ToListDto> getAllBook(){
        logger.info("getBook: Fetching all books");
        final var books = bookService.findAll();
        if(books.isEmpty()){
            logger.error("getBooks: No Books found");
            throw new NoBooksFoundException(
                    String.format("No Books available in database")
            );
        }
        final var bookDtoList = books.stream().map(this::EntityToDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(new ToListDto(bookDtoList));
    }

    @PostMapping("/add")
    public ResponseEntity<BookDto> addBook(@Valid @RequestBody PostRequestDto postRequestDto) {
        logger.info("createBook: Creating book with title {}", postRequestDto.title());
        bookService.findByIsbn(postRequestDto.isbn())
                .ifPresent(movie -> {
                    logger.error("createBook: Book with isbn {}  already exists", postRequestDto.isbn());
                    throw new BookAlreadyExistsException(
                            String.format("Book with isbn %s already exists", postRequestDto.isbn())
                    );
                });
        Book book = adminBookService.save(PostToEntity(postRequestDto));
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(book.getIsbn()).toUri()
        ).body(EntityToDto(book));
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<BookDto> deleteBook(@PathVariable("isbn") String isbn){
        logger.info("deleteBook: Deleting book with isbn {}",isbn);
        final var book = bookService.findByIsbn(isbn);
        if(book.isEmpty()){
            logger.error("deleteBook: Book with id {} not found",isbn);
            throw new BookNotFoundException(
                    String.format("Book with isbn %s not found", isbn)
            );
        }
        adminBookService.delete(book.get().getBookId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{isbn}")
    public ResponseEntity<BookDto> updateBook(@PathVariable("isbn") String isbn, @RequestBody UpdateDto updateDto) {
        logger.info("updateBook: Updating book with isbn {}", isbn);
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
        logger.error("updateMovie: Movie with isbn {} not found", isbn);
        throw new BookNotFoundException(
                String.format("Book with isbn %s not found", isbn)
        );
    }

    @PostMapping("/rating")
    public ResponseEntity<BookDto> updateRating(@RequestParam String isbn,@RequestParam double rating){
        logger.info("updateBook: Update book rating with isbn {}", isbn);
        var book = bookService.findByIsbn(isbn);
        if(book.isPresent()) {
            if (rating!=0.0) {
                book.get().setRating(rating);
            }
            adminBookService.update(book.get());
            return ResponseEntity.ok(EntityToDto(book.get()));
        }
        logger.error("updateMovie: Movie with isbn {} not found", isbn);
        throw new BookNotFoundException(
                String.format("Book with isbn %s not found", isbn)
        );
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

