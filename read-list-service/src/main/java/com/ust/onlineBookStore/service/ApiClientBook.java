package com.ust.onlineBookStore.service;

import com.ust.onlineBookStore.dto.BookDto;
import com.ust.onlineBookStore.dto.ToListDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "book-service",url = "http://localhost:8100/api/v1/books/user")
public interface ApiClientBook {
    @PostMapping("/isbns")
    public ResponseEntity<ToListDto> getAllByIsbn(@RequestBody List<String> isbns);

    @GetMapping("/{isbn}")
    public ResponseEntity<BookDto> getByIsbn(@PathVariable String isbn);
}
