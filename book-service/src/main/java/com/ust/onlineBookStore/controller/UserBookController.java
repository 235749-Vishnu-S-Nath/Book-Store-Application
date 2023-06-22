package com.ust.onlineBookStore.controller;

import com.ust.onlineBookStore.domain.Book;
import com.ust.onlineBookStore.dto.BookDto;
import com.ust.onlineBookStore.dto.PostRequestDto;
import com.ust.onlineBookStore.dto.ToListDto;
import com.ust.onlineBookStore.exception.BookNotFoundException;
import com.ust.onlineBookStore.exception.NoBooksFoundException;
import com.ust.onlineBookStore.service.BookService;
import com.ust.onlineBookStore.service.UserBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books/user")
@CrossOrigin("*")
public class UserBookController {

    private Logger logger = LoggerFactory.getLogger(AdminBookController.class);

    @Autowired
    private BookService bookService;

    @Autowired
    private UserBookService userBookService;


    @GetMapping("/{isbn}")
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

    @PostMapping("/isbns")
    public ResponseEntity<ToListDto> getAllByIsbn(@RequestBody List<String> isbns){
        logger.info("getAllByIsbn: Fetching all book with isbns {}", isbns.toString());
        List<Book> books = bookService.findByAllIsbn(isbns);
        if(books.isEmpty()){
            logger.error("getAllByIsbn: Fetching book with isbns {} not found", isbns.toString());
            throw new NoBooksFoundException(
                    String.format("Book with isbn %s not found", isbns.toString())
            );
        }
        List<BookDto> bookDtoList = books.stream().map(this::EntityToDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(new ToListDto(bookDtoList));
    }

    @PostMapping("/filter")
    public ResponseEntity<ToListDto> getAllBookFilter(
            @RequestBody String[] categories )
    {
        logger.info("getAllBookFilter: Fetching all book with categories {}", categories);
        List<Book> books;

        if (categories != null && categories.length!=0) {
            // Filter by categories only
            books = userBookService.findByCategories(categories);
        } else {
            // No filters applied, retrieve all books
            books = bookService.findAll();
        }

        if (books.isEmpty()) {
            logger.error("getAllBookFilter: No books found for the filter {} ", categories);
            throw new NoBooksFoundException(
                    String.format("No Books found with isbns %s", categories)
            );
        }

        List<BookDto> bookDtoList = books.stream().map(this::EntityToDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(new ToListDto(bookDtoList));
    }

    @GetMapping("/title")
    public ResponseEntity<ToListDto> getAllBookByTitle(@RequestParam(value = "title", required = false) String title)
    {
        logger.info("getAllBookFilter: Fetching all book with title {}", title);
        List<Book> books;

        if (!title.isEmpty()  ) {
            // Filter by categories only
            books = userBookService.findByTitle(title);
        } else {
            // No filters applied, retrieve all books
            books = bookService.findAll();
        }

        if (books.isEmpty()) {
            logger.error("getAllByTitle: Fetching book with title {} not found", title);
            throw new NoBooksFoundException(
                    String.format("Book with title %s not found", title)
            );
        }

        List<BookDto> bookDtoList = books.stream().map(this::EntityToDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(new ToListDto(bookDtoList));
    }

    @GetMapping("/author")
    public ResponseEntity<ToListDto> getAllBookByAuthor(@RequestParam(value = "author", required = false) String author)
    {
        logger.info("getAllBookFilter: Fetching all book with author {}", author);
        List<Book> books;

        if (!author.isEmpty()  ) {
            // Filter by categories only
            books = userBookService.findByAuthor(author);
        } else {
            // No filters applied, retrieve all books
            books = bookService.findAll();
        }

        if (books.isEmpty()) {
            logger.error("getAllByIsbn: Fetching book with author {} not found", author);
            throw new NoBooksFoundException(
                    String.format("Book with author %s not found", author)
            );
        }

        List<BookDto> bookDtoList = books.stream().map(this::EntityToDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(new ToListDto(bookDtoList));
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
