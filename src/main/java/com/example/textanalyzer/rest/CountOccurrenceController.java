package com.example.textanalyzer.rest;

import com.example.textanalyzer.core.TextAnalyzer;
import com.example.textanalyzer.core.model.OrderedOccurrenceInfo;
import com.example.textanalyzer.core.model.Text;
import com.example.textanalyzer.dto.CharOccurrenceDto;
import com.example.textanalyzer.dto.CountOccurrenceRequest;
import com.example.textanalyzer.dto.CountOccurrenceResponse;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@Slf4j
@Value
public class CountOccurrenceController {

    TextAnalyzer service;

    @PostMapping(value = "/process")
    public ResponseEntity<CountOccurrenceResponse> handle(
            @Validated @RequestBody CountOccurrenceRequest request) {
        log.info("CountOccurrenceRequest = {}", request);
        OrderedOccurrenceInfo info = service.countOccurrence(Text.wrap(request.getText()));
        CountOccurrenceResponse response = convert(info);
        return ResponseEntity.ok(response);
    }

    private CountOccurrenceResponse convert(OrderedOccurrenceInfo info) {
        log.debug("OrderedOccurrenceInfo = {}", info);
        return CountOccurrenceResponse.from(
                info.getData()
                        .stream()
                        .map(co -> CharOccurrenceDto.from(co.getCharacter(), co.getCounter()))
                        .collect(Collectors.toList()),
                info.getDirection().name());
    }

}