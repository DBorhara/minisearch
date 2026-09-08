package com.minisearch.io;

import com.minisearch.model.Document;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DocumentLoader {

  public List<Document> loadDocuments(Path directory) throws IOException {

    List<Path> files;

    try (var paths = Files.list(directory)) {
      files =
          paths
              .filter(Files::isRegularFile)
              .filter(path -> path.toString().endsWith(".txt"))
              .sorted(Comparator.comparing(path -> path.getFileName().toString()))
              .toList();
    }

    List<Document> documents = new ArrayList<>();

    int id = 1;

    for (Path file : files) {
      String content = Files.readString(file);

      String title = file.getFileName().toString().replaceFirst("\\.txt$", "");

      documents.add(new Document(id, title, content));

      id++;
    }

    return documents;
  }
}
