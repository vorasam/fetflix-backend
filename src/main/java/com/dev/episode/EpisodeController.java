package com.dev.episode;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("admin/api/episodes")
@AllArgsConstructor
public class EpisodeController {

    private final EpisodeService service;

    @PostMapping("/season/{seasonId}")
    public Episode addEpisode(@PathVariable Long seasonId, @Valid @RequestBody EpisodeRequest req) {
        return service.addEpisode(seasonId, req);
    }

    @PatchMapping("/{id}/status")
    public void episodeEnable(@PathVariable Long id){
        service.episodeEnabled(id);
    }

    @GetMapping("/{episodeId}")
    public Episode getEpisodeById(@PathVariable Long episodeId) {
        return service.getEpisodeById(episodeId);
    }

    @GetMapping("/season/{seasonId}")
    public List<Episode> getEpisodes(@PathVariable Long seasonId) {
        return service.getEpisodes(seasonId);
    }

    // UPDATE EPISODE
    @PutMapping("/{episodeId}")
    public Episode updateEpisode(@PathVariable Long episodeId,
                                 @Valid @RequestBody EpisodeRequest req) {
        return service.updateEpisode(episodeId, req);
    }

    // DELETE EPISODE
    @DeleteMapping("/{episodeId}")
    public String deleteEpisode(@PathVariable Long episodeId) {
        service.deleteEpisode(episodeId);
        return "Episode deleted successfully!";
    }

}
