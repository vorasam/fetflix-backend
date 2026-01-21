package com.dev.content;

import com.dev.cast.Cast;
import com.dev.genre.Genre;
import com.dev.scene.Scene;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "content")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private int releaseYear;
    private String ageRating;
    private String poster;
    @Column(nullable = false)
    private boolean enabled = true;

    @ManyToMany
    @JoinTable(name = "content_cast",
            joinColumns = @JoinColumn(name = "content_id"),
            inverseJoinColumns = @JoinColumn(name = "cast_id"))
    private List<Cast> castList;

    @ManyToMany
    @JoinTable(name = "content_genres",
            joinColumns = @JoinColumn(name = "content_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres;

    @ManyToMany
    @JoinTable(name = "content_scenes",
            joinColumns = @JoinColumn(name = "content_id"),
            inverseJoinColumns = @JoinColumn(name = "scene_id"))
    private List<Scene> scenes;
}
