package com.github.joncros.random_word.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Demo {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    public static void main(String[] args ) {
        System.out.println("Chooses random letters until a complete word existing in a specific list of " +
                "words is assembled.");
        Scanner in = new Scanner(System.in);
        System.out.println("Type the location of a text file containing a list of words," +
                " or leave blank to use the Datamuse service:");
        String choice = in.nextLine();
        WordService service;
        if (choice.isBlank()) {
            service = new DatamuseWordService();
        }
        else {
            try {
                service = new TextFileWordService(new File(choice));
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
                return;
            }
        }

        System.out.print("What is the minimum length of the word to be generated? ");
        int minLength = in.nextInt();
        System.out.print("What is the maximum length of the word to be generated? ");
        int maxLength = in.nextInt();

        RandomWord randomWord = new RandomWord(
                minLength,
                maxLength,
                service,
                new RandomLetterGenerator(ALPHABET));
        CLI cli = new CLI(randomWord, System.out);

        String word;
        try {
            word = randomWord.generateWord();
            System.out.println("\nWord generated: " + word);
        }
        catch (IOException e) {
            System.out.println("\nError accessing word list");
            System.out.println(e.getMessage());
        }
    }
}
