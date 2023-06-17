package com.ust.onlineBookStore.dto;

public record PostRequestDto( String isbn, String title,
                             String seriesName, String author, Integer lexile,
                             Integer pageCount, Integer minAge, Integer maxAge,
                             String[] categories, String summary, String coverArtUrl,
                             String authorFirstName,String authorLastName,Integer copyright,
                             String publishedWorkId, String binding, String language) {
}
