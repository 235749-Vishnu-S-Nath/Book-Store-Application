package com.ust.onlineBookStore.dto;

public record PostRequestDto(long isbn,String title,String author,String summary,String language,int pageCount,int publishYear,String imgUrl) {
}
