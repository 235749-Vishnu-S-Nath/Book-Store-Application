package com.ust.onlineBookStore.service;

import com.ust.onlineBookStore.domain.Book;

public interface AdminBookService {
    Book save(Book book);
    void delete(long id);

    void update(Book book);
}
