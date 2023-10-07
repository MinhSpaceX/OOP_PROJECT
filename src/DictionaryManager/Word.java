package DictionaryManager;

public class Word {
    private final String word_target;
    private final String word_explain;

    Word(String word_target, String word_explain) {
        this.word_target = word_target;
        this.word_explain = word_explain;
    }

    public String GetWordTarget() {
        return word_target;
    }

    public String GetWordExplain() {
        return word_explain;
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return String.format("%s | %s\n", word_target, word_explain);
    }
}
