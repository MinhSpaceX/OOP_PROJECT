package com.zeus.Managers.Database;

import com.zeus.Managers.Search.SearchManager;
import com.zeus.Managers.SystemApp.SystemManager;
import com.zeus.utils.DictionaryUtil.SingleWord;
import com.zeus.utils.DictionaryUtil.Word;
import com.zeus.utils.DictionaryUtil.WordFactory;
import com.zeus.utils.encode.Encoder;
import com.zeus.utils.file.FileManager;
import com.zeus.utils.log.Logger;
import com.zeus.utils.managerfactory.Manager;
import com.zeus.utils.trie.Trie;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.*;

public class SQLite extends Manager {
    public static String pathToDatabase;
    public static String userDatabase;
    private String url;

    public SQLite() {
        Logger.info("SQLiteManager created.");
    }

    /**
     * sets the pathToDatabase variable to the path of a file specified by the @param path.
     * It then sets the url variable to a string that concatenates the pathToDatabase variable with the string "jdbc:sqlite:"
     */
    private void setDatabase(String path) throws FileNotFoundException, UnsupportedEncodingException {
        pathToDatabase = FileManager.getPathFromFile(path);
        this.url = "jdbc:sqlite:" + pathToDatabase;
    }

    /**
     * counts the number of rows in a table named WORD.
     * @return the result.
     */
    private int countRow() {
        String query = "SELECT COUNT(*) FROM WORD";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet result = pstmt.executeQuery();
            if (result.next()) {
                return result.getInt(1);
            }
        } catch (SQLException e) {
            Logger.printStackTrace(e);
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

    /**
     * establishes a connection to a database.
     * @return the connection.
     */
    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            if (conn == null) throw new NullPointerException("Failed to establish a connection.");
            Logger.info("Connection established.");
        } catch (SQLException e) {
            Logger.printStackTrace(e);
        }
        return conn;
    }

    /**
     * establishes a connection to a SQLite database located at the path.
     * @param path the path of a SQLite database.
     * @return the connection.
     */
    private Connection connect(String path) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + path);
            if (conn == null) throw new NullPointerException("Failed to establish a connection.");
            Logger.info("Connection established.");
        } catch (SQLException e) {
            Logger.printStackTrace(e);
        }
        return conn;
    }

    /**
     * Inserts data into the WORD, MEANING, and EXAMPLE tables based on the provided Word object.
     * @param word   The Word object to insert into the database.
     * @param wordID The encoded wordID associated with the Word object.
     * @param sW     The PreparedStatement for inserting data into the WORD table.
     * @param sM     The PreparedStatement for inserting data into the MEANING table.
     * @param sE     The PreparedStatement for inserting data into the EXAMPLE table.
     */
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
        } catch (Exception e) {
            Logger.printStackTrace(e);
        }
    }

    /**
     * insert a SingleWord object into SQL.
     * @param word the SingleWord object
     * @return successfull or unsuccessfull insertion.
     */
    public boolean insert(SingleWord word) {
        SystemManager.getManager(SearchManager.class).getUserTrie().insert(word.getWordTarget());
        SystemManager.getManager(SearchManager.class).getSearchPathTrie().insert(word.getWordTarget());
        boolean success = true;
        String queryWord = "INSERT INTO WORD(wordID, target, pronoun) SELECT ?, ?, ? WHERE NOT EXISTS (SELECT 1 FROM WORD WHERE wordID = ?)";
        String queryMeaning = "INSERT INTO MEANING(wordID, meaningID, target, type) VALUES(?, ?, ?, ?)";
        String queryExample = "INSERT INTO EXAMPLE(wordID, exampleID, meaningID, targetEN, targetVN) VALUES(?, ?, ?, ?, ?)";
        try (Connection connection = this.connect(userDatabase);
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
            if (meaningExist.next() && meaningExist.getInt(1) != 0)
                throw new Exception("Insert aborted. Meaning existed.");

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
            success = false;
        }
        return success;
    }

    /**
     * Creates a database and tables from the SQL queries specified in the provided file.
     * @param filePath  The path to the file containing SQL queries.
     * @param database  The name of the database to create.
     */
    private void createDatabaseFromQuery(String filePath, String database) {
        try (Connection conn = this.connect(database);
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

    /**
     * import data from a JSON file into SQLite.
     * @param jsonPath the file path of a JSON file.
     */
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
                if (count % batchSize == 0) {
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
            Logger.printStackTrace(e);
        }
    }

    /**
     * retrieves a specified number of random word-meaning pairs from a database.
     * @param minWordLength Minimum length of the words to consider.
     * @param numberOfWords Number of random word-meaning pairs to retrieve.
     * @return the list of word-meaning pairs.
     */
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

    /**
     * Update a Word object into database.
     * @param oldWord The original word information to be updated.
     * @param newWord The new word information to replace the old information.
     */
    public void updateWord(SingleWord oldWord, SingleWord newWord) {
        try (Connection connection = this.connect(userDatabase);
             PreparedStatement updateWORD = connection.prepareStatement("UPDATE WORD SET pronoun = ? WHERE wordID = ?");
             PreparedStatement updateMEANING = connection.prepareStatement("UPDATE MEANING SET target = ?, type = ? WHERE wordID = ? AND target = ? AND type = ?");
             PreparedStatement updateEXAMPLE = connection.prepareStatement("UPDATE EXAMPLE SET targetEN = ?, targetVN = ? WHERE wordID = ? AND meaningID = ? AND targetEN = ? AND targetVN = ?");
             PreparedStatement getMeaningID = connection.prepareStatement("SELECT meaningID FROM MEANING WHERE wordID = ? AND target = ? AND type = ?")) {
            String wordID = Encoder.encode(newWord.getWordTarget());
            if (!Objects.equals(oldWord.getWordTarget(), newWord.getWordTarget()))
                throw new Exception(String.format("Old word target(%s) must be the same as new word target(%s)", oldWord.getWordTarget(), newWord.getWordTarget()));
            getMeaningID.setString(1, wordID);
            getMeaningID.setString(2, oldWord.getMeaning());
            getMeaningID.setString(3, oldWord.getType());
            ResultSet resultSet = getMeaningID.executeQuery();
            if (!resultSet.next())
                throw new Exception(String.format("There is no word(wordID: %s) with meaning: %s and type: %s.", wordID, oldWord.getMeaning(), oldWord.getType()));

            updateWORD.setString(1, newWord.getPronoun());
            updateWORD.setString(2, wordID);
            updateWORD.executeUpdate();

            updateMEANING.setString(1, newWord.getMeaning());
            updateMEANING.setString(2, newWord.getType());
            updateMEANING.setString(3, wordID);
            updateMEANING.setString(4, oldWord.getMeaning());
            updateMEANING.setString(5, oldWord.getType());
            updateMEANING.executeUpdate();

            updateEXAMPLE.setString(3, wordID);
            updateEXAMPLE.setInt(4, resultSet.getInt(1));
            List<Pair<String, String>> oldExamples = oldWord.getExamples();
            List<Pair<String, String>> newExamples = newWord.getExamples();
            if (oldExamples != null && newExamples != null) {
                if (oldExamples.size() != newExamples.size())
                    throw new Exception(String.format("Old word examples array(%d) must be of same size as new word examples array(%d).", oldExamples.size(), newExamples.size()));
                for (int i = 0; i < oldExamples.size(); i++) {
                    Pair<String, String> oldPair = oldExamples.get(i);
                    Pair<String, String> newPair = newExamples.get(i);
                    if (!newPair.equals(oldPair)) {
                        updateEXAMPLE.setString(1, newPair.getKey());
                        updateEXAMPLE.setString(2, newPair.getValue());
                        updateEXAMPLE.setString(5, oldPair.getKey());
                        updateEXAMPLE.setString(6, oldPair.getValue());
                        updateEXAMPLE.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            Logger.printStackTrace(e);
        }
    }

    /**
     * retrieve information about a word from the userDatabase based on its wordTarget.
     * @param wordTarget The target word for which information is retrieved.
     * @return A map where the key is the word type, and the value is a list of SingleWord objects.
     */
    public Map<String, List<SingleWord>> getWord(String wordTarget) {
        try (Connection connection = this.connect(userDatabase);
             PreparedStatement getWORD = connection.prepareStatement("SELECT WORD.pronoun, MEANING.type ,MEANING.target, EXAMPLE.targetEN, EXAMPLE.targetVN FROM WORD LEFT JOIN MEANING ON WORD.wordID = MEANING.wordID LEFT JOIN EXAMPLE ON WORD.wordID = EXAMPLE.wordID AND EXAMPLE.meaningID = MEANING.meaningID WHERE WORD.wordID = ?")) {
            String wordID = Encoder.encode(wordTarget);
            getWORD.setString(1, wordID);
            ResultSet wordRs = getWORD.executeQuery();
            Map<String, List<SingleWord>> words = new HashMap<>();
            Map<String, List<Pair<String, String>>> examples = new HashMap<>();
            while (wordRs.next()) {
                String pronoun = wordRs.getString(1);
                String type = wordRs.getString(2);
                String meaning = wordRs.getString(3);
                String targetEN = wordRs.getString(4);
                String targetVN = wordRs.getString(5);
                Pair<String, String> example = null;
                if (targetEN != null && targetVN != null) example = new Pair<>(targetEN, targetVN);
                if (!examples.containsKey(meaning)) {
                    List<Pair<String, String>> tempExample = new ArrayList<>();
                    SingleWord singleWord = new SingleWord(wordTarget, pronoun, type, meaning, tempExample);
                    examples.put(meaning, tempExample);
                    if (!words.containsKey(type)) {
                        words.put(type, new ArrayList<>());
                    }
                    words.get(type).add(singleWord);
                }
                if (example != null) examples.get(meaning).add(example);
            }
            return words;
        } catch (SQLException e) {
            Logger.printStackTrace(e);
        }
        return null;
    }

    /**
     * Retrieves information about a word from the database based on its target word.
     * @param wordTarget The target word for which information is retrieved.
     * @return A map where the key is the word type, and the value is a list of SingleWord objects.
     */
    public Map<String, List<SingleWord>> getWordFromDb(String wordTarget) {
        try (Connection connection = this.connect();
             PreparedStatement getWORD = connection.prepareStatement("SELECT WORD.pronoun, MEANING.type ,MEANING.target, EXAMPLE.targetEN, EXAMPLE.targetVN FROM WORD LEFT JOIN MEANING ON WORD.wordID = MEANING.wordID LEFT JOIN EXAMPLE ON WORD.wordID = EXAMPLE.wordID AND EXAMPLE.meaningID = MEANING.meaningID WHERE WORD.wordID = ?")) {
            String wordID = Encoder.encode(wordTarget);
            getWORD.setString(1, wordID);
            ResultSet wordRs = getWORD.executeQuery();
            Map<String, List<SingleWord>> words = new HashMap<>();
            Map<String, List<Pair<String, String>>> examples = new HashMap<>();
            while (wordRs.next()) {
                String pronoun = wordRs.getString(1);
                String type = wordRs.getString(2);
                String meaning = wordRs.getString(3);
                String targetEN = wordRs.getString(4);
                String targetVN = wordRs.getString(5);
                Pair<String, String> example = null;
                if (targetEN != null && targetVN != null) example = new Pair<>(targetEN, targetVN);
                if (!examples.containsKey(meaning)) {
                    List<Pair<String, String>> tempExample = new ArrayList<>();
                    SingleWord singleWord = new SingleWord(wordTarget, pronoun, type, meaning, tempExample);
                    examples.put(meaning, tempExample);
                    if (!words.containsKey(type)) {
                        words.put(type, new ArrayList<>());
                    }
                    words.get(type).add(singleWord);
                }
                if (example != null) examples.get(meaning).add(example);
            }
            return words;
        } catch (SQLException e) {
            Logger.printStackTrace(e);
        }
        return null;
    }

    /**
     * Loads words from the user-specific database into two Trie structures.
     * Inserts words into both the searchTrie and userTrie for efficient word lookup.
     * @param searchTrie The Trie structure used for general word search functionality.
     * @param userTrie The Trie structure specific to user-added words.
     */
    public void loadTrieFromUserDb(Trie searchTrie, Trie userTrie) {
        try (Connection connection = this.connect(userDatabase);
             Connection connection1 = this.connect();
             PreparedStatement getWords1 = connection1.prepareStatement("SELECT target FROM WORD");
             PreparedStatement getWords = connection.prepareStatement("SELECT target FROM WORD")) {
            ResultSet resultSet = getWords.executeQuery();
            ResultSet resultSet1 = getWords1.executeQuery();
            while (resultSet.next()) {
                String word = resultSet.getString(1);
                userTrie.insert(word);
                searchTrie.insert(word);
            }
            while (resultSet1.next()) {
                searchTrie.insert(resultSet1.getString(1));
            }
        } catch (SQLException e) {
            Logger.printStackTrace(e);
        }
    }

    /**
     * Deletes a word and its associated data from the user database.
     * @param wordTarget The target word to be deleted.
     */
    public void delete(String wordTarget) {
        try (Connection connection = this.connect(userDatabase);
             PreparedStatement deleteQuery = connection.prepareStatement("DELETE FROM WORD WHERE wordID = ?")) {
            deleteQuery.setString(1, Encoder.encode(wordTarget));
            deleteQuery.executeUpdate();
            SearchManager.searchPath.delete(wordTarget);
            SearchManager.userTrie.delete(wordTarget);
        } catch (Exception e) {
            Logger.printStackTrace(e);
        }
    }

    /**
     *Initializes the application, setting up database paths, URLs, and performing necessary checks.
     * Also imports data from a JSON file specified in the configuration.
     */
    @Override
    public void init() {
        pathToDatabase = config.getProperty("localSQLitePath", String.class);
        try {
            userDatabase = FileManager.getPathFromFile(config.getProperty("localUserDatabase", String.class));
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            Logger.printStackTrace(e);
        }
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

    /**
     * Sets up the configuration for the database using the configuration factory from {@link SystemManager}.
     * The configuration is retrieved based on the "Database" identifier.
     */
    @Override
    protected void setConfig() {
        config = SystemManager.getConfigFactory().getConfig("Database");
    }
}