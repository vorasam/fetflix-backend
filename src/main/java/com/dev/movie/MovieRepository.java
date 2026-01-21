package com.dev.movie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("""
        SELECT m
        FROM Movie m
        WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
        AND m.enabled = true
    """)
    List<Movie> searchMovies(@Param("keyword") String keyword);
}
