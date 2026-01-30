package com.dev.video;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.dev.episode.Episode;
import com.dev.episode.EpisodeRepository;
import com.dev.movie.Movie;
import com.dev.movie.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.InputStream;
@RestController
@RequestMapping("/api/stream")
@RequiredArgsConstructor
public class VideoStreamController {

    private final MovieRepository repo;
    private final EpisodeRepository epRepo;
    private final AmazonS3 storjClient;

    private static final String BUCKET = "fetflix-video";

    // ================= MOVIE =================
    @GetMapping(value = "/movie/{id}")
    public ResponseEntity<StreamingResponseBody> streamMovie(
            @PathVariable Long id,
            @RequestHeader(value = "Range", required = false) String range
    ) {

        Movie movie = repo.findById(id).orElseThrow();
        return streamFromS3(movie.getMovieVideo(), range);
    }

    // ================= EPISODE =================
    @GetMapping(value = "/episode/{id}")
    public ResponseEntity<StreamingResponseBody> streamEpisode(
            @PathVariable Long id,
            @RequestHeader(value = "Range", required = false) String range
    ) {

        Episode ep = epRepo.findById(id).orElseThrow();
        return streamFromS3(ep.getEpisodeVideo(), range);
    }

    // ================= CORE STREAM LOGIC =================
    private ResponseEntity<StreamingResponseBody> streamFromS3(
            String key,
            String range
    ) {

        long fileSize = storjClient.getObjectMetadata(BUCKET, key).getContentLength();

        long start = 0;
        long end = fileSize - 1;

        if (range != null && range.startsWith("bytes=")) {
            String[] parts = range.replace("bytes=", "").split("-");
            start = Long.parseLong(parts[0]);
            if (parts.length > 1 && !parts[1].isEmpty()) {
                end = Long.parseLong(parts[1]);
            }
        }

        long contentLength = end - start + 1;

        GetObjectRequest request = new GetObjectRequest(BUCKET, key)
                .withRange(start, end);

        InputStream inputStream = storjClient.getObject(request).getObjectContent();

        StreamingResponseBody stream = outputStream -> {
            inputStream.transferTo(outputStream);
        };

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .header("Content-Type", "video/mp4")
                .header("Accept-Ranges", "bytes")
                .header("Content-Length", String.valueOf(contentLength))
                .header("Content-Range",
                        "bytes " + start + "-" + end + "/" + fileSize)
                .body(stream);
    }
}
