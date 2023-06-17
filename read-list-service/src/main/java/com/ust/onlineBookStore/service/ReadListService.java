package com.ust.onlineBookStore.service;

import com.ust.onlineBookStore.domain.ReadList;
import com.ust.onlineBookStore.dto.ReadListDto;


import java.util.Optional;

public interface ReadListService {
    Optional<ReadList> findByIsbn(String isbn);

    ReadList addFavourites(ReadList readList);

    Optional<ReadList> findByIsbnAndUserId(String isbn, long userId);

    Optional<ReadList> findByUserId(long id);

    void deleteFavourites(long id);
}
