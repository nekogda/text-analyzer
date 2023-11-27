package com.example.textanalyzer.core;

import com.example.textanalyzer.core.model.CharOccurrence;
import com.example.textanalyzer.core.model.OrderedOccurrenceInfo;
import com.example.textanalyzer.core.model.OrderedOccurrenceInfo.Direction;
import com.example.textanalyzer.core.model.Text;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;


@SpringBootTest
@DisplayName("TextAnalyzer должен")
class TextAnalyzerImplTests {

    @Autowired
    private TextAnalyzer textAnalyzer;
    @Autowired
    CacheManager cacheManager;

    @DisplayName("вернуть упорядоченную последовательность")
    @ParameterizedTest
    @MethodSource("provideData")
    void returnOrderedSequence(String text) {
        OrderedOccurrenceInfo info = textAnalyzer.countOccurrence(Text.wrap(text));

        Assertions
                .assertThat(info.getDirection())
                .isEqualTo(Direction.DESC);

        Assertions
                .assertThat(info.getData())
                .isSortedAccordingTo(Comparator.comparingInt(CharOccurrence::getCounter).reversed());
    }

    @DisplayName("вернуть результат когда получит текст")
    @ParameterizedTest
    @MethodSource("provideData")
    public void returnResultWhenReceivesText(String text, OrderedOccurrenceInfo expected) {
        OrderedOccurrenceInfo info = textAnalyzer.countOccurrence(Text.wrap(text));
        Assertions
                .assertThat(info.getData())
                .containsExactlyInAnyOrderElementsOf(expected.getData());
    }

    public static Stream<Arguments> provideData() {
        return Stream.of(Arguments.of("", OrderedOccurrenceInfo.of()),
                Arguments.of("a", // bmp
                        OrderedOccurrenceInfo.of(
                                CharOccurrence.from("a", 1))),
                Arguments.of("ä", // composed
                        OrderedOccurrenceInfo.of(
                                CharOccurrence.from("ä", 1))),
                Arguments.of("\u0061\u0308", // decomposed
                        OrderedOccurrenceInfo.of(
                                CharOccurrence.from("ä", 1))),
                Arguments.of("\u0061\u0308ä", // canonical equivalence
                        OrderedOccurrenceInfo.of(
                                CharOccurrence.from("ä", 2))),
                Arguments.of("\uD835\uDD21d", // compatibility equivalence
                        OrderedOccurrenceInfo.of(
                                CharOccurrence.from("d", 1),
                                CharOccurrence.from("\uD835\uDD21", 1))),
                Arguments.of("aab",
                        OrderedOccurrenceInfo.of(
                                CharOccurrence.from("a", 2),
                                CharOccurrence.from("b", 1))),
                Arguments.of("aba",
                        OrderedOccurrenceInfo.of(
                                CharOccurrence.from("a", 2),
                                CharOccurrence.from("b", 1))),
                Arguments.of("abb",
                        OrderedOccurrenceInfo.of(
                                CharOccurrence.from("b", 2),
                                CharOccurrence.from("a", 1)))
        );
    }

    @Test
    @DisplayName("разместить значение в кэше")
    void cacheResult() {
        Text text = Text.wrap("ab");
        OrderedOccurrenceInfo info = textAnalyzer.countOccurrence(text);
        Cache cache = Optional
                .ofNullable(cacheManager.getCache("process"))
                .orElseThrow();
        Assertions
                .assertThat(cache.get(text))
                .isNotNull()
                .extracting(Cache.ValueWrapper::get)
                .isEqualTo(info);
    }

}