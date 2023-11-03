package com.zeus.DictionaryManager;

import com.zeus.utils.log.Logger;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, List<SingleWord>> getSingleWordMap() {
        Map<String, List<SingleWord>> wordMap = new HashMap<>();
        String wordTarget = word.getWordTarget();
        String pronoun = word.getPronoun();
        word.getDescription().getTypes().forEach(type -> {
            List<SingleWord> words = new ArrayList<>();
            type.getMeanings().forEach(meaning -> {
                List<Pair<String, String>> examples = new ArrayList<>();
                meaning.getExamples().forEach(example -> {
                    Pair<String, String> tempExample = new Pair<>(example.getEnglish(), example.getVietnamese());
                    examples.add(tempExample);
                });
                words.add(new SingleWord(wordTarget, pronoun, type.getName(), meaning.getExplain(), examples));
            });
            wordMap.put(type.getName(), words);
        });
        return wordMap;
    }
}
