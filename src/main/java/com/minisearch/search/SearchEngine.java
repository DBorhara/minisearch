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
  // title boost = score x 2
  private static final double TITLE_BOOST = 2.0;
  // phrase bonus = score + 2
  private static final double PHRASE_BONUS = 2.0;

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

  /* Search Flow
   tokenize query
       ↓
  calculate content TF-IDF
       ↓
  calculate boosted title TF-IDF
       ↓
  check exact phrase
       ↓
  add phrase bonus
       ↓
  sort
  */
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
    // Phrase Scoring
    if (queryTerms.size() > 1) {
      for (Integer documentId : scores.keySet()) {
        Document document = documents.get(documentId);

        if (containsPhrase(document, query)) {
          scores.merge(documentId, PHRASE_BONUS, Double::sum);
        }
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

  // Phrase Detection Algorithm
  public boolean containsPhrase(Document document, String phrase) {
    List<String> terms = tokenizer.tokenize(phrase);

    if (terms.isEmpty()) {
      return false;
    }

    List<Integer> firstPositions = index.getPositions(terms.get(0), document.getId());

    for (int start : firstPositions) {
      boolean matches = true;

      for (int i = 1; i < terms.size(); i++) {
        List<Integer> positions = index.getPositions(terms.get(i), document.getId());

        if (!positions.contains(start + i)) {
          matches = false;
          break;
        }
      }

      if (matches) {
        return true;
      }
    }

    return false;
  }
}
