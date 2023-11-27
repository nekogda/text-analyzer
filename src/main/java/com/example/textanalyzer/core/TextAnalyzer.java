package com.example.textanalyzer.core;

import com.example.textanalyzer.core.model.OrderedOccurrenceInfo;
import com.example.textanalyzer.core.model.Text;

public interface TextAnalyzer {

    OrderedOccurrenceInfo countOccurrence(Text text);

}
