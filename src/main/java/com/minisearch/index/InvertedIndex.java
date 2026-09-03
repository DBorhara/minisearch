package com.minisearch.index;

import com.minisearch.model.Document;
import com.minisearch.text.Tokenizer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvertedIndex {
  // {"word":{document_id:frequency_of_word}}
  private final Map<String, Map<Integer, Integer>> index;
  private final Tokenizer tokenizer;

  public InvertedIndex() {
    this.index = new HashMap<>();
    this.tokenizer = new Tokenizer();
  }

  public void addDocument(Document document) {
    List<String> tokens = tokenizer.tokenize(document.getContent());
    // If "word" isn't already in the map,
    // create an empty map for it. {"word":{}}
    // Then add 1 to the document's count {document_id:frequency_of_word +1}
    for (String token : tokens) {
      index.computeIfAbsent(token, key -> new HashMap<>()).merge(document.getId(), 1, Integer::sum);
    }
  }

  // Which documents contain this "word"?
  public Map<Integer, Integer> getPostings(String word) {
    return index.getOrDefault(word.toLowerCase(), Map.of());
  }
}
