package com.ust.onlineBookStore.dto;

public record UpdateDto( String seriesName, String author,
                         String summary, Integer minAge, Integer maxAge) {
}
