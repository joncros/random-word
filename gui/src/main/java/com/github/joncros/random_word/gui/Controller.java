package com.github.joncros.random_word.gui;

import com.github.joncros.random_word.core.RandomWord;
import eu.hansolo.fx.spinner.CanvasSpinner;
import eu.hansolo.fx.spinner.Spinner;
import eu.hansolo.fx.spinner.event.SpinnerEvent;
import eu.hansolo.fx.spinner.event.SpinnerObserver;
import javafx.animation.AnimationTimer;
import javafx.collections.ListChangeListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

class Controller {
    // Number of seconds a CanvasSpinnner should take to cycle from 'A' to ' '
    private static final double ANIMATION_RATE = 250;

    private RandomWord randomWord;
    private String word;

    private ListChangeListener<Character> randomWordListener;

    private List<SpinnerWrapper> spinnerWrappers;
    private int numSpinners;

    /*
    Queue that holds the index values for SpinnerWrappers that have a new target value,
    in the order that changes were reported by randomWordListener
    */
    private Deque<Integer> spinnersWithTargets;

    Controller(RandomWord randomWord, List<CanvasSpinner> spinners) {
        this.randomWord = randomWord;
        this.word = null;
        numSpinners = spinners.size();

        spinnerWrappers = new ArrayList<>();
        for (var spinner : spinners) {
            SpinnerWrapper sw = new SpinnerWrapper(spinner, ANIMATION_RATE);
            spinnerWrappers.add(sw);
        }

        randomWordListener = change -> {
            while (change.next()) {
                for (int i = change.getFrom(); i < change.getTo(); i++) {
                    Character c = randomWord.characterList().get(i);
                    // todo add character to SpinnerWrapper's target queue;
                    // start spinner if currently stopped
                }
            }
        };

    }

    String generateRandomWord() throws IOException {
        // for all SpinnerWrappers, call start()
        word = randomWord.generateWord();
        /*
        when last used spinner (index word.length() - 1) finished spinning to last target, call
        spinToEnd on all remaining spinners (index >= word.length())
         */
        return word;
    }

    /*
    Converts a char into the int value, in the range 0-27, required to display it on a
    eu.hansolo.fx alphabetic Spinner. Alphabetic spinners have the characters A-Z and space.
    lowercase letters are converted to uppercase, and any non-letter character is converted to
    a space.
    */
    private int convert(char c) {
        if (c == ' ') {
            return 27;
        }
        else if (c >= 65 && c <= 90) {  //uppercase letters
            return c - 65;
        }
        else if (c >= 97 && c <= 122) {  //lowercase letters, c - 65 - 32
            return c - 97;
        }
        else {  //character not present on Alphabetic spinners
            return 27;
        }
    }
}
