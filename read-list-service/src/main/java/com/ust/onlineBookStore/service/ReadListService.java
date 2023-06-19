package com.ust.onlineBookStore.service;

import com.ust.onlineBookStore.domain.ReadList;
import com.ust.onlineBookStore.dto.ReadListDto;


import java.util.Optional;

public interface ReadListService {
    Optional<ReadList> findByIsbn(String isbn);

    ReadList addFavourites(ReadList readList);

    Optional<ReadList> findByIsbnAndUsername(String isbn, String username);

    Optional<ReadList> findByUsername(String username);

    void deleteFavourites(long id);

//    Optional<ReadList> findByUsernameAndIsbn(String , String );
}
