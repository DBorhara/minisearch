package com.minisearch;

import com.minisearch.io.DocumentLoader;
import com.minisearch.model.Document;
import com.minisearch.model.SearchResult;
import com.minisearch.search.SearchEngine;
import com.minisearch.search.SnippetGenerator;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) throws Exception {

    DocumentLoader loader = new DocumentLoader();

    List<Document> documents = loader.loadDocuments(Path.of("documents"));

    SearchEngine searchEngine = new SearchEngine();

    for (Document document : documents) {
      searchEngine.addDocument(document);
    }

    System.out.println("Indexed " + documents.size() + " documents.");

    try (Scanner scanner = new Scanner(System.in)) {

      while (true) {
        System.out.print("\nSearch (or type 'exit'): ");

        String query = scanner.nextLine().trim();

        if (query.equalsIgnoreCase("exit")) {
          break;
        }

        if (query.isBlank()) {
          continue;
        }

        List<SearchResult> results = searchEngine.search(query);

        if (results.isEmpty()) {
          System.out.println("No results found.");

          continue;
        }

        SnippetGenerator snippetGenerator = new SnippetGenerator();

        for (int i = 0; i < results.size(); i++) {
          SearchResult result = results.get(i);
          Document document = result.getDocument();

          String snippet = snippetGenerator.generate(document, query);

          System.out.printf(
              "%d. %s - score: %.3f%n", i + 1, document.getTitle(), result.getScore());

          System.out.println("   " + snippet);
        }
      }
    }

    System.out.println("Goodbye!");
  }
}
