package com.minisearch.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.minisearch.model.Document;
import com.minisearch.model.SearchResult;
import java.util.List;
import org.junit.jupiter.api.Test;

class SearchEngineTest {

  @Test
  void returnsMatchingDocumentsInRankedOrder() {
    SearchEngine searchEngine = new SearchEngine();

    searchEngine.addDocument(new Document(1, "Java Basics", "Java Java programming language"));

    searchEngine.addDocument(new Document(2, "Web Development", "Java TypeScript programming"));

    searchEngine.addDocument(new Document(3, "Databases", "PostgreSQL relational database"));

    List<SearchResult> results = searchEngine.search("java programming");

    assertEquals(2, results.size());

    assertEquals("Java Basics", results.get(0).getDocument().getTitle());

    assertEquals("Web Development", results.get(1).getDocument().getTitle());

    assertTrue(results.get(0).getScore() > results.get(1).getScore());
  }

  @Test
  void returnsEmptyListWhenNothingMatches() {
    SearchEngine searchEngine = new SearchEngine();

    searchEngine.addDocument(new Document(1, "Java Basics", "Java programming language"));

    List<SearchResult> results = searchEngine.search("elephant");

    assertTrue(results.isEmpty());
  }
}
