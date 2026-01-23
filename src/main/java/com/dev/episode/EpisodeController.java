package com.dev.episode;

import com.cloudinary.Cloudinary;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("admin/api/episodes")
@AllArgsConstructor
public class EpisodeController {

    private final EpisodeService service;
    private final Cloudinary cloudinary;

    // ========== CREATE EPISODE ==========
    @PostMapping(value = "/season/{seasonId}", consumes = "multipart/form-data")
    public Episode addEpisode(
            @PathVariable Long seasonId,
            @Valid @RequestPart("episode") EpisodeRequest req,
            @RequestPart(required = false) MultipartFile video,
            @RequestPart(required = false) MultipartFile image
    ) throws IOException {
        return service.addEpisode(seasonId, req, video, image);
    }

    // ========== ENABLE / DISABLE ==========
    @PatchMapping("/{id}/status")
    public void episodeEnable(@PathVariable Long id) {
        service.episodeEnabled(id);
    }

    // ========== GET ==========
    @GetMapping("/{episodeId}")
    public Episode getEpisodeById(@PathVariable Long episodeId) {
        return service.getEpisodeById(episodeId);
    }

    @GetMapping("/season/{seasonId}")
    public List<Episode> getEpisodes(@PathVariable Long seasonId) {
        return service.getEpisodes(seasonId);
    }

    // ========== UPDATE ==========
    @PutMapping(value = "/{episodeId}", consumes = "multipart/form-data")
    public Episode updateEpisode(
            @PathVariable Long episodeId,
            @Valid @RequestPart("episode") EpisodeRequest req,
            @RequestPart(required = false) MultipartFile video,
            @RequestPart(required = false) MultipartFile image
    ) throws IOException {
        return service.updateEpisode(episodeId, req, video, image);
    }

    // ========== DELETE ==========
    @DeleteMapping("/{episodeId}")
    public String deleteEpisode(@PathVariable Long episodeId) {
        service.deleteEpisode(episodeId);
        return "Episode deleted successfully!";
    }
}
