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
import com.dev.user.User;
import com.dev.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserShow {

    private final MovieRepository movieRepository;
    private final ShowRepository showRepository;
    private final SeasonRepository seasonRepository;
    private final EpisodeRepository episodeRepository;
    private final UserRepository userRepository;

    // 🔐 CENTRAL PAYMENT CHECK
    private void checkAccess(Authentication auth) {

        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        if (!user.isPaymentDone()
                || user.getPlanExpiry() == null
                || user.getPlanExpiry().isBefore(LocalDateTime.now())) {

            // auto-expire
            user.setPaymentDone(false);
            userRepository.save(user);

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Your plan expired. Please renew."
            );
        }
    }

    // ================= MOVIES =================

    public List<Movie> getAllMovies(Authentication auth) {
        return movieRepository.findAll();
    }

    public Movie getMovie(Long id, Authentication auth) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found:" + id));
    }

    public List<Movie> searchMovies(String keyword, Authentication auth) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }

        return movieRepository.searchMovies(keyword.trim());
    }

    // ================= SHOWS =================

    public List<Show> getAllShows(Authentication auth) {
        return showRepository.findAll();
    }

    public Show getShow(Long id, Authentication auth) {
        checkAccess(auth);
        return showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found: " + id));
    }

    public List<Season> getSeasons(Long showId, Authentication auth) {

        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found: " + showId));

        return show.getSeasons();
    }

    public List<Episode> getEpisodes(Long seasonId, Authentication auth) {

        Season season = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new ResourceNotFoundException("Season not found: " + seasonId));

        return episodeRepository.findAll()
                .stream()
                .filter(e -> e.getSeason().getId().equals(seasonId))
                .collect(Collectors.toList());
    }

    public List<Show> searchShows(String keyword, Authentication auth) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }

        return showRepository.searchShows(keyword.trim());
    }
}
