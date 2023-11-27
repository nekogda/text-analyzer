package com.example.textanalyzer.core.model;

import lombok.Value;
import lombok.experimental.FieldNameConstants;

import java.util.Arrays;
import java.util.List;

@Value
@FieldNameConstants
public class OrderedOccurrenceInfo {

    List<CharOccurrence> data;
    Direction direction;

    public static OrderedOccurrenceInfo of(CharOccurrence... cos) {
        return new OrderedOccurrenceInfo(Arrays.asList(cos), Direction.DESC);
    }

    public enum Direction {
        ASC, DESC
    }

}
