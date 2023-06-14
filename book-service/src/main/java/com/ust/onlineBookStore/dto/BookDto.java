package com.ust.onlineBookStore.dto;

public record BookDto(long bookId,long isbn,String title,String author,String summary,String language,int pageCount,int publishYear,String imageUrl) {
}
