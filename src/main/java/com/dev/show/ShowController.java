package com.dev.show;

import com.dev.movie.MovieRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("admin/api/shows")
@AllArgsConstructor
public class ShowController {

    private final ShowService service;

    /**
     * CREATE SHOW
     * - Accepts multipart/form-data
     * - "show" -> JSON (ShowRequest)
     * - "image" -> poster image
     */
    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public Show createShow(
            @Valid @RequestPart("show") ShowRequest req,
            @RequestPart("image") MultipartFile image
    ) throws IOException {
        return service.createShow(req, image);
    }

    /**
     * TOGGLE SHOW ENABLED STATUS
     * - If enabled -> disable
     * - If disabled -> enable
     * - Cascades to seasons & episodes
     */
    @PatchMapping("/{id}/status")
    public void showEnabled(@PathVariable Long id) {
        service.showEnabled(id);
    }

    /**
     * GET ALL SHOWS (admin)
     */
    @GetMapping
    public List<Show> getAllShows() {
        return service.getAllShows();
    }

    /**
     * GET SINGLE SHOW BY ID
     */
    @GetMapping("/{id}")
    public Show getShow(@PathVariable Long id) {
        return service.getShow(id);
    }

    /**
     * UPDATE SHOW
     * - Replaces fields
     * - Re-uploads image if provided
     */
    @PutMapping("/{id}")
    public Show updateShow(
            @PathVariable Long id,
            @Valid @RequestPart("show") ShowRequest req,
            @RequestPart("image") MultipartFile image
    ) throws IOException {
        return service.updateShow(id, req, image);
    }

    /**
     * DELETE SHOW
     */
    @DeleteMapping("/{id}")
    public String deleteShow(@PathVariable Long id) {
        service.deleteShow(id);
        return "Show deleted successfully!";
    }
}
