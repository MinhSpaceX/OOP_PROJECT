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
    SQLiteManager(String path) {
        pathToDatabase = path;
        init();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SQLiteManager a = new SQLiteManager("src/Database/data/Dictionary.db");
    }

    private void init() {
        checkExist();
    }

    private void checkExist() {
        String url = "jdbc:sqlite:" + pathToDatabase;

        try (Connection conn = DriverManager.getConnection(url)) {
            System.out.printf("LOG: Database status: EXIST. File path: '%s'.", pathToDatabase);

        } catch (SQLException e) {
            System.out.printf("LOG: %s.", e.getMessage());
        }
    }

    void createDatabase(String filePath) {
        String url = "jdbc:sqlite:" + filePath;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("LOG: The driver name is %s." + meta.getDriverName());
                System.out.printf("LOG: Database created. Path: '%s'.", filePath);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}