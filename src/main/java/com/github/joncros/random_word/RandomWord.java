package com.github.joncros.random_word;

public class RandomWord {
    private StringBuilder stringBuilder;
    private RandomLetterGenerator letterGenerator;
    private WordService wordService;
    private RandomWordUI ui;
	private int maxLength;

    /**
     * If query result size is <= SMALL, next random letter is chosen from QueryResult.charsInNthPlace
      */
	private final int SMALL = 20;

    public static void main( String[] args ) {
        //todo

        System.out.println("Chooses random letters until a complete word existing in a specific list of " +
                "words is assembled");
        // If a GUI is implemented, offer choice of CLI or GUI here
        // If more than one WordService is implemented, offer choice of WordSevice here
        // Ask for desired maximum word length

        // create RandomWord instance
        // execute generateWord method
    }

    public RandomWord(int maxLength, WordService wordService, RandomWordUI ui) {
        //todo
    }

    public String generateWord() {
        //todo
		// Generate two random letters
		// Append them to stringBuilder, display each in ui
		// until all letters are chosen (stringBuilder length equals maxLength)
            // query WordService for words starting with chosen letters
            // if 0 words
                // replace latest letter with another letter
                // display word, including replacement letter, in ui
            // else if 1 word
                // choose this word
                // break
            // else if small number of words
                // choose next letter from QueryResult.charsInNthPlace
                // append to stringBuilder
                // display letter in ui
            // else choose another random letter
                // append to stringBuilder
                // display letter in ui
        // display final word in ui
        return "";
    }
}
