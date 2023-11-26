package com.zeus.utils.DictionaryUtil;

import javafx.util.Pair;

import java.util.List;

/**
 * Single word class contains 1 word target, 1 pronoun,
 * 1 type, 1 meaning, and a list of examples.
 */
public class SingleWord {
    private String wordTarget;
    private String pronoun;
    private String type;
    private String meaning;
    private List<Pair<String, String>> examples;

    /**
     * Constructor.
     *
     * @param wordTarget The word target.
     * @param pronoun    Thw word's pronoun.
     * @param type       Thw word's type.
     * @param meaning    Thw word's meaning.
     * @param examples   Thw word's examples.
     */
    public SingleWord(String wordTarget, String pronoun, String type, String meaning, List<Pair<String, String>> examples) {
        this.wordTarget = wordTarget;
        this.pronoun = pronoun;
        this.type = type;
        this.meaning = meaning;
        this.examples = examples;
    }

    /**
     * Get word target.
     *
     * @return Word target.
     */
    public String getWordTarget() {
        return wordTarget;
    }

    /**
     * Set word target.
     *
     * @param wordTarget Word target.
     */
    public void setWordTarget(String wordTarget) {
        this.wordTarget = wordTarget;
    }

    /**
     * Get word's pronoun
     *
     * @return Word's pronoun.
     */
    public String getPronoun() {
        return pronoun;
    }

    /**
     * Set word's pronoun.
     *
     * @param pronoun Pronoun.
     */
    public void setPronoun(String pronoun) {
        this.pronoun = pronoun;
    }

    /**
     * Get word's type.
     *
     * @return Word's type.
     */
    public String getType() {
        return type;
    }

    /**
     * Set word's type
     *
     * @param type Type.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get word's meaning.
     *
     * @return Word's meaning.
     */
    public String getMeaning() {
        return meaning;
    }

    /**
     * Set word's meaning.
     *
     * @param meaning Meaning.
     */
    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    /**
     * Get word's examples.
     *
     * @return Examples.
     */
    public List<Pair<String, String>> getExamples() {
        return examples;
    }

    /**
     * Set word's examples
     *
     * @param examples Examples
     */
    public void setExamples(List<Pair<String, String>> examples) {
        this.examples = examples;
    }

    /**
     * To string method.
     *
     * @return String represent the word information.
     */
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("- ").append(getMeaning()).append("\n");
        for (Pair<String, String> example : examples) {
            result.append("Ex: " + example.getKey())
                    .append(": ")
                    .append(example.getValue())
                    .append("\n");
        }
        return result.toString();
    }
}
