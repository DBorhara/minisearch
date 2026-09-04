package com.minisearch.search;

public class Ranker {
  // Inverse Document Freq (IDF) =
  // log((total # of documents +1)/ (total # of documents term appears in +1))+1
  // +1's for smoothing(protection against divide by 0)
  // tfidf = Term Frequency * Inverse Document Frequency
  public double calculateTfIdf(int termFrequency, int documentFrequency, int totalDocuments) {
    double idf = Math.log((double) (totalDocuments + 1) / (documentFrequency + 1)) + 1.0;

    return termFrequency * idf;
  }
}
