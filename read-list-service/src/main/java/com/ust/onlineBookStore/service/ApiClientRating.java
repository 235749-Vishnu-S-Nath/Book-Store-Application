package com.ust.onlineBookStore.service;

import com.ust.onlineBookStore.dto.RatingResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "rating-service",url = "http://localhost:8200/api/v1/rating")
public interface ApiClientRating {

    @GetMapping
    public ResponseEntity<RatingResponseDto> getTheRating(@RequestParam String isbn, String username);
}
