package com.dev.movie;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.dev.cast.CastService;
import com.dev.genre.GenreService;
import com.dev.exception.ResourceNotFoundException;
import com.dev.scene.SceneService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.List;

@Service
@AllArgsConstructor
public class MovieService {
    private final MovieRepository repo;
    private final CastService castService;
    private final GenreService genreService;
    private final SceneService sceneService;
    private final Cloudinary cloudinary;


    public Movie createMovie(MovieRequest req, MultipartFile file, MultipartFile image) throws IOException {
        System.out.println("Creating movie");
        Movie movie = new Movie();


        mapField(movie,req,file,image);
        return repo.save(movie);
    }

    public List<Movie> getAllMovies(){
        return repo.findAll();
    }

    public Movie getMovie(Long id){
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found:"+id));
    }

    public Movie updateMovie(Long id, MovieRequest req, MultipartFile file, MultipartFile image){

           Movie movie = repo.findById(id)
              .orElseThrow(() -> new ResourceNotFoundException("Movie not found: " + id));;


        mapField(movie,req,file,image);
        return repo.save(movie);

        
      
    }



    public void deleteMovie(Long id){
        getMovie(id);
        repo.deleteById(id);
    }


    private void mapField(Movie movie, MovieRequest req,MultipartFile file,MultipartFile image) throws IOException {
        System.out.println("call map field");
        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap("resource_type", "video")
        );
        String url = uploadResult.get("secure_url").toString();

        Map uploadResultImage = cloudinary.uploader().upload(
                image.getBytes(),
                ObjectUtils.asMap("resource_type", "image")
        );

        String urlImage = uploadResultImage.get("secure_url").toString();
        movie.setTitle(req.getTitle());
        movie.setDescription(req.getDescription());
        movie.setReleaseYear(req.getReleaseYear());
        movie.setAgeRating(req.getAgeRating());
        movie.setDurationMinutes(req.getDurationMinutes());
        movie.setPoster(urlImage);
        movie.setEnabled(req.isEnabled());
        movie.setMovieVideo(url);
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

        System.out.println(movie);
    }

    public void movieEnabled(Long id, boolean enabled) {
        Movie movie = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie Not found"));
        movie.setEnabled(enabled);
        repo.save(movie);
    }
}
