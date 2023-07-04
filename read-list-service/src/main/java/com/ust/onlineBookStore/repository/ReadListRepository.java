package com.ust.onlineBookStore.repository;

import com.ust.onlineBookStore.domain.ReadList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReadListRepository extends JpaRepository <ReadList,Long> {
    Optional<ReadList> findByIsbn(String isbn);


    List<ReadList> findByUsername(String username);

    Optional<ReadList> findByIsbnAndUsername(String isbn, String username);
}
