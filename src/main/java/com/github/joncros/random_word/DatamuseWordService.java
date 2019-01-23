package com.github.joncros.random_word;

import datamuse.DatamuseQuery;
import datamuse.JSONParse;

import java.util.List;

/**
 * Queries the Datamuse RESTful API for matching words
 */
public class DatamuseWordService implements WordService {
    private DatamuseQuery datamuseQuery;
    private JSONParse jsonParse;

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
    public QueryResult findWordsStartingWith(String s) {
        //todo tests?
        String resultString = datamuseQuery.wordsStartingWith(s);
        String[] resultArray = jsonParse.parseWords(resultString);
        return new QueryResult(resultArray);
    }

    /**
     * Find words of a specific length starting with a specified string
     * @param s the letter(s) the words should start with
     * @param wordLength the length each matching word should be
     * @return a QueryResult holding the matching words
     */
    @Override
    public QueryResult findWordsStartingWith(String s, int wordLength) {
        //todo tests?
        int length = wordLength - s.length();
        String resultString = datamuseQuery.wordsStartingWith(s, wordLength);
        String[] resultArray = jsonParse.parseWords(resultString);
        return new QueryResult(resultArray);
    }
}
