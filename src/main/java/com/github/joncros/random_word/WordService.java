package com.github.joncros.random_word;

import java.util.List;

public interface WordService {
    String[] findWordsStartingWith(String s);

    String[] findWordsStartingWith(String s, int maxWordLength);
}
