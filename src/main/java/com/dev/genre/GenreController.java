package com.dev.genre;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/api/genres")
@AllArgsConstructor
public class GenreController {

    private final GenreService service;

    // GET ALL (dropdown)
    @GetMapping
    public List<Genre> getAllGenres() {
        return service.getAll();
    }

    // ADD NEW Genre (admin)
    @PostMapping
    public Genre addGenre(@RequestBody Genre genre) {
        return service.createGenre(genre.getName());
    }
}
