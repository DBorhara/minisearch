package com.minisearch.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.minisearch.model.Document;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class DocumentLoaderTest {

  @TempDir Path tempDirectory;

  @Test
  void loadsTextFilesAsDocuments() throws Exception {

    Files.writeString(tempDirectory.resolve("java.txt"), "Java programming language");

    Files.writeString(tempDirectory.resolve("database.txt"), "PostgreSQL relational database");

    DocumentLoader loader = new DocumentLoader();

    List<Document> documents = loader.loadDocuments(tempDirectory);

    assertEquals(2, documents.size());

    assertEquals("database", documents.get(0).getTitle());

    assertEquals("java", documents.get(1).getTitle());
  }

  @Test
  void ignoresNonTextFiles() throws Exception {

    Files.writeString(tempDirectory.resolve("java.txt"), "Java programming");

    Files.writeString(tempDirectory.resolve("image.png"), "Not really an image");

    DocumentLoader loader = new DocumentLoader();

    List<Document> documents = loader.loadDocuments(tempDirectory);

    assertEquals(1, documents.size());

    assertEquals("java", documents.get(0).getTitle());
  }
}
