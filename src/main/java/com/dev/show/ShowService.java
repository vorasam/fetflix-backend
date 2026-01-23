package com.dev.show;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.dev.cast.CastService;
import com.dev.episode.Episode;
import com.dev.episode.EpisodeRepository;
import com.dev.genre.GenreService;
import com.dev.scene.SceneService;
import com.dev.exception.ResourceNotFoundException;
import com.dev.season.Season;
import com.dev.season.SeasonRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowService {

    private final ShowRepository repo;
    private final SeasonRepository seasonRepository;
    private final EpisodeRepository episodeRepository;
    private final CastService castService;
    private final GenreService genreService;
    private final SceneService sceneService;
    private final Cloudinary cloudinary;

    /**
     * CREATE SHOW
     */
    public Show createShow(ShowRequest req, MultipartFile image) throws IOException {
        Show show = new Show();
        mapFields(show, req, image);
        return repo.save(show);
    }

    /**
     * GET ALL SHOWS
     */
    public List<Show> getAllShows() {
        return repo.findAll();
    }

    /**
     * GET SHOW BY ID
     */
    public Show getShow(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found: " + id));
    }

    /**
     * UPDATE SHOW
     */
    public Show updateShow(Long id, ShowRequest req, MultipartFile image) throws IOException {
        Show show = getShow(id);
        mapFields(show, req, image);
        return repo.save(show);
    }

    /**
     * DELETE SHOW
     */
    public void deleteShow(Long id) {
        getShow(id); // ensure exists
        repo.deleteById(id);
    }

    /**
     * MAP REQUEST DATA TO ENTITY
     */
    private void mapFields(Show show, ShowRequest req, MultipartFile image) throws IOException {

        // ---------- IMAGE UPLOAD ----------
        if (image != null && !image.isEmpty()) {
            Map<?, ?> imageUpload = cloudinary.uploader().upload(
                    image.getBytes(),
                    ObjectUtils.asMap("resource_type", "image")
            );
            show.setPoster(imageUpload.get("secure_url").toString());
        }

        // ---------- BASIC FIELDS ----------
        show.setTitle(req.getTitle());
        show.setDescription(req.getDescription());
        show.setReleaseYear(req.getReleaseYear());
        show.setAgeRating(req.getAgeRating());
        show.setEnabled(req.isEnable());

        // ---------- CAST ----------
        show.setCastList(
                req.getCast().stream()
                        .map(castService::getOrCreate)
                        .collect(Collectors.toList())
        );

        // ---------- GENRES ----------
        show.setGenres(
                req.getGenres().stream()
                        .map(genreService::getOrCreate)
                        .collect(Collectors.toList())
        );

        // ---------- SCENES ----------
        show.setScenes(
                req.getScenes().stream()
                        .map(sceneService::getOrCreate)
                        .collect(Collectors.toList())
        );
    }

    /**
     * TOGGLE SHOW ENABLED STATUS
     * - Cascades to:
     *   - Seasons
     *   - Episodes
     */
    @Transactional
    public void showEnabled(Long showId) {

        Show show = repo.findById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"+ showId));

        // Toggle current status
        boolean newStatus = !show.isEnabled();
        show.setEnabled(newStatus);

        // Fetch all seasons under show
        List<Season> seasons = seasonRepository.findByShowId(showId);

        for (Season season : seasons) {
            season.setEnabled(newStatus);

            // Fetch all episodes under season
            List<Episode> episodes =
                    episodeRepository.findBySeasonId(season.getId());

            for (Episode episode : episodes) {
                episode.setEnable(newStatus);
            }

            // Save episodes
            episodeRepository.saveAll(episodes);
        }

        // Save seasons & show
        seasonRepository.saveAll(seasons);
        repo.save(show);
    }
}
