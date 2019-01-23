package com.github.joncros.random_word;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.management.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class QueryResultTest {
    private QueryResult queryResult;



    @Nested
    @DisplayName("constructor tests")
    class constructorTests {

        @Test
        @DisplayName("constructing from a list when list null")
        void constructFromListNull() {
            ArrayList<String> list = null;
            assertThrows(NullPointerException.class, () -> new QueryResult(list));
        }

        @Test
        @DisplayName("constructing from a String[] when String[] null")
        void constructFromArrayNull() {
            String[] array = null;
            assertThrows(NullPointerException.class, () -> new QueryResult(array));
        }

    }

    @Nested
    @DisplayName("method tests")
    class methodTests {
        String[] strings;

        @BeforeEach
        void setUp() {
            strings = new String[]{"ardency", "ardent", "ardently", "aready"};
            queryResult = new QueryResult(strings);
        }

        @Test
        void getWords() {
            String[] result = queryResult.getWords();
            assertArrayEquals(strings, result);
        }

        @Test
        @DisplayName("tests getWords(int length)")
        void getWordsWithLengthParameter() {
            queryResult = queryResult.getWords(6);
            String[] expected = {"ardent", "aready"};
            String[] result = queryResult.getWords();
            Arrays.sort(result);
            assertArrayEquals(expected, result);
        }

        @Test
        @DisplayName("tests getWords(int length) when length parameter is zero")
        void getWordsLengthZero() {
            assertThrows(IllegalArgumentException.class, () -> queryResult.getWords(0));
        }

        @Test
        @DisplayName("tests getWords(int length) when length parameter is negative")
        void getWordsLengthNegative() {
            assertThrows(IllegalArgumentException.class, () -> queryResult.getWords(-2));
        }

        @Test
        void getWordsInLengthRange() {
            queryResult = queryResult.getWordsInLengthRange(6,7);
            String[] expected = {"ardency", "ardent", "aready"};
            String[] result = queryResult.getWords();
            Arrays.sort(result);
            assertArrayEquals(expected, result);
        }

        @Test
        @DisplayName("tests getWordsInLengthRange when minLength less than one")
        void getWordsInLengthRangeMinLessThanOne() {
            assertThrows(IllegalArgumentException.class,
                    () -> queryResult.getWordsInLengthRange(0, 5));
        }

        @Test
        @DisplayName("tests getWordsInLengthRange when minLength > maxLength")
        void getWordsInLengthRangeMinGreaterThanMax() {
            assertThrows(IllegalArgumentException.class,
                    () -> queryResult.getWordsInLengthRange(6,5));
        }

        @Test
        @DisplayName("tests getWordsWithMaxLength")
        void getWordsWithMaxLength() {
            queryResult = queryResult.getWordsWithMaxLength(7);
            String[] expected = {"ardency", "ardent", "aready"};
            String[] result = queryResult.getWords();
            Arrays.sort(result);
            assertArrayEquals(expected, result);
        }

        @Test
        @DisplayName("tests getWordsWithMaxLength when maxLength parameter is zero")
        void getWordsWithMaxLengthZero() {
            assertThrows(IllegalArgumentException.class, () -> queryResult.getWordsWithMaxLength(0));
        }


        @Test
        @DisplayName("tests getWordsWithMaxLength when maxLength parameter is negative")
        void getWordsWithMaxLengthNegative() {
            assertThrows(IllegalArgumentException.class, () -> queryResult.getWordsWithMaxLength(-2));
        }

        @Test
        void findWordsStartingWith() {
            queryResult = queryResult.findWordsStartingWith("ard");
            String[] expected = {"ardency", "ardent", "ardently"};
            String[] result = queryResult.getWords();
            Arrays.sort(result);
            assertArrayEquals(expected, result);
        }

        @Test
        @DisplayName("tests findWordsStartingWith using a string parameter that none of the words start with")
        void findWordsStartingWithEmptyResult() {
            queryResult = queryResult.findWordsStartingWith("b");
            int size = queryResult.getSize();
            assertEquals(0, size);
        }

        @Test
        @DisplayName("tests findWordsStartingWith using a null String reference as the parameter")
        void findWordsStartingWithNullString() {
            String string = null;
            assertThrows(NullPointerException.class, () -> queryResult.findWordsStartingWith(string));
        }

        @Test
        @DisplayName("tests findWordsStartingWith using a zero-length string as the parameter")
        void findWordsStartingWithZeroLengthString() {
            assertThrows(IllegalArgumentException.class, () -> queryResult.findWordsStartingWith(""));
        }

        @Test
        void charsInNthPlace() {
            Set<Character> expected = new HashSet<>();
            expected.add('d');
            expected.add('n');
            Set<Character> result = queryResult.charsInNthPlace(4);
            assertEquals(expected, result);

        }

        @Test
        @DisplayName("tests charsInNthPlace with parameter less than 0")
        void charsInNthPlaceNegativeParameter() {
            assertThrows(IllegalArgumentException.class, () -> queryResult.charsInNthPlace(-1));
        }
    }

    @Nested
    @DisplayName("method tests when QueryResult size is zero")
    class whenZeroSize {
        String[] EMPTY_ARRAY = new String[0];

        @BeforeEach
        void setUp() {
            queryResult = new QueryResult(EMPTY_ARRAY);
        }

        @Test
        void getWords() {
            String[] result = queryResult.getWords();
            assertArrayEquals(EMPTY_ARRAY, result);
        }
    }
}