package com.zeus.DictionaryManager;

import com.zeus.utils.log.Logger;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class SingleWord {
    private String wordTarget;
    private String pronoun;
    private String type;
    private String meaning;
    private List<Pair<String, String>> examples;

    public SingleWord(String wordTarget, String pronoun, String type, String meaning, List<Pair<String, String>> examples) {
        this.wordTarget = wordTarget;
        this.pronoun = pronoun;
        this.type = type;
        this.meaning = meaning;
        this.examples = examples;
    }

    public String getWordTarget() {
        return wordTarget;
    }

    public void setWordTarget(String wordTarget) {
        this.wordTarget = wordTarget;
    }

    public String getPronoun() {
        return pronoun;
    }

    public void setPronoun(String pronoun) {
        this.pronoun = pronoun;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public List<Pair<String, String>> getExamples() {
        return examples;
    }

    public void setExamples(List<Pair<String, String>> examples) {
        this.examples = examples;
    }

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
