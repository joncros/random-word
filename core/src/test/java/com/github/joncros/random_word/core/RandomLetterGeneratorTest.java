package com.github.joncros.random_word.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RandomLetterGeneratorTest {

    @BeforeEach
    void setUp() {
    }

    /*
    0-parameter generate: test that it produces a character contained in letters; test that it can return
    a whitespace character if letters contains a whitespace character
     */

    /*
    1-param generate: test that returned character is one of the characters in the set. test with null set,
    set with null member(s), set with more than one member equal to whitepace
     */

    /**
     * Tests the constructor with a valid String for the letters parameter
     * (at least one non-whitespace character in the String)
     */
    @Test
    void constructorValidLettersParameter() {
        String letters = "abcdefgh ";
        RandomLetterGenerator generator = new RandomLetterGenerator(letters);
        String generatorLetters = generator.getLetters();
        assertEquals(letters, generatorLetters);
    }

    /**
     * Tests the constructor with null passed in to the parameter
     */
    @Test
    void constructorNullLettersParameter() {
        String letters = null;
        assertThrows(NullPointerException.class, () -> {
            RandomLetterGenerator generator = new RandomLetterGenerator(letters);
        });
    }

    /**
     * Tests the constructor with a zero-length string passed into the parameter
     */
    @Test
    void constructorZeroLengthLettersParameter() {
        String letters = "";
        assertThrows(IllegalArgumentException.class, () -> {
            RandomLetterGenerator generator = new RandomLetterGenerator(letters);
        });
    }

    /**
     * Tests the constructor with a string consisting of only whitespace characters passed into
     * the parameter
     */
    @Test
    void constructorWhitespaceLettersParameter() {
        String letters = " ";
        assertThrows(IllegalArgumentException.class, () -> {
            RandomLetterGenerator generator = new RandomLetterGenerator(letters);
        });
    }

    /**
     * Tests the zero-parameter generate method
     */
    @Test
    void generate() {
        String letters = "abcdefgh";
        RandomLetterGenerator generator = new RandomLetterGenerator(letters);
        char c = generator.generate();
        assertTrue(letters.contains(Character.toString(c)));
    }

    /**
     * Tests the zero-parameter generate method to determine if it can return a whitespace character
     * if the whitespace character is included in the RandomLetterGenerator letters parameter
     */
    @Test
    void generateWhitespaceCharacter() {
        String letters = "a ";
        RandomLetterGenerator generator = new RandomLetterGenerator(letters);
        char[] chars = new char[20];
        for (int i = 0; i < 20; i++) {
            chars[i] = generator.generate();
        }
        assertTrue(String.valueOf(chars).contains(" "));
    }

    /**
     * Test the one-parameter generate method
     */
    @Test
    void generateFromSetOfCharacters() {
        String letters = "abcdefgh";
        RandomLetterGenerator generator = new RandomLetterGenerator(letters);

        String setString = "abcd";
        Set<Character> chars = new HashSet<>();
        for (int i = 0; i < setString.length(); i++) {
            chars.add(setString.charAt(i));
        }
        char c = generator.generate(chars);
        assertTrue(setString.contains(Character.toString(c)));
    }

    /**
     * Test that the one-parameter generate method can return a whitespace character if the Set of Characters
     * contains a whitespace character
     */
    @Test
    void generateWhiteSpaceFromSetOfCharacters() {
        String letters = "abcdefgh";
        RandomLetterGenerator generator = new RandomLetterGenerator(letters);

        String setString = "a ";
        Set<Character> chars = new HashSet<>();
        for (int i = 0; i < setString.length(); i++) {
            chars.add(setString.charAt(i));
        }

        char[] result = new char[20];
        for (int i = 0; i < 20; i++) {
            result[i] = generator.generate(chars);
        }
        assertTrue(String.valueOf(result).contains(" "));
    }

    /**
     * Test the one-parameter generate method when passing in a null parameter
     */
    @Test
    void generateFromSetOfCharactersNull() {
        String letters = "abcdefgh";
        RandomLetterGenerator generator = new RandomLetterGenerator(letters);

        Set<Character> chars = null;
        assertThrows(NullPointerException.class, () -> generator.generate(chars));
    }

    /**
     * Test the one-parameter generate method when passing in an empty set
     */
    @Test
    void generateFromEmptySetOfCharacters() {
        String letters = "abcdefgh";
        RandomLetterGenerator generator = new RandomLetterGenerator(letters);

        Set<Character> chars = new HashSet<>();
        assertThrows(IllegalArgumentException.class, () -> generator.generate(chars));
    }

    /**
     * Test the one-parameter generate method when passing in a set with null members
     */
    @Test
    void generateFromSetOfCharactersNullMember() {
        String letters = "abcdefgh";
        RandomLetterGenerator generator = new RandomLetterGenerator(letters);

        Set<Character> chars = new HashSet<>();
        chars.add('a');
        chars.add(null);
        assertThrows(IllegalArgumentException.class, () -> generator.generate(chars));
    }
}