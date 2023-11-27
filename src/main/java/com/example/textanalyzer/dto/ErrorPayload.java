package com.example.textanalyzer.dto;

import lombok.Value;

import java.util.List;

@Value(staticConstructor = "from")
public class ErrorPayload {

    int status;
    List<String> messages;

}
