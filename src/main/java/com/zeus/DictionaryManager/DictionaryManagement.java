package com.zeus.DictionaryManager;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.zeus.System.Default;
import com.zeus.utils.input.Input;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DictionaryManagement {
    private final Dictionary dictionary;

    public DictionaryManagement() {
        dictionary = new Dictionary();
        System.out.println("DictionaryManagement initialized\n");
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void addWordToDictionary(Word word) {
        dictionary.addWord(word);
    }

    public ArrayList<String> dictionarySearcher(String word) {
        ArrayList<String> pList = new ArrayList<>();

        //look up all the word with similar prefix
        for (Word a : dictionary.getDictionary()) {
            if (a.getWordTarget().startsWith(word)) {
                pList.add(a.getWordTarget());
                if (pList.size() > 10 ) break;
            }
        }
        return pList;
    }

    public Word createWord() {
        List<Word.Description.Type.Meaning.Example> exampleList = new ArrayList<Word.Description.Type.Meaning.Example>();
        List<Word.Description.Type> typeslist = new ArrayList<Word.Description.Type>();
        List<Word.Description.Type.Meaning> meaningList = new ArrayList<Word.Description.Type.Meaning>();
        System.out.print("Enter word_target:");
        String word_target = Input.getLine();
        System.out.println("Enter pronoun: ");
        String pronoun = Input.getLine();
        System.out.println("Enter number of type: ");
        int number = Input.getInteger();
        for (int i = 0; i < number; i++) {
            System.out.print("Enter type: ");
            String nameType = Input.getLine();
            System.out.println("Enter number of meaning: ");
            int numberMeaning = Input.getInteger();
            for (int j = 0; j < numberMeaning; j++) {
                System.out.print("Enter explain: ");
                String explain = Input.getLine();
                System.out.println("Enter number of example: ");
                int numberExample = Input.getInteger();
                for (int k = 0; k < numberExample; k++) {
                    System.out.print("Enter example_English <enter> example_VietNames: ");
                    String example_English = Input.getLine();
                    String example_Vietnamese = Input.getLine();
                    Word.Description.Type.Meaning.Example example = new Word.Description.Type.Meaning.Example();
                    example.setExample(example_English, example_Vietnamese);
                    exampleList.add(example);
                }
                Word.Description.Type.Meaning meaning = new Word.Description.Type.Meaning();
                meaning.setMeaning(explain, exampleList);
                meaningList.add(meaning);
            }
            Word.Description.Type type = new Word.Description.Type();
            type.setType(nameType, meaningList);
            typeslist.add(type);
        }
        Word.Description description = new Word.Description();
        description.setDescription(pronoun, typeslist);

       return new Word(word_target, description);
    }
    public void insertFromCommandLine() {
        //System.out.println("Enter your input with format: word_target <enter> word_explain <enter> word_type");
        addWordToDictionary(createWord());
        System.out.println("Successfull!");
    }

    public void removeFromDictionary(int index) {
        //Search for the word
        dictionary.getDictionary().remove(index - 1);
    }

    public void updateDictionary(int index) {
        System.out.println("Input the change: word_target <enter> word_explain <enter> word_type");
        dictionary.getDictionary().set(index - 1, createWord());
    }

    public void insertFromFile(String file) {
        URL url = DictionaryManagement.class.getResource(file);
        ObjectMapper o = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();
        try (JsonParser jsonParser = jsonFactory.createParser(url)) {
            jsonParser.nextToken();
            while (jsonParser.nextToken() != null) {
                if (jsonParser.currentToken() == JsonToken.START_OBJECT) {
                    Word w = new Word(jsonParser.currentName(), o.readValue(jsonParser, Word.Description.class));
                    addWordToDictionary(w);
                    jsonParser.skipChildren();
                }
            }
        } catch (Exception e) {
            System.out.printf("%s.", e.getMessage());
        }
    }

    public void dictionaryExportToFile(String file) {
        JsonFactory jsonFactory = new JsonFactory();

        try {
            File jsonFile = new File(file);
            FileReader reader = new FileReader(Objects.requireNonNull(getClass().getResource(file)).getPath());
            JsonParser jsonParser = jsonFactory.createParser(reader);

            // Di chuyển đến phần tử JSON cuối cùng trong tệp
            while (jsonParser.nextToken() != null && jsonParser.getCurrentToken() != JsonToken.END_ARRAY) {
                // Bỏ qua tất cả các token trước mảng JSON;
                jsonParser.skipChildren();
            }

            FileWriter writer = new FileWriter(Objects.requireNonNull(getClass().getResource(file)).getPath(), true); // Mở tệp JSON để ghi thêm dữ liệu
            JsonGenerator jsonGenerator = jsonFactory.createGenerator(writer);
            jsonGenerator.setCodec(new ObjectMapper());
            if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
                // Nếu mảng JSON đã tồn tại, thêm dấu phẩy
                jsonGenerator.writeRaw(',');
            } else {
                // Nếu mảng JSON chưa tồn tại, viết một mảng mới
                jsonGenerator.writeStartArray();
            }

            // Ghi các đối tượng Word mới vào tệp JSON
            for (Word word : dictionary.getDictionary()) {
                jsonGenerator.writeObject(word);
            }

            jsonGenerator.writeEndArray();
            jsonGenerator.close();
            reader.close();
            System.out.println("Dữ liệu đã được ghi vào tệp JSON thành công.");
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi dữ liệu vào tệp JSON: " + e.getMessage());
        }
    }
}

