package com.example.textanalyzer.dto;

import lombok.Value;

@Value(staticConstructor = "from")
public class CharOccurrenceDto {

    String character;
    Integer counter;

}
