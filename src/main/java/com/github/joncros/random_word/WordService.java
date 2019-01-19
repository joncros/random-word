package com.github.joncros.random_word;

import java.util.List;

public interface WordService {
    QueryResult findWordsStartingWith(String s);

    QueryResult findWordsStartingWith(String s, int maxWordLength);
}
