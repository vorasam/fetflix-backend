package com.dev.movie;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.dev.cast.CastService;
import com.dev.genre.GenreService;
import com.dev.scene.SceneService;
import com.dev.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MovieService {

    private final MovieRepository repo;
    private final CastService castService;
    private final GenreService genreService;
    private final SceneService sceneService;
    private final Cloudinary cloudinary;

    // ===================== CREATE =====================
    public Movie createMovie(
            MovieRequest req,
            MultipartFile file,
            MultipartFile image
    ) throws IOException {

        Movie movie = new Movie();
        mapFields(movie, req, file, image);
        return repo.save(movie);
    }

    // ===================== GET ALL =====================
    public List<Movie> getAllMovies() {
        return repo.findAll();
    }

    // ===================== GET BY ID =====================
    public Movie getMovie(Long id) {
        return repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Movie not found: " + id));
    }

    // ===================== UPDATE =====================
    public Movie updateMovie(
            Long id,
            MovieRequest req,
            MultipartFile file,
            MultipartFile image
    ) throws IOException {

        Movie movie = repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Movie not found: " + id));

        mapFields(movie, req, file, image);
        return repo.save(movie);
    }

    // ===================== DELETE =====================
    public void deleteMovie(Long id) {
        Movie movie = getMovie(id);
        repo.delete(movie);
    }

    // ===================== ENABLE / DISABLE =====================
    public Movie movieEnabled(Long id, boolean enabled) {
        Movie movie = repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Movie not found: " + id));

        movie.setEnabled(enabled);
        return repo.save(movie);
    }

    // ===================== COMMON FIELD MAPPER =====================
    private void mapFields(
            Movie movie,
            MovieRequest req,
            MultipartFile file,
            MultipartFile image
    ) throws IOException {

        // ---------- VIDEO UPLOAD ----------
// ---------- VIDEO UPLOAD ----------
        // In MovieService.mapFields()
        // ---------- VIDEO UPLOAD ----------
        if (file != null && !file.isEmpty()) {
            Map<String, Object> options = ObjectUtils.asMap(
                    "resource_type", "video",
                    "folder", "fetflix/videos",
                    "chunk_size", 6000000 // 6MB chunks - critical for large files!
            );

            // Use InputStream for proper chunked upload
            Map uploadResult = cloudinary.uploader()
                    .uploadLarge(file.getInputStream(), options);

            // Store just the URL (simplest approach)
            movie.setMovieVideo(uploadResult.get("secure_url").toString());

            // OR if you need both URL and publicId, use proper JSON:
            // String videoData = String.format(
            //     "{\"url\":\"%s\",\"publicId\":\"%s\"}",
            //     uploadResult.get("secure_url"),
            //     uploadResult.get("public_id")
            // );
            // movie.setMovieVideo(videoData);
        }
        // ---------- IMAGE UPLOAD ----------
        if (image != null && !image.isEmpty()) {
            Map<?, ?> imageUpload = cloudinary.uploader().upload(
                    image.getBytes(),
                    ObjectUtils.asMap("resource_type", "image")
            );
            movie.setPoster(imageUpload.get("secure_url").toString());
        }

        // ---------- BASIC FIELDS ----------
        movie.setTitle(req.getTitle());
        movie.setDescription(req.getDescription());
        movie.setReleaseYear(req.getReleaseYear());
        movie.setAgeRating(req.getAgeRating());
        movie.setDurationMinutes(req.getDurationMinutes());
        movie.setEnabled(req.isEnabled());

        // ---------- CAST ----------
        movie.setCastList(
                req.getCast().stream()
                        .map(castService::getOrCreate)
                        .collect(Collectors.toList())
        );

        // ---------- GENRES ----------
        movie.setGenres(
                req.getGenres().stream()
                        .map(genreService::getOrCreate)
                        .collect(Collectors.toList())
        );

        // ---------- SCENES ----------
        movie.setScenes(
                req.getScenes().stream()
                        .map(sceneService::getOrCreate)
                        .collect(Collectors.toList())
        );
    }
}
