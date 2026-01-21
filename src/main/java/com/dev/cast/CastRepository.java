package com.dev.cast;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CastRepository extends JpaRepository<Cast, Long> {
    Optional<Cast> findByActorName(String actorName);
}
