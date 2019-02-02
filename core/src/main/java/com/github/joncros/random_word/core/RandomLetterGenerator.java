package com.github.joncros.random_word.core;

import java.util.Iterator;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

/**
 * Generates a random letter out of those contained in the parameter letters.
 */
class RandomLetterGenerator {
    final private String letters;
    final private Random random;

    /**
     * Constructs a new RandomLetterGenerator
     * @param letters a String containing all of the letters from which to choose random letters
     */
    RandomLetterGenerator(String letters) {
        Objects.requireNonNull(letters);
        if (letters.isBlank()) {
            throw new IllegalArgumentException("letters cannot contain only whitespace");
        }
        this.letters = letters;
        random = new Random();
    }

    String getLetters() {
        return letters;
    }

    /**
     * Generates a single randomly chosen letter
     * @return a char corresponing to the chosen letter
     */
    char generate() {
        int randomInt = random.nextInt(letters.length());
        return letters.charAt(randomInt);
    }

    /**
     * Generates a single random letter chosen from the Set chars
     * @param chars A Set of Character objects from which to chose a letter
     * @return a char corresponing to the chosen letter
     */
    char generate(Set<Character> chars) {
        Objects.requireNonNull(chars);
        if (chars.isEmpty() || chars.contains(null)) {
            throw new IllegalArgumentException("parameter chars must be non-empty and cannot contain null references");
        }
        int randomInt = random.nextInt(chars.size());
        Iterator iter = chars.iterator();
        char result = '\u0000';
        for (int i = 0; i <= randomInt; i++ ) {
            result = (char) iter.next();
        }
        return result;
    }
}
