package com.zeus.utils.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class JavaFx {
    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JavaFx.class.getResource(fxml));
        return fxmlLoader.load();
    }
}
