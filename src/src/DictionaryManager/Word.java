package DictionaryManager;

public class Word {
    private String word_target;
    private String word_explain;

    public Word(){}
    public Word(String word_target, String word_explain) {
        this.word_target = word_target;
        this.word_explain = word_explain;
    }

    public String GetWordTarget() {
        return  word_target;
    }

    public String GetWordExplain(){
        return  word_explain;
    }

}
