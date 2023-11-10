package com.zeus.DatabaseManager;

import com.zeus.DictionaryManager.SingleWord;
import com.zeus.DictionaryManager.Word;
import com.zeus.DictionaryManager.WordFactory;
import com.zeus.utils.clock.Clock;
import com.zeus.utils.config.Config;
import com.zeus.utils.encode.Encoder;
import com.zeus.utils.file.FileManager;
import com.zeus.utils.log.Logger;
import com.zeus.utils.managerfactory.Manager;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.apache.commons.logging.Log;

import javax.swing.plaf.nimbus.State;
import java.io.*;
import java.sql.*;
import java.util.*;

public class SQLite extends Manager {
    private String pathToDatabase;
    private String url;

    public SQLite() {
        Logger.info("SQLiteManager created.");
    }

    private void setDatabase(String path) throws FileNotFoundException, UnsupportedEncodingException {
        this.pathToDatabase = FileManager.getPathFromFile(path);
        this.url = "jdbc:sqlite:" + this.pathToDatabase;
    }

    private int countRow() {
        String query = "SELECT COUNT(*) FROM WORD";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet result = pstmt.executeQuery();
            if (result.next()) {
                int rowCount = result.getInt(1);
                return rowCount;
            }
        } catch (SQLException e) {
            System.out.printf("ERROR: %s.\n", e.getMessage());
        }
        return -1;
    }

    /**
     * Functions to execute when create a manager.
     */

    private void checkExist() {
        try (Connection conn = this.connect()) {
            Logger.info(String.format("Database status: EXIST. File path: '%s'.\n", pathToDatabase));
        } catch (SQLException e) {
            Logger.printStackTrace(e);
        }
    }

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            if (conn == null) throw new NullPointerException("Failed to establish a connection.");
            Logger.printStackTrace("Connection established.");
        } catch (SQLException e) {
            Logger.printStackTrace(e);
        }
        return conn;
    }

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        SQLite sqLite = new SQLite();
        sqLite.setDatabase("/com/zeus/data/Dictionary.db");
        sqLite.checkExist();
        Clock.timer(() -> sqLite.importFromJson("/com/zeus/data/test.json"));
        Clock.timer(() -> sqLite.getRandomWords(3, 10));
    }

    private void insert(Word word, String wordID, PreparedStatement sW, PreparedStatement sM, PreparedStatement sE) {
        Map<String, List<SingleWord>> wordMap = new WordFactory(word).getSingleWordMap();

        try {
            sW.setString(1, wordID);
            sW.setString(2, word.getWordTarget());
            sW.setString(3, word.getPronoun());
            //sW.executeUpdate(); //Insert in word table
            sW.addBatch();//Batch
            sM.setString(1, wordID);
            int meaningID = 0;
            for (Map.Entry<String, List<SingleWord>> pair : wordMap.entrySet()) {
                String wordType = pair.getKey();
                sM.setString(4, wordType);
                for (SingleWord singleWord : pair.getValue()) {
                    String wordMeaning = singleWord.getMeaning();
                    sM.setInt(2, meaningID);
                    sM.setString(3, wordMeaning);
                    //sM.executeUpdate(); //Insert in meaning table
                    sM.addBatch(); // Batch
                    int exampleID = 0;
                    for (Pair<String, String> example : singleWord.getExamples()) {
                        String targetEN = example.getKey();
                        String targetVN = example.getValue();
                        sE.setString(1, wordID);
                        sE.setInt(2, exampleID);
                        sE.setInt(3, meaningID);
                        sE.setString(4, targetEN);
                        sE.setString(5, targetVN);
                        //sE.executeUpdate();//
                        sE.addBatch();
                        exampleID++;
                    }
                    meaningID++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(SingleWord word) {
        String queryWord = "INSERT INTO WORD(wordID, target, pronoun) SELECT ?, ?, ? WHERE NOT EXISTS (SELECT 1 FROM WORD WHERE wordID = ?)";
        String queryMeaning = "INSERT INTO MEANING(wordID, meaningID, target, type) VALUES(?, ?, ?, ?)";
        String queryExample = "INSERT INTO EXAMPLE(wordID, exampleID, meaningID, targetEN, targetVN) VALUES(?, ?, ?, ?, ?)";
        try (Connection connection = this.connect();
             PreparedStatement sW = connection.prepareStatement(queryWord);
             PreparedStatement sM = connection.prepareStatement(queryMeaning);
             PreparedStatement sE = connection.prepareStatement(queryExample);
             PreparedStatement sMeaning = connection.prepareStatement("SELECT COUNT(*) FROM MEANING WHERE wordID = ?");
             PreparedStatement sExample = connection.prepareStatement("SELECT COUNT(*) FROM EXAMPLE WHERE wordID = ? AND meaningID = ?");
             PreparedStatement checkMeaningExist = connection.prepareStatement("SELECT COUNT(*) FROM MEANING WHERE wordID = ? AND target = ?")) {
            String wordID = Encoder.encode(word.getWordTarget());

            //Check if meaning existed
            checkMeaningExist.setString(1, wordID);
            checkMeaningExist.setString(2, word.getMeaning());
            ResultSet meaningExist = checkMeaningExist.executeQuery();
            if (meaningExist.next() && meaningExist.getInt(1) != 0) throw new Exception("Insert aborted. Meaning existed.");

            // Insert word into WORD table
            sW.setString(1, wordID);
            sW.setString(2, word.getWordTarget());
            sW.setString(3, word.getPronoun());
            sW.setString(4, wordID);
            sW.executeUpdate();

            // Count number of meanings and examples to assign ID.
            Integer meaningID = null;
            Integer exampleID = null;
            sMeaning.setString(1, wordID);
            ResultSet meaningIDCount = sMeaning.executeQuery();
            if (meaningIDCount.next()) {
                meaningID = meaningIDCount.getInt(1);
            }
            if (meaningID == null) throw new NullPointerException("Null meaningID.");
            sExample.setString(1, wordID);
            sExample.setInt(2, meaningID);
            ResultSet exampleIDCount = sExample.executeQuery();
            if (exampleIDCount.next()) {
                exampleID = exampleIDCount.getInt(1);
            }
            if (exampleID == null) throw new NullPointerException("Null exampleID");

            // Insert into MEANING table
            sM.setString(1, wordID);
            sM.setInt(2, meaningID);
            sM.setString(3, word.getMeaning());
            sM.setString(4, word.getType());
            sM.executeUpdate();
            if (word.getExamples() != null) {
                sE.setString(1, wordID);
                sE.setInt(3, meaningID);
                for (Pair<String, String> examples : word.getExamples()) {
                    sE.setInt(2, exampleID);
                    sE.setString(4, examples.getKey());
                    sE.setString(5, examples.getValue());
                    sE.executeUpdate();
                    exampleID++;
                }
            }
            Logger.info("Insert successful.");
        } catch (Exception e) {
            Logger.printStackTrace(e);
        }
    }

    private void createDatabaseFromQuery(String filePath) {
        try (Connection conn = this.connect();
             BufferedReader bufferedReader = new BufferedReader(new FileReader(FileManager.getFileFromPath(filePath)));
             PreparedStatement countTables = conn.prepareStatement("SELECT COUNT(*) FROM sqlite_master WHERE type = 'table' AND name != 'sqlite_sequence'")) {
            ResultSet count = countTables.executeQuery();
            if (count.next() && count.getInt(1) != 0) {
                Logger.warn("Cannot create tables, database has many tables already, check the database again or delete the tables in it.");
            } else {
                StringBuilder query = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    query.append(line);
                    query.append('\n');
                }
                conn.createStatement().executeUpdate(query.toString());
                Logger.info("Database created successfully.");
            }
        } catch (Exception e) {
            Logger.printStackTrace(e);
        }
    }

    private void importFromJson(String jsonPath) {
        int batchSize = 1000;
        List<Word> wordList = FileManager.deserializeFromFile(jsonPath);
        String queryWord = "INSERT INTO WORD(wordID, target, pronoun) VALUES(?, ?, ?)";
        String queryMeaning = "INSERT INTO MEANING(wordID, meaningID, target, type) VALUES(?, ?, ?, ?)";
        String queryExample = "INSERT INTO EXAMPLE(wordID, exampleID, meaningID, targetEN, targetVN) VALUES(?, ?, ?, ?, ?)";
        try (Connection connection = this.connect();
             PreparedStatement sW = connection.prepareStatement(queryWord);
             PreparedStatement sM = connection.prepareStatement(queryMeaning);
             PreparedStatement sE = connection.prepareStatement(queryExample)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM WORD");
            statement.executeUpdate("DELETE FROM MEANING");
            statement.executeUpdate("DELETE FROM EXAMPLE");
            connection.setAutoCommit(false);
            if (wordList == null) throw new NullPointerException("Null word list.");
            int count = 0;
            for (Word word : wordList) {
                insert(word, Encoder.encode(word.getWordTarget()), sW, sM, sE);
                count++;
                if (count % 1000 == 0) {
                    sW.executeBatch();
                    sM.executeBatch();
                    sE.executeBatch();
                }
            }
            sW.executeBatch();
            sM.executeBatch();
            sE.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Pair<String, String>> getRandomWords(int minWordLength, int numberOfWords) {
        List<Pair<String, String>> result = new ArrayList<>();
        try (Connection connection = this.connect();
             PreparedStatement query = connection.prepareStatement("SELECT WORD.target, MEANING.target FROM WORD INNER JOIN MEANING ON WORD.wordID = MEANING.wordID WHERE LENGTH(WORD.target >= ?) GROUP BY WORD.wordID ORDER BY RANDOM() LIMIT ?;")) {
            query.setInt(1, minWordLength);
            query.setInt(2, numberOfWords);
            ResultSet queryResult = query.executeQuery();
            if (!queryResult.next()) {
                Logger.warn("Query random return 0 row.");
            } else {
                do {
                    result.add(new Pair<>(queryResult.getString(1), queryResult.getString(2)));
                } while (queryResult.next());
            }
        } catch (SQLException e) {
            Logger.printStackTrace(e);
        }
        return result;
    }

    @Override
    public void init(Config config) {
        pathToDatabase = config.getProperty("localSQLitePath", String.class);
        try {
            url = "jdbc:sqlite:" + FileManager.getPathFromFile(pathToDatabase);
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        checkExist();
        try {
            importFromJson(config.getProperty("localTest", String.class));
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }
    }
}
