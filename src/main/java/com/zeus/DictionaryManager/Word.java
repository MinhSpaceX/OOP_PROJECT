package com.zeus.DictionaryManager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Word {

    private String word_target;
    private String word_explain;
    private String word_type;

    public Word(String word_target, String word_explain, String word_type) {
        this.word_target = word_target;
        this.word_explain = word_explain;
        this.word_type = word_type;
    }

    public Word() {}

    public void setWord_target(String word_target) {
        this.word_target = word_target;
    }

    public void setWord_explain(String word_explain) {
        this.word_explain = word_explain;
    }

    public void setWord_type(String word_type) {
        this.word_type = word_type;
    }

    public String getWordTarget() {
        return word_target;
    }

    public String getWordExplain() {
        return word_explain;
    }

    public String getWordType() {
        return word_type;
    }

    /**
     * @return the word with explain.
     */
    @Override
    public String toString() {
        return String.format("%s | %s | %s\n", word_target, word_explain, word_type);
    }
    public boolean equals(Word a) {
        return (a.getWordTarget().equals(word_target) && a.getWordType().equals(word_type));
    }
}
