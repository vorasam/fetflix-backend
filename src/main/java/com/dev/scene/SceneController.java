package com.dev.scene;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api/scenes")
@AllArgsConstructor
public class SceneController {

    private final SceneService service;

    // GET ALL (dropdown)
    @GetMapping
    public List<Scene> getAllScenes() {
        return service.getAll();
    }

    // ADD NEW Scene Tag (admin)
    @PostMapping
    public Scene addScene(@RequestBody Scene scene) {
        return service.createScene(scene.getSceneName());
    }
}
