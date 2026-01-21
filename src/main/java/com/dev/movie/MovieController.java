package com.dev.movie;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/admin/api/movies")
@AllArgsConstructor
public class MovieController {

    private final MovieService service;

    // CREATE
    @PostMapping("/create")
    public Movie createMovie(@Valid @RequestBody MovieRequest req) {
        return service.createMovie(req);
    }

    @PatchMapping("/{id}/status")
    public void movieEnabled(@PathVariable Long id, @RequestParam boolean enabled){
         service.movieEnabled(id,enabled);
    }

    @GetMapping
    public List<Movie> getAllMovies() {
        return service.getAllMovies();
    }

    @GetMapping("/{id}")
    public Movie getMovie(@PathVariable Long id) {
        return service.getMovie(id);
    }

    @PutMapping("/{id}")
    public Movie updateMovie(@PathVariable Long id, @Valid @RequestBody MovieRequest req) {
        return service.updateMovie(id, req);
    }

    @DeleteMapping("/{id}")
    public String deleteMovie(@PathVariable Long id) {
        service.deleteMovie(id);
        return "Movie deleted successfully!";
    }
}
