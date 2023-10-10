package com.zeus.DictionaryManager;

public class Word {
    private final String word_target;
    private final String word_explain;
    private final String word_type;

    Word(String word_target, String word_explain, String word_type) {
        this.word_target = word_target;
        this.word_explain = word_explain;
        this.word_type = word_type;
    }

    public String GetWordTarget() {
        return word_target;
    }

    public String GetWordExplain() {
        return word_explain;
    }

    public String GetWordType() {
        return word_type;
    }

    /**
     * @return the word with explain.
     */
    @Override
    public String toString() {
        return String.format("%s | %s | %s\n", word_target, word_explain, word_type);
    }
}
