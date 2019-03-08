package com.github.joncros.random_word.core;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Set;

public class RandomWord {
    private ObservableList<Character> chars;
    private RandomLetterGenerator letterGenerator;
    private WordService wordService;
    private int minLength;
	private int maxLength;

    /**
     * If query result size is <= SMALL, next random letter is chosen from QueryResult.charsInNthPlace
      */
	private final int SMALL = 20;

    public RandomWord(int minLength, int maxLength, WordService wordService,
                      RandomLetterGenerator generator) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.wordService = wordService;
        this.letterGenerator = generator;
        this.chars = FXCollections.observableArrayList();
    }

    /**
     * Returns an {@code ObservableList<Character>} holding the letters chosen so far
     * @return an unmodifiable ObservableList
     */
    public ObservableList<Character> characterList() {
        return FXCollections.unmodifiableObservableList(chars);
    }

    /**
     * Returns the generated word at this point
     * @return A String representing the entire word so far
     */
    public String curentWord() {
        int size = chars.size();
        char[] c_array = new char[size];
        for (int i = 0; i < size; i++) {
            c_array[i] = chars.get(i);
        }
        return String.valueOf(c_array);
    }

    public String generateWord() throws IOException {
		/*
		* Generate first two letters.
		*/
		int current = 0; //index of next char to add to chars
        char first = letterGenerator.generate();
        chars.add(current++, first);

        char second = letterGenerator.generate();
        chars.add(current++, second);

        QueryResult result = wordService.findWordsStartingWith(curentWord())
                .getWordsInLengthRange(minLength, maxLength);

        while (current < maxLength) {
            if (result.getSize() == 0) {
                //replace last letter with a new letter, assign new word list to result
                char c = letterGenerator.generate();
                chars.set(--current, c);
                result = wordService.findWordsStartingWith(curentWord())
                        .getWordsInLengthRange(minLength, maxLength);
            }
            else if (result.getSize() == 1) {
                String chosen = result.getWords()[0];
                for (int i = current; i < chosen.length(); i++) {
                    chars.add(i, chosen.charAt(i));
                }
                break;
            }
            else if (result.getSize() <= SMALL) {
                Set<Character> possibleLetters = result.charsInNthPlace(current);
                char c = letterGenerator.generate(possibleLetters);
                chars.add(current, c);
                result = result.findWordsWithLetter(c, current);
            }
            else {
                char c = letterGenerator.generate();
                chars.add(current, c);
                result = result.findWordsWithLetter(c, current);
            }

            current++;
        }
        return curentWord();
    }
}
