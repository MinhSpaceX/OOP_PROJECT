package com.zeus.DatabaseManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.zeus.DictionaryManager.Word;
import com.zeus.utils.config.Config;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MongoPanel {
    private MongoClient client;
    private MongoDatabase db;
    private MongoCollection collection;
    private ObjectMapper objm = new ObjectMapper();
    private ObservableList<Word> dictionary = FXCollections.observableArrayList();
    public MongoPanel(String mongodbPath){
        client = MongoClients.create(mongodbPath);
        db = client.getDatabase("dictionary");
        collection = db.getCollection("words");
    }

    public void addDocument(String word_target, String word_explain, String word_type) throws JsonProcessingException {
        Document sampledoc = new Document();
        sampledoc.append("word_target", word_target);
        sampledoc.append("word_explain", word_explain);
        sampledoc.append("word_type", word_type);

        dictionary.add(JsonDeserializer(sampledoc));
        collection.insertOne(sampledoc);
    }

    public void deleteDocument(String word_target){
        Document filter = new Document();
        filter.append("word_target", word_target);
        dictionary.removeIf(i -> i.getWordTarget().equals(word_target));
        collection.deleteOne(filter);
    }

    public ObservableList<Word> FetchingData(){
        Document query = new Document();
        FindIterable<Document> cursor = collection.find(query); // query == null mean all data will be retrieved

        try(MongoCursor<Document> iterator = cursor.iterator()){
            while(iterator.hasNext()){
                Document document = iterator.next();
                Word word = JsonDeserializer(document);
                dictionary.add(word);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return dictionary;
    }

    public void findWord(String searchPattern) throws JsonProcessingException {
        Bson regexFilter = Filters.regex("word_target", searchPattern);
        FindIterable<Document> result = collection.find(regexFilter);
        dictionary.clear();
        for(var i : result){
            Word word = JsonDeserializer(i);
            dictionary.add(word);
        }
    }

    private Word JsonDeserializer(Document doc) throws JsonProcessingException {
        return objm.readValue(doc.toJson(), Word.class);
    }

    public void terminate(){
        client.close();
    }

}
