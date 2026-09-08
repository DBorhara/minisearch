# MiniSearch

MiniSearch is a Java-based search engine that indexes text documents and returns ranked search results using an inverted index, TF-IDF scoring, title weighting, positional indexing, and phrase-aware ranking.

The project is designed to demonstrate core information retrieval concepts, Java data structures, object-oriented design, file I/O, algorithmic ranking, and automated testing.

## Features

- Loads and indexes `.txt` documents from a directory
- Tokenizes and normalizes document content
- Builds an inverted index for efficient term lookup
- Tracks term frequency by document
- Calculates document frequency for ranking
- Ranks search results using TF-IDF
- Applies additional weighting to title matches
- Tracks token positions within documents
- Detects exact phrase matches
- Boosts phrase matches in search rankings
- Generates contextual result snippets
- Provides an interactive command-line search interface
- Includes automated JUnit tests

## Tech Stack

- Java 21
- Maven
- JUnit 5
- Java Collections Framework
- Java NIO file APIs

## Architecture

```text
Text Documents
      |
      v
DocumentLoader
      |
      v
Document Objects
      |
      v
Tokenizer
      |
      v
InvertedIndex
      |
      +-------------------+
      |                   |
      v                   v
Content Index       Title Index
      |
      v
Positional Index
      |
      v
SearchEngine
      |
      v
TF-IDF Ranker
      |
      v
Phrase + Title Boosting
      |
      v
SearchResult
      |
      v
SnippetGenerator
      |
      v
CLI Output
```

## Project Structure

```text
minisearch/
├── documents/
│   ├── java.txt
│   ├── postgresql.txt
│   └── typescript.txt
│
├── src/
│   ├── main/
│   │   └── java/com/minisearch/
│   │       ├── Main.java
│   │       ├── index/
│   │       │   └── InvertedIndex.java
│   │       ├── io/
│   │       │   └── DocumentLoader.java
│   │       ├── model/
│   │       │   ├── Document.java
│   │       │   └── SearchResult.java
│   │       ├── search/
│   │       │   ├── Ranker.java
│   │       │   ├── SearchEngine.java
│   │       │   └── SnippetGenerator.java
│   │       └── text/
│   │           └── Tokenizer.java
│   │
│   └── test/
│       └── java/com/minisearch/
│           ├── index/
│           ├── io/
│           ├── search/
│           └── text/
│
├── .gitignore
├── pom.xml
└── README.md
```

## How Search Works

MiniSearch follows a multi-stage search pipeline.

### 1. Document Loading

`DocumentLoader` reads `.txt` files from the `documents/` directory and converts each file into a `Document`.

Each document contains:

```text
id
title
content
```

The filename is used as the document title.

For example:

```text
java.txt
```

becomes a document with the title:

```text
java
```

### 2. Tokenization

The `Tokenizer` converts document text into normalized search terms.

For example:

```text
Java, Java! Programming language.
```

becomes:

```text
[java, java, programming, language]
```

The tokenizer:

- Converts text to lowercase
- Removes punctuation
- Splits text on whitespace
- Removes empty tokens

Duplicate tokens are intentionally preserved because term frequency is used during ranking.

## Inverted Index

MiniSearch uses an inverted index rather than scanning every document for every query.

Instead of storing:

```text
Document 1 -> java, programming, language
Document 2 -> java, typescript
```

the index stores:

```text
java
  -> Document 1: 1 occurrence
  -> Document 2: 1 occurrence

programming
  -> Document 1: 1 occurrence

typescript
  -> Document 2: 1 occurrence
```

This allows the search engine to quickly identify which documents contain a given term.

## TF-IDF Ranking

Search results are ranked using TF-IDF.

TF-IDF combines two measurements:

```text
TF = Term Frequency
IDF = Inverse Document Frequency
```

Term frequency measures how often a term appears within a document.

Inverse document frequency gives more weight to terms that appear in fewer documents.

MiniSearch calculates a smoothed IDF value using:

```text
IDF = log((N + 1) / (DF + 1)) + 1
```

where:

```text
N  = total number of documents
DF = number of documents containing the term
```

The final term score is:

```text
TF-IDF = TF * IDF
```

Scores for multiple query terms are combined to produce the document's search score.

## Title-Aware Ranking

MiniSearch maintains separate indexes for document titles and document content.

Matches in a document title receive additional weight because a title match is often more relevant than the same term appearing only in the document body.

For example, a search for:

```text
java
```

may rank:

```text
Java Guide
```

above a document that only mentions Java once in its body.

## Positional Indexing

The search engine also records the positions at which terms occur.

For the text:

```text
java programming language
```

positions are stored approximately as:

```text
java        -> 0
programming -> 1
language    -> 2
```

This enables MiniSearch to determine whether multiple query terms appear next to each other.

## Phrase-Aware Ranking

For a query such as:

```text
java programming
```

MiniSearch can distinguish between:

```text
Java programming language
```

and:

```text
Java is a popular programming language
```

The first document contains the exact phrase and receives an additional ranking bonus.

## Search Result Snippets

MiniSearch generates a contextual snippet for each result.

Instead of displaying only:

```text
java - score: 5.432
```

the CLI can display:

```text
1. java - score: 5.432
   Java is a popular programming language used for backend systems...
```

For longer documents, the snippet attempts to center the displayed text around a matching query term.

## Interactive CLI

Run MiniSearch and enter queries directly from the terminal.

Example:

```text
Indexed 3 documents.

Search (or type 'exit'): java programming

1. java - score: 7.243
   Java is a popular programming language. Java supports object oriented...

2. typescript - score: 1.288
   TypeScript is a programming language built on JavaScript...

Search (or type 'exit'): database

1. postgresql - score: 4.575
   PostgreSQL is a relational database management system...

Search (or type 'exit'): exit

Goodbye!
```

Exact scores depend on the indexed document collection.

## Running the Project

### Prerequisites

Install:

- Java 21 or newer
- Maven

Verify:

```bash
java -version
mvn -version
```

### Clone the Repository

```bash
git clone https://github.com/DBorhara/minisearch.git
cd minisearch
```

### Compile

```bash
mvn compile
```

### Run

```bash
mvn exec:java
```

MiniSearch automatically loads `.txt` files from:

```text
documents/
```

## Adding Documents

Add additional `.txt` files to the `documents/` directory.

For example:

```text
documents/
├── algorithms.txt
├── databases.txt
├── java.txt
└── networking.txt
```

Restart MiniSearch to index the updated collection.

## Testing

MiniSearch includes automated JUnit tests covering major components of the search pipeline.

Tests include:

- Text tokenization
- Text normalization
- Inverted index term frequencies
- Document frequency
- Title indexing
- Positional indexing
- TF-IDF calculations
- Ranking behavior
- Title boosting
- Exact phrase detection
- Phrase ranking
- Document ingestion
- File filtering
- Empty search results
- Search result snippets

Run all tests with:

```bash
mvn test
```

For a clean build and test run:

```bash
mvn clean test
```

## Key Concepts Demonstrated

This project applies several computer science and software engineering concepts:

- Inverted indexes
- Hash-based lookup
- Nested maps
- Lists and collections
- Term frequency
- Inverse document frequency
- TF-IDF ranking
- Positional indexes
- Phrase matching
- Ranking heuristics
- Object-oriented design
- File I/O
- Java streams
- Resource management
- Maven project structure
- Unit testing with JUnit

## Future Improvements

Potential extensions include:

- Multithreaded document indexing
- Query result caching
- Stop-word filtering
- Word stemming or lemmatization
- BM25 ranking
- Boolean query operators
- Quoted phrase syntax
- Persistent indexes
- Larger document collections
- Benchmarking and performance measurement
- REST API or web interface

## What I Learned

Building MiniSearch provided hands-on experience with:

- Designing search-oriented data structures
- Implementing an inverted index in Java
- Applying TF-IDF to information retrieval
- Separating title and content indexes
- Building positional indexes for phrase matching
- Designing ranking heuristics
- Processing documents using Java NIO
- Structuring a multi-package Java application
- Writing automated tests with JUnit
- Building an interactive command-line application
