package com.dev.show;

import java.util.stream.Collectors;

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

    public Show createShow(ShowRequest req) {
        Show show = new Show();
        mapFields(show, req);
        return repo.save(show);
    }

    public List<Show> getAllShows() {
        return repo.findAll();
    }

    public Show getShow(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found: " + id));
    }

    public Show updateShow(Long id, ShowRequest req) {
        Show show = getShow(id);
        mapFields(show, req);
        return repo.save(show);
    }

    public void deleteShow(Long id) {
        getShow(id); // check exists
        repo.deleteById(id);
    }


    private void mapFields(Show show, ShowRequest req) {

        show.setTitle(req.getTitle());
        show.setDescription(req.getDescription());
        show.setReleaseYear(req.getReleaseYear());
        show.setAgeRating(req.getAgeRating());
        show.setEnabled(req.isEnable());
        show.setPoster(req.getPoster());

        show.setCastList(
                req.getCast().stream()
                        .map(castService::getOrCreate)
                        .collect(Collectors.toList())
        );

        show.setGenres(
                req.getGenres().stream()
                        .map(genreService::getOrCreate)
                        .collect(Collectors.toList())
        );

        show.setScenes(
                req.getScenes().stream()
                        .map(sceneService::getOrCreate)
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public void showEnabled(Long showId) {
        Show show = repo.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show not found"));

        boolean newStatus = !show.isEnabled();
        show.setEnabled(newStatus);

        List<Season> seasons = seasonRepository.findByShowId(showId);

        for (Season season : seasons) {
            season.setEnabled(newStatus);

            List<Episode> episodes = episodeRepository.findBySeasonId(season.getId());
            for (Episode episode : episodes) {
                episode.setEnable(newStatus);
            }
            episodeRepository.saveAll(episodes);
        }

        seasonRepository.saveAll(seasons);
        repo.save(show);
    }



//    public void showEnabled(Long showId, boolean enabled) {
//        Show show = repo.findById(showId)
//                .orElseThrow(()-> new RuntimeException("Show not found"));
//        show.setEnabled(enabled);
//
//        List<Season> seasons = seasonRepository.findByShowId(showId);
//        for (Season season : seasons) {
//            season.setEnabled(enabled);
//    }
//
////        List<Episode> episodes =
////                episodeRepository.findBySeasonId(season.getId());
////        episodes.forEach(ep -> ep.setEnable(enabled));
//    }
}

