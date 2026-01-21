package com.dev.user_show;

import com.dev.episode.Episode;
import com.dev.episode.EpisodeRepository;
import com.dev.exception.ResourceNotFoundException;
import com.dev.movie.Movie;
import com.dev.movie.MovieRepository;
import com.dev.season.Season;
import com.dev.season.SeasonRepository;
import com.dev.show.Show;
import com.dev.show.ShowRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserShow {

    private final MovieRepository movieRepository;
    private final ShowRepository showRepository;
    private final SeasonRepository seasonRepository;
    private final EpisodeRepository episodeRepository;


    public List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }

    public Movie getMovie(Long id){
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found:"+id));
    }

    public List<Show> getAllShows() {
        return showRepository.findAll();
    }

    public Show getShow(Long id) {
        return showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found: " + id));
    }

    public List<Season> getSeasons(Long showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found: " + showId));
        return show.getSeasons();
    }

    public List<Show> searchShows(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }

        return showRepository.searchShows(keyword.trim());
    }

    public List<Episode> getEpisodes(Long seasonId) {
        Season season = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new ResourceNotFoundException("Season not found: " + seasonId));

        return episodeRepository.findAll()
                .stream()
                .filter(e -> e.getSeason().getId().equals(seasonId))
                .collect(Collectors.toList());
    }

    public List<Movie> searchMovies(String keyword) {

        // safety check
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of(); // empty list
        }

        return movieRepository.searchMovies(keyword.trim());
    }
}
