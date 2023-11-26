package com.zeus.Managers.Database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.*;
import com.zeus.Managers.SystemApp.SystemManager;
import com.zeus.utils.DictionaryUtil.Word;
import com.zeus.utils.clock.Clock;
import com.zeus.utils.log.Logger;
import com.zeus.utils.managerfactory.Manager;
import com.zeus.utils.trie.Trie;
import org.bson.BsonNull;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MongoManager extends Manager {
    MongoClient client = null;
    MongoDatabase database = null;
    List<Document> pipeline = null;
    private MongoCollection<Document> collection;
    private Trie trie = null;

    /**
     * Take data from base then load to trie
     */
    private void fetchDatafromBase() {
        AtomicInteger count = new AtomicInteger();
        try (MongoCursor<Document> cursor = collection.aggregate(pipeline).iterator()) {
            Clock.timer(new Clock.CustomRunnableClass("Trie load time") {
                @Override
                public void run() {
                    if (cursor.hasNext()) {
                        Document result = cursor.next();
                        ArrayList<String> keysList = (ArrayList<String>) result.get("allKeys");
                        for (String key : keysList) {
                            trie.insert(key);
                            count.getAndIncrement();
                        }
                    }
                }
            });
        }
        if (trie == null) {
            Logger.error("Trie is null after fetch.");
        }
        Logger.info(String.valueOf(count.get()));
    }

    /**
     * Take a Word from database with wordTarget.
     *
     * @param wordTarget the wordtarget of a Word.
     * @return a word has the wordtarget is @param wordTarget.
     */
    public Word fetchWord(String wordTarget) {
        List<Document> tempPipeline = Arrays.asList(new Document("$match", new Document(wordTarget, new Document("$exists", true).append("$ne", new BsonNull()))), new Document("$project", new Document("_id", 0L).append(wordTarget, 1L)));
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

    /**
     * return trie loaded from MongoDB.
     *
     * @return {@link #trie}.
     */
    public Trie ReturnTrie() {
        return trie;
    }

    /**
     * initializes a MongoDB client and connects to a database named dictionary_metadata.
     * It then fetches a collection named dictionary from the database and creates a Trie object.
     * Then, it creates a pipeline and fetches data from the database using the pipeline.
     * Finally, operate {@link #fetchDatafromBase()}.
     */
    @Override
    public void init() {
        try {
            client = MongoClients.create(config.getProperty("mongodbPath", String.class));
            database = client.getDatabase("dictionary_metadata");
            collection = database.getCollection("dictionary");
            trie = new Trie();
            pipeline = Arrays.asList(
                    new Document("$project", new Document("keys", new Document("$objectToArray", "$$ROOT"))),
                    new Document("$unwind", "$keys"),
                    new Document("$group", new Document("_id", null)
                            .append("allKeys", new Document("$addToSet", "$keys.k")))
            );
            fetchDatafromBase();
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }
    }

    /**
     * sets the config variable to a Config object that is created using the getConfig() method of {@link SystemManager}.
     * The getConfig() method takes a string argument "Database" and returns a Config object.
     */
    @Override
    protected void setConfig() {
        config = SystemManager.getConfigFactory().getConfig("Database");
    }
}
