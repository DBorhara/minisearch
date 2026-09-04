package com.minisearch.index;

import com.minisearch.model.Document;
import com.minisearch.text.Tokenizer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvertedIndex {
  // {"term":{document_id:frequency_of_term}}
  private final Map<String, Map<Integer, Integer>> index;
  private final Tokenizer tokenizer;

  public InvertedIndex() {
    this.index = new HashMap<>();
    this.tokenizer = new Tokenizer();
  }

  public void addDocument(Document document) {
    List<String> tokens = tokenizer.tokenize(document.getContent());
    // If "term" isn't already in the map,
    // create an empty map for it. {"term":{}}
    // Then add 1 to the document's count {document_id:frequency_of_terterm +1}
    for (String token : tokens) {
      index.computeIfAbsent(token, key -> new HashMap<>()).merge(document.getId(), 1, Integer::sum);
    }
  }

  // Which documents contain this "term"?
  public Map<Integer, Integer> getPostings(String term) {
    return index.getOrDefault(term.toLowerCase(), Map.of());
  }

  public int getDocumentFrequency(String term) {
    return getPostings(term).size();
  }
}
