package com.dev.movie;

import com.dev.cast.CastService;
import com.dev.genre.GenreService;
import com.dev.exception.ResourceNotFoundException;
import com.dev.scene.SceneService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

import java.util.List;

@Service
@AllArgsConstructor
public class MovieService {
    private final MovieRepository repo;
    private final CastService castService;
    private final GenreService genreService;
    private final SceneService sceneService;

    public Movie createMovie(MovieRequest req){
        Movie movie = new Movie();
        mapField(movie,req);
        return repo.save(movie);
    }

    public List<Movie> getAllMovies(){
        return repo.findAll();
    }

    public Movie getMovie(Long id){
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found:"+id));
    }

    public Movie updateMovie(Long id, MovieRequest req){
      Movie movie = repo.findById(id)
              .orElseThrow(() -> new ResourceNotFoundException("Movie not found: " + id));
        movie.setTitle(req.getTitle());
        movie.setDescription(req.getDescription());
        movie.setReleaseYear(req.getReleaseYear());
        movie.setAgeRating(req.getAgeRating());
        movie.setDurationMinutes(req.getDurationMinutes());
        movie.setPoster(req.getPoster());
        movie.setEnabled(req.isEnabled());
        movie.setMovieVideo(req.getMovieVideo());
        movie.setCastList(
                req.getCast().stream()
                        .map(castService::getOrCreate)
                        .collect(Collectors.toList())
        );

        movie.setGenres(
                req.getGenres().stream()
                        .map(genreService::getOrCreate)
                        .collect(Collectors.toList())
        );

        movie.setScenes(
                req.getScenes().stream()
                        .map(sceneService::getOrCreate)
                        .collect(Collectors.toList())
        );
        return repo.save(movie);
    }



    public void deleteMovie(Long id){
        getMovie(id);
        repo.deleteById(id);
    }


    private void mapField(Movie movie, MovieRequest req) {

        movie.setTitle(req.getTitle());
        movie.setDescription(req.getDescription());
        movie.setReleaseYear(req.getReleaseYear());
        movie.setAgeRating(req.getAgeRating());
        movie.setDurationMinutes(req.getDurationMinutes());
        movie.setPoster(req.getPoster());
        movie.setEnabled(req.isEnabled());
        movie.setMovieVideo(req.getMovieVideo());
        movie.setCastList(
                req.getCast().stream()
                        .map(castService::getOrCreate)
                        .collect(Collectors.toList())
        );

        movie.setGenres(
                req.getGenres().stream()
                        .map(genreService::getOrCreate)
                        .collect(Collectors.toList())
        );

        movie.setScenes(
                req.getScenes().stream()
                        .map(sceneService::getOrCreate)
                        .collect(Collectors.toList())
        );
    }

    public void movieEnabled(Long id, boolean enabled) {
        Movie movie = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie Not found"));
        movie.setEnabled(enabled);
        repo.save(movie);
    }
}
