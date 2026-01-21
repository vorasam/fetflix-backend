package com.dev.season;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("admin/api/seasons")
@AllArgsConstructor
public class SeasonController {

    private final SeasonService service;

    @PostMapping("/show/{showId}")
    public Season addSeason(@PathVariable Long showId, @Valid @RequestBody SeasonRequest req) {
        return service.addSeasonToShow(showId, req);
    }

    @GetMapping("/{seasonId}")
    public ResponseEntity<Season> getSeasonById(@PathVariable Long seasonId) {
        return ResponseEntity.ok(service.getSeasonById(seasonId));
    }

    @PatchMapping("/{id}/status")
    public void seasonEnable(@PathVariable Long id){
        service.seasonEnabled(id);
    }

    @GetMapping("/show/{showId}")
    public List<Season> getSeasons(@PathVariable Long showId) {
        return service.getSeasons(showId);
    }

    // UPDATE SEASON
    @PutMapping("/{seasonId}")
    public Season updateSeason(@PathVariable Long seasonId, @Valid @RequestBody SeasonRequest req) {
        return service.updateSeason(seasonId, req);
    }

    // DELETE SEASON
    @DeleteMapping("/{seasonId}")
    public String deleteSeason(@PathVariable Long seasonId) {
        service.deleteSeason(seasonId);
        return "Season deleted successfully!";
    }

}
