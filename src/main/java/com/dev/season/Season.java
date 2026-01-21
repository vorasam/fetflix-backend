package com.dev.season;

import com.dev.episode.Episode;
import com.dev.show.Show;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "seasons")
@Data
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int seasonNumber;

    @ManyToOne
    @JoinColumn(name = "show_id")
//    @JsonIgnore
    private Show show;

    private boolean enabled;

    @OneToMany(
            mappedBy = "season",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Episode> episodes = new ArrayList<>();

}
