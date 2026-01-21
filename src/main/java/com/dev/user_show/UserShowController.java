package com.dev.user_show;

import com.dev.episode.Episode;
import com.dev.movie.Movie;
import com.dev.season.Season;
import com.dev.show.Show;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserShowController {

    private final UserShow service ;



    @GetMapping("/movies")
    public List<Movie> getAllMovies() {
        return service.getAllMovies();
    }

    @GetMapping("/movies/search")
    public List<Movie> searchMovies(@RequestParam("q") String q) {
        return service.searchMovies(q);
    }

    @GetMapping("/shows/search")
    public List<Show> searchShows(@RequestParam("q") String q) {
        return service.searchShows(q);
    }

//    @GetMapping("/movie/{id}")
//    public Movie getMovie(@PathVariable Long id) {
//        return service.getMovie(id);
//    }

    @GetMapping("/shows")
    public List<Show> getAllShows() {
        return service.getAllShows();
    }

//    @GetMapping("show/{id}")
//    public Show getShow(@PathVariable Long id) {
//        return service.getShow(id);
//    }

    @GetMapping("/seasons/show/{showId}")
    public List<Season> getSeasons(@PathVariable Long showId) {
        return service.getSeasons(showId);
    }


    @GetMapping("/episodes/season/{seasonId}")
    public List<Episode> getEpisodes(@PathVariable Long seasonId) {
        return service.getEpisodes(seasonId);
    }
}
