package com.github.joncros.random_word;

import java.util.List;
import java.util.Set;

public class RandomWord {
    private StringBuilder stringBuilder;
    private RandomLetterGenerator letterGenerator;
    private WordListService wordListService;
    private RandomWordUI ui;

    public RandomWord(int maxLength, WordListService wordListService, RandomWordUI ui) {

    }

    /**
     * Utility method to find all of the characters located in the nth place in each word from a list of words
     * @param words List of words to examine
     * @param n position to consider for each of the words, i.e. 0 for the first letter of each word
     * @return a Set of Characters
     */
    public static Set<Character> charsInNthPlace(List<String> words, int n) {

    }
}
