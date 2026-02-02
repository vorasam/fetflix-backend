package com.dev.user_show;

import com.dev.episode.Episode;
import com.dev.movie.Movie;
import com.dev.season.Season;
import com.dev.show.Show;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserShowController {

    private final UserShow service;

    // ================= MOVIES =================

    @GetMapping("/movies")
    public List<Movie> getAllMovies(Authentication auth) {
        return service.getAllMovies(auth);
    }

    @GetMapping("/movies/search")
    public List<Movie> searchMovies(
            @RequestParam("q") String q,
            Authentication auth) {
        return service.searchMovies(q, auth);
    }

    // ================= SHOWS =================

    @GetMapping("/shows")
    public List<Show> getAllShows(Authentication auth) {
        return service.getAllShows(auth);
    }

    @GetMapping("/shows/search")
    public List<Show> searchShows(
            @RequestParam("q") String q,
            Authentication auth) {
        return service.searchShows(q, auth);
    }

    // ================= SEASONS =================

    @GetMapping("/seasons/show/{showId}")
    public List<Season> getSeasons(
            @PathVariable Long showId,
            Authentication auth) {
        return service.getSeasons(showId, auth);
    }

    // ================= EPISODES =================

    @GetMapping("/episodes/season/{seasonId}")
    public List<Episode> getEpisodes(
            @PathVariable Long seasonId,
            Authentication auth) {
        return service.getEpisodes(seasonId, auth);
    }
}
