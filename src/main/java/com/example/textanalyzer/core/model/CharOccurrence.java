package com.example.textanalyzer.core.model;

import lombok.Value;

@Value(staticConstructor = "from")
public class CharOccurrence {

    String character;
    int counter;

}
