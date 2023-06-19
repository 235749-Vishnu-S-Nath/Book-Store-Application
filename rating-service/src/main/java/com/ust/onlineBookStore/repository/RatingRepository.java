package com.ust.onlineBookStore.repository;

import com.ust.onlineBookStore.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating,Long> {
    Optional<Rating> findByIsbnAndUsername(String isbn, String username);

    List<Rating> findAllByIsbn(String isbn);
}
