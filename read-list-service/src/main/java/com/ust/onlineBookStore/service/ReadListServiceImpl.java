package com.ust.onlineBookStore.service;

import com.ust.onlineBookStore.domain.ReadList;
import com.ust.onlineBookStore.dto.ReadListDto;
import com.ust.onlineBookStore.repository.ReadListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ReadListServiceImpl implements ReadListService {

    @Autowired
    ReadListRepository readListRepository;
    @Override
    public Optional<ReadList> findByIsbn(String isbn) {
        return readListRepository.findByIsbn(isbn);
    }

    @Override
    public ReadList addFavourites(ReadList fav) {
        return readListRepository.save(fav);
    }

    @Override
    public Optional<ReadList> findByIsbnAndUserId(String isbn, long userId) {
        return readListRepository.findByIsbnAndUserId(isbn,userId);
    }

    @Override
    public Optional<ReadList> findByUserId(long id) {
        return readListRepository.findByUserId(id);
    }

    @Override
    public void deleteFavourites(long id) {
        readListRepository.deleteById(id);
    }

}
