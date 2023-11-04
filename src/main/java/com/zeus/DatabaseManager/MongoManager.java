package com.zeus.DatabaseManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.*;
import com.zeus.DictionaryManager.Word;
import com.zeus.utils.clock.Clock;
import com.zeus.utils.config.Config;
import com.zeus.utils.log.Logger;
import com.zeus.utils.managerfactory.Manager;
import com.zeus.utils.trie.Trie;
import org.apache.commons.logging.Log;
import org.bson.BsonNull;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MongoManager extends Manager {
    private MongoCollection<Document> collection;
    MongoClient client = null;
    MongoDatabase database = null;
    private Trie trie = null;
    List<Document> pipeline = null;

    /**
     * take data from base then load to trie
     */
    private void fetchDatafromBase(){
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
        if (trie == null) {
            Logger.error("Trie is null after fetch.");
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

    @Override
    public void init(Config config) {
        try {
            MongoClients.create(config.getProperty("mongodbPath", String.class));
            database = client.getDatabase("dictionary_metadata");
            collection = database.getCollection("dictionary");
            trie = new Trie();
            pipeline = Arrays.asList(
                    new Document("$project", new Document("keys", new Document("$objectToArray", "$$ROOT"))),
                    new Document("$unwind", "$keys"),
                    new Document("$group", new Document("_id", null)
                            .append("allKeys", new Document("$addToSet", "$keys.k")))
            );
            Clock.timer(this::fetchDatafromBase);
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }
    }
}
