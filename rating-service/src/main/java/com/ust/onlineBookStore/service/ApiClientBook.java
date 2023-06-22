package com.ust.onlineBookStore.service;

import com.ust.onlineBookStore.dto.BookDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "book-service",url = "http://localhost:8100/api/v1/books/admin")
public interface ApiClientBook {
    @PostMapping("/rating")
    public ResponseEntity<BookDto> updateRating(@RequestParam String isbn,@RequestParam double rating);
}
