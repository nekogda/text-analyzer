package com.example.textanalyzer.core;

import com.example.textanalyzer.core.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TextAnalyzerImpl implements TextAnalyzer {

    @Override
    @Cacheable("process")
    public OrderedOccurrenceInfo countOccurrence(Text text) {
        log.info("text = {}", text);
        NormalizedText normalizedText = normalize(text);
        OccurrenceInfo occurrenceInfo = countChars(normalizedText);
        return sort(occurrenceInfo);
    }

    private NormalizedText normalize(Text text) {
        log.debug("text = {}", text);
        return NormalizedText.wrap(Normalizer
                .normalize(text.unwrap(), Normalizer.Form.NFC));
    }

    private OccurrenceInfo countChars(NormalizedText normalizedText) {
        log.debug("called");
        HashMap<String, Integer> collect = normalizedText
                .unwrap()
                .codePoints()
                .collect(HashMap::new,
                        (acc, k) -> acc.merge(Character.toString(k), 1, Integer::sum),
                        HashMap::putAll);
        return OccurrenceInfo.from(collect.entrySet()
                .stream()
                .map(e -> CharOccurrence.from(e.getKey(), e.getValue()))
                .collect(Collectors.toList()));

    }

    private OrderedOccurrenceInfo sort(OccurrenceInfo info) {
        log.debug("occurrenceInfo = {}", info);
        List<CharOccurrence> data = info.getData();
        data.sort(Comparator.comparingInt(CharOccurrence::getCounter).reversed());

        return new OrderedOccurrenceInfo(data, OrderedOccurrenceInfo.Direction.DESC);
    }

}
