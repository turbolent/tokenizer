package com.turbolent.tokenizer;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class TokenizerTest {

    @Test
    public void testTokenize() {
        assertTokenization("Good muffins cost $3.88\nin New York. "
                           + " Please buy me\ntwo of them.\nThanks.",
                           new String[]{
                               "Good", "muffins", "cost", "$", "3.88", "in", "New", "York.",
                               "Please", "buy", "me", "two", "of", "them.", "Thanks", "."});

        assertTokenization("On a $50,000 mortgage of 30 years at 8 percent, "
                           + "the monthly payment would be $366.88.",
                           new String[] {
                               "On", "a", "$", "50,000", "mortgage", "of", "30", "years", "at",
                               "8", "percent", ",", "the", "monthly", "payment", "would", "be",
                               "$", "366.88", "."});

        assertTokenization("\"We beat some pretty good teams to get here,\" Slocum said.",
                           new String[] {
                               "``", "We", "beat", "some", "pretty", "good", "teams", "to", "get",
                               "here", ",", "''", "Slocum", "said", "."});


        assertTokenization("Well, we couldn't have this predictable, cliche-ridden, "
                           + "\"Touched by an Angel\" (a show creator John Masius worked on)"
                           + " wanna-be if she didn't.",
                           new String[] {
                               "Well", ",", "we", "could", "n't", "have", "this", "predictable",
                               ",", "cliche-ridden", ",", "``", "Touched", "by", "an", "Angel",
                               "''", "(", "a", "show", "creator", "John", "Masius", "worked",
                               "on", ")", "wanna-be", "if", "she", "did", "n't", "."});

        assertTokenization("I cannot cannot work under these conditions!",
                           new String[] {
                               "I", "can", "not", "can", "not", "work", "under", "these",
                               "conditions", "!"
                           });

        assertTokenization("The company spent $30,000,000 last year.",
                           new String[] {
                               "The", "company", "spent", "$", "30,000,000",
                               "last", "year", "."});

        assertTokenization("The company spent 40.75% of its income last year.",
                           new String[] {
                               "The", "company", "spent", "40.75", "%", "of",
                               "its", "income", "last", "year", "."});

        assertTokenization("He arrived at 3:00 pm.",
                           new String[] {
                               "He", "arrived", "at", "3:00", "pm", "."});

        assertTokenization("I bought these items: books, pencils, and pens.",
                           new String[] {
                               "I", "bought", "these", "items", ":", "books", ",",
                               "pencils", ",", "and", "pens", "."});

        assertTokenization("Though there were 150, 100 of them were old.",
                           new String[] {
                               "Though", "there", "were", "150", ",",
                               "100", "of", "them", "were", "old", "."});

        assertTokenization("There were 300,000, but that wasn't enough.",
                           new String[] {
                               "There", "were", "300,000", ",",
                               "but", "that", "was", "n't", "enough", "."});
    }

    private void assertTokenization(String sentence, String[] tokens) {
        assertArrayEquals(tokens, Tokenizer.tokenize(sentence));
    }

}
