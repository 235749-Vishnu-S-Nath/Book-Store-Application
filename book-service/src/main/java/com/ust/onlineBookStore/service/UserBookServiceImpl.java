package com.ust.onlineBookStore.service;

import com.ust.onlineBookStore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserBookServiceImpl implements UserBookService{
    @Autowired
    private BookRepository bookRepository;
}
