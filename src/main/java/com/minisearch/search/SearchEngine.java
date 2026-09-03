package com.minisearch.search;

import com.minisearch.index.InvertedIndex;
import com.minisearch.model.Document;
import com.minisearch.model.SearchResult;
import com.minisearch.text.Tokenizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchEngine {

  private final InvertedIndex index;
  private final Tokenizer tokenizer;
  private final Map<Integer, Document> documents;

  public SearchEngine() {
    this.index = new InvertedIndex();
    this.tokenizer = new Tokenizer();
    this.documents = new HashMap<>();
  }

  public void addDocument(Document document) {
    documents.put(document.getId(), document);

    index.addDocument(document);
  }

  public List<SearchResult> search(String query) {
    List<String> queryTerms = tokenizer.tokenize(query);

    Map<Integer, Integer> scores = new HashMap<>();

    for (String term : queryTerms) {
      Map<Integer, Integer> postings = index.getPostings(term);

      for (Map.Entry<Integer, Integer> entry : postings.entrySet()) {
        int documentId = entry.getKey();

        int frequency = entry.getValue();

        scores.merge(documentId, frequency, Integer::sum);
      }
    }

    List<Map.Entry<Integer, Integer>> ranked = new ArrayList<>(scores.entrySet());

    // Sort rankings highests to lowest
    ranked.sort(Map.Entry.<Integer, Integer>comparingByValue().reversed());

    List<SearchResult> results = new ArrayList<>();

    for (Map.Entry<Integer, Integer> entry : ranked) {
      Document document = documents.get(entry.getKey());

      SearchResult result = new SearchResult(document, entry.getValue());

      results.add(result);
    }

    return results;
  }
}
