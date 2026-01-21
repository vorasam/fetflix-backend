package com.dev.episode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class EpisodeRequest {

    @NotNull(message = "Episode number is required")
    private Integer episodeNumber;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Release year is required")
    private Integer releaseYear;

    @NotBlank(message = "Age rating is required")
    private String ageRating;

    @NotNull
    private List<String> cast;

    @NotNull
    private List<String> genres;

    @NotNull
    private List<String> scenes;

    @NotNull(message = "Poster cannot be null")
    private String poster;

    private boolean enable = true;

    @NotNull(message = "Video cannot be null")
    private String episodeVideo;
}
