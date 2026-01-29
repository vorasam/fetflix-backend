package com.dev.video;

import com.amazonaws.services.s3.AmazonS3;
import com.dev.episode.Episode;
import com.dev.episode.EpisodeRepository;
import com.dev.movie.Movie;
import com.dev.movie.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.InputStream;

@RestController
@RequestMapping("/api/stream")
@RequiredArgsConstructor
public class VideoStreamController {

    private final MovieRepository repo;
    private final EpisodeRepository epRepo;
    private final AmazonS3 storjClient;


    @GetMapping(value = "/movie/{id}", produces = "video/mp4")
    public ResponseEntity<StreamingResponseBody> streamMovie(@PathVariable Long id) {

        Movie movie = repo.findById(id).orElseThrow();

        InputStream inputStream = storjClient
                .getObject("fetflix-video", movie.getMovieVideo())
                .getObjectContent();

        StreamingResponseBody stream = outputStream -> {
            inputStream.transferTo(outputStream);
        };

        return ResponseEntity.ok()
                .header("Content-Type", "video/mp4")
                .header("Accept-Ranges", "bytes")
                .body(stream);
    }


    @GetMapping(value = "/episode/{id}", produces = "video/mp4")
    public ResponseEntity<StreamingResponseBody> streamEpisode(@PathVariable Long id) {

        Episode ep = epRepo.findById(id).orElseThrow();

        InputStream inputStream = storjClient
                .getObject("fetflix-video", ep.getEpisodeVideo())
                .getObjectContent();

        StreamingResponseBody stream = outputStream -> {
            inputStream.transferTo(outputStream);
        };

        return ResponseEntity.ok()
                .header("Content-Type", "video/mp4")
                .header("Accept-Ranges", "bytes")
                .body(stream);
    }
}

