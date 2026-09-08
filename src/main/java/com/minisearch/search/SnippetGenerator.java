package com.minisearch.search;

import com.minisearch.model.Document;
import com.minisearch.text.Tokenizer;
import java.util.List;

public class SnippetGenerator {

  private static final int MAX_LENGTH = 100;

  private final Tokenizer tokenizer;

  public SnippetGenerator() {
    this.tokenizer = new Tokenizer();
  }

  public String generate(Document document, String query) {
    String content = document.getContent();

    if (content.length() <= MAX_LENGTH) {
      return content;
    }

    List<String> queryTerms = tokenizer.tokenize(query);

    String lowerContent = content.toLowerCase();

    int matchIndex = -1;

    for (String term : queryTerms) {
      matchIndex = lowerContent.indexOf(term);

      if (matchIndex >= 0) {
        break;
      }
    }

    if (matchIndex < 0) {
      return content.substring(0, MAX_LENGTH) + "...";
    }

    int start = Math.max(0, matchIndex - MAX_LENGTH / 2);

    int end = Math.min(content.length(), start + MAX_LENGTH);

    String snippet = content.substring(start, end).trim();

    if (start > 0) {
      snippet = "..." + snippet;
    }

    if (end < content.length()) {
      snippet = snippet + "...";
    }

    return snippet;
  }
}
