package com.ust.onlineBookStore.service;

import com.ust.onlineBookStore.domain.ReadList;
import com.ust.onlineBookStore.repository.ReadListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public Optional<ReadList> findByIsbnAndUsername(String isbn, String username) {
        return readListRepository.findByIsbnAndUsername(isbn,username);
    }

    @Override
    public List<ReadList> findByUsername(String username) {
        return readListRepository.findByUsername(username);
    }

    @Override
    public void deleteFavourites(long id) {
        readListRepository.deleteById(id);
    }

//    @Override
//    public Optional<ReadList> findByUsernameAndIsbn(String username, String isbn) {
//        return readListRepository.findByIsbnAndUsername(isbn,username);
//    }

}
