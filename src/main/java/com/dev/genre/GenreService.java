package com.dev.genre;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreService {

    private final GenreRepository repo;

    public List<Genre> getAll() {
        return repo.findAll();
    }

    public Genre createGenre(String name) {
        return repo.findByName(name)
                .orElseGet(() -> repo.save(new Genre(null, name)));
    }

    public Genre getOrCreate(String name) {
        return createGenre(name);
    }
}
