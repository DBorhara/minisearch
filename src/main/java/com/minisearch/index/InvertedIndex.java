package com.minisearch.index;

import com.minisearch.model.Document;
import com.minisearch.text.Tokenizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvertedIndex {
  private final Map<String, Map<Integer, List<Integer>>> positionIndex;
  private final Map<String, Map<Integer, Integer>> contentIndex;
  private final Map<String, Map<Integer, Integer>> titleIndex;
  private final Tokenizer tokenizer;

  public InvertedIndex() {
    this.positionIndex = new HashMap<>();
    this.contentIndex = new HashMap<>();
    this.titleIndex = new HashMap<>();
    this.tokenizer = new Tokenizer();
  }

  public void addDocument(Document document) {
    List<String> contentTokens = tokenizer.tokenize(document.getContent());

    List<String> titleTokens = tokenizer.tokenize(document.getTitle());
    for (int position = 0; position < contentTokens.size(); position++) {

      String token = contentTokens.get(position);

      positionIndex
          .computeIfAbsent(token, key -> new HashMap<>())
          .computeIfAbsent(document.getId(), key -> new ArrayList<>())
          .add(position);
    }
    addTokens(contentIndex, document.getId(), contentTokens);

    addTokens(titleIndex, document.getId(), titleTokens);
  }

  private void addTokens(
      Map<String, Map<Integer, Integer>> targetIndex, int documentId, List<String> tokens) {
    for (String token : tokens) {
      targetIndex.computeIfAbsent(token, key -> new HashMap<>()).merge(documentId, 1, Integer::sum);
    }
  }

  public Map<Integer, Integer> getContentPostings(String term) {
    return contentIndex.getOrDefault(term.toLowerCase(), Map.of());
  }

  public Map<Integer, Integer> getTitlePostings(String term) {
    return titleIndex.getOrDefault(term.toLowerCase(), Map.of());
  }

  public int getContentDocumentFrequency(String term) {
    return getContentPostings(term).size();
  }

  public int getTitleDocumentFrequency(String term) {
    return getTitlePostings(term).size();
  }

  public List<Integer> getPositions(String term, int documentId) {
    return positionIndex
        .getOrDefault(term.toLowerCase(), Map.of())
        .getOrDefault(documentId, List.of());
  }
}
