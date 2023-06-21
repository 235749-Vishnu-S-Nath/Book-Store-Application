package com.ust.onlineBookStore.dto;

public record BookDto( String isbn,String title, String seriesName, String author,
                       Integer lexile, Integer pageCount, Integer minAge, Integer maxAge,
                      String[] categories, String summary, String coverArtUrl,
                      Integer copyright, String language, double rating) {
}
