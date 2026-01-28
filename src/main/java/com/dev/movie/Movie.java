package com.dev.movie;

import com.dev.content.Content;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "movies")
@AllArgsConstructor
@NoArgsConstructor
public class Movie extends Content {

    private int durationMinutes;
    @Column(columnDefinition = "TEXT")
    private String movieVideo;
}


