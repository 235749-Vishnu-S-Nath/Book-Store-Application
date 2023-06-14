package com.ust.onlineBookStore.repository;

import com.ust.onlineBookStore.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book,Long> {
    Optional<Book> findByIsbn(long isbn);
}
