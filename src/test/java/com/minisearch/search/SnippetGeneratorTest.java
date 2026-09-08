package com.minisearch.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.minisearch.model.Document;
import org.junit.jupiter.api.Test;

class SnippetGeneratorTest {

  @Test
  void returnsEntireShortDocument() {
    SnippetGenerator generator = new SnippetGenerator();

    Document document = new Document(1, "Java", "Java programming language");

    String snippet = generator.generate(document, "java");

    assertEquals("Java programming language", snippet);
  }

  @Test
  void createsSnippetAroundMatchingTerm() {
    SnippetGenerator generator = new SnippetGenerator();

    String content =
        "This is a long introduction about software "
            + "development and computer science. "
            + "Java programming is widely used for "
            + "backend applications and enterprise systems.";

    Document document = new Document(1, "Java", content);

    String snippet = generator.generate(document, "java");

    assertTrue(snippet.toLowerCase().contains("java"));

    assertTrue(snippet.length() <= 106);
  }

  @Test
  void returnsBeginningWhenNoQueryTermMatches() {
    SnippetGenerator generator = new SnippetGenerator();

    String content =
        "This document contains a large amount "
            + "of information about Java programming "
            + "and backend development. "
            + "There is even more information here "
            + "so the document is longer than the "
            + "configured snippet size.";

    Document document = new Document(1, "Java", content);

    String snippet = generator.generate(document, "elephant");

    assertTrue(snippet.startsWith("This document"));

    assertTrue(snippet.endsWith("..."));
  }
}
