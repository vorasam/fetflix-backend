package com.dev.show;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShowRepository extends JpaRepository<Show, Long> {
    @Query("""
        SELECT s
        FROM Show s
        WHERE LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
        AND s.enabled = true
    """)
    List<Show> searchShows(@Param("keyword") String keyword);
}
