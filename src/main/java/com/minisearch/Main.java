package com.minisearch;

import com.minisearch.model.Document;
import com.minisearch.model.SearchResult;
import com.minisearch.search.SearchEngine;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    SearchEngine searchEngine = new SearchEngine();

    searchEngine.addDocument(new Document(1, "Java Tutorial", "Java programming language"));

    searchEngine.addDocument(
        new Document(2, "Programming Tutorial", "Java is a popular programming language"));

    List<SearchResult> results = searchEngine.search("java programming");

    for (SearchResult result : results) {
      System.out.printf("%s - %.3f%n", result.getDocument().getTitle(), result.getScore());
    }
  }
}
