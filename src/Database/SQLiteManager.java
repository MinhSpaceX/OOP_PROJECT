package Database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteManager {
    private final String pathToDatabase;

    /**
     * Constructor for the class.
     *
     * @param path path to the database file.
     */
    public SQLiteManager(String path) {
        pathToDatabase = path;
        init();
        System.out.printf("SQLiteManager created.\n");
    }

    /**
     * Functions to execute when create a manager.
     */
    private void init() {
        checkExist();
    }

    private void checkExist() {
        String url = "jdbc:sqlite:" + pathToDatabase;

        try (Connection conn = DriverManager.getConnection(url)) {
            System.out.printf("Database status: EXIST. File path: '%s'.\n", pathToDatabase);

        } catch (SQLException e) {
            System.out.printf("ERROR: %s.\n", e.getMessage());
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