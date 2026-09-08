package com.minisearch.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

  @Test
  void boostsTitleMatches() {
    SearchEngine searchEngine = new SearchEngine();

    searchEngine.addDocument(new Document(1, "Java Guide", "Programming basics"));

    searchEngine.addDocument(new Document(2, "Programming Guide", "Java basics"));

    List<SearchResult> results = searchEngine.search("java");

    assertEquals(2, results.size());

    assertEquals(1, results.get(0).getDocument().getId());

    assertTrue(results.get(0).getScore() > results.get(1).getScore());
  }

  @Test
  void detectsExactPhrase() {
    SearchEngine searchEngine = new SearchEngine();

    Document document = new Document(1, "Java", "Java programming language");

    searchEngine.addDocument(document);

    assertTrue(searchEngine.containsPhrase(document, "java programming"));
  }

  @Test
  void rejectsWordsThatAreNotAdjacent() {
    SearchEngine searchEngine = new SearchEngine();

    Document document = new Document(1, "Java", "Java is a programming language");

    searchEngine.addDocument(document);

    assertFalse(searchEngine.containsPhrase(document, "java programming"));
  }

  @Test
  void boostsExactPhraseMatches() {
    SearchEngine searchEngine = new SearchEngine();

    searchEngine.addDocument(new Document(1, "First Document", "Java programming language"));

    searchEngine.addDocument(new Document(2, "Second Document", "Java is a programming language"));

    List<SearchResult> results = searchEngine.search("java programming");

    assertEquals(2, results.size());

    assertEquals(1, results.get(0).getDocument().getId());

    assertTrue(results.get(0).getScore() > results.get(1).getScore());
  }
}
