package com.minisearch.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RankerTest {

  @Test
  void calculatesTermFrequencyWhenTermAppearsEverywhere() {
    Ranker ranker = new Ranker();

    double score = ranker.calculateTfIdf(2, 3, 3);
    // Within 0.0001 precision
    assertEquals(2.0, score, 0.0001);
  }

  @Test
  void givesRarerTermsHigherScores() {
    Ranker ranker = new Ranker();

    double commonTerm = ranker.calculateTfIdf(1, 9, 10);

    double rareTerm = ranker.calculateTfIdf(1, 1, 10);

    assertTrue(rareTerm > commonTerm);
  }
}
