package com.github.joncros.random_word;

import java.util.*;

/**
 * Holds results of a query to a WordService
 */
public class QueryResult {
    private List<String> words;

    public QueryResult(List<String> words) {
        // todo handle bad parameters
        this.words = words;
    }

    public QueryResult(String[] words) {
        // todo handle bad parameters
        this.words = new ArrayList<>(Arrays.asList(words));
    }

    public int getNumberOfWords() {
        return words.size();
    }

    /**
     * Returns the words contained in the QueryResult
     * @return an array holding all of the Strings in the result
     */
    public String[] getWords() {
        return (String[]) words.toArray();
    }

    /**
     * Searches this result for words starting with a string
     * @param s String that matching words should start with
     * @return a new QueryResult containing all matching words
     */
    public QueryResult findWordsStartingWith(String s) {
        //todo tests
        List<String> newList = new ArrayList<>();
        int length = s.length();
        for (String word : words) {
            if (word.length() >= s.length()) {
                String subStr = word.substring(0,length);
                if (subStr.equals(s))
                    newList.add(word);
            }
        }

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
        //todo tests
        Set<Character> chars = new HashSet<>();
        for (String word : words) {
            if (n < word.length())
                chars.add(word.charAt(n));
        }
        return chars;
    }
}
