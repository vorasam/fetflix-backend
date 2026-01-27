package com.dev.movie;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin/api/movies")
@AllArgsConstructor
public class MovieController {

    private final MovieService service;

    // ===================== CREATE MOVIE =====================
    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public Movie createMovie(
            @Valid @RequestPart("movie") MovieRequest req,
            @RequestPart("file") MultipartFile file,
            @RequestPart("image") MultipartFile image
    ) throws IOException {
        return service.createMovie(req, file, image);
    }

    // ===================== ENABLE / DISABLE =====================
    @PatchMapping("/{id}/status")
    public Movie movieEnabled(
            @PathVariable Long id,
            @RequestParam boolean enabled
    ) {
        return service.movieEnabled(id, enabled);
    }

    // ===================== GET ALL =====================
    @GetMapping
    public List<Movie> getAllMovies() {
        return service.getAllMovies();
    }

    // ===================== GET BY ID =====================
    @GetMapping("/{id}")
    public Movie getMovie(@PathVariable Long id) {
        return service.getMovie(id);
    }

    // ===================== UPDATE MOVIE =====================
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public Movie updateMovie(
            @PathVariable Long id,
            @Valid @RequestPart("movie") MovieRequest req,
            @RequestPart(required = false) MultipartFile file,
            @RequestPart(required = false) MultipartFile image
    ) throws IOException {
        return service.updateMovie(id, req, file, image);
    }

    // ===================== DELETE =====================
    @DeleteMapping("/{id}")
    public String deleteMovie(@PathVariable Long id) {
        service.deleteMovie(id);
        return "Movie deleted successfully!";
    }
}
