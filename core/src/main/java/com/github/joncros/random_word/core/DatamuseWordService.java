package com.github.joncros.random_word.core;

import datamuse.DatamuseQuery;
import datamuse.JSONParse;

import java.io.IOException;
import java.util.Objects;

/**
 * Queries the Datamuse RESTful API for matching words
 */
public class DatamuseWordService implements WordService {
    private DatamuseQuery datamuseQuery;
    private JSONParse jsonParse;
    private final String[] EMPTY_ARRAY = {};

    public DatamuseWordService() {
        datamuseQuery = new DatamuseQuery(1000);
        jsonParse = new JSONParse();
    }

    /**
     * Find words starting with a specified string
     * @param s the letter(s) the words should start with
     * @return a QueryResult holding the matching words
     */
    @Override
    public QueryResult findWordsStartingWith(String s) throws IOException {
        //todo tests?
        Objects.requireNonNull(s);
        String resultString = datamuseQuery.wordsStartingWith(s);
        if (resultString == null) {
            throw new IOException("DataMuse service not available");
        }
        String[] resultArray = EMPTY_ARRAY;
        if (!resultString.equals("[]")) {   //if wordsStartingWith did not return an empty JSON result
            resultArray = jsonParse.parseWords(resultString);
        }
        return new QueryResult(resultArray);
    }

    /**
     * Find words of a specific length starting with a specified string
     * @param s the letter(s) the words should start with
     * @param wordLength the length each matching word should be, greater than or equal to 1 and greater
     *                   than or equal to s.length()
     * @return a QueryResult holding the matching words
     */
    @Override
    public QueryResult findWordsStartingWith(String s, int wordLength) throws IOException{
        //todo tests?
        Objects.requireNonNull(s);
        if (wordLength < 1) {
            throw new IllegalArgumentException("wordLength less than one");
        }
        if (wordLength < s.length()) {
            throw new IllegalArgumentException("wordLength is less than length of s");
        }
        int length = wordLength - s.length();
        String resultString = datamuseQuery.wordsStartingWith(s, wordLength);
        if (resultString == null) {
            throw new IOException("DataMuse service not available");
        }
        String[] resultArray = EMPTY_ARRAY;
        if (!resultString.equals("[]")) {   //if wordsStartingWith did not return an empty JSON result
            resultArray = jsonParse.parseWords(resultString);
        }
        return new QueryResult(resultArray);
    }
}
