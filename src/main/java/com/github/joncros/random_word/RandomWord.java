package com.github.joncros.random_word;

import java.io.File;
import java.util.Scanner;
import java.util.Set;

public class RandomWord {
    private StringBuilder stringBuilder;
    private RandomLetterGenerator letterGenerator;
    private WordService wordService;
    private RandomWordUI ui;
    private int minLength;
	private int maxLength;

    /**
     * If query result size is <= SMALL, next random letter is chosen from QueryResult.charsInNthPlace
      */
	private final int SMALL = 20;

    public RandomWord(int minLength, int maxLength, WordService wordService,
                      RandomLetterGenerator generator, RandomWordUI ui) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.wordService = wordService;
        this.letterGenerator = generator;
        this.ui = ui;
        this.stringBuilder = new StringBuilder(maxLength);
    }

    public String generateWord() throws InterruptedException {
		/*
		* Generate first two letters. append them to stringBuilder and display each in ui
		*/
        char first = letterGenerator.generate();
        stringBuilder.append(first);
        ui.display(first);

        char second = letterGenerator.generate();
        stringBuilder.append(second);
        ui.display(second);

        String chosenWord = "";
        while (stringBuilder.length() < maxLength) {
            QueryResult result = wordService.findWordsStartingWith(stringBuilder.toString());
            result = result.getWordsInLengthRange(minLength, maxLength);
            if (result.getSize() == 0) {
                //replace last letter with a new letter
                char c = letterGenerator.generate();
                stringBuilder.setCharAt(stringBuilder.length()-1, c);
                ui.display(stringBuilder.toString());
            }
            else if (result.getSize() == 1) {
                chosenWord = result.getWords()[0];

                /*
                * displays the remaining characters (not displayed in previous iterrations) of the final word.
                * stringBuilder holds the letters displayed so far, so stringBuilder.length()
                * is equal to the index of the first letter not yet displayed
                 */
                ui.displayFinalWord(chosenWord, stringBuilder.length());
                break;
            }
            else if (result.getSize() <= SMALL) {
                Set<Character> possibleLetters = result.charsInNthPlace(stringBuilder.length());
                char c = letterGenerator.generate(possibleLetters);
                stringBuilder.append(c);
                ui.display(c);
            }
            else {
                char c = letterGenerator.generate();
                stringBuilder.append(c);
                ui.display(c);
            }
        }
        return chosenWord;
    }
}
