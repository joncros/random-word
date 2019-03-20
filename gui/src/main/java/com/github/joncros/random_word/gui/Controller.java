package com.github.joncros.random_word.gui;

import com.github.joncros.random_word.core.RandomWord;
import eu.hansolo.fx.spinner.CanvasSpinner;
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

    private AnimationTimer timer;
    private long lastTimerCall;

    //Time that should elapse between timer calls
    private final long INTERVAL = 250_000_000L;

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
        numSpinners = spinners.size();

        spinnerWrappers = new ArrayList<>();
        for (var spinner : spinners) {
            spinnerWrappers.add(new SpinnerWrapper(spinner, ANIMATION_RATE));
        }

        randomWordListener = change -> {
            while (change.next()) {
                for (int i = change.getFrom(); i < change.getTo(); i++) {
                    Character c = randomWord.characterList().get(i);
                    //todo
                }
            }
        };

        lastTimerCall = System.nanoTime();
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now > lastTimerCall + INTERVAL) {
                    for (int i = 0; i < numSpinners; i++) {
                        //todo
                        // if spinnner has target and spinner value == target
                        // stop at target
                        // else spinUp() spinner
                    }
                    lastTimerCall = now;
                }
            }
        };
    }

    String generateRandomWord() throws IOException {
        timer.start();
        return "";
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
