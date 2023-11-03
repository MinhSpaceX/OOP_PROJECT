package com.zeus.DictionaryManager;

import com.zeus.utils.log.Logger;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class WordFactory {
    private Word word = null;
    public WordFactory(Word word) {
        if (word != null) {
            this.word = word;
        }
        else {
            Logger.warn("Word is null.");
        }
    }

    public List<SingleWord> getSingleWordArray() {
        List<SingleWord> words = new ArrayList<>();
        String wordTarget = word.getWordTarget();
        String pronoun = word.getPronoun();
        word.getDescription().getTypes().forEach(type -> {
            type.getMeanings().forEach(meaning -> {
                List<Pair<String, String>> examples = new ArrayList<>();
                meaning.getExamples().forEach(example -> {
                    Pair<String, String> tempExample = new Pair<>(example.getEnglish(), example.getVietnamese());
                    examples.add(tempExample);
                });
                words.add(new SingleWord(wordTarget, pronoun, type.getName(), meaning.getExplain(), examples));
            });
        });
        return words;
    }
}
