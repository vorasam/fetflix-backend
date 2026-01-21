package com.dev.scene;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SceneRepository extends JpaRepository<Scene, Long> {
    Optional<Scene> findBySceneName(String sceneName);
}
