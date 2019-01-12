package com.github.joncros.random_word;

public interface RandomWordUI {

    /**
     * Displays the current word generated by RandomWord
     * @param s the current word
     */
    void display(String s);

    /**
     * Called by RandomWord to indicate it has finished generating the word
     * @param s The complete word generated by RandomWord
     */
    void finish(String s);
}
