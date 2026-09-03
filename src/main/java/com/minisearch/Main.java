package com.minisearch;

import com.minisearch.text.Tokenizer;

public class Main {
  public static void main(String[] args) {
    Tokenizer tokenizer = new Tokenizer();

    System.out.println(tokenizer.tokenize("Hello, World! This is java?"));
  }
}
