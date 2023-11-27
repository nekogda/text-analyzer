package com.example.textanalyzer.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CountOccurrenceRequest {

    @NotNull
    @Size(max = 1000)
    private String text;

}
