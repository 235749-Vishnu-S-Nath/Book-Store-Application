package com.ust.onlineBookStore.service;

import com.ust.onlineBookStore.domain.Book;

import java.util.List;

public interface UserBookService {

    List<Book> findByCategories(String[] categories);

    List<Book> findByTitle(String title);

    List<Book> findByAuthor(String author);
}
