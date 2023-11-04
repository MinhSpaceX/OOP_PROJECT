package com.zeus.DatabaseManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.*;
import com.zeus.DictionaryManager.Word;
import com.zeus.utils.log.Logger;
import com.zeus.utils.trie.Trie;
import org.bson.BsonNull;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MongoManager {
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

    public MongoManager(String url){
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
        List<Document> tempPipeline = Arrays.asList(new Document("$match",new Document("run",new Document("$exists", true).append("$ne",new BsonNull()))),new Document("$project",new Document("_id", 0L).append("run", 1L)));
        ObjectMapper objectMapper = new ObjectMapper();
        try (MongoCursor<Document> cursor = collection.aggregate(tempPipeline).iterator()) {
            if (!cursor.hasNext()) throw new Exception("Query return null.");
            Document wordDoc = cursor.next();
            if (wordDoc.isEmpty()) throw new Exception("Query return empty.");
            Word word = new Word(wordTarget, objectMapper.convertValue(wordDoc.get(wordTarget, Document.class), Word.Description.class));
            if (word.getDescription() == null) throw new Exception("Fail to fetch word's description.");
            return word;
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }
        return null;
    }

    public Trie ReturnTrie(){
        return trie;
    }

}
