package com.github.joncros.random_word;

import java.util.List;
import java.util.Set;

/**
 * Holds results of a query to a WordService
 */
public class QueryResult {
    private List<String> words;

    public int getNumberOfWords() {
        //todo
        return 0;
    }

    /**
     * Returns the words contained in the QueryResult
     * @return an array holding all of the Strings in the result
     */
    public String[] getWords() {
        //todo
        return null;
    }

    /**
     * Searches this result for words starting with a string
     * @param s String that matching words should start with
     * @param wordLength length that matching words should be
     * @return
     */
    public QueryResult findWordsStartingWith(String s, int wordLength) {
        //todo

        // Create new List of Strings
        // for each String in words
            // if substring from 0 to length of s is equal to s
                // add word to new list
        return null;
    }

    /**
     * Returns a Set consisting of all of the characters occuring in the nth position of the words in the QueryResult.
     * i.e. for the words apple, orange, grape, if n = 2 the set would contain a and p
     * @param n position in each of the words to examine. n=0 examines the first letter in each word.
     * @return A set of the letters occuring in the nth position in the words, or an empty set if none of the words
     * have a letter in the nth place (that is, if n is greater than length-1 for all words in the QueryResult)
     */
    public Set<Character> charsInNthPlace(int n) {
        //todo
        return null;
    }
}
