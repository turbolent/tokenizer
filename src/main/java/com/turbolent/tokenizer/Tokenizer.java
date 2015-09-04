package com.turbolent.tokenizer;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Tokenizer {

    private static String replaceAll(String sentence, Pattern pattern,
                                     Function<Matcher, String> replacer)
    {
        Matcher matcher = pattern.matcher(sentence);
        if (!matcher.find())
            return sentence;

        StringBuffer buffer = new StringBuffer();
        do {
            // NOTE: replace with empty string, then append actual replacement,
            //       as appendReplacement uses dollar sign as a special character
            matcher.appendReplacement(buffer, "");
            String replacement = replacer.apply(matcher);
            buffer.append(replacement);
        } while (matcher.find());
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    private static String replaceAll(String sentence, Pattern pattern, String replacement) {
        Matcher matcher = pattern.matcher(sentence);
        if (!matcher.find())
            return sentence;

        StringBuffer buffer = new StringBuffer();
        do {
            // NOTE: replace with empty string, then append actual replacement,
            //       as appendReplacement uses dollar sign as a special character
            matcher.appendReplacement(buffer, "");
            buffer.append(replacement);
        } while (matcher.find());
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    private static String surroundGroupWithSpaces(Matcher matcher) {
        String match = matcher.group();
        return (" " + match + " ");
    }

    private static String prefixTwoGroupsWithSpaces(Matcher matcher) {
        return (" " + matcher.group(1)
                + " "  + matcher.group(2));
    }

    private static String suffixTwoGroupsWithSpaces(Matcher matcher) {
        return (matcher.group(1) + " "
                + matcher.group(2) + " ");
    }

    private static Pattern[] compileContractions(String... parts) {
        return Arrays.stream(parts)
                     .map(part -> Pattern.compile("\\b" + part + "\\b",
                                                  Pattern.CASE_INSENSITIVE))
                     .toArray(Pattern[]::new);
    }

    private static final Pattern QUOTE_AT_START = Pattern.compile("^\"");
    private static final Pattern GRAVES = Pattern.compile("``");
    private static final Pattern OPEN_QUOTE = Pattern.compile("([ (\\[{<])\"");
    private static final Pattern COLON_OR_COMMA = Pattern.compile("([:,])([^\\d])");
    private static final Pattern ELLIPSIS = Pattern.compile("\\.\\.\\.");
    private static final Pattern SPECIAL = Pattern.compile("[;@#$%&]");
    private static final Pattern DOT_AT_END = Pattern.compile("([^\\.])(\\.)([\\]\\)}>\"']*)\\s*$");
    private static final Pattern QUESTION_OR_EXCLAMATION = Pattern.compile("[?!]");
    private static final Pattern SINGLE_QUOTE = Pattern.compile("([^'])' ");
    private static final Pattern SPECIAL2 = Pattern.compile("[\\]\\[\\(\\)\\{\\}\\<\\>]");
    private static final Pattern HYPHENS = Pattern.compile("--");
    private static final Pattern QUOTE = Pattern.compile("\"");
    private static final Pattern SINGLE_QUOTES = Pattern.compile("(\\S)('')");
    private static final Pattern CONTRACTIONS1_1 = Pattern.compile("([^' ])('[smd]?) ",
                                                                   Pattern.CASE_INSENSITIVE);
    private static final Pattern CONTRACTIONS1_2 = Pattern.compile("([^' ])('ll|'re|'ve|n't) ",
                                                                   Pattern.CASE_INSENSITIVE);

    private static final Pattern[] CONTRACTIONS2 = compileContractions("(can)(not)");
    private static final Pattern WHITESPACE = Pattern.compile("\\s+");

    public static List<String> tokenize(String sentence) {
        return new Tokenizer(sentence).tokenize();
    }

    private String sentence;

    private Tokenizer(String sentence) {
        this.sentence = sentence;
    }

    private void replaceAll(Pattern pattern, String replacement) {
        sentence = replaceAll(sentence, pattern, replacement);
    }

    private void replaceAll(Pattern pattern, Function<Matcher, String> replacer) {
        sentence = replaceAll(sentence, pattern, replacer);
    }

    private List<String> tokenize() {
        // Starting quotes
        replaceAll(QUOTE_AT_START, "``");
        replaceAll(GRAVES, " `` ");
        replaceAll(OPEN_QUOTE, matcher -> matcher.group(1) + " `` ");

        // Punctuation
        replaceAll(COLON_OR_COMMA, Tokenizer::prefixTwoGroupsWithSpaces);
        replaceAll(ELLIPSIS, " ... ");
        replaceAll(SPECIAL, Tokenizer::surroundGroupWithSpaces);
        replaceAll(DOT_AT_END, matcher ->
            (matcher.group(1) + " " + matcher.group(2) + matcher.group(3) + " "));
        replaceAll(QUESTION_OR_EXCLAMATION, Tokenizer::surroundGroupWithSpaces);
        replaceAll(SINGLE_QUOTE, matcher -> matcher.group(1) + " ' ");

        // Parentheses, brackets, etc.
        replaceAll(SPECIAL2,
                   Tokenizer::surroundGroupWithSpaces);
        replaceAll(HYPHENS, " -- ");

        // Add a space to the beginning and end to make things easier
        sentence = " " + sentence + " ";

        // Ending quotes
        replaceAll(QUOTE, " '' ");
        // Possessive or close-single-quote
        replaceAll(SINGLE_QUOTES, Tokenizer::suffixTwoGroupsWithSpaces);

        // Contractions
        replaceAll(CONTRACTIONS1_1, Tokenizer::suffixTwoGroupsWithSpaces);
        replaceAll(CONTRACTIONS1_2, Tokenizer::suffixTwoGroupsWithSpaces);

        for (Pattern contraction : CONTRACTIONS2) {
            replaceAll(contraction, matcher ->
                (" " + matcher.group(1) + " "  + matcher.group(2) + " "));
        }

        return WHITESPACE.splitAsStream(sentence.trim())
                         .collect(Collectors.toList());
    }

}
