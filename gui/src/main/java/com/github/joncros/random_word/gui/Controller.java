package com.github.joncros.random_word.gui;

import com.github.joncros.random_word.core.RandomWord;
import eu.hansolo.fx.spinner.CanvasSpinner;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

class Controller {
    //Number of milliseconds it should take a Spinner to spin to an adjacent value
    private static final double ANIMATION_RATE = 250;

    private RandomWord randomWord;
    private ObservableList<Character> charList;
    private String word;

    private List<SpinnerWrapper> spinnerWrappers;
    private int numSpinners;

    /*
    Queue that holds the index values for SpinnerWrappers that have a new target value,
    in the order that changes were reported by randomWordListener
    */
    private Queue<SpinnerWrapper> spinnersWithTargets;

    private ListChangeListener<Character> randomWordListener = change -> {
        while (change.next()) {
            for (int i = change.getFrom(); i < change.getTo(); i++) {
                Character c = charList.get(i);
                SpinnerWrapper spinnerWrapper = spinnerWrappers.get(i);
                spinnerWrapper.addTargetValue(convert(c));
                if (spinnersWithTargets.isEmpty()) {
                    spinnerWrapper.spinToTarget();
                }
                spinnersWithTargets.add(spinnerWrapper);
            }
        }
    };

    Controller(RandomWord randomWord, List<CanvasSpinner> spinners) {
        this.randomWord = randomWord;
        this.word = null;
        spinnersWithTargets = new ArrayDeque<>();
        numSpinners = spinners.size();

        spinnerWrappers = new ArrayList<>();
        for (int i = 0; i < spinners.size(); i++) {
            CanvasSpinner spinner = spinners.get(i);
            spinnerWrappers.add(new SpinnerWrapper(this, spinner, i, ANIMATION_RATE));
        }

        charList = randomWord.characterList();
        charList.addListener(randomWordListener);
    }

    /**
     * For a SpinnerWrapper to call whenever its Status is SPINNING_TO_TARGET and the spinner reaches
     * its target.
     */
    void onSpinnerTargetReached(SpinnerWrapper spinnerWrapper) {
        spinnersWithTargets.remove();
        SpinnerWrapper nextWrapper = spinnersWithTargets.peek();
        if (nextWrapper == null) {
            for (int i = 0; i < word.length(); i++) {
                // remove observers for spinners that reached their final targets
                spinnerWrappers.get(i).getSpinner().removeAllObservers();
            }
            for (int i = word.length(); i < numSpinners; i++) {
                // Set remaining spinners to ' '
                spinnerWrappers.get(i).spinToEnd();
            }
        }
        else if (nextWrapper == spinnerWrapper) {
            // Next target is on same spinner, pause the spinner
            spinnerWrapper.pause();
        }
        else {
            spinnerWrapper.stop();
            nextWrapper.spinToTarget();
        }

    }

    String generateRandomWord() throws IOException {
        for (SpinnerWrapper wrapper : spinnerWrappers) {
            wrapper.start();
        }

        word = randomWord.generateWord();
        charList.removeListener(randomWordListener);
        return word;
    }

    /*
    Converts a char into the int value, in the range 0-27, required to display it on a
    eu.hansolo.fx alphabetic Spinner. Alphabetic spinners have the characters A-Z and space.
    lowercase letters are converted to uppercase, and any non-letter character is converted to
    a space.
    */
    static Double convert(char c) {
        if (c == ' ') {
            return 27.0;
        }
        else if (c >= 65 && c <= 90) {  //uppercase letters
            return c - 65.0;
        }
        else if (c >= 97 && c <= 122) {  //lowercase letters, c - 65 - 32
            return c - 97.0;
        }
        else {  //character not present on Alphabetic spinners, return value for ' '
            return 27.0;
        }
    }
}
