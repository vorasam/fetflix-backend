package com.dev.cast;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api/cast")
@AllArgsConstructor
public class CastController {

    private final CastService service;

    @GetMapping
    public List<Cast> getAllCast() {
        return service.getAll();
    }

    @PostMapping
    public Cast addCast(@RequestBody Cast cast) {
        return service.createCast(cast.getActorName());
    }
}
