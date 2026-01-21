package com.dev.season;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeasonRepository extends JpaRepository<Season, Long> {
    List<Season> findByShowId(Long showId);

}
