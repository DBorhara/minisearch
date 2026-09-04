package com.minisearch.index;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.minisearch.model.Document;
import java.util.Map;
import org.junit.jupiter.api.Test;

class InvertedIndexTest {

  @Test
  void tracksTermFrequencyByDocument() {
    InvertedIndex index = new InvertedIndex();

    index.addDocument(new Document(1, "Java", "Java Java programming"));

    index.addDocument(new Document(2, "Web", "Java TypeScript"));

    assertEquals(
        Map.of(
            1, 2,
            2, 1),
        index.getPostings("java"));
  }

  @Test
  void tracksDocumentFrequency() {
    InvertedIndex index = new InvertedIndex();

    index.addDocument(new Document(1, "First", "Java Java"));

    index.addDocument(new Document(2, "Second", "Java programming"));

    assertEquals(2, index.getDocumentFrequency("java"));
  }

  @Test
  void returnsEmptyPostingsForUnknownTerm() {
    InvertedIndex index = new InvertedIndex();

    assertEquals(Map.of(), index.getPostings("database"));
  }
}
