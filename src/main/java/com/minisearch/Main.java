package com.minisearch;

import com.minisearch.model.Document;

public class Main {
  public static void main(String[] args) {
    Document document = new Document(1, "Java Search", "minisearch is a WIP");

    System.out.println(document.getTitle());
    System.out.println(document.getContent());
  }
}
