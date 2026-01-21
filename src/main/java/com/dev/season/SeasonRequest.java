package com.dev.season;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SeasonRequest {

    @NotNull(message = "Season number is required")
    private Integer seasonNumber;

    @NotNull
    private boolean enable = true;
}
