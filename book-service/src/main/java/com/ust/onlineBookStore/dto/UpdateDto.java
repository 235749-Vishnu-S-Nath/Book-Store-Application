package com.ust.onlineBookStore.dto;

public record UpdateDto( String title, String seriesName, String author,
                         String summary, Integer copyright) {
}

