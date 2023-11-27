package com.example.textanalyzer.dto;

import lombok.Data;

import java.util.List;

@Data(staticConstructor = "from")
public class CountOccurrenceResponse {

    private final List<CharOccurrenceDto> data;
    private final String direction;

}
