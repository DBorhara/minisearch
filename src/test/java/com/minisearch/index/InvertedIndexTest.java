package com.minisearch.index;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.minisearch.model.Document;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class InvertedIndexTest {

  @Test
  void tracksContentTermFrequencyByDocument() {
    InvertedIndex index = new InvertedIndex();

    index.addDocument(new Document(1, "Java", "Java Java programming"));

    index.addDocument(new Document(2, "Web", "Java TypeScript"));

    assertEquals(
        // Document 1 contains "java" 2 times
        // Document 2 contains "java" 1 time
        Map.of(
            1, 2,
            2, 1),
        index.getContentPostings("java"));
  }

  @Test
  void tracksContentDocumentFrequency() {
    InvertedIndex index = new InvertedIndex();

    index.addDocument(new Document(1, "First", "Java Java"));

    index.addDocument(new Document(2, "Second", "Java programming"));

    assertEquals(2, index.getContentDocumentFrequency("java"));
  }

  @Test
  void returnsEmptyContentPostingsForUnknownTerm() {
    InvertedIndex index = new InvertedIndex();

    assertEquals(Map.of(), index.getContentPostings("database"));
  }

  @Test
  void indexesTitleTermsSeparately() {
    InvertedIndex index = new InvertedIndex();

    index.addDocument(new Document(1, "Java Basics", "Programming language"));

    assertEquals(Map.of(1, 1), index.getTitlePostings("java"));

    assertEquals(Map.of(), index.getContentPostings("java"));
  }

  @Test
  void tracksTitleDocumentFrequency() {
    InvertedIndex index = new InvertedIndex();

    index.addDocument(new Document(1, "Java Basics", "Programming language"));

    index.addDocument(new Document(2, "Advanced Java", "Object oriented programming"));

    index.addDocument(new Document(3, "Databases", "PostgreSQL database"));

    assertEquals(2, index.getTitleDocumentFrequency("java"));
  }

  @Test
  void tracksContentPositions() {
    InvertedIndex index = new InvertedIndex();

    index.addDocument(new Document(1, "Java", "Java Java programming language"));

    assertEquals(List.of(0, 1), index.getPositions("java", 1));

    assertEquals(List.of(2), index.getPositions("programming", 1));
  }
}
