package com.ust.onlineBookStore.service;

import com.ust.onlineBookStore.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<Book> findByIsbn(String isbn);
    List<Book> findAll();

    List<Book> findByAllIsbn(List<String> isbns);
}
