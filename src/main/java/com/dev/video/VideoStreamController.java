package com.dev.video;

import com.dev.episode.Episode;
import com.dev.episode.EpisodeRepository;
import com.dev.movie.Movie;
import com.dev.movie.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stream")
@RequiredArgsConstructor
public class VideoStreamController {

    private final MovieRepository repo;
    private final EpisodeRepository epRepo;
    private final VideoStreamService streamService;

    @GetMapping("/movie/{movieId}")
    public String streamMovie(@PathVariable Long movieId) {

        Movie movie = repo.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        return streamService.getSignedUrl(movie.getMovieVideo());
    }

    @GetMapping("/episode/{epId}")
    public String streamEpisode(@PathVariable Long epid) {

        Episode episode = epRepo.findById(epid)
                .orElseThrow(() -> new RuntimeException("Episode not found"));

        return streamService.getSignedUrl(episode.getEpisodeVideo());
    }
}

