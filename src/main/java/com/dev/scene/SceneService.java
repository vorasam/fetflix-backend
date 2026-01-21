package com.dev.scene;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SceneService {

    private final SceneRepository repo;

    public List<Scene> getAll() {
        return repo.findAll();
    }

    public Scene createScene(String sceneName) {
        return repo.findBySceneName(sceneName)
                .orElseGet(() -> repo.save(new Scene(null, sceneName)));
    }

    public Scene getOrCreate(String sceneName) {
        return createScene(sceneName);
    }
}
