package com.dev.show;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/api/shows")
@AllArgsConstructor
public class ShowController {

    private final ShowService service;

    @PostMapping("/create")
    public Show createShow(@Valid @RequestBody ShowRequest req) {
        return service.createShow(req);
    }

    @PatchMapping("/{id}/status")
    public void showEnabled(@PathVariable Long id){
        service.showEnabled(id);
    }

//    @PatchMapping("/{id}/status")
//    public void showEnabled(@PathVariable Long id, @RequestParam boolean enabled){
//        service.showEnabled(id,enabled);
//    }

    @GetMapping
    public List<Show> getAllShows() {
        return service.getAllShows();
    }

    @GetMapping("/{id}")
    public Show getShow(@PathVariable Long id) {
        return service.getShow(id);
    }

    @PutMapping("/{id}")
    public Show updateShow(@PathVariable Long id, @Valid @RequestBody ShowRequest req) {
        return service.updateShow(id, req);
    }

    @DeleteMapping("/{id}")
    public String deleteShow(@PathVariable Long id) {
        service.deleteShow(id);
        return "Show deleted successfully!";
    }
}
