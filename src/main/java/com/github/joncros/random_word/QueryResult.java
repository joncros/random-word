package com.github.joncros.random_word;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Holds results of a query to a WordService
 */
public class QueryResult {
    private List<String> words;
    //private Map<Integer,String> wordsByLength;

    public QueryResult(List<String> words) {
        Objects.requireNonNull(words);
        this.words = words;
    }

    public QueryResult(String[] words) {
        Objects.requireNonNull(words);
        this.words = new ArrayList<>(Arrays.asList(words));
    }

    public int getSize() {
        return words.size();
    }

    /**
     * Returns the words contained in the QueryResult
     * @return an array holding all of the Strings in the result
     */
    public String[] getWords() {
        return words.toArray(new String[0]);
    }

    /**
     * Gets the words in the result that have a certain length
     * @param length length of each of the words that should be included
     * @return an array of words
     */
    public QueryResult getWords(int length) {
        if (length < 1)
            throw new IllegalArgumentException("length must be at least 1");
        List<String> newList = words.stream()
                .filter(word -> word.length() == length)
                .collect(Collectors.toList());
        return new QueryResult(newList);
    }

    /**
     * Finds the words in a QueryResult that fall within a range of lengths
     * @param minLength minimum length of words to include
     * @param maxLength maximum length of words to include
     * @return a new QueryResult containing the words
     */
    public QueryResult getWordsInLengthRange(int minLength, int maxLength) {
        if (minLength < 1)
            throw new IllegalArgumentException("minLength must be at least 1");
        if (minLength > maxLength)
            throw new IllegalArgumentException("minLength cannot be greater than maxLength");
        List<String> newList = words.stream()
                .filter(word -> word.length() <= maxLength)
                .filter(word -> word.length() >= minLength)
                .collect(Collectors.toList());
        return new QueryResult(newList);
    }

    /**
     * Finds the words in a QueryResult whose length does not exceed a limit
     * @param maxLength maximum length of words to include
     * @return a new QueryResult containing the words
     */
    public QueryResult getWordsWithMaxLength(int maxLength) {
        if (maxLength < 1)
            throw new IllegalArgumentException("maxLength must be at least 1");
        List<String> newList = words.stream()
                .filter(word -> word.length() <= maxLength)
                .collect(Collectors.toList());
        return new QueryResult(newList);
    }

    /**
     * Searches this result for words starting with a string
     * @param s String that matching words should start with
     * @return a new QueryResult containing all matching words
     */
    public QueryResult findWordsStartingWith(String s) {
        Objects.requireNonNull(s);
        int length = s.length();
        if (length == 0)
            throw new IllegalArgumentException("parameter s must have length of at least 1");
        List<String> newList;

        /*
         * Loop implementation
         * newList = new ArrayList<>();
        for (String word : words) {
            if (word.length() >= length) {
                String subStr = word.substring(0,length);
                if (subStr.equals(s))
                    newList.add(word);
            }
        }
         */

        newList = words.stream()
                .filter(word -> word.length() >= length)
                .filter(word -> word.substring(0, length).equals(s))
                .collect(Collectors.toList());

        return new QueryResult(newList);
    }

    /**
     * Returns a Set consisting of all of the characters occuring in the nth position of the words in the QueryResult.
     * i.e. for the words apple, orange, grape, if n = 2 the set would contain a and p
     * @param n position in each of the words to examine. n=0 examines the first letter in each word.
     * @return A set of the letters occuring in the nth position in the words, or an empty set if none of the words
     * have a letter in the nth place (that is, if n is greater than length-1 for all words in the QueryResult)
     */
    public Set<Character> charsInNthPlace(int n) {
        if (n < 0)
            throw new IllegalArgumentException("n cannot be less than 0");
        Set<Character> chars = new HashSet<>();
        for (String word : words) {
            if (n < word.length())
                chars.add(word.charAt(n));
        }
        return chars;
    }
}
