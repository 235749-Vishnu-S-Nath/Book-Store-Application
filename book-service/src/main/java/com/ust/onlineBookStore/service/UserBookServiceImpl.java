package com.ust.onlineBookStore.service;

import com.ust.onlineBookStore.domain.Book;
import com.ust.onlineBookStore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserBookServiceImpl implements UserBookService{
    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> findByCategories(String[] categories) {
        return bookRepository.findByCategoriesIn(categories);
    }

    @Override
    public List<Book> findByTitle(String title) {
        return bookRepository.findByTitleContainingWord(title);
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return bookRepository.findByAuthorContainingWord(author);
    }

}
