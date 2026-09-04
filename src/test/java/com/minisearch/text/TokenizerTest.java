package com.minisearch.text;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class TokenizerTest {

  @Test
  void tokenizesAndNormalizesText() {
    Tokenizer tokenizer = new Tokenizer();

    List<String> tokens = tokenizer.tokenize("Java, Java! Programming language.");

    assertEquals(List.of("java", "java", "programming", "language"), tokens);
  }

  @Test
  void returnsEmptyListForEmptyText() {
    Tokenizer tokenizer = new Tokenizer();

    assertEquals(List.of(), tokenizer.tokenize(""));
  }
}
