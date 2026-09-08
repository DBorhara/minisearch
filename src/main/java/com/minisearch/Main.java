package com.minisearch;

import com.minisearch.io.DocumentLoader;
import com.minisearch.model.Document;
import com.minisearch.model.SearchResult;
import com.minisearch.search.SearchEngine;
import java.nio.file.Path;
import java.util.List;

public class Main {

  public static void main(String[] args) throws Exception {
    /*
     documents/
        ↓
    DocumentLoader
        ↓
    List<Document>
        ↓
    SearchEngine
        ↓
    InvertedIndex
        ↓
    search results
    */

    DocumentLoader loader = new DocumentLoader();

    List<Document> documents = loader.loadDocuments(Path.of("documents"));

    SearchEngine searchEngine = new SearchEngine();

    for (Document document : documents) {
      searchEngine.addDocument(document);
    }

    List<SearchResult> results = searchEngine.search("java programming");

    for (SearchResult result : results) {
      System.out.printf("%s - score: %.3f%n", result.getDocument().getTitle(), result.getScore());
    }
  }
}
