package com.example.textanalyzer.core.model;

import lombok.Value;

@Value(staticConstructor = "wrap")
public class Text {

    String value;

    public String unwrap() {
        return value;
    }

}
