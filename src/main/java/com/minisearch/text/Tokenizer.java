package com.minisearch.text;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

  public List<String> tokenize(String text) {
    List<String> tokens = new ArrayList<>();

    String cleanedText = text.toLowerCase().replaceAll("[^a-z0-9\\s]", " ");

    String[] terms = cleanedText.split("\\s+");

    for (String term : terms) {
      if (!term.isBlank()) {
        tokens.add(term);
      }
    }

    return tokens;
  }
}
