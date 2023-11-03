package com.zeus.DatabaseManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.zeus.DictionaryManager.Word;
import com.zeus.utils.config.Config;
import com.zeus.utils.log.Logger;
import com.zeus.utils.trie.Trie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MongoPanel {
    private MongoClient client ;
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    private Trie trie = new Trie();
    List<Document> pipeline = Arrays.asList(
            new Document("$project", new Document("keys", new Document("$objectToArray", "$$ROOT"))),
            new Document("$unwind", "$keys"),
            new Document("$group", new Document("_id", null)
                    .append("allKeys", new Document("$addToSet", "$keys.k")))
    );

    public MongoPanel(String url){
        client = MongoClients.create(url);
        database = client.getDatabase("dictionary_metadata");
        collection = database.getCollection("dictionary");
    }

    /**
     * take data from base then load to trie
     */
    public void fetchDatafromBase(){
        int count = 0;
        MongoCursor<Document> cursor = collection.aggregate(pipeline).iterator();
        if(cursor.hasNext()) {
            Document result = cursor.next();
            ArrayList<String> keysList = (ArrayList<String>) result.get("allKeys");
            for(String key : keysList){
                trie.insert(key);
                count++;
            }
        }
        System.out.println(count);
    }

    public Word fetchWord(String wordTarget){
        List<Document> tempPipeline = List.of(new Document("$project", new Document("_id", 0L).append(wordTarget, 1L)));
        try (MongoCursor<Document> cursor = collection.aggregate(tempPipeline).iterator()) {
            if (cursor.hasNext()) {
                Document wordDoc = cursor.next();
                Word word = new Word();
                word.setWordTarget(wordTarget);
                ObjectMapper objectMapper = new ObjectMapper();
                word.setDescription(objectMapper.convertValue(wordDoc.get(wordTarget, Document.class), Word.Description.class));
                if (word.getDescription() != null) {
                    return word;
                }
                else {
                    Logger.warn("Fail to fetch word's description.");
                }
            }
            else {
                Logger.warn("Query return null.");
            }
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }
        return null;
    }

    public Trie ReturnTrie(){
        return trie;
    }

}
