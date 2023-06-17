package com.ust.onlineBookStore.controller;

import com.ust.onlineBookStore.domain.Book;
import com.ust.onlineBookStore.dto.BookDto;
import com.ust.onlineBookStore.dto.PostRequestDto;
import com.ust.onlineBookStore.service.BookService;
import com.ust.onlineBookStore.service.UserBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/books")
@CrossOrigin("*")
public class UserBookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserBookService userBookService;

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDto> getByIsbn(@PathVariable String isbn){
        final var book = bookService.findByIsbn(isbn);
        if(book.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(EntityToDto(book.get()));

    }

    @PostMapping("/isbns")
    public ResponseEntity<List<BookDto>> getAllByIsbn(@RequestBody List<String> isbns){
        List<Book> books = bookService.findByAllIsbn(isbns);
        if(books.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        List<BookDto> bookDtoList = books.stream().map(this::EntityToDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(bookDtoList);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<BookDto>> getAllBookFilter(
            @RequestBody String[] categories )
    {

        List<Book> books;

        if (categories != null && categories.length!=0) {
            // Filter by categories only
            books = userBookService.findByCategories(categories);
        } else {
            // No filters applied, retrieve all books
            books = bookService.findAll();
        }

        if (books.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        List<BookDto> bookDtoList = books.stream().map(this::EntityToDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(bookDtoList);
    }

    @GetMapping("/title")
    public ResponseEntity<List<BookDto>> getAllBookByTitle(@RequestParam(value = "title", required = false) String title)
    {
        List<Book> books;

        if (!title.isEmpty()  ) {
            // Filter by categories only
            books = userBookService.findByTitle(title);
        } else {
            // No filters applied, retrieve all books
            books = bookService.findAll();
        }

        if (books.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        List<BookDto> bookDtoList = books.stream().map(this::EntityToDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(bookDtoList);
    }

    @GetMapping("/author")
    public ResponseEntity<List<BookDto>> getAllBookByAuthor(@RequestParam(value = "author", required = false) String author)
    {
        List<Book> books;

        if (!author.isEmpty()  ) {
            // Filter by categories only
            books = userBookService.findByAuthor(author);
        } else {
            // No filters applied, retrieve all books
            books = bookService.findAll();
        }

        if (books.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        List<BookDto> bookDtoList = books.stream().map(this::EntityToDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(bookDtoList);
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
                book.getLanguage()
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
                postRequestDto.language()
        );
    }
}
