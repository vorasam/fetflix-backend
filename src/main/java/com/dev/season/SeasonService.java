package com.dev.season;

import com.dev.episode.Episode;
import com.dev.episode.EpisodeRepository;
import com.dev.show.Show;
import com.dev.exception.ResourceNotFoundException;
import com.dev.show.ShowRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SeasonService {

    private final SeasonRepository seasonRepo;
    private final ShowRepository showRepo;
    private final EpisodeRepository episodeRepository;

    public Season addSeasonToShow(Long showId, SeasonRequest req) {
        Show show = showRepo.findById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found: " + showId));

        Season season = new Season();
        season.setSeasonNumber(req.getSeasonNumber());
        season.setEnabled(req.isEnable());
        season.setShow(show);

        return seasonRepo.save(season);
    }

    public List<Season> getSeasons(Long showId) {
        Show show = showRepo.findById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found: " + showId));
        return show.getSeasons();
    }

    public Season getSeasonById(Long seasonId) {
        return seasonRepo.findById(seasonId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Season not found: " + seasonId)
                );
    }


    public Season updateSeason(Long seasonId, SeasonRequest req) {
        Season season = seasonRepo.findById(seasonId)
                .orElseThrow(() -> new ResourceNotFoundException("Season not found: " + seasonId));

        season.setSeasonNumber(req.getSeasonNumber());
        return seasonRepo.save(season);
    }

    public void deleteSeason(Long seasonId) {
        Season season = seasonRepo.findById(seasonId)
                .orElseThrow(() -> new ResourceNotFoundException("Season not found: " + seasonId));

        seasonRepo.delete(season);
    }

    public void seasonEnabled(Long id) {
        Season season = seasonRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("No season found"));

        Show show = season.getShow();

        boolean newStatus = !season.isEnabled();

        // 🔒 RULE: cannot ENABLE season if show disabled
        if (newStatus && !show.isEnabled()) {
            throw new IllegalStateException(
                    "Cannot enable season while show is disabled"
            );
        }

        season.setEnabled(newStatus);

        // 🔥 cascade to episodes
        List<Episode> episodes = episodeRepository.findBySeasonId(id);
        for (Episode episode : episodes) {
            episode.setEnable(newStatus);
        }

        episodeRepository.saveAll(episodes);
        seasonRepo.save(season);
    }

}
