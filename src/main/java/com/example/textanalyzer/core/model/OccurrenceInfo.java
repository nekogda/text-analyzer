package com.example.textanalyzer.core.model;

import lombok.Value;

import java.util.List;

@Value(staticConstructor = "from")
public class OccurrenceInfo {

    List<CharOccurrence> data;

}
