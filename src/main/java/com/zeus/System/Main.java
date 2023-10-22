package com.zeus.System;

import com.mongodb.client.*;
import com.zeus.utils.trie.Trie;
import org.bson.Document;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Shutdown function when the program end.
        long startTime = System.currentTimeMillis();

        Trie trie = new Trie();

        MongoClient client = MongoClients.create("mongodb+srv://hotmashmallow:SydFj7MBdnl57nID@cluster0.z4stprp.mongodb.net/");
        MongoDatabase database = client.getDatabase("dictionary_metadata");
        MongoCollection<Document> col = database.getCollection("dictionary");

        List<Document> pipeline = Arrays.asList(new Document("$project", new Document("keys", new Document("$objectToArray", "$$ROOT"))),
                new Document("$unwind", "$keys"),
                new Document("$group", new Document("_id", null).append("allKeys", new Document("$addToSet", "$keys.k"))),
                new Document("$project", new Document("_id", 0).append("allKeys", 1)));
        AggregateIterable<Document> result = col.aggregate(pipeline);

       long count = 0;
       for (Document doc : result) {
           List<String> allKeys = (List<String>) doc.get("allKeys");
           for (String key : allKeys) {
               trie.insert(key);
           }
       }

       trie.autoFill("Man", 3, 4).forEach(System.out::println);
       long endTime = System.currentTimeMillis();
       System.out.printf("Execution Time: %s Millisecond\n",endTime-startTime);
    }
}
