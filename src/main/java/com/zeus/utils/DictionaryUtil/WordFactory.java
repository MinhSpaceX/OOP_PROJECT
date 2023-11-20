package com.zeus.utils.DictionaryUtil;

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
            if(type.getName().equalsIgnoreCase("danh từ")) type.setName("noun");
            if(type.getName().equalsIgnoreCase("động từ")) type.setName("verb");
            if(type.getName().equalsIgnoreCase("tính từ")) type.setName("adj");
            if(type.getName().equalsIgnoreCase("trạng từ")) type.setName("adv");
            if(type.getName().equalsIgnoreCase("ngoại động từ")) type.setName("transitive verb");
            if(type.getName().equalsIgnoreCase("nội động từ")) type.setName("intransitive verb");
            wordMap.put(type.getName(), words);
        });
        return wordMap;
    }
}
