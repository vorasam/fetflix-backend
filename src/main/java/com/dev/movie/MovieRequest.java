package com.dev.movie;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class MovieRequest {

    @NotBlank(message = "Movie title is required")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 10, message = "Description must be at least 10 characters")
    private String description;

    @NotNull(message = "Release year is required")
    private Integer releaseYear;

    @NotBlank(message = "Age rating is required")
    private String ageRating;

    @NotNull(message = "Duration is required")
    private Integer durationMinutes;

    @NotNull(message = "Cast list cannot be null")
    private List<String> cast;

    @NotNull(message = "Genres cannot be null")
    private List<String> genres;

    @NotNull(message = "Scenes cannot be null")
    private List<String> scenes;

    private String poster;

    @NotNull(message = "Give Access to movie")
    private boolean enabled = true;

    private String movieVideo;

}
