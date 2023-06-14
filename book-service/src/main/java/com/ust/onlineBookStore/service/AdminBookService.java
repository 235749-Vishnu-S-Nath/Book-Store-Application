package com.ust.onlineBookStore.service;

import com.ust.onlineBookStore.domain.Book;

import java.util.Optional;

public interface AdminBookService {
    Book save(Book book);
}
