package com.github.joncros.random_word.core;

import java.io.IOException;

/**
 * Interface for querying an (online or offline) word list for matching words
 */
public interface WordService {

    /**
     * Find words starting with a specified string
     * @param s the letter(s) the words should start with
     * @return a QueryResult holding the matching words
     */
    QueryResult findWordsStartingWith(String s) throws IOException;

    /**
     * Find words of a specific length starting with a specified string
     * @param s the letter(s) the words should start with
     * @param wordLength the length each matching word should be
     * @return a QueryResult holding the matching words
     */
    QueryResult findWordsStartingWith(String s, int wordLength) throws IOException;
}
