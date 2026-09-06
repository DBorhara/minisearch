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
  private final Ranker ranker;
  private static final double TITLE_BOOST = 2.0;

  public SearchEngine() {
    this.index = new InvertedIndex();
    this.tokenizer = new Tokenizer();
    this.documents = new HashMap<>();
    this.ranker = new Ranker();
  }

  public void addDocument(Document document) {
    documents.put(document.getId(), document);

    index.addDocument(document);
  }

  public List<SearchResult> search(String query) {
    List<String> queryTerms = tokenizer.tokenize(query);

    Map<Integer, Double> scores = new HashMap<>();

    for (String term : queryTerms) {

      Map<Integer, Integer> contentPostings = index.getContentPostings(term);

      int contentDocumentFrequency = index.getContentDocumentFrequency(term);

      for (Map.Entry<Integer, Integer> entry : contentPostings.entrySet()) {
        double tfIdf =
            ranker.calculateTfIdf(entry.getValue(), contentDocumentFrequency, documents.size());

        scores.merge(entry.getKey(), tfIdf, Double::sum);
      }

      Map<Integer, Integer> titlePostings = index.getTitlePostings(term);

      int titleDocumentFrequency = index.getTitleDocumentFrequency(term);

      for (Map.Entry<Integer, Integer> entry : titlePostings.entrySet()) {
        double tfIdf =
            ranker.calculateTfIdf(entry.getValue(), titleDocumentFrequency, documents.size());

        double boostedScore = tfIdf * TITLE_BOOST;

        scores.merge(entry.getKey(), boostedScore, Double::sum);
      }
    }

    List<Map.Entry<Integer, Double>> ranked = new ArrayList<>(scores.entrySet());

    ranked.sort(Map.Entry.<Integer, Double>comparingByValue().reversed());

    List<SearchResult> results = new ArrayList<>();

    for (Map.Entry<Integer, Double> entry : ranked) {
      Document document = documents.get(entry.getKey());

      results.add(new SearchResult(document, entry.getValue()));
    }

    return results;
  }
}
