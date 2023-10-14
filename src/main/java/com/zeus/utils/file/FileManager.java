package com.zeus.utils.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeus.App.Config.Config;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class FileManager {
    public static String readLineFromFile(String filePath, int numberReadline) {
        try (FileReader fr = new FileReader(new File(filePath));
             BufferedReader br = new BufferedReader(fr)) {

            int countLine = 0;
            while (numberReadline >= countLine) {
                if (countLine > 1) throw new Exception("ERROR: File contains more than 1 line");
                String line = br.readLine();
                countLine++;
                if (line == null) break;
                return line;
            }

        } catch (Exception e) {
            System.out.printf("%s. File path: '%s'.\n", e.getMessage(), filePath);
        }
        return null;
    }
    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FileManager.class.getResource(fxml));
        return fxmlLoader.load();
    }

    public static Image loadImage(String filePath) {
        return new Image(filePath);
    }

    public static Config getConfig(String target, String jsonPath) {
        ObjectMapper objm = new ObjectMapper();
        try {
            List<Config> configs = objm.readValue(FileManager.class.getResource(jsonPath), objm.getTypeFactory().constructCollectionType(List.class, Config.class));
            for (Config c : configs) {
                return c;
            }
        } catch (Exception e) {
            System.out.printf("%s.", e.getMessage());
        }
        return null;
    }

}
