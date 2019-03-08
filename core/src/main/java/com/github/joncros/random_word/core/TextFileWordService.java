package com.github.joncros.random_word.core;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Finds words from a text file on a local hard disk
 */
public class TextFileWordService implements WordService{
    private File textFile;

    /**
     * Constructs a TextFileWordService instance
     * @param textFile a text file. file should be formatted so that there is single word
     *                 on each line of the file, and should be alphabetized.
     * @throws UncheckedIOException wrapping a FileNotFoundException if the file does not exist
     */
    public TextFileWordService(File textFile) throws FileNotFoundException {
        if ( !Files.exists(textFile.toPath()) )
            throw new FileNotFoundException("File not found: " + textFile.toString());
        this.textFile = textFile;
    }

    @Override
    public QueryResult findWordsStartingWith(String s) throws  IOException {
        List<String> result = new ArrayList<>();
        try (BufferedReader in
                     = new BufferedReader(new FileReader(textFile))) {
            String line;
            int length = s.length();
            while ((line = in.readLine()) != null) {
                line = line.strip();
                if (line.length() >= s.length()) {
                    String subString = line.substring(0, length);
                    if (subString.equals(s)) {
                        result.add(line);
                    } else if (subString.compareToIgnoreCase(s) > 0) {
                        break;  //substring of line occurs alphabetically after s, so done searching
                    }
                }
            }
        }
        return new QueryResult(result);
    }

    @Override
    public QueryResult findWordsStartingWith(String s, int wordLength) throws IOException {
        List<String> result = new ArrayList<>();
        try (BufferedReader in
                     = new BufferedReader(new FileReader(textFile))) {
            String line;
            int length = s.length();
            while ((line = in.readLine()) != null) {
                line = line.strip();
                if (line.length() >= s.length()) {
                    String subString = line.substring(0, length);
                    if (subString.equals(s) && line.length() == wordLength) {
                        result.add(line);
                    } else if (subString.compareToIgnoreCase(s) > 0) {
                        break;  //substring of line occurs alphabetically after s, so done searching
                    }
                }
            }
        }
        return new QueryResult(result);
    }
}
