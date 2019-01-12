package com.github.joncros.random_word;

import java.util.List;

public interface WordListService {
    List<String> findStartsWith(String s, int maxWordLength);
}
