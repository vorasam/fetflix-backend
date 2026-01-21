package com.dev.show;

import com.dev.content.Content;
import com.dev.season.Season;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "shows")
@Data
public class Show extends Content {

    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Season> seasons;
}
