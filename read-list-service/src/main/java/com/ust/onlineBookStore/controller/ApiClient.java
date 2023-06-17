package com.ust.onlineBookStore.controller;

import com.ust.onlineBookStore.dto.BookDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "book-service",url = "http://localhost:8100/api/v1/users/books")
public interface ApiClient {
    @PostMapping("/isbns")
    public ResponseEntity<List<BookDto>> getAllByIsbn(@RequestBody List<String> isbns);
}
