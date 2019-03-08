package com.github.joncros.random_word.core;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.PrintStream;

/**
 * Adds a listener to the {@code ObservableList<Character>} provided by RandomWord.
 * When a letter is added, it is printed on the current line of a PrintStream.
 * When a letter is replaced, the entire word is printed on a new line on the PrintStream.
 */
public class CLI {

    /**
     * Construct a CLI
     * @param randomWord A RandomWord instance
     * @param ps PrintStream to print changes to
     */
    public CLI(RandomWord randomWord, PrintStream ps) {
        ObservableList<Character> charList = randomWord.characterList();
        charList.addListener((ListChangeListener<Character>) change -> {
            while (change.next()) {
                if (change.wasReplaced()) {
                    //print entire word on a new line
                    ps.print("\n" + randomWord.curentWord());
                } else if (change.wasAdded()) {
                    for (int i = change.getFrom(); i < change.getTo(); i++) {
                        //print added letter(s) to current line
                        ps.print(charList.get(i));
                    }
                }
            }
        });
    }
}
