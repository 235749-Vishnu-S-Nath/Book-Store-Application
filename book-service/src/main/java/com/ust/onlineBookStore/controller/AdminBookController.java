package com.ust.onlineBookStore.controller;

import com.ust.onlineBookStore.domain.Book;
import com.ust.onlineBookStore.dto.BookDto;
import com.ust.onlineBookStore.dto.PostRequestDto;
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

    @PostMapping("/add")
    public ResponseEntity<BookDto> addBook(@RequestBody PostRequestDto postRequestDto){
        final var book = PostToEntity(postRequestDto);
        final var result = bookService.findByIsbn(book.getIsbn());
        if(result.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityToDto(adminBookService.save(book)));
    }

    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBook(){
        final var books = bookService.findAll();
        if(books.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        final var bookDtoList = books.stream().map(this::EntityToDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(bookDtoList);
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDto> getByIsbn(@PathVariable long isbn){
        final var book = bookService.findByIsbn(isbn);
        if(book.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(EntityToDto(book.get()));
    }

    public Book DtoToEntity(BookDto bookDto){
        return new Book(bookDto.bookId(), bookDto.isbn(), bookDto.title(), bookDto.author(), bookDto.summary(),
                bookDto.language(), bookDto.pageCount(), bookDto.publishYear(), bookDto.imageUrl());
    }

    public BookDto EntityToDto(Book book){
        return new BookDto(book.getBookId(), book.getIsbn(), book.getTitle(), book.getAuthor(), book.getSummary(),
                book.getLanguage(), book.getPageCount(), book.getPublishYear(), book.getImageUrl());
    }

    public Book PostToEntity(PostRequestDto postRequestDto){
        return new Book(0, postRequestDto.isbn(), postRequestDto.title(), postRequestDto.author(), postRequestDto.summary(),
                postRequestDto.language(), postRequestDto.pageCount(), postRequestDto.publishYear(), postRequestDto.imgUrl());
    }
}