package utils.File;

import java.io.BufferedReader;
//import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;

//import DictionaryManager.DictionaryID;
//import DictionaryManager.Word;
//import DictionaryManager.DictionaryManagement;

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
            System.out.printf("%s. File path: '%s'.",e.getMessage(), filePath);
        }
        return null;
    }
}