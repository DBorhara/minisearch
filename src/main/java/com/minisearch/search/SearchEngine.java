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
      Map<Integer, Integer> postings = index.getPostings(term);

      for (Map.Entry<Integer, Integer> entry : postings.entrySet()) {
        int documentId = entry.getKey();

        int termFrequency = entry.getValue();

        int documentFrequency = index.getDocumentFrequency(term);
        // idf = (log(documents.size()+1/documentFrequency+1)+1)
        // tdIdf = idf * termFrequency
        double tfIdf = ranker.calculateTfIdf(termFrequency, documentFrequency, documents.size());
        scores.merge(documentId, tfIdf, Double::sum);
      }
    }

    List<Map.Entry<Integer, Double>> ranked = new ArrayList<>(scores.entrySet());

    // Sort rankings highests to lowest
    ranked.sort(Map.Entry.<Integer, Double>comparingByValue().reversed());

    List<SearchResult> results = new ArrayList<>();

    for (Map.Entry<Integer, Double> entry : ranked) {
      Document document = documents.get(entry.getKey());

      SearchResult result = new SearchResult(document, entry.getValue());

      results.add(result);
    }

    return results;
  }
}
