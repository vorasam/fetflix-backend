package com.dev.episode;

import com.dev.cast.Cast;
import com.dev.genre.Genre;
import com.dev.scene.Scene;
import com.dev.season.Season;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "episodes")
@Data
public class Episode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int episodeNumber;
    private boolean enable;
    private String episodeVideo;

    private String title;

    @Column(length = 2000)
    private String description;

    private Integer releaseYear;

    private String ageRating;
    private String poster;

    @ManyToMany
    @JoinTable(
            name = "episode_cast",
            joinColumns = @JoinColumn(name = "episode_id"),
            inverseJoinColumns = @JoinColumn(name = "cast_id")
    )
    private Set<Cast> castList = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "episode_genre",
            joinColumns = @JoinColumn(name = "episode_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "episode_scene",
            joinColumns = @JoinColumn(name = "episode_id"),
            inverseJoinColumns = @JoinColumn(name = "scene_id")
    )
    private Set<Scene> scenes = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "season_id")
    @JsonIgnore                     // IMPORTANT: prevents infinite loop
    private Season season;
}
