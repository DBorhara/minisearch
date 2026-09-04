package com.minisearch;

import com.minisearch.model.Document;
import com.minisearch.model.SearchResult;
import com.minisearch.search.SearchEngine;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    SearchEngine searchEngine = new SearchEngine();

    searchEngine.addDocument(new Document(1, "Java Basics", "Java Java programming language"));

    searchEngine.addDocument(new Document(2, "Web Development", "Java TypeScript programming"));

    searchEngine.addDocument(new Document(3, "Databases", "PostgreSQL relational database"));

    List<SearchResult> results = searchEngine.search("java programming");

    for (SearchResult result : results) {
      System.out.printf("%s - score: %.3f%n", result.getDocument().getTitle(), result.getScore());
    }
  }
}
