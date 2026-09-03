package com.minisearch.model;

public class SearchResult {

  private final Document document;
  private final int score;

  public SearchResult(Document document, int score) {
    this.document = document;
    this.score = score;
  }

  public Document getDocument() {
    return document;
  }

  public int getScore() {
    return score;
  }
}
