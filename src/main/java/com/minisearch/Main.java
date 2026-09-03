package com.minisearch;

import com.minisearch.index.InvertedIndex;
import com.minisearch.model.Document;

public class Main {
  public static void main(String[] args) {
    Document doc1 = new Document(1, "Java Basics", "Java Java programming language");

    Document doc2 = new Document(2, "Web Development", "Java TypeScript programming");

    InvertedIndex index = new InvertedIndex();

    index.addDocument(doc1);
    index.addDocument(doc2);

    System.out.println(index.getPostings("java"));

    System.out.println(index.getPostings("programming"));

    System.out.println(index.getPostings("database"));
  }
}
