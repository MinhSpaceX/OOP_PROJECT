package com.zeus.DatabaseManager;

import com.zeus.DictionaryManager.DictionaryManagement;
import com.zeus.DictionaryManager.Word;
import com.zeus.utils.file.FileManager;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLite {
    private String pathToDatabase;
    private String url;

    /**
     * Constructor for the class.
     *
     * @param path path to the database file.
     */
    public SQLite(String path) {
        pathToDatabase = path;
        url = "jdbc:sqlite:" + pathToDatabase;
        init();
        System.out.printf("SQLiteManager created.\n");
    }

    public static void main(String[] args) {
        try {
            SQLite sqLite = new SQLite(FileManager.getPathFromFile("/classes/com/zeus/data/Dictionary.db"));
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void setDatabase(String path) {
        this.pathToDatabase = path;
        this.url = "jdbc:sqlite:" + this.pathToDatabase;
    }

    private int countRow() {
        String query = "SELECT COUNT(*) FROM EN_VN";
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
    private void init() {
        checkExist();
    }

    private void checkExist() {
        try (Connection conn = this.connect()) {
            System.out.printf("Database status: EXIST. File path: '%s'.\n", pathToDatabase);
        } catch (SQLException e) {
            System.out.printf("ERROR: %s.\n", e.getMessage());
        }
    }

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            System.out.printf("Connection established.\n");
        } catch (SQLException e) {
            System.out.printf("ERROR: %s\n", e.getMessage());
        }
        return conn;
    }

    public void insert(Word word) {
        int key = countRow() + 1;
        String query = "INSERT INTO English(wordID, wordTarget, wordExplain, wordType) VALUES(?, ?, ?, ?)";
        String wordEN = word.getWordTarget();
        String wordVN = word.getDescription().getPronoun();
        String wordType = word.getWordTarget();
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, key);
            pstmt.setString(2, wordEN);
            pstmt.setString(3, wordEN);
            pstmt.setString(4, wordType);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.printf("ERROR: %s\n", e.getMessage());
        }
    }

    void createDatabase(String filePath) {
        String url = "jdbc:sqlite:" + filePath;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("ERROR: The driver name is %s.\n" + meta.getDriverName());
                System.out.printf("ERROR: Database created. Path: '%s'.\n", filePath);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
